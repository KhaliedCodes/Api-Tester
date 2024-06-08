package com.instabug.android_challenge.ui.cachedRequests

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.instabug.android_challenge.data.source.HttpConnectionRepository
import com.instabug.android_challenge.model.Filter
import com.instabug.android_challenge.model.Request
import com.instabug.android_challenge.model.Sort
import java.util.concurrent.Executors

class CachedRequestsViewModel(private val httpConnectionRepository: HttpConnectionRepository) : ViewModel() {

    var isSorted: Boolean? = null

    var filter: Filter? = null

    private var sort: Sort? = null

    private val _requestList = MutableLiveData<List<Request>>()
    val requestList : LiveData<List<Request>>
        get() = _requestList

    private val _loading = MutableLiveData(false)
    val loading : LiveData<Boolean>
        get() = _loading

    init {
        getAllRequests()
    }

    fun getAllRequests(filter: Filter? = this.filter, sort: Sort? = this.sort){
        _loading.value = true
        this.filter = filter
        this.sort = sort
        Executors.newSingleThreadExecutor().execute {
            _requestList.postValue(httpConnectionRepository.getAllRequests(this.filter, this.sort))
            _loading.postValue(false)
        }
    }
}