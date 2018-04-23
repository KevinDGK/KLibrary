@file:JvmName("DateUtil")
package com.dgk.common.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by daigaokai on 2018/4/11.
 */

const val DATE_PATTERN_01 = "yyyyMMddHHmmssSSS"
const val DATE_PATTERN_02 = "yyyy-MM-dd HH:mm:ss:SSS"
const val DATE_PATTERN_DEFAULT = DATE_PATTERN_01

/**
 * 格式化日期
 * @param time 时间戳
 * @param pattern 日期格式，默认为DATE_PATTERN_DEFAULT
 */
fun format(time: Long = System.currentTimeMillis(), pattern: String = DATE_PATTERN_DEFAULT): String
        = SimpleDateFormat(pattern, Locale.getDefault()).format(time)


/**
 * 判断日期是否为今天
 */
fun isToday(date: Long): Boolean {
    val today = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(System.currentTimeMillis())
    val temp = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(date)
    return today == temp
}