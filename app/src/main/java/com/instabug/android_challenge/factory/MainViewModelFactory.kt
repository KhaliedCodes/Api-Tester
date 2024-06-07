package com.instabug.android_challenge.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.instabug.android_challenge.data.remote.HttpConnectionService
import com.instabug.android_challenge.ui.MainViewModel

class MainViewModelFactory( private val httpConnectionService: HttpConnectionService): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = (MainViewModel(httpConnectionService) as T)
}