package com.instabug.android_challenge.data.source


import com.instabug.android_challenge.model.Filter
import com.instabug.android_challenge.model.Request
import com.instabug.android_challenge.model.Response
import com.instabug.android_challenge.model.Sort
import java.io.File



class DefaultHttpConnectionRepository(private val remoteDataSource: DataSource, private val localDataSource: DataSource) :
    HttpConnectionRepository {
    override fun sendGetRequest(url: String, headers: Map<String, String>): Response =
        remoteDataSource.sendGetRequest(url, headers)

    override fun sendPostRequest(url: String, body: String, headers: Map<String, String>): Response =
        remoteDataSource.sendPostRequest(url, body, headers)

    override fun sendPostRequestMultiPart(url: String, file: File?, headers: Map<String, String>): Response =
        remoteDataSource.sendPostRequestMultiPart(url, file, headers)


    override fun insertRequest(request: Request) =
        localDataSource.insertRequest(request)


    override fun getAllRequests(filter: Filter?, sort: Sort?): List<Request> =
        localDataSource.getAllRequests(filter,sort)



}