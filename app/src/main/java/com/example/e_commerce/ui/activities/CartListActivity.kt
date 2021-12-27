package com.example.e_commerce.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerce.R
import com.example.e_commerce.firestore.FireStoreClass
import com.example.e_commerce.models.CartItem
import com.example.e_commerce.models.Product
import com.example.e_commerce.ui.adapters.CartItemsListAdapter
import com.example.e_commerce.utils.Constants
import com.google.api.Distribution
import kotlinx.android.synthetic.main.activity_cart_list.*

class CartListActivity : BaseActivity() {

    private lateinit var mProductsList: ArrayList<Product>
    private lateinit var mCartListItems: ArrayList<CartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)

        setupActionBar()

        btn_checkout.setOnClickListener {
            val intent = Intent(this@CartListActivity, AddressListActivity::class.java)
            intent.putExtra(Constants.EXTRA_SELECT_ADDRESS, true)
            startActivity(intent)
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_cart_list_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        toolbar_cart_list_activity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onResume() {
        super.onResume()
        //getCartItemsList()
        getProductList()
    }

    private fun getCartItemsList() {
        //showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getCartList(this@CartListActivity)
    }

    private fun getProductList() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getAllProductsList(this)
    }

    /**
     * A function to notify the success result of the cart items list from cloud firestore.
     *
     * @param cartList
     */
    fun successCartItemsList(cartList: ArrayList<CartItem>) {
        // Hide progress dialog.
        hideProgressDialog()

        for(product in mProductsList) {
            for(cartItem in cartList) {
                if(product.product_id == cartItem.product_id) {
                    cartItem.stock_quantity = product.stock_quantity

                    if(product.stock_quantity.toInt() == 0) {
                        cartItem.cart_quantity = product.stock_quantity
                    }
                }
            }
        }
        mCartListItems = cartList

        if(cartList.size > 0) {
            rv_cart_items_list.visibility = View.VISIBLE
            ll_checkout.visibility = View.VISIBLE
            tv_no_cart_item_found.visibility = View.GONE

            rv_cart_items_list.layoutManager = LinearLayoutManager(this@CartListActivity)
            rv_cart_items_list.setHasFixedSize(true)
            val cartListAdapter = CartItemsListAdapter(this@CartListActivity, mCartListItems, true)
            rv_cart_items_list.adapter = cartListAdapter

            var subTotal: Double = 0.0

            for(item in mCartListItems) {
                val availableQuantity = item.stock_quantity.toInt()

                if(availableQuantity > 0) {
                    val price = item.price.toDouble()
                    val quantity = item.cart_quantity.toInt()
                    subTotal += (price * quantity)
                }

            }

            tv_sub_total.setText("$$subTotal")
            tv_shipping_charge.setText("$10.0")

            if(subTotal > 0) {
                ll_checkout.visibility = View.VISIBLE

                val total = subTotal + 10
                tv_total_amount.setText("$$total")
            }else{
                ll_checkout.visibility = View.GONE
            }
        }
        else{
            rv_cart_items_list.visibility = View.GONE
            ll_checkout.visibility = View.GONE
            tv_no_cart_item_found.visibility = View.VISIBLE
        }
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {
        hideProgressDialog()
        mProductsList = productsList
        getCartItemsList()
    }

    fun itemRemovedSuccess() {
        hideProgressDialog()
        Toast.makeText(this@CartListActivity, resources.getString(R.string.msg_item_removed_successfully),
        Toast.LENGTH_SHORT).show()

        getCartItemsList()
    }

    fun itemUpdateSuccess() {
        hideProgressDialog()
        getCartItemsList()
    }
}