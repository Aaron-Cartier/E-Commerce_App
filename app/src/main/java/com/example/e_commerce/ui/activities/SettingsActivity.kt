package com.example.e_commerce.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import com.example.e_commerce.R
import com.example.e_commerce.firestore.FireStoreClass
import com.example.e_commerce.models.User
import com.example.e_commerce.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    override fun onResume() {
        super.onResume()

        getUserDetails()
    }

    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        setSupportActionBar(toolbar_settings_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_settings_activity.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     * A function to get the user details from firestore.
     */
    private fun getUserDetails() {

        // Show the progress dialog
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of Firestore class to get the user details from firestore which is already created.
        FireStoreClass().getUserDetails(this@SettingsActivity)
    }

    /**
     * A function to receive the user details and populate it in the UI.
     */
    fun userDetailsSuccess(user: User) {
        // Hide the progress dialog
        hideProgressDialog()

        // Load the image using the Glide Loader class.
        GlideLoader(this@SettingsActivity).loadUserPicture(user.image, iv_user_photo)

        tv_name.text = Editable.Factory.getInstance().newEditable("${user.firstName} ${user.lastName}") //"${user.firstName} ${user.lastName}"
        tv_gender.text = Editable.Factory.getInstance().newEditable(user.gender) //user.gender
        tv_email.text = Editable.Factory.getInstance().newEditable(user.email) //user.email
        tv_mobile_number.text = Editable.Factory.getInstance().newEditable("${user.mobile}") //"${user.mobile}"
    }
}