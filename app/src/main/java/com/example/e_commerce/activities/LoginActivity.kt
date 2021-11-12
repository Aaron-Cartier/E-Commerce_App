package com.example.e_commerce.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.example.e_commerce.R
import com.example.e_commerce.firestore.FireStoreClass
import com.example.e_commerce.models.User
import com.example.e_commerce.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.core.FirestoreClient
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        //click event assigned to Forgot Password text
        tv_forgot_password.setOnClickListener(this)

        //click event assigned to Login button
        btn_login.setOnClickListener(this)

        //click event assigned to Register text
        tv_register.setOnClickListener(this )

//        tv_register.setOnClickListener {
//            //Launches the register screen when the user clicks on the text
//            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
//            startActivity(intent)
//        }
    }

    override fun  onClick(view: View?) {
        if(view != null) {
            when(view.id) {
                R.id.tv_forgot_password -> {
                    //Launches the forgot password screen when the user clicks on the text
                    val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                    startActivity(intent)
                }

                R.id.btn_login -> {
                    logInRegisteredUser()
                }

                R.id.tv_register -> {
                    //Launches the register screen when the user clicks on the text
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_email.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(et_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }else -> {
                //showErrorSnackBar("Your details are valid, Login successful", false)
                true
            }
        }
    }

    private fun logInRegisteredUser() {
        if(validateLoginDetails()) {
            //show the progress dialog
            showProgressDialog(resources.getString(R.string.please_wait))

            //show the text from editText and trim the space
            val email = et_email.text.toString().trim {it <= ' '}
            val password = et_password.text.toString().trim() {it <= ' '}

            //log in using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if(task.isSuccessful) {
                        FireStoreClass().getUserDetails(this@LoginActivity)
                    }else{
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }

    fun userLoggedInSuccess(user: User) {
        //hide the progress dialog
        hideProgressDialog()

        //print the user details in the log as of now
        Log.i("First name: ", user.firstName)
        Log.i("Last name: ", user.lastName)
        Log.i("Email: ", user.email)

        if(user.profileCompleted == 0) {
            //if the user profile is incomplete, then launch the UserProfileActivity
            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
            startActivity(intent)
        }else{
            //redirect the user to the main screen after log in
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
        finish()
    }
}