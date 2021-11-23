package com.example.e_commerce.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.e_commerce.R
import com.example.e_commerce.firestore.FireStoreClass
import com.example.e_commerce.models.User
import com.example.e_commerce.utils.Constants
import com.example.e_commerce.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException

class UserProfileActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mUserDeails: User
    private var mSelectedImageFileUri: Uri? = null
    private var mUserProfileImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        if(intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            //get the user details from intent as a ParcelableExtra
            mUserDeails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        et_first_name.isEnabled = false
        et_first_name.setText(mUserDeails.firstName)

        et_last_name.isEnabled = false
        et_last_name.setText(mUserDeails.lastName)

        et_email.isEnabled = false
        et_email.setText(mUserDeails.email)

        iv_user_photo.setOnClickListener(this@UserProfileActivity)

        btn_save.setOnClickListener(this@UserProfileActivity)
    }

    override fun onClick(v: View?) {
        if(v != null) {
            when(v.id) {
                R.id.iv_user_photo -> {
                    //Here we will check if the permission is already allowed or we need to request for it
                    //first of all we will check the READ_EXTERNAL_STORAGE permission and if it is allowed we will request for the same.
                    if(ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    )
                        {
                            //showErrorSnackBar("You already have the storage permission.", false)
                            Constants.showImageChooser(this)
                        }else{
                            /*Requests permissions to be granted to this application. These permissions
                             must be requested in your manifest, they should not be granted to your app,
                             and they should have protection level*/

                            ActivityCompat.requestPermissions(
                                this,
                                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                Constants.READ_STORAGE_PERMISSION_CODE
                            )
                    }
                }

                R.id.btn_save -> {
                    if(validateUserProfileDetails()) {
                        showProgressDialog(resources.getString(R.string.please_wait))

                        if(mSelectedImageFileUri != null) {
                            FireStoreClass().uploadImageToCloudStorage(this, mSelectedImageFileUri)
                        }else{
                            updateUserProfileDetails()
                        }
                    }
                }
            }
        }
    }

    private fun updateUserProfileDetails() {
        val userHashMap = HashMap<String, Any>()

        val mobileNumber = et_mobile_number.text.toString().trim {it <= ' '}

        val gender = if(rb_male.isChecked) {
            Constants.MALE
        }else{
            Constants.FEMALE
        }

        if(mUserProfileImageURL.isNotEmpty()) {
            userHashMap[Constants.IMAGE] = mUserProfileImageURL
        }
        if(mobileNumber.isNotEmpty()) {
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }

        //key: Gender value: male
        //gender:male
        userHashMap[Constants.GENDER] = gender

        userHashMap[Constants.COMPLETE_PROFILE] = 1

        //showProgressDialog(resources.getString(R.string.please_wait))

        FireStoreClass().updateUserProfileData(this, userHashMap)

        //showErrorSnackBar("Your details are valid. You can update them.", false)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //if permission is granted
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showErrorSnackBar("The storage permission is grant", false)
                }else{
                    //displaying another toast if permission is not granted
                Toast.makeText(this, resources.getString(R.string.read_storage_permission_denied), Toast.LENGTH_LONG).show()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if(data != null) {
                    try {
                        //the uri of selected image from phone storage
                        mSelectedImageFileUri = data.data!!

                        //iv_user_photo.setImageURI(selectedImageFileUri)
                        GlideLoader(this).loadUserPicture(mSelectedImageFileUri!!,iv_user_photo)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(this@UserProfileActivity, resources.getString(R.string.image_selection_failed),
                        Toast.LENGTH_SHORT).show()
                    }
                }
            }else if(resultCode == Activity.RESULT_CANCELED) {
                //A log is printed when user closes or cancels the image selection
                Log.e("Request cancelled", "Image selection cancelled")
            }
        }
    }

    private fun validateUserProfileDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_mobile_number.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_number), true)
                false
            }else -> {
                true
            }
        }
    }

    fun userProfileUpdateSuccess() {
        hideProgressDialog()

        Toast.makeText(
            this@UserProfileActivity, resources.getString(R.string.msg_profile_update_success),
            Toast.LENGTH_SHORT).show()

        startActivity(Intent(this@UserProfileActivity, MainActivity::class.java))
        finish()
    }

    fun imageUploadSuccess(imageUrl: String) {
        //hideProgressDialog()

//        Toast.makeText(this@UserProfileActivity, "Your image has been uploaded at the URL $imageUrl", Toast.LENGTH_SHORT)
//            .show()

        mUserProfileImageURL = imageUrl
        updateUserProfileDetails()
    }
}