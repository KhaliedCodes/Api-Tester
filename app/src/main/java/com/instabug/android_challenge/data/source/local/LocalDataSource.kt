package com.instabug.android_challenge.data.source.local

import com.instabug.android_challenge.data.source.DataSource
import com.instabug.android_challenge.model.Filter
import com.instabug.android_challenge.model.Request
import com.instabug.android_challenge.model.Response
import com.instabug.android_challenge.model.Sort
import java.io.File

class LocalDataSource(private val requestDao: RequestDao): DataSource {
    override fun sendGetRequest(url: String, headers: Map<String, String>): Response {
        TODO("Not yet implemented")
    }

    override fun sendPostRequest(url: String, body: String, headers: Map<String, String>): Response {
        TODO("Not yet implemented")
    }

    override fun sendPostRequestMultiPart(url: String, file: File?, headers: Map<String, String>): Response {
        TODO("Not yet implemented")
    }


    override fun insertRequest(request: Request) =
        requestDao.insertRequest(request)


    override fun getAllRequests(filter: Filter?, sort: Sort?): List<Request> =
        requestDao.getRequests(filter,sort)
}