package com.instabug.android_challenge.data.source

import com.instabug.android_challenge.model.Filter
import com.instabug.android_challenge.model.Request
import com.instabug.android_challenge.model.Response
import com.instabug.android_challenge.model.Sort
import com.instabug.android_challenge.model.enums.SortDirection
import java.io.File

class FakeHttpConnectionRepository: HttpConnectionRepository {


    var response: Response? = null

    var request: Request? = null

    var requestsList: List<Request> = emptyList()

    var insertRequestCalled = false
    var throwException: Boolean = false




    override fun sendGetRequest(url: String, headers: Map<String, String>): Response {
        return response!!
    }

    override fun sendPostRequest(
        url: String,
        body: String,
        headers: Map<String, String>
    ): Response {
        if(throwException) throw RuntimeException("Mocked exception")
        return response!!
    }

    override fun sendPostRequestMultiPart(
        url: String,
        file: File?,
        headers: Map<String, String>
    ): Response {
        if(throwException) throw RuntimeException("Mocked exception")
        return response!!
    }

    override fun insertRequest(request: Request) {
        this.request = request
        insertRequestCalled = true
        return
    }

    override fun getAllRequests(filter: Filter?, sort: Sort?): List<Request> {

        if(throwException) throw RuntimeException("Mocked exception")
        val filteredList = requestsList.filter {
            if(filter?.filterBy != null && filter.filterValue != null){
                val field = it::class.java.getDeclaredField("${filter.filterBy}")
                field.isAccessible = true
                field.get(it) == filter.filterValue
            } else true
        }

        val sortedList = if(sort?.sortBy != null && sort.sortDirection != null){
            if(sort.sortDirection == SortDirection.ASC) filteredList.sortedBy { it.executionTime }
            else filteredList.sortedByDescending { it.executionTime }
        } else filteredList

        return sortedList
    }
}