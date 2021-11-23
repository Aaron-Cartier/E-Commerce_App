package com.example.e_commerce.activities

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.e_commerce.R
import com.example.e_commerce.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import android.Manifest
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences(Constants.E_COMMERCE_PREFERENCES, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "")!!
        tv_main.text = "Hello $username."

//        btnCameraPermission.setOnClickListener {
//            if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
//               ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this,"You already have the permission to access camera and the GPS", Toast.LENGTH_LONG).show()
//            }else{
//                //Request permission
//                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA,
//                                                                        Manifest.permission.ACCESS_FINE_LOCATION), CAMERA_AND_FIND_LOCATION_PERMISSION_CODE)
//            }
//        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == CAMERA_PERMISSION_CODE) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission granted for Camera", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,"Permission denied", Toast.LENGTH_LONG).show()

            }
        }
    }

    companion object {
        private const val CAMERA_PERMISSION_CODE = 1
        private const val FINE_LOCATION_PERMISSION_CODE = 2
        private const val CAMERA_AND_FIND_LOCATION_PERMISSION_CODE = 12
    }
}