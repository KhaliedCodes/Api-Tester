package com.instabug.android_challenge

import android.content.Context
import com.instabug.android_challenge.data.source.DefaultHttpConnectionRepository
import com.instabug.android_challenge.data.source.HttpConnectionRepository
import com.instabug.android_challenge.data.source.local.DbHandler
import com.instabug.android_challenge.data.source.local.LocalDataSource
import com.instabug.android_challenge.data.source.local.RequestDao
import com.instabug.android_challenge.data.source.remote.HttpConnectionService
import com.instabug.android_challenge.data.source.remote.RemoteDataSource

object ServiceLocator {
    var dbHandler: DbHandler? = null

    fun provideHttpConnectionRepository(context: Context): HttpConnectionRepository {
        synchronized(this) {
            return DefaultHttpConnectionRepository(
                RemoteDataSource(HttpConnectionService()),
                LocalDataSource(RequestDao(getDatabase(context)))
            )
        }
    }
    fun createDatabase(context: Context): DbHandler = DbHandler(context).also { dbHandler = it }

    fun getDatabase(context: Context): DbHandler = dbHandler?: createDatabase(context)
}