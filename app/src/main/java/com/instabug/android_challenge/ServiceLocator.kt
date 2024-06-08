package com.instabug.android_challenge

import android.content.Context
import com.instabug.android_challenge.data.HttpConnectionRepository
import com.instabug.android_challenge.data.local.DbHandler
import com.instabug.android_challenge.data.local.RequestDao
import com.instabug.android_challenge.data.remote.HttpConnectionService

object ServiceLocator {
    var dbHandler: DbHandler? = null

    fun provideHttpConnectionRepository(context: Context): HttpConnectionRepository {
        synchronized(this) {
            return HttpConnectionRepository(
                HttpConnectionService(),
                RequestDao(getDatabase(context))
            )
        }
    }
    fun createDatabase(context: Context): DbHandler = DbHandler(context).also { dbHandler = it }

    fun getDatabase(context: Context):DbHandler = dbHandler?: createDatabase(context)
}