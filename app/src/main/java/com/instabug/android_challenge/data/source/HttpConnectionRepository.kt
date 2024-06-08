package com.instabug.android_challenge.data.source

import com.instabug.android_challenge.model.Filter
import com.instabug.android_challenge.model.Request
import com.instabug.android_challenge.model.Response
import com.instabug.android_challenge.model.Sort
import java.io.File

interface HttpConnectionRepository {

    fun sendGetRequest(url: String, headers: Map<String, String> = emptyMap()): Response
    fun sendPostRequest(url: String, body: String = "", headers: Map<String, String> = emptyMap()): Response
    fun sendPostRequestMultiPart(url: String, file: File?, headers: Map<String, String> = emptyMap()): Response
    fun insertRequest(request: Request)
    fun getAllRequests(filter: Filter?, sort: Sort?): List<Request>
}