package com.instabug.android_challenge.data.local

import android.provider.BaseColumns

object RequestDbContract {
    object Request: BaseColumns{
        const val TABLE_NAME = "request"
        const val COLUMN_NAME_URL = "url"
        const val COLUMN_NAME_METHOD = "method"
        const val COLUMN_NAME_REQUEST_BODY = "request_body"
        const val COLUMN_NAME_RESPONSE_BODY = "response_body"
        const val COLUMN_NAME_STATUS_CODE = "status_code"
        const val COLUMN_NAME_STATUS = "status"
        const val COLUMN_NAME_ERROR_BODY = "error_body"
        const val COLUMN_NAME_EXECUTION_TIME = "execution_time"

    }

    const val SQL_CREATE_REQUESTS =
        "CREATE TABLE ${Request.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${Request.COLUMN_NAME_URL} TEXT," +
                "${Request.COLUMN_NAME_METHOD} TEXT," +
                "${Request.COLUMN_NAME_REQUEST_BODY} TEXT," +
                "${Request.COLUMN_NAME_RESPONSE_BODY} TEXT," +
                "${Request.COLUMN_NAME_STATUS_CODE} INTEGER," +
                "${Request.COLUMN_NAME_STATUS} INTEGER," +
                "${Request.COLUMN_NAME_ERROR_BODY} TEXT," +
                "${Request.COLUMN_NAME_EXECUTION_TIME} INTEGER)"

    const val SQL_DELETE_REQUESTS = "DROP TABLE IF EXISTS ${Request.TABLE_NAME}"
}