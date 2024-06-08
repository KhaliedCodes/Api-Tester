package com.instabug.android_challenge.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.instabug.android_challenge.R
import com.instabug.android_challenge.ServiceLocator
import com.instabug.android_challenge.Utils.FilesUtils
import com.instabug.android_challenge.model.enums.RequestMethodEnum
import com.instabug.android_challenge.databinding.ActivityMainBinding
import com.instabug.android_challenge.factory.MainViewModelFactory
import com.instabug.android_challenge.model.Header
import com.instabug.android_challenge.model.enums.PostTypeEnum
import com.instabug.android_challenge.ui.cachedRequests.CachedRequestsActivity
import java.io.File
import java.net.URI

class MainActivity : AppCompatActivity(){
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            ServiceLocator.provideHttpConnectionRepository(this)
        )
    }

    private val permissionCode = 101

    private val attachmentsActivityLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()){uri ->
        val outputFolder = File(filesDir,"upload")
        FilesUtils.createFolder(outputFolder)
        val fileStream = try{
            this.contentResolver.openInputStream(uri!!)
        } catch (e: Exception){
            Toast.makeText(this, resources.getText(R.string.file_upload_cannot_read_file_error), Toast.LENGTH_LONG).show()
            return@registerForActivityResult
        }
        val outputFileName = FilesUtils.getFileName(contentResolver, uri)
        viewModel.currentUploadFile = FilesUtils.convertInputStreamToFile(fileStream,outputFileName,outputFolder)
        viewModel.currentUploadFileName.value = outputFileName
        viewModel.fileUri = uri
    }

    private lateinit var binding : ActivityMainBinding
    private lateinit var headerListAdapter: HeaderListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setupPostTypeSpinner()
        setupMethodSpinner()
        setupListeners()
        setupHeaderListAdapter()
        setupObservables()
        setupUi()
    }


    private fun setupUi(){
        setSupportActionBar(binding.toolbar)
        binding.fileNameTv.paint.isUnderlineText = true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_tool_bar_actions, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when(item.itemId){
            R.id.action_cached_requests -> {
                startActivity(Intent(this, CachedRequestsActivity::class.java))
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }

    }

    private fun setupHeaderListAdapter(){
        headerListAdapter = HeaderListAdapter(viewModel.headerList) {
            viewModel.headerList.removeAt(it)
            headerListAdapter.notifyItemRemoved(it)
            headerListAdapter.notifyItemRangeChanged(it, viewModel.headerList.size)
        }
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation =  LinearLayoutManager.VERTICAL
        binding.headerListRv.layoutManager = linearLayoutManager
        binding.headerListRv.adapter = headerListAdapter
    }


    private fun setupListeners(){
        binding.sendButton.setOnClickListener {
            when(viewModel.selectedMethod){
                RequestMethodEnum.GET.name -> {
                    if(!validate()) return@setOnClickListener
                    showResult()
                    viewModel.makeGETRequest()
                }
                RequestMethodEnum.POST.name -> {
                    if(viewModel.selectedPostType == PostTypeEnum.JSON.name){
                        if(!validate()) return@setOnClickListener
                        showResult()
                        viewModel.makePOSTRequest()
                    }
                    if (viewModel.selectedPostType == PostTypeEnum.Multipart.name){
                        if(!validate()) return@setOnClickListener
                        showResult()
                        viewModel.makePOSTMultipartRequest()
                    }
                }
            }

        }

        binding.addHeaderButton.setOnClickListener {
            viewModel.headerList.add(Header("", ""))
            headerListAdapter.currentList = viewModel.headerList
            headerListAdapter.notifyItemInserted(viewModel.headerList.size - 1)
        }

        binding.selectFileButton.setOnClickListener {
            if(checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_DENIED
                && checkSelfPermission(Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_DENIED
                && checkSelfPermission(Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_DENIED){
                attachmentsActivityLauncher.launch(arrayOf("*/*"))
            } else {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE),permissionCode)
            }
        }

        binding.fileNameTv.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            val mime = contentResolver.getType(viewModel.fileUri!!)
            intent.setDataAndType(viewModel.fileUri!!, mime)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            permissionCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    attachmentsActivityLauncher.launch(arrayOf("*/*"))

                }
            }
        }
    }

    private fun showResult(){
        binding.resultContainer.isVisible = false
    }

    private fun HideResult(){
        binding.resultContainer.isVisible = true
    }


    private fun validate(): Boolean{
        val urlRegex = Regex("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)")

        if(!viewModel.urlInput.matches(urlRegex)) {
            binding.urlEditText.error = "Invalid url"
            return false
        }
        if(viewModel.headerList.any { it.key.isBlank() || it.value.isBlank()}){
            Toast.makeText(this, "Please fill up all of the header fields", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }


    private fun setupPostTypeSpinner(){
        val spinner = binding.postTypeSpinner
        ArrayAdapter.createFromResource(this, R.array.post_type, com.google.android.material.R.layout.support_simple_spinner_dropdown_item).also {
            it.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item)
            spinner.adapter = it
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                viewModel.selectedPostType = parent?.getItemAtPosition(pos) as String
                when(viewModel.selectedPostType){
                    PostTypeEnum.JSON.name-> {
                        if(viewModel.selectedMethod == RequestMethodEnum.POST.name){
                            binding.bodyEditText.isVisible = true
                            binding.fileUploadContainer.isVisible = false
                        }
                    }
                    PostTypeEnum.Multipart.name -> {
                        if(viewModel.selectedMethod == RequestMethodEnum.POST.name) {
                            binding.bodyEditText.isVisible = false
                            binding.fileUploadContainer.isVisible = true
                        }
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }


    private fun setupMethodSpinner(){
        val spinner = binding.methodsSpinner
        ArrayAdapter.createFromResource(this, R.array.request_methods, com.google.android.material.R.layout.support_simple_spinner_dropdown_item).also {
            it.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item)
            spinner.adapter = it
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                viewModel.selectedMethod = parent?.getItemAtPosition(pos) as String
                when(viewModel.selectedMethod){
                    RequestMethodEnum.GET.name -> {
                        binding.postTypeSpinner.isVisible = false
                        binding.bodyEditText.isVisible = false
                        binding.fileUploadContainer.isVisible = false
                    }
                    RequestMethodEnum.POST.name -> {
                        binding.postTypeSpinner.isVisible = true
                        if(viewModel.selectedPostType == PostTypeEnum.JSON.name)
                            binding.bodyEditText.isVisible = true
                        else
                            binding.fileUploadContainer.isVisible = true
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun setupObservables(){
        viewModel.response.observe(this) {
            if (it == null) {
                Toast.makeText(this, "Could not send request.", Toast.LENGTH_LONG).show()
                return@observe
            }
            binding.requestHeaders.text = it?.requestHeaders?.toList()
                ?.joinToString("\n") { "${it.first} : ${it.second}" }
            binding.requestQuery.text =
                URI.create(viewModel.url.value).query?.split("&")?.joinToString("\n")
            binding.responseHeaders.text = it?.responseHeaders?.toList()
                ?.joinToString("\n") { "${it.first} : ${it.second}" }
            HideResult()
        }
    }



}


