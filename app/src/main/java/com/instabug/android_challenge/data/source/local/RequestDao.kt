package com.instabug.android_challenge.data.source.local

import android.content.ContentValues
import android.provider.BaseColumns
import com.instabug.android_challenge.model.Filter
import com.instabug.android_challenge.model.Request
import com.instabug.android_challenge.model.Sort

class RequestDao(dbHandler: DbHandler?) {
    private val dbWrite = dbHandler?.writableDatabase
    private val dbRead = dbHandler?.readableDatabase
    fun insertRequest(request: Request){
        val values = ContentValues().apply {
            put(RequestDbContract.Request.COLUMN_NAME_URL, request.url)
            put(RequestDbContract.Request.COLUMN_NAME_METHOD, request.method)
            put(RequestDbContract.Request.COLUMN_NAME_REQUEST_BODY, request.requestBody)
            put(RequestDbContract.Request.COLUMN_NAME_RESPONSE_BODY, request.responseBody)
            put(RequestDbContract.Request.COLUMN_NAME_STATUS_CODE, request.code)
            put(RequestDbContract.Request.COLUMN_NAME_STATUS, if(request.code in 200..299) 1 else 0)
            put(RequestDbContract.Request.COLUMN_NAME_ERROR_BODY, request.errorBody)
            put(RequestDbContract.Request.COLUMN_NAME_EXECUTION_TIME, request.executionTime)
        }

        dbWrite?.insert(RequestDbContract.Request.TABLE_NAME, null, values)
    }


    fun getRequests(filter: Filter?, sort: Sort?): List<Request>{

        val cursor = dbRead?.rawQuery("SELECT * FROM ${RequestDbContract.Request.TABLE_NAME}" +
                (if(filter?.filterBy != null && filter.filterValue != null) " WHERE ${filter.filterBy} = \"${filter.filterValue}\""  else "") +
                (if(sort?.sortBy != null && sort.sortDirection != null) " ORDER BY ${sort.sortBy}  ${sort.sortDirection.name}" else ""),
            arrayOf())
        val requests = mutableListOf<Request>()
        cursor?.let {
            with(it){
                while(moveToNext()){
                    requests.add(
                        Request(
                            getString(getColumnIndexOrThrow(RequestDbContract.Request.COLUMN_NAME_URL)),
                            getString(getColumnIndexOrThrow(RequestDbContract.Request.COLUMN_NAME_RESPONSE_BODY)),
                            getString(getColumnIndexOrThrow(RequestDbContract.Request.COLUMN_NAME_REQUEST_BODY)),
                            getString(getColumnIndexOrThrow(RequestDbContract.Request.COLUMN_NAME_ERROR_BODY)),
                            getInt(getColumnIndexOrThrow(RequestDbContract.Request.COLUMN_NAME_STATUS_CODE)),
                            getLong(getColumnIndexOrThrow(RequestDbContract.Request.COLUMN_NAME_EXECUTION_TIME)),
                            getString(getColumnIndexOrThrow(RequestDbContract.Request.COLUMN_NAME_METHOD)),
                            id = getInt(getColumnIndexOrThrow(BaseColumns._ID))
                        )
                    )
                }
            }
        }
        cursor?.close()
        return requests
    }
}