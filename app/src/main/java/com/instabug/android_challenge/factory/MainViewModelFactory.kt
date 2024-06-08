package com.instabug.android_challenge.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.instabug.android_challenge.data.HttpConnectionRepository
import com.instabug.android_challenge.ui.main.MainViewModel

class MainViewModelFactory( private val httpConnectionRepository: HttpConnectionRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = (MainViewModel(httpConnectionRepository) as T)
}