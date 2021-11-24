package com.example.e_commerce.ui.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.e_commerce.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_progress.*

open class BaseActivity : AppCompatActivity() {

    private lateinit var mProgressDialog: Dialog

    private var doubleBackToExitClickedOnce = false


    fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if(errorMessage) {
            snackBarView.setBackgroundColor(ContextCompat.getColor(this@BaseActivity, R.color.colorSnackBarError))
        }else{
            snackBarView.setBackgroundColor(ContextCompat.getColor(this@BaseActivity, R.color.colorSnackBarSuccess))
        }
        snackBar.show()

    }

    fun showProgressDialog(text: String) {
        mProgressDialog = Dialog(this)

        /*
        set the screen content from a layout resource
        the resource will be inflated, adding all top-level views to the screen
         */
        mProgressDialog.setContentView(R.layout.dialog_progress)

        mProgressDialog.tv_progress_text.setText(text)

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        //start the dialog and display it on screen.
        mProgressDialog.show()
    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

    fun doubleBackToExit() {
        if(doubleBackToExitClickedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitClickedOnce = true
        Toast.makeText(this, resources.getString(R.string.please_click_back_again_to_exit), Toast.LENGTH_LONG)
            .show()

        @Suppress("DEPRECATION")
        Handler().postDelayed({doubleBackToExitClickedOnce = false}, 2000)
    }
}