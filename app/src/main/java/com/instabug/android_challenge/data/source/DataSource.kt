package com.instabug.android_challenge.data.source

import com.instabug.android_challenge.model.Filter
import com.instabug.android_challenge.model.Request
import com.instabug.android_challenge.model.Response
import com.instabug.android_challenge.model.Sort
import java.io.File

interface DataSource {
    fun sendGetRequest(url: String, headers: Map<String, String>): Response

     fun sendPostRequest(
        url: String,
        body: String,
        headers: Map<String, String>
    ): Response

    fun sendPostRequestMultiPart(
        url: String,
        file: File?,
        headers: Map<String, String>
    ): Response

    fun insertRequest(request: Request)

    fun getAllRequests(filter: Filter?, sort: Sort?): List<Request>
}