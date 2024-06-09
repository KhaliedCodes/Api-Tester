package com.instabug.android_challenge.data.source.remote

import com.instabug.android_challenge.data.source.DataSource
import com.instabug.android_challenge.data.source.HttpConnectionRepository
import com.instabug.android_challenge.model.Filter
import com.instabug.android_challenge.model.Request
import com.instabug.android_challenge.model.Response
import com.instabug.android_challenge.model.Sort
import java.io.File

class RemoteDataSource(private val httpConnectionService: HttpConnectionService): DataSource {
    override fun sendGetRequest(url: String, headers: Map<String, String>): Response =
        httpConnectionService.sendGetRequest(url, headers)

    override fun sendPostRequest(url: String, body: String, headers: Map<String, String>): Response =
        httpConnectionService.sendPostRequest(url, body, headers)

    override fun sendPostRequestMultiPart(url: String, file: File?, headers: Map<String, String>): Response =
        httpConnectionService.sendPostRequestMultiPart(url, file, headers)


    override fun insertRequest(request: Request) {
        TODO("Not yet implemented")
    }
    override fun getAllRequests(filter: Filter?, sort: Sort?): List<Request> {
        TODO("Not yet implemented")
    }


}