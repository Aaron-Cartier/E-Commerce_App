package com.example.e_commerce.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.e_commerce.R
import com.example.e_commerce.models.User
import com.example.e_commerce.utils.Constants
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException

class UserProfileActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        var userDetails: User = User()
        if(intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            //get the user details from intent as a ParcelableExtra
            userDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        et_first_name.isEnabled = false
        et_first_name.setText(userDetails.firstName)

        et_last_name.isEnabled = false
        et_last_name.setText(userDetails.lastName)

        et_email.isEnabled = false
        et_email.setText(userDetails.email)

        iv_user_photo.setOnClickListener(this@UserProfileActivity)
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
            }
        }
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
                    //displaying another toast if persmission is not granted
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
                        val selectedImageFileUri = data.data!!

                        iv_user_photo.setImageURI(selectedImageFileUri)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(this@UserProfileActivity, resources.getString(R.string.image_selection_failed),
                        Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}