@file:JvmName("KLog")
package com.dgk.common.util

import android.util.Log

/**
 * 日志工具类
 * Created by daigaokai on 2018/4/26.
 */

// 默认在日志前加上统一的前缀，方便过滤日志
const val LOG_TAG_DEFAULT_PREFIX = "KLog"


fun KLogi(msg: String) {
    KLogi(null, msg)
}

fun KLogi(cls: Any?, msg: String) {
    if (cls == null) {
        Log.i(LOG_TAG_DEFAULT_PREFIX, msg)
    } else {
        Log.i(LOG_TAG_DEFAULT_PREFIX, "${cls.javaClass.simpleName}|$msg")
    }
}

fun KLoge(msg: String) {
    KLogi(null, msg)
}

fun KLoge(cls: Any?, msg: String) {
    if (cls == null) {
        Log.i(LOG_TAG_DEFAULT_PREFIX, msg)
    } else {
        Log.e(LOG_TAG_DEFAULT_PREFIX, "${cls.javaClass.simpleName}|$msg")
    }
}