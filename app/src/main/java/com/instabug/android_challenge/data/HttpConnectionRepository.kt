package com.instabug.android_challenge.data

import com.instabug.android_challenge.data.local.RequestDao
import com.instabug.android_challenge.data.remote.HttpConnectionService
import com.instabug.android_challenge.model.Filter
import com.instabug.android_challenge.model.Request
import com.instabug.android_challenge.model.Response
import com.instabug.android_challenge.model.Sort
import java.io.File

class HttpConnectionRepository(private val httpConnectionService: HttpConnectionService, private val requestDao: RequestDao) {
    fun sendGetRequest(url: String, headers: Map<String, String> = emptyMap()): Response =
        httpConnectionService.sendGetRequest(url, headers)

    fun sendPostRequest(url: String, body: String = "", headers: Map<String, String> = emptyMap()): Response =
        httpConnectionService.sendPostRequest(url, body, headers)

    fun sendPostRequestMultiPart(url: String, file: File?, headers: Map<String, String> = emptyMap()): Response =
        httpConnectionService.sendPostRequestMultiPart(url, file, headers)


    fun insertRequest(request: Request) =
        requestDao.insertRequest(request)


    fun getAllRequests(filter: Filter?, sort: Sort?): List<Request> =
        requestDao.getRequests(filter,sort)



}