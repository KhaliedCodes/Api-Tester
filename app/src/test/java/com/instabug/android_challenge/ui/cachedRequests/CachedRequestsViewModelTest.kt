package com.instabug.android_challenge.ui.cachedRequests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.instabug.android_challenge.data.fakeSource.FakeHttpConnectionRepository
import com.instabug.android_challenge.model.Filter
import com.instabug.android_challenge.model.Request
import com.instabug.android_challenge.model.Sort
import com.instabug.android_challenge.model.enums.SortDirection
import com.instabug.android_challenge.testUtils.getOrAwaitValue
import org.junit.Assert.*;
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class CachedRequestsViewModelTest {

    companion object{
        val requestsList = listOf(
            Request("fake url",
                "fake response body",
                null,
                null,
                200,
                200,
                "GET",
            ),
            Request("fake url",
                null,
                "fake request body",
                "fake error body",
                400,
                200,
                "GET",
            ),
            Request("fake url",
                "fake response body",
                null,
                null,
                200,
                200,
                "POST",
            ),
            Request("fake url",
                null,
                "fake request body",
                "fake error body",
                400,
                200,
                "POST",
            )
        )

    }

    private val fakeRepository = FakeHttpConnectionRepository()

    private lateinit var viewModel: CachedRequestsViewModel



    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        viewModel = CachedRequestsViewModel(fakeRepository)
    }


    @Test
    fun `getAllRequests returns all of requestsList`(){
        fakeRepository.requestsList = requestsList
        viewModel.getAllRequests()
        val result = viewModel.requestList.getOrAwaitValue()
        assertEquals(requestsList, result)
    }
    @Test
    fun `getAllRequests returns requestsList filtered by GET method`(){
        fakeRepository.requestsList = requestsList
        val filter = Filter("method", "GET")
        viewModel.getAllRequests(filter)
        val result = viewModel.requestList.getOrAwaitValue()
        assertEquals(requestsList.filter { it.method == "GET" }, result)
    }

    @Test
    fun `getAllRequests returns requestsList sorted by execution time descending`(){
        fakeRepository.requestsList = requestsList
        val sort = Sort("executionTime", SortDirection.DESC)
        viewModel.getAllRequests(sort = sort)
        val result = viewModel.requestList.getOrAwaitValue()
        assertEquals(requestsList.sortedByDescending { it.executionTime }, result)
    }
    @Test
    fun `getAllRequests returns requestsList filtered by POST method and sorted by execution time ascending`(){
        fakeRepository.requestsList = requestsList
        val filter = Filter("method", "POST")
        val sort = Sort("executionTime", SortDirection.ASC)
        viewModel.getAllRequests(filter, sort)
        val result = viewModel.requestList.getOrAwaitValue()
        assertEquals(requestsList.filter { it.method == "POST" }.sortedBy { it.executionTime }, result)
    }


    @Test
    fun `getAllRequests returns empty list if exception is thrown`(){
        fakeRepository.throwException = true
        viewModel.getAllRequests()
        val result = viewModel.requestList.getOrAwaitValue()
        assertEquals(emptyList<Request>(), result)
    }

}