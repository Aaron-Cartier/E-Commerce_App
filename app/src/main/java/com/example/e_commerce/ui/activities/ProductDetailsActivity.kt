package com.example.e_commerce.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.e_commerce.R
import com.example.e_commerce.firestore.FireStoreClass
import com.example.e_commerce.models.CartItem
import com.example.e_commerce.models.Product
import com.example.e_commerce.utils.Constants
import com.example.e_commerce.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_product_details.*

class ProductDetailsActivity : BaseActivity(), View.OnClickListener {

    private var mProductId: String = ""
    private lateinit var mProductDetails: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        setupActionBar()

        if(intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }

        var productOwnerId: String = ""

        if(intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)) {
            productOwnerId = intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
        }

        if(FireStoreClass().getCurrentUserId() == productOwnerId){
            btn_add_to_cart.visibility = View.GONE
            btn_cart.visibility = View.GONE
        }else{
            btn_add_to_cart.visibility = View.VISIBLE
        }

        getProductDetails()

        btn_add_to_cart.setOnClickListener(this)
        btn_cart.setOnClickListener(this)
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
        mProductDetails = product

        GlideLoader(this@ProductDetailsActivity).loadUserPicture(
            product.image,
            iv_product_detail_image
        )
        tv_product_details_title.setText(product.title)
        tv_product_details_price.setText("${product.price}")
        tv_product_details_description.setText(product.description)
        tv_product_details_stock_quantity.setText(product.stock_quantity)

        if(product.stock_quantity.toInt() == 0) {
            hideProgressDialog()
            btn_add_to_cart.visibility = View.GONE
            tv_product_details_stock_quantity.setText(resources.getString(R.string.lbl_out_of_stock))
            tv_product_details_stock_quantity.setTextColor(ContextCompat.getColor(this@ProductDetailsActivity, R.color.colorSnackBarError))
        }else{
            if(FireStoreClass().getCurrentUserId() == product.user_id) {
                hideProgressDialog()
            }else{
                FireStoreClass().checkIfItemExistsInCart(this, mProductId)
            }
        }

        if(FireStoreClass().getCurrentUserId() == product.user_id) {
            hideProgressDialog()
        }else{
            FireStoreClass().checkIfItemExistsInCart(this, mProductId)
        }
    }

    private fun addToCart() {
        val cartItem = CartItem(
            FireStoreClass().getCurrentUserId(),
            mProductId,
            mProductDetails.title,
            mProductDetails.price,
            mProductDetails.image,
            Constants.DEFAULT_CART_QUANTITY
        )

        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().addCartItems(this, cartItem)
    }

    fun addToCartSuccess() {
        hideProgressDialog()
        Toast.makeText(this@ProductDetailsActivity,
        resources.getString(R.string.successfully_added_item_to_cart),
        Toast.LENGTH_SHORT
        ).show()

        btn_add_to_cart.visibility = View.GONE
        btn_cart.visibility = View.VISIBLE
    }

    fun productExistsInCart() {
        hideProgressDialog()
        btn_cart.visibility = View.VISIBLE
        btn_add_to_cart.visibility = View.GONE
    }

    override fun onClick(v: View?) {
        if(v != null) {
            when(v.id) {
                R.id.btn_add_to_cart -> {
                    addToCart()
                }
            }

            when(v.id) {
                R.id.btn_cart -> {
                startActivity(Intent(this@ProductDetailsActivity, CartListActivity::class.java))
                }
            }
        }
    }

}