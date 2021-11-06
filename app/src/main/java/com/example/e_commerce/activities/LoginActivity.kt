package com.example.e_commerce.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.example.e_commerce.R
import com.google.firebase.auth.FirebaseAuth
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
                    //hide the progress dialog
                    hideProgressDialog()

                    if(task.isSuccessful) {
                        //TODO - send user to main activity
                        showErrorSnackBar("You are now logged in", false)
                    }else{
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }
}