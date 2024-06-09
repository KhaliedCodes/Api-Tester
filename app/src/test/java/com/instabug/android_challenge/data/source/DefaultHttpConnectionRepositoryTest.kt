package com.instabug.android_challenge.data.source

import com.instabug.android_challenge.data.fakeSource.FakeDataSource
import com.instabug.android_challenge.model.Filter
import com.instabug.android_challenge.model.Request
import com.instabug.android_challenge.model.Response
import com.instabug.android_challenge.model.Sort
import com.instabug.android_challenge.model.enums.SortDirection
import com.instabug.android_challenge.testUtils.getOrAwaitValue
import com.instabug.android_challenge.ui.cachedRequests.CachedRequestsViewModelTest
import com.instabug.android_challenge.ui.main.MainViewModelTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class DefaultHttpConnectionRepositoryTest {

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

    private lateinit var repository: HttpConnectionRepository

    private val fakeDataSource = FakeDataSource()


    @Before
    fun setup(){
        repository = DefaultHttpConnectionRepository(fakeDataSource, fakeDataSource)
    }


    @Test
    fun `sendGetRequest returns successResponse`(){
        fakeDataSource.response = responseSuccess
        val result = repository.sendGetRequest(responseSuccess.url)
        Assert.assertEquals(responseSuccess, result)
    }

    @Test
    fun `makeGETRequest returns failureResponse`(){
        fakeDataSource.response = MainViewModelTest.responseFailure
        val result = repository.sendGetRequest(responseFailure.url)
        Assert.assertEquals(MainViewModelTest.responseFailure, result)

    }

    @Test
    fun `makeGETRequest returns null if exception is thrown`(){
        fakeDataSource.throwException = true
        Assert.assertThrows(Exception::class.java) { repository.sendGetRequest(responseSuccess.url) }

    }

    @Test
    fun `makePOSTRequest returns responseSuccess`(){
        fakeDataSource.response = MainViewModelTest.responseSuccess
        val result = repository.sendPostRequest(responseSuccess.url)
        Assert.assertEquals(MainViewModelTest.responseSuccess, result)

    }

    @Test
    fun `makePOSTRequest returns responseFailure`(){
        fakeDataSource.response = MainViewModelTest.responseFailure
        val result = repository.sendPostRequest(responseFailure.url)
        Assert.assertEquals(MainViewModelTest.responseFailure, result)
    }

    @Test
    fun `makePOSTRequest throws if exception is thrown`(){
        fakeDataSource.throwException = true
        Assert.assertThrows(Exception::class.java) { repository.sendPostRequest(responseSuccess.url) }

    }

    @Test
    fun `makePOSTMultipartRequest returns responseSuccess`(){
        fakeDataSource.response = MainViewModelTest.responseSuccess
        val result = repository.sendPostRequestMultiPart(responseSuccess.url, null)
        Assert.assertEquals(MainViewModelTest.responseSuccess, result)
    }

    @Test
    fun `makePOSTMultipartRequest returns responseFailure`(){
        fakeDataSource.response = MainViewModelTest.responseFailure
        val result = repository.sendPostRequestMultiPart(responseFailure.url, null)
        Assert.assertEquals(MainViewModelTest.responseFailure, result)
    }

    @Test
    fun `makePOSTMultipartRequest returns null if exception is thrown`(){
        fakeDataSource.throwException = true
        Assert.assertThrows(Exception::class.java) {
            repository.sendPostRequestMultiPart(
                responseSuccess.url,
                null
            )
        }

    }


    @Test
    fun `getAllRequests returns all of requestsList`(){
        fakeDataSource.requestsList = CachedRequestsViewModelTest.requestsList
        val result = repository.getAllRequests(null, null)
        Assert.assertEquals(CachedRequestsViewModelTest.requestsList, result)
    }
    @Test
    fun `getAllRequests returns requestsList filtered by GET method`(){
        fakeDataSource.requestsList = CachedRequestsViewModelTest.requestsList
        val filter = Filter("method", "GET")
        val result = repository.getAllRequests(filter, null)
        Assert.assertEquals(
            CachedRequestsViewModelTest.requestsList.filter { it.method == "GET" },
            result
        )
    }

    @Test
    fun `getAllRequests returns requestsList sorted by execution time descending`(){
        fakeDataSource.requestsList = CachedRequestsViewModelTest.requestsList
        val sort = Sort("executionTime", SortDirection.DESC)
        val result = repository.getAllRequests(null, sort)
        Assert.assertEquals(
            CachedRequestsViewModelTest.requestsList.sortedByDescending { it.executionTime },
            result
        )
    }
    @Test
    fun `getAllRequests returns requestsList filtered by POST method and sorted by execution time ascending`(){
        fakeDataSource.requestsList = CachedRequestsViewModelTest.requestsList
        val filter = Filter("method", "POST")
        val sort = Sort("executionTime", SortDirection.ASC)
        val result = repository.getAllRequests(filter, sort)
        Assert.assertEquals(CachedRequestsViewModelTest.requestsList.filter { it.method == "POST" }
            .sortedBy { it.executionTime }, result)
    }


    @Test
    fun `getAllRequests returns empty list if exception is thrown`(){
        fakeDataSource.throwException = true
        Assert.assertThrows(Exception::class.java) { repository.getAllRequests(null, null) }
    }
}