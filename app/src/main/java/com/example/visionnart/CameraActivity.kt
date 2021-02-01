package com.example.visionnart

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.visionnart.models.Oeuvre
import com.example.visionnart.models.WebEntitesResponse
import com.example.visionnart.models.WebEntitiesRequest
import com.example.visionnart.utils.FirestoreUtil
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.response
import com.github.kittinunf.fuel.gson.jsonBody
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.GsonBuilder
import com.wonderkiln.camerakit.*
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.LinkedHashMap


class CameraActivity : AppCompatActivity() {


    var cameraView: CameraView? = null
    var btnDetect: Button? = null
    var waintingDialog: AlertDialog? = null

    private lateinit var auth: FirebaseAuth
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private lateinit var filePath: Uri
    private var mapOeuvreToPoid: LinkedHashMap<String?, LinkedList<Poids>> = LinkedHashMap()
    private var mapResultOeuvre: LinkedHashMap<String?, Oeuvre> = LinkedHashMap()

    override fun onResume() {
        super.onResume()
        cameraView?.start()
    }

    override fun onPause() {
        super.onPause()
        cameraView?.stop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        //init firebaseStorage
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference

        cameraView = findViewById(R.id.camera_view)
        btnDetect = findViewById(R.id.btn_detect)

        // waintingDialog = SpotsDialog.Builder().setMessage("Attendez s'il vous plait...").setContext(this).setCancelable(false).build()

        cameraView!!.addCameraKitListener(object : CameraKitEventListener {
            override fun onEvent(cameraKitEvent: CameraKitEvent) {

            }

            override fun onError(cameraKitError: CameraKitError) {

            }

            override fun onImage(cameraKitImage: CameraKitImage) {
                waintingDialog?.show()
                var bitmap = cameraKitImage.bitmap
                bitmap = Bitmap.createScaledBitmap(
                    bitmap,
                    cameraView?.width ?: 1,
                    cameraView?.height ?: 1,
                    false
                )
                cameraView?.stop()
                takeAPicture(bitmap)
            }

            override fun onVideo(cameraKitVideo: CameraKitVideo) {

            }
        })

        btnDetect?.setOnClickListener {
            cameraView?.start()
            cameraView?.captureImage()
        }
    }

    private fun takeAPicture(bitmap: Bitmap) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading...")
        progressDialog.show()
        val imageRef = storageReference!!.child("scan/" + UUID.randomUUID().toString())
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val data = outputStream.toByteArray()

        imageRef.putBytes(data)
            .addOnSuccessListener {
                progressDialog.setTitle("File Uploaded")
                progressDialog.show()
                it.metadata!!.reference!!.downloadUrl.addOnSuccessListener { imageUrl ->
                    val requestToObject =
                        WebEntitiesRequest(imageUrl.toString(), "WEB_DETECTION", 5)
                    Fuel.post(
                        "https://vision.googleapis.com/v1/images:annotate",
                        listOf("key" to "AIzaSyDf3akoH0ASRW1WwXYyAwVOY__AGQEtB4U")
                    )
                        .jsonBody(requestToObject.getRequests())
                        .response(WebEntitesResponse.Responses.Deserializer()) { request, response, result ->
                            progressDialog.setTitle("Searching...")
                            if (android.os.Build.VERSION.SDK_INT > 8) {
                                val policy = StrictMode.ThreadPolicy.Builder()
                                    .permitAll().build()
                                StrictMode.setThreadPolicy(policy)
                                val listResponses = ArrayList<WebEntitesResponse.Responses>()
                                val (datas, error) = result
                                datas?.forEach { data ->
                                    listResponses.add(data)
                                }
                                val gsonBuilder = GsonBuilder().create()
                                val base = gsonBuilder.fromJson(
                                    response.body().asString("application/json"),
                                    WebEntitesResponse.Base::class.java
                                )
                                println("Base = $base")
                                val mapResult: LinkedHashMap<String, Float> = LinkedHashMap()
                                base?.responses?.forEach { responseUnitaire ->
                                    responseUnitaire.webDetection?.webEntities?.forEach { webEntitie ->
                                        mapResult[webEntitie.description.toString().toLowerCase()] =
                                            webEntitie.score!!.toFloat()
                                    }
                                }
                                search(mapResult, progressDialog)
                            }
                        }
                }
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
            }
            .addOnProgressListener { taskSnapshot ->
                val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                progressDialog.setMessage("Uploaded " + progress.toInt() + "%..")
            }
    }

    private fun search(
        mapResult: LinkedHashMap<String, Float>,
        progressDialog: ProgressDialog
    ) {
        mapResultOeuvre = LinkedHashMap()
        mapOeuvreToPoid = LinkedHashMap()
        FirestoreUtil.firestoreInstance.collection("oeuvre")
            .whereIn("keyword_name", mapResult.keys.toList())
            .get().addOnSuccessListener { documents ->
                for (data in documents) {
                    val oeuvre = data.toObject(Oeuvre::class.java)
                    println("dataOeuvre name "+ oeuvre.id_oeuvre)
                    if (!mapResultOeuvre.containsKey(oeuvre.id_oeuvre)) {
                        mapResultOeuvre[oeuvre.id_oeuvre] =
                            oeuvre
                    }
                    if (!mapOeuvreToPoid.containsKey(oeuvre.id_oeuvre)) {
                        mapOeuvreToPoid[oeuvre.id_oeuvre] =
                            LinkedList()
                    }
                    mapOeuvreToPoid[oeuvre.id_oeuvre]?.add(
                        Poids(
                            "keyword_name",
                            mapResult[oeuvre.keyword_name]
                        )
                    )
                }
                FirestoreUtil.firestoreInstance.collection("oeuvre")
                    .whereIn("keyword_museum", mapResult.keys.toList())
                    .get().addOnSuccessListener { documents
                        for(data in documents) {
                            val oeuvre = data.toObject(Oeuvre::class.java)
                            println("dataOeuvre museum "+ oeuvre.id_oeuvre)
                            if (!mapResultOeuvre.containsKey(oeuvre.id_oeuvre)) {
                                mapResultOeuvre[oeuvre.id_oeuvre] =
                                    oeuvre
                            }
                            if (!mapOeuvreToPoid.containsKey(oeuvre.id_oeuvre)) {
                                mapOeuvreToPoid[oeuvre.id_oeuvre] =
                                    LinkedList()
                            }
                            mapOeuvreToPoid[oeuvre.id_oeuvre]?.add(
                                Poids(
                                    "keyword_museum",
                                    mapResult[oeuvre.keyword_museum]
                                )
                            )
//                            fillMap(mapResultOeuvre, data.toObject(Oeuvre::class.java), mapOeuvreToPoid,"keyword_museum",mapResult)
                        }
                        FirestoreUtil.firestoreInstance.collection("oeuvre")
                            .whereIn("keyword_artiste", mapResult.keys.toList())
                            .get()
                            .addOnSuccessListener { documents ->
                                for(data in documents) {
                                    val oeuvre = data.toObject(Oeuvre::class.java)
                                    println("dataOeuvre artiste"+ oeuvre.id_oeuvre)
                                    if (!mapResultOeuvre.containsKey(oeuvre.id_oeuvre)) {
                                        mapResultOeuvre[oeuvre.id_oeuvre] =
                                            oeuvre
                                    }
                                    if (!mapOeuvreToPoid.containsKey(oeuvre.id_oeuvre)) {
                                        mapOeuvreToPoid[oeuvre.id_oeuvre] =
                                            LinkedList()
                                    }
                                    mapOeuvreToPoid[oeuvre.id_oeuvre]?.add(
                                        Poids(
                                            "keyword_artiste",
                                            mapResult[oeuvre.keyword_artiste]
                                        )
                                    )
//                                    fillMap(mapResultOeuvre, data.toObject(Oeuvre::class.java), mapOeuvreToPoid,"keyword_artiste",mapResult)
                                }
                                chooseMostResult(mapOeuvreToPoid,mapResultOeuvre)
                            }
                    }
            }.addOnFailureListener{ e: Exception ->
                Log.d("Exception", e.toString())
            }
    }

    private fun fillMap(mapResultOeuvre: LinkedHashMap<String?, Oeuvre>, oeuvre: Oeuvre?, mapOeuvreToPoid: LinkedHashMap<String?, LinkedList<Poids>>, keyword:String, mapResult: LinkedHashMap<String, Float>) {
        if (!mapResultOeuvre.containsKey(oeuvre?.id_oeuvre)) {
            mapResultOeuvre[oeuvre!!.id_oeuvre] =
                oeuvre
        }
        if (!mapOeuvreToPoid.containsKey(oeuvre?.id_oeuvre)) {
            mapOeuvreToPoid[oeuvre!!.id_oeuvre] =
                LinkedList()
        }
        mapOeuvreToPoid[oeuvre!!.id_oeuvre]?.add(
            Poids(
                keyword,
                mapResult[oeuvre.keyword_museum]
            )
        )
    }

    private fun chooseMostResult(mapOeuvreToPoid: LinkedHashMap<String?, LinkedList<Poids>>, mapResultOeuvre: LinkedHashMap<String?, Oeuvre>) {
        val listPoidsRecord3: LinkedList<Oeuvre?> = LinkedList()
        val listPoidsRecord2: LinkedList<Oeuvre?> = LinkedList()
        val listPoidsRecord1: LinkedList<Oeuvre?> = LinkedList()
        mapOeuvreToPoid.keys.forEach { keyPoids ->
            if (mapOeuvreToPoid[keyPoids]!!.size == 3) {
                listPoidsRecord3.add(mapResultOeuvre[keyPoids])
            }
            if (mapOeuvreToPoid[keyPoids]!!.size == 2) {
                listPoidsRecord2.add(mapResultOeuvre[keyPoids])
            }
            if (mapOeuvreToPoid[keyPoids]!!.size == 1) {
                listPoidsRecord1.add(mapResultOeuvre[keyPoids])
            }
        }
        println("mapResultOeuvre.values" + mapResultOeuvre.values)
        println("mapOeuvreToPoid$mapOeuvreToPoid")
        when {            // Si ya que un resultat avec 3 matchs
            listPoidsRecord3.size == 1 ->
                // println("listPoidsRecord3.size == 1")
                replaceFragment(listPoidsRecord3[0]?.id_oeuvre)
            // Si ya plusieurs resultats avec 3 matchs
            listPoidsRecord3.size > 1 ->
                // println("listPoidsRecord3.size > 1")  //
                searchResult(listPoidsRecord3,mapOeuvreToPoid)
            // Si ya que un resultat avec 2 matchs
            listPoidsRecord2.size == 1 -> // println("listPoidsRecord2.size == 1")
                replaceFragment(listPoidsRecord2[0]?.id_oeuvre)
            // Si ya plusieurs resultats avec 2 matchs
            listPoidsRecord2.size > 1 -> // println("listPoidsRecord2.size > 1")
                searchResult(listPoidsRecord2,mapOeuvreToPoid)
            // Si ya que un resultat avec 2 matchs
            listPoidsRecord1.size == 1 -> // println("listPoidsRecord1.size == 1")
                replaceFragment(listPoidsRecord1[0]?.id_oeuvre)
            // Si ya plusieurs resultats avec 2 matchs
            listPoidsRecord1.size > 1 -> // println("listPoidsRecord1.size > 1")
                searchResult(listPoidsRecord1,mapOeuvreToPoid)
            else ->
                Toast.makeText(applicationContext, "Oeuvre non trouvée", Toast.LENGTH_SHORT).show()
        }
    }

    private fun searchResult(listPoidsRecord: LinkedList<Oeuvre?>,mapOeuvreToPoid: LinkedHashMap<String?, LinkedList<Poids>> ) {
        var mapForResultRecord3 :LinkedHashMap<String?,Float> = LinkedHashMap()
        listPoidsRecord.forEach{
                oeuvre ->
            mapForResultRecord3[oeuvre?.id_oeuvre] = 0F
            mapOeuvreToPoid[oeuvre?.id_oeuvre]?.forEach{
                    poid ->
                mapForResultRecord3[oeuvre?.id_oeuvre]?.plus(poid.scoring!!)
            }
        }
        var mostResult: String? = ""
        var mostScoring: Float? = 0F
        mapForResultRecord3.keys.forEach {
                oeuvreRecord ->
            if(mapForResultRecord3[oeuvreRecord]!! > mostScoring!!) {
                mostScoring = mapForResultRecord3[oeuvreRecord]
                mostResult = oeuvreRecord
            }
        }
        replaceFragment(mostResult!!)
    }

    data class Poids(val name: String, val scoring: Float?) {
        constructor() : this(
            "",
            0F
        )
    }


    public fun replaceFragment(oeuvre_id:String?) {
        val intent = Intent(this, MainActivity::class.java)
        val bundle = Bundle()
        bundle.putString("oeuvre_id", oeuvre_id)
        intent.putExtras(bundle)
        startActivity(intent)
    }
    /**
     * @Deprecated
     */
    /**private fun runDetector(bitmap: Bitmap) {
    val image = FirebaseVisionImage.fromBitmap(bitmap)
    Checker(Checker.Consumer { internet ->
    if (internet) {
    // setMaxResult(int) Get 1 Result with highest Confidence Threshold
    val options = FirebaseVisionCloudDetectorOptions.Builder().setMaxResults(1).build()
    val detector = FirebaseVision.getInstance().getVisionCloudLabelDetector(options)
    detector.detectInImage(image).addOnSuccessListener { firebaseVisionCloudLabels ->
    processDataResultCloud(firebaseVisionCloudLabels)
    }.addOnFailureListener { e -> Log.d("ERROR", e.message) }

    val detector2 = FirebaseVision.getInstance()
    } else {
    // setMaxResult(int) Get 1 Result with highest Confidence Threshold
    val options =
    FirebaseVisionLabelDetectorOptions.Builder().setConfidenceThreshold(0.8f)
    .build()
    val detector = FirebaseVision.getInstance().getVisionLabelDetector(options)
    detector.detectInImage(image)
    .addOnSuccessListener { firebaseVisionLabels ->
    processDataResult(
    firebaseVisionLabels
    )
    }
    .addOnFailureListener { e -> Log.d("ERROR", e.message) }
    }
    })
    }

    private fun processDataResultCloud(firebaseVisionCloudLabels: List<FirebaseVisionCloudLabel>) {
    for (label in firebaseVisionCloudLabels) {
    Toast.makeText(this, "Cloud result : " + label.label, Toast.LENGTH_SHORT).show()
    }
    waintingDialog?.dismiss()
    }

    private fun processDataResult(firebaseVisionCloudLabels: List<FirebaseVisionLabel>) {
    for (label in firebaseVisionCloudLabels) {
    Toast.makeText(this, "Cloud result : " + label.label, Toast.LENGTH_SHORT).show()
    }
    waintingDialog?.dismiss()
    }**/
}
