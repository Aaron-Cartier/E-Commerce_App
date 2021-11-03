package com.example.e_commerce.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import com.example.e_commerce.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        setupActionBar()

        tv_login.setOnClickListener {
            //Launches the register screen when the user clicks on the text
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        btn_register.setOnClickListener{
            registerUser()
        }
    }
    private fun setupActionBar() {
        setSupportActionBar(toolbar_register_activity)

        val actionBar = supportActionBar
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar_register_activity.setNavigationOnClickListener {onBackPressed()}
    }

    /**
     * A function to validate the entries of a new user
     */
    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_first_name.text.toString().trim {it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }

            TextUtils.isEmpty(et_last_name.text.toString().trim {it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }

            TextUtils.isEmpty(et_emailR.text.toString().trim {it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(et_passwordR.text.toString().trim {it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            TextUtils.isEmpty(et_confirm_password.text.toString().trim {it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_password), true)
                false
            }

            et_passwordR.text.toString().trim {it <= ' '} != et_confirm_password.text.toString().trim {it <= ' '} -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_and_confirm_password_mismatch), true)
                false
            }

            !cb_terms_and_condition.isChecked -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_agree_terms_and_condition),true)
                false
            }
            else -> {
                //showErrorSnackBar("Your details are valid", false)
                true
            }
        }
    }

    private fun registerUser() {
        if(validateRegisterDetails()) {
            val email: String = et_emailR.text.toString().trim {it <= ' '}
            val password: String = et_passwordR.text.toString().trim {it <= ' '}

            //create an instance and create a register a user with email and password
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(OnCompleteListener<AuthResult> { task ->

                    //if the registration is successfully done
                    if(task.isSuccessful) {
                        //firebase registered user
                        val firebaseUser: FirebaseUser = task.result!!.user!!
                        showErrorSnackBar("You have successfully registered. Your user id is ${firebaseUser.uid}", false)
                    }else{
                        //if the registering is not success then shows error message
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                })
        }
    }
}