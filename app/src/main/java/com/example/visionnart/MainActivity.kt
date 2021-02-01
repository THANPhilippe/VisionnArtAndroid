package com.example.visionnart

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.visionnart.fragment.*
import com.example.visionnart.models.Oeuvre
import com.example.visionnart.utils.FirestoreUtil
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_my_account.*
import org.jetbrains.anko.*
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {


    //    Valeur identifiant si l'utilisateur est connecté
    private val RC_SIGN_IN = 2
    //    Valeur identifiant si l'utilisateur a utiliser l'image Picker
    public val PICK_IMAGE_REQUEST = 3
    private lateinit var auth: FirebaseAuth
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private lateinit var filePath: Uri

    private val singInProviders = listOf(
        AuthUI.IdpConfig.EmailBuilder().setAllowNewAccounts(true).setRequireName(true).build()
    )
    private lateinit var textMessage: TextView
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                replaceFragment(ExplorerFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                replaceFragment(ScanFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_myAccount -> {
                if (auth.currentUser?.isAnonymous == true || auth.currentUser == null) {
                    replaceFragment(SigninFragment())
                    return@OnNavigationItemSelectedListener true
                } else {
                    replaceFragment(MyaccountFragment())
                    return@OnNavigationItemSelectedListener true
                }
            }
            else -> {
                return@OnNavigationItemSelectedListener true
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        if (intent != null) {
            val bundle = intent.extras
            val oeuvreId = bundle?.getString("oeuvre_id") ?: ""
            if (oeuvreId != "") {
                println("replaceFrag $oeuvreId")
                FirestoreUtil.firestoreInstance.collection("oeuvre").document(oeuvreId).get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            val oeuvre: Oeuvre? = document?.toObject(Oeuvre::class.java)
                            replaceFragment(OeuvreInfoFragment(oeuvre, oeuvreId))
                        } else {
                            print("error")
                        }
                    }.addOnFailureListener { exception ->
                        Log.d("ErrorOeuvre", "get failed with ", exception)
                    }
            }
        }
        //init firebaseStorage
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        replaceFragment(ExplorerFragment())
    }
//    Methode qui gere l'affichage du fragment

    public fun toCamera() {
        startActivity<CameraActivity>()
    }

    public fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_layout, fragment)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    //    Methode de connection a Firebase (gere l'inscription si l'utilisateur n'est pas present dans les users de Firebase)
    public fun connection() {
        val intent = AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(singInProviders)
            .setLogo(R.drawable.ic_launcher_background)
            .build()
        startActivityForResult(intent, RC_SIGN_IN)
    }

    //    Methode qui lance l'image Picker
    public fun showFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select picture"), PICK_IMAGE_REQUEST)
    }

    //    Methode qui permet d'upload les image de l'image Picker dans la base de donnée Firebase
    fun uploadFile() {
        if (filePath != null) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()

            val imageRef = storageReference!!.child("Img/ProfilPicture/" + UUID.randomUUID().toString())
            imageRef.putFile(filePath)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    Toast.makeText(applicationContext, "File Uploaded", Toast.LENGTH_SHORT).show()

                    it.metadata!!.reference!!.downloadUrl.addOnSuccessListener { imageUrl ->
                        FirestoreUtil.updateImageUser(
                            imageUrl.toString()
                        )
                        Toast.makeText(applicationContext, "Save", Toast.LENGTH_SHORT).show()
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
    }


    fun updateUser(email: String, name: String, phone: String, radiobutton: RadioButton, age: String, ville: String) {
        FirestoreUtil.updateCurrentUser(
            email,
            name,
            phone,
            radiobutton.text.toString(),
            age,
            ville
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                val progressDialog = indeterminateProgressDialog("Setting up your account")
                FirestoreUtil.initCurrentUserIfFirstTime {
                    startActivity(intentFor<MainActivity>().newTask().clearTask())
                    progressDialog.dismiss()

                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                if (response == null) return

                when (response.error?.errorCode) {
                    ErrorCodes.NO_NETWORK ->
                        toast("no network")
                    ErrorCodes.UNKNOWN_ERROR -> {
                        toast("Unknown Error")
                    }

                }
            }
        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.data!!
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imageView_profile_picture.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        } else {
            if (resultCode == Activity.RESULT_OK) {

            }
        }
    }
}