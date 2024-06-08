package com.instabug.android_challenge.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.instabug.android_challenge.data.source.FakeHttpConnectionRepository
import com.instabug.android_challenge.testUtils.getOrAwaitValue
import com.instabug.android_challenge.ui.main.MainViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class MainViewModelTest {

    private val fakeRepository = FakeHttpConnectionRepository()

    private lateinit var mainViewModel: MainViewModel



    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        mainViewModel = MainViewModel(fakeRepository)
    }



    @Test
    fun getRequest(){
        mainViewModel.makeGETRequest()
        val res = mainViewModel.response.getOrAwaitValue()
        Assert.assertEquals(FakeHttpConnectionRepository.response, res )
    }

    @Test
    fun makePOSTRequest(){
        mainViewModel.makePOSTRequest()
        val res = mainViewModel.response.getOrAwaitValue()
        Assert.assertEquals(FakeHttpConnectionRepository.response, res )
    }

    @Test
    fun makePOSTMultipartRequest(){
        mainViewModel.makePOSTMultipartRequest()
        val res = mainViewModel.response.getOrAwaitValue()
        Assert.assertEquals(FakeHttpConnectionRepository.response, res )
    }


}