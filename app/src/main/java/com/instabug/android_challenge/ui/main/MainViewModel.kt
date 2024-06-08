package com.instabug.android_challenge.ui.main

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.instabug.android_challenge.data.HttpConnectionRepository
import com.instabug.android_challenge.model.Header
import com.instabug.android_challenge.model.Request
import com.instabug.android_challenge.model.Response
import com.instabug.android_challenge.model.enums.PostTypeEnum
import com.instabug.android_challenge.model.enums.RequestMethodEnum
import java.io.File
import java.util.concurrent.Executors

class MainViewModel(private val httpConnectionRepository: HttpConnectionRepository) : ViewModel() {
    var selectedMethod: String = RequestMethodEnum.GET.name
    var selectedPostType: String = PostTypeEnum.JSON.name
    var currentUploadFile: File? = null
    var currentUploadFileName = MutableLiveData("")
    var urlInput = ""
    var fileUri:Uri? = null

    private val _url = MutableLiveData("")
    val url : LiveData<String>
        get() = _url

    val headerList = mutableListOf<Header>()


    var requestBodyInput = ""



    private val _response = MutableLiveData<Response?>()
    val response : LiveData<Response?>
        get() = _response


    private val _loading = MutableLiveData(false)
    val loading : LiveData<Boolean>
        get() = _loading



    fun makeGETRequest(){
        _loading.value = true
        Executors.newSingleThreadExecutor().execute{
            try {
                val res = httpConnectionRepository.sendGetRequest(urlInput, headerList.associate { it.key to it.value })

                httpConnectionRepository.insertRequest(Request(
                    res.url,
                    res.responseBody,
                    res.requestBody,
                    res.errorBody,
                    res.code,
                    res.executionTime,
                    selectedMethod
                ))
                _response.postValue(res)
            }catch (e : Exception){
                _response.postValue(null)
            }
            _loading.postValue(false)
        }


    }

    fun makePOSTRequest(){

        _loading.value = true
        Executors.newSingleThreadExecutor().execute {
            try {
                val res = httpConnectionRepository.sendPostRequest(urlInput, requestBodyInput, headerList.associate { it.key to it.value })
                httpConnectionRepository.insertRequest(Request(
                    res.url,
                    res.responseBody,
                    res.requestBody,
                    res.errorBody,
                    res.code,
                    res.executionTime,
                    selectedMethod
                ))
                _response.postValue(res)
            }catch (e: Exception){
                _response.postValue(null)
            }
            _loading.postValue(false)
        }
    }


    fun makePOSTMultipartRequest(){

        _loading.value = true
        Executors.newSingleThreadExecutor().execute {
            try {
                val res = httpConnectionRepository.sendPostRequestMultiPart(urlInput, currentUploadFile, headerList.associate { it.key to it.value })
                _response.postValue(res)

                httpConnectionRepository.insertRequest(Request(
                    res.url,
                    res.responseBody,
                    res.requestBody,
                    res.errorBody,
                    res.code,
                    res.executionTime,
                    selectedMethod
                ))
            }catch (e: Exception){
                _response.postValue(null)
            }
            _loading.postValue(false)
        }
    }



}