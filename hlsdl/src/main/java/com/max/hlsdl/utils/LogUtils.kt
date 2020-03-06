package com.max.hlsdl.utils

import android.util.Log

private const val LOG_TAG = "HDL_LOG"

fun logD(msg: String) {
    Log.d(LOG_TAG, msg)
}

fun logI(msg: String) {
    Log.i(LOG_TAG, msg)
}

fun logE(msg: String) {
    Log.e(LOG_TAG, msg)
}
