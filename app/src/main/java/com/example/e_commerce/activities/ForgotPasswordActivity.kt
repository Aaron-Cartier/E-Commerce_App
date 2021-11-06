package com.example.e_commerce.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.e_commerce.R
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_register.*

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        setupActionBar()
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_forgot_password_activity)

        val actionBar = supportActionBar
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar_forgot_password_activity.setNavigationOnClickListener {onBackPressed()}
    }
}