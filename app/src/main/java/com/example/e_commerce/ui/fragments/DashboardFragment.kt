package com.example.e_commerce.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentDashboardBinding
import com.example.e_commerce.firestore.FireStoreClass
import com.example.e_commerce.models.Product
import com.example.e_commerce.ui.activities.SettingsActivity
import com.example.e_commerce.ui.adapters.DashboardItemsListAdapter
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : BaseFragment() {

    //private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //if we want to use the option menu in fragment we need to add it
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()

        getDashboardItemsList()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun successDashboardItemsList(dashboardItemsList: ArrayList<Product>) {
        hideProgressDialog()

        if(dashboardItemsList.size > 0) {
            rv_dashboard_items.visibility = View.VISIBLE
            tv_no_dashboard_items_found.visibility = View.GONE

            rv_dashboard_items.layoutManager = GridLayoutManager(activity,2)
            rv_dashboard_items.setHasFixedSize(true)

            val  adapter = DashboardItemsListAdapter(requireActivity(), dashboardItemsList)
            rv_dashboard_items.adapter = adapter
        }else{
            rv_dashboard_items.visibility = View.GONE
            tv_no_dashboard_items_found.visibility = View.VISIBLE
        }
    }

    private fun getDashboardItemsList() {
        //show the progress dialog
        showProgressDialog(resources.getString(R.string.please_wait))

        FireStoreClass().getDashboardItemsList(this@DashboardFragment)

    }
}