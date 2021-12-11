package com.example.e_commerce.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.e_commerce.R
import com.example.e_commerce.firestore.FireStoreClass
import com.example.e_commerce.models.Product
import com.example.e_commerce.utils.Constants
import com.example.e_commerce.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_product_details.*

class ProductDetailsActivity : BaseActivity() {

    private var mProductId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        setupActionBar()

        if(intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
            Log.i("product id", mProductId)
        }

        getProductDetails()
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_product_details_activity)
        val actionBar = supportActionBar
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_product_details_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getProductDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getProductDetails(this, mProductId)
    }
    fun productDetailsSuccess(product: Product) {
        hideProgressDialog()
        GlideLoader(this@ProductDetailsActivity).loadUserPicture(
            product.image,
            iv_product_detail_image
        )
        tv_product_details_title.setText(product.title)
        tv_product_details_price.setText("${product.price}")
        tv_product_details_description.setText(product.description)
        tv_product_details_stock_quantity.setText(product.stock_quantity)
    }

}