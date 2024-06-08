package com.instabug.android_challenge.ui.cachedRequests

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.instabug.android_challenge.R
import com.instabug.android_challenge.ServiceLocator
import com.instabug.android_challenge.data.local.RequestDbContract
import com.instabug.android_challenge.databinding.ActivityCachedRequestsBinding
import com.instabug.android_challenge.databinding.PopupFilterListBinding
import com.instabug.android_challenge.factory.CachedRequestsViewModelFactory
import com.instabug.android_challenge.model.Filter
import com.instabug.android_challenge.model.Sort
import com.instabug.android_challenge.model.enums.RequestMethodEnum
import com.instabug.android_challenge.model.enums.SortDirection


class CachedRequestsActivity : AppCompatActivity() {
    private val viewModel: CachedRequestsViewModel by viewModels{
        CachedRequestsViewModelFactory(
            ServiceLocator.provideHttpConnectionRepository(this)
        )
    }
    private lateinit var binding : ActivityCachedRequestsBinding

    private lateinit var cachedRequestsAdapter: CachedRequestsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCachedRequestsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setSupportActionBar(binding.toolbar)

        setupAdapter()
        setupObservables()
        setupUi()
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.cached_requests_tool_bar_actions, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when(item.itemId){
            R.id.action_sort_requests -> {
                if(!viewModel.isSorted){
                    item.icon = ResourcesCompat.getDrawable(resources, R.drawable.icon_sort_desc, theme)
                    viewModel.getAllRequests( sort = Sort(
                        RequestDbContract.Request.COLUMN_NAME_EXECUTION_TIME,
                        SortDirection.ASC
                    )
                    )
                }
                if(viewModel.isSorted){
                    item.icon = ResourcesCompat.getDrawable(resources, R.drawable.icon_sort_asc, theme)
                    viewModel.getAllRequests( sort = Sort(
                        RequestDbContract.Request.COLUMN_NAME_EXECUTION_TIME,
                        SortDirection.DESC
                    ))
                }
                true
            }
            R.id.action_filter_requests -> {

                val popupWindowBinding =
                    PopupFilterListBinding.inflate(layoutInflater)
                val popupWindow =  PopupWindow(
                    popupWindowBinding.root,
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    true
                )
                val allBtn = popupWindowBinding.radioAll
                val typeGetBtn = popupWindowBinding.radioGetType
                val typePostBtn = popupWindowBinding.radioPostType
                val statusSuccessBtn = popupWindowBinding.radioStatusSuccess
                val statusFailedBtn = popupWindowBinding.radioStatusFailed

                when {
                    (viewModel.filter?.filterBy == RequestDbContract.Request.COLUMN_NAME_METHOD
                            && viewModel.filter?.filterValue == RequestMethodEnum.GET.name) -> {
                        typeGetBtn.isChecked = true
                            }
                    (viewModel.filter?.filterBy == RequestDbContract.Request.COLUMN_NAME_METHOD
                            && viewModel.filter?.filterValue == RequestMethodEnum.POST.name) -> {
                        typePostBtn.isChecked = true
                    }
                    (viewModel.filter?.filterBy == RequestDbContract.Request.COLUMN_NAME_STATUS
                            && viewModel.filter?.filterValue == "1") -> {
                        statusSuccessBtn.isChecked = true
                    }
                    (viewModel.filter?.filterBy == RequestDbContract.Request.COLUMN_NAME_STATUS
                            && viewModel.filter?.filterValue == "0") -> {
                        statusFailedBtn.isChecked = true
                    }
                    else -> {
                        allBtn.isChecked = true
                    }
                }
                allBtn.setOnCheckedChangeListener { _, checked ->
                    if(checked){
                        viewModel.getAllRequests(filter = null)
                        popupWindow.dismiss()
                    }
                }

                typeGetBtn.setOnCheckedChangeListener { _, checked ->
                    if(checked){
                        viewModel.getAllRequests(filter = Filter(
                            RequestDbContract.Request.COLUMN_NAME_METHOD,
                            RequestMethodEnum.GET.name
                        ))
                        popupWindow.dismiss()
                    }
                }

                typePostBtn.setOnCheckedChangeListener { _, checked ->
                    if(checked){
                        viewModel.getAllRequests(filter = Filter(
                            RequestDbContract.Request.COLUMN_NAME_METHOD,
                            RequestMethodEnum.POST.name
                        ))
                        popupWindow.dismiss()
                    }
                }
                statusSuccessBtn.setOnCheckedChangeListener { _, checked ->
                    if(checked){
                        viewModel.getAllRequests(filter = Filter(
                            RequestDbContract.Request.COLUMN_NAME_STATUS,
                            "1"
                        ))
                        popupWindow.dismiss()
                    }
                }
                statusFailedBtn.setOnCheckedChangeListener { _, checked ->
                    if(checked){
                        viewModel.getAllRequests(filter = Filter(
                            RequestDbContract.Request.COLUMN_NAME_STATUS,
                            "0"
                        ))
                        popupWindow.dismiss()
                    }
                }

                popupWindow.showAsDropDown(binding.overflow)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }

        }

    private fun setupUi(){

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
    }


    private fun setupAdapter(){
        cachedRequestsAdapter = CachedRequestsAdapter()
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation =  LinearLayoutManager.VERTICAL
        binding.requestsListRv.layoutManager = linearLayoutManager
        binding.requestsListRv.adapter = cachedRequestsAdapter
    }

    private fun setupObservables(){
        viewModel.requestList.observe(this){
            cachedRequestsAdapter.submitList(it){
                binding.requestsListRv.scrollToPosition(0)
            }
        }
    }
}