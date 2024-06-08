package com.instabug.android_challenge.data.source

import com.instabug.android_challenge.model.Filter
import com.instabug.android_challenge.model.Request
import com.instabug.android_challenge.model.Response
import com.instabug.android_challenge.model.Sort
import java.io.File

class FakeHttpConnectionRepository: HttpConnectionRepository {

    companion object{
        val response = Response("fake url",
            "fake response body",
            "false request body",
            "fake error body",
            200,
            emptyMap(),
            emptyMap(),
            200

        )

        val request = Request("fake url",
            "fake response body",
            "false request body",
            "fake error body",
            200,
            200,
            "GET",
        )
    }
    override fun sendGetRequest(url: String, headers: Map<String, String>): Response {
        return response
    }

    override fun sendPostRequest(
        url: String,
        body: String,
        headers: Map<String, String>
    ): Response {
        return response
    }

    override fun sendPostRequestMultiPart(
        url: String,
        file: File?,
        headers: Map<String, String>
    ): Response {
        return response
    }

    override fun insertRequest(request: Request) {
        return
    }

    override fun getAllRequests(filter: Filter?, sort: Sort?): List<Request> {
        return listOf(request)
    }
}