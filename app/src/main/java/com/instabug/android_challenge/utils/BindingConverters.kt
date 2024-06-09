package com.instabug.android_challenge.utils

import android.view.View
import androidx.databinding.BindingConversion

object BindingConverters {
    @BindingConversion
    @JvmStatic fun booleanToVisibility(isVisible: Boolean): Int {
        return if (isVisible) View.VISIBLE else View.GONE
    }
}