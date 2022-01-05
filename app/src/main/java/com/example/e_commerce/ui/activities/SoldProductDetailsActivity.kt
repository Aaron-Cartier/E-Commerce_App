package com.example.e_commerce.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract
import com.example.e_commerce.R
import com.example.e_commerce.models.SoldProduct
import com.example.e_commerce.utils.Constants

class SoldProductDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sold_product_details)

        var productDetails: SoldProduct = SoldProduct()

        if(intent.hasExtra(Constants.EXTRA_SOLD_PRODUCT_DETAILS)) {
            productDetails = intent.getParcelableExtra<SoldProduct>(Constants.EXTRA_SOLD_PRODUCT_DETAILS)!!
        }
    }
}