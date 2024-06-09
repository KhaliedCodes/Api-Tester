package com.instabug.android_challenge.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.instabug.android_challenge.data.source.FakeHttpConnectionRepository
import com.instabug.android_challenge.model.Request
import com.instabug.android_challenge.model.Response
import com.instabug.android_challenge.model.enums.RequestMethodEnum
import com.instabug.android_challenge.testUtils.getOrAwaitValue
import org.junit.Assert.*;
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class MainViewModelTest {

    companion object{
        val responseSuccess = Response("fake url",
            "fake response body",
            null,
            null,
            200,
            emptyMap(),
            emptyMap(),
            200

        )

        val responseFailure = Response("fake url",
            null,
            "fake request body",
            "fake error body",
            400,
            emptyMap(),
            emptyMap(),
            200

        )

        var requestSuccessGet= Request("fake url",
            "fake response body",
            null,
            null,
            200,
            200,
            "GET",
        )

        var requestFailureGet= Request("fake url",
            null,
            "fake request body",
            "fake error body",
            400,
            200,
            "GET",
        )

        var requestSuccessPost= Request("fake url",
            "fake response body",
            null,
            null,
            200,
            200,
            "POST",
        )

        var requestFailurePost= Request("fake url",
            null,
            "fake request body",
            "fake error body",
            400,
            200,
            "POST",
        )
    }

    private val fakeRepository = FakeHttpConnectionRepository()

    private lateinit var mainViewModel: MainViewModel



    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        mainViewModel = MainViewModel(fakeRepository)
    }



    @Test
    fun `makeGETRequest returns successResponse`(){
        fakeRepository.response = responseSuccess
        mainViewModel.makeGETRequest()
        val result = mainViewModel.response.getOrAwaitValue()
        assertEquals(responseSuccess, result)
        assertTrue(fakeRepository.insertRequestCalled)
        assertEquals(fakeRepository.request, requestSuccessGet)
    }

    @Test
    fun `makeGETRequest returns failureResponse`(){
        fakeRepository.response = responseFailure
        mainViewModel.makeGETRequest()
        val result = mainViewModel.response.getOrAwaitValue()
        assertEquals(responseFailure, result)
        assertTrue(fakeRepository.insertRequestCalled)
        assertEquals(fakeRepository.request, requestFailureGet)

    }

    @Test
    fun `makeGETRequest returns null if exception is thrown`(){
        fakeRepository.throwException = true
        mainViewModel.makeGETRequest()
        val result = mainViewModel.response.getOrAwaitValue()
        assertNull(result)
        assertFalse(fakeRepository.insertRequestCalled)

    }

    @Test
    fun `makePOSTRequest returns responseSuccess`(){
        mainViewModel.selectedMethod = RequestMethodEnum.POST.name
        fakeRepository.response = responseSuccess
        mainViewModel.makePOSTRequest()
        val result = mainViewModel.response.getOrAwaitValue()
        assertEquals(responseSuccess, result)
        assertTrue(fakeRepository.insertRequestCalled)
        assertEquals(fakeRepository.request, requestSuccessPost)

    }

    @Test
    fun `makePOSTRequest returns responseFailure`(){
        mainViewModel.selectedMethod = RequestMethodEnum.POST.name
        fakeRepository.response = responseFailure
        mainViewModel.makePOSTRequest()
        val result = mainViewModel.response.getOrAwaitValue()
        assertEquals(responseFailure, result)
        assertTrue(fakeRepository.insertRequestCalled)
        assertEquals(fakeRepository.request, requestFailurePost)
    }

    @Test
    fun `makePOSTRequest returns null if exception is thrown`(){
        fakeRepository.throwException = true
        mainViewModel.makePOSTRequest()
        val result = mainViewModel.response.getOrAwaitValue()
        assertNull(result)
        assertFalse(fakeRepository.insertRequestCalled)

    }

    @Test
    fun `makePOSTMultipartRequest returns responseSuccess`(){
        mainViewModel.selectedMethod = RequestMethodEnum.POST.name
        fakeRepository.response = responseSuccess
        mainViewModel.makePOSTMultipartRequest()
        val result = mainViewModel.response.getOrAwaitValue()
        assertEquals(responseSuccess, result)
        assertTrue(fakeRepository.insertRequestCalled)
        assertEquals(fakeRepository.request, requestSuccessPost)
    }

    @Test
    fun `makePOSTMultipartRequest returns responseFailure`(){
        mainViewModel.selectedMethod = RequestMethodEnum.POST.name
        fakeRepository.response = responseFailure
        mainViewModel.makePOSTMultipartRequest()
        val result = mainViewModel.response.getOrAwaitValue()
        assertEquals(responseFailure, result)
        assertTrue(fakeRepository.insertRequestCalled)
        assertEquals(fakeRepository.request, requestFailurePost)
    }

    @Test
    fun `makePOSTMultipartRequest returns null if exception is thrown`(){
        fakeRepository.throwException = true
        mainViewModel.makePOSTMultipartRequest()
        val result = mainViewModel.response.getOrAwaitValue()
        assertNull(result)
        assertFalse(fakeRepository.insertRequestCalled)

    }


}