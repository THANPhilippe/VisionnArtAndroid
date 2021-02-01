package com.example.visionnart.fragment


//import com.example.visionnart.glide.GlideApp
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import co.metalab.asyncawait.async
import com.example.visionnart.MainActivity
import com.example.visionnart.utils.FirestoreUtil
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_my_account.*
import kotlinx.android.synthetic.main.fragment_my_account.view.*
import android.R
import androidx.fragment.app.ListFragment


class MyaccountFragment : Fragment() {

    private lateinit var radiobutton: RadioButton
    private lateinit var auth: FirebaseAuth
    private var mStorageRef: StorageReference? = null
    private var profilepictureChange = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mStorageRef = FirebaseStorage.getInstance().getReference();

        FirestoreUtil.getCurrentUser { user ->
            if (this@MyaccountFragment.isVisible) {
                if (user != null && profilepictureChange == 0 && user.image.isNotEmpty()) {
                    Picasso.get().load(user.image).into(imageView_profile_picture);
                }
                if (user != null) {
                    editText_email.setText(user.email)
                }
                if (user != null) {
                    editText_name.setText(user.name)
                }
                if (user != null) {
                    editText_phone.setText(user.phone)
                }
                if (user != null) {
                    when {
                        user.gender == "Homme" -> radioButtonMale.isChecked = true
                        user.gender == "Femme" -> radioButtonFemale.isChecked = true
                        else -> {

                        }
                    }
                }
                if (user != null) {
                    editText_Ville.setText(user.town)
                }
                if (user != null) {
                    editText_Age2.setText(user.age)
                }
            }
        }


        val view = inflater.inflate(com.example.visionnart.R.layout.fragment_my_account, container, false)


        view.apply {


            btn_save.setOnClickListener {
                val radioId = radioGroup.getCheckedRadioButtonId();
                radiobutton = findViewById(radioId)
                if(profilepictureChange == 1){
                    (activity as MainActivity).uploadFile()
                }
                (activity as MainActivity).updateUser(
                    editText_email.text.toString(),
                    editText_name.text.toString(),
                    editText_phone.text.toString(),
                    radiobutton,
                    editText_Age2.text.toString(),
                    editText_Ville.text.toString()
                )
                loadContentFragment()
            }

            btn_sign_out.setOnClickListener {
                AuthUI.getInstance()
                    .signOut(this@MyaccountFragment.context!!)
                    .addOnCompleteListener {
                        auth = FirebaseAuth.getInstance()
                        auth.signInAnonymously()
                        (activity as MainActivity).replaceFragment(SigninFragment())
                    }
            }


            modifyprofilpicture.setOnClickListener {
                profilepictureChange = 1
                (activity as MainActivity).showFileChooser()
                Picasso.get().load((activity as MainActivity).PICK_IMAGE_REQUEST)
                    .into(imageView_profile_picture);
            }
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

    }

    private fun loadContentFragment() {
        fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
    }

}
