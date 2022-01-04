package com.example.e_commerce.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerce.R
import com.example.e_commerce.firestore.FireStoreClass
import com.example.e_commerce.models.SoldProduct
import com.example.e_commerce.ui.adapters.SoldProductsListAdapter
import kotlinx.android.synthetic.main.fragment_sold_product.*

/**
 * A simple [Fragment] subclass.
 * Use the [SoldProductFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SoldProductFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sold_product, container, false)
    }

    private fun getSoldProductsList() {
        //show the progress dialog
        showProgressDialog(resources.getString(R.string.please_wait))

        //call the function of firestore class
        FireStoreClass().getSoldProductsList(this@SoldProductFragment)
    }

    fun successSoldProductsList(soldProductsList: ArrayList<SoldProduct>) {
        hideProgressDialog()
        if(soldProductsList.size > 0) {
            rv_sold_product_items.visibility = View.VISIBLE
            tv_no_sold_products_found.visibility = View.GONE

            rv_sold_product_items.layoutManager = LinearLayoutManager(activity)
            rv_sold_product_items.setHasFixedSize(true)

            val soldProductsListAdapter = SoldProductsListAdapter(requireActivity(), soldProductsList)
            rv_sold_product_items.adapter = soldProductsListAdapter
        }else{
            rv_sold_product_items.visibility = View.GONE
            tv_no_sold_products_found.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        getSoldProductsList()
    }
}