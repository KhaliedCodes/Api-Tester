package com.instabug.android_challenge.model

import com.instabug.android_challenge.model.enums.SortDirection

data class Sort (
    val sortBy: String?,
    val sortDirection: SortDirection?
)