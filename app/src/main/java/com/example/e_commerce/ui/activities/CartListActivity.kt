package com.example.e_commerce.ui.activities

import android.os.Bundle
import android.util.Log
import com.example.e_commerce.R
import com.example.e_commerce.firestore.FireStoreClass
import com.example.e_commerce.models.CartItem
import kotlinx.android.synthetic.main.activity_cart_list.*

class CartListActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)

        setupActionBar()
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

        getCartItemsList()
    }

    private fun getCartItemsList() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getCartList(this@CartListActivity)
    }

    /**
     * A function to notify the success result of the cart items list from cloud firestore.
     *
     * @param cartList
     */
    fun successCartItemsList(cartList: ArrayList<CartItem>) {
        // Hide progress dialog.
        hideProgressDialog()

        for(i in cartList) {
            Log.i("Cart Item Title", i.title)
        }

//        if (cartList.size > 0) {
//            rv_cart_items_list.visibility = View.VISIBLE
//            ll_checkout.visibility = View.VISIBLE
//            tv_no_cart_item_found.visibility = View.GONE
//
//            rv_cart_items_list.layoutManager = LinearLayoutManager(this@CartListActivity)
//            rv_cart_items_list.setHasFixedSize(true)
//
//            val cartListAdapter = CartItemsListAdapter(this@CartListActivity, cartList)
//            rv_cart_items_list.adapter = cartListAdapter
//
//            var subTotal: Double = 0.0
//
//            for (item in cartList) {
//                val price = item.price.toDouble()
//                val quantity = item.cart_quantity.toInt()
//                subTotal += (price * quantity)
//            }
//
//            tv_sub_total.setText("$$subTotal")
//            // Here we have kept Shipping Charge is fixed as $10 but in your case it may cary.
//            // Also, it depends on the location and total amount.
//            tv_shipping_charge.setText("$10.0")
//
//            if (subTotal > 0) {
//                ll_checkout.visibility = View.VISIBLE
//
//                val total = subTotal + 10
//                tv_total_amount.setText("$$total")
//            } else {
//                ll_checkout.visibility = View.GONE
//            }
//
//        } else {
//            rv_cart_items_list.visibility = View.GONE
//            ll_checkout.visibility = View.GONE
//            tv_no_cart_item_found.visibility = View.VISIBLE
//        }
    }
}