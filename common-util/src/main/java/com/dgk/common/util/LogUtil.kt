@file:JvmName("LogUtil")

package com.dgk.common.util

/**
 * 日志工具类
 * Created by daigaokai on 2018/4/23.
 */

val LOG_TAG_DEFAULT_PREFIX = "KLog|"

// 计算事件耗时
var startTime: Long = 0
var stopTime: Long = 0
var currentEventName: String? = ""

/**
 * 开始记录事件
 * - 开始计时
 * - 输出日志
 */
fun startRecordEvent(eventName: String) {
    startTime = System.currentTimeMillis()
    currentEventName = eventName
    printlnDefault("$eventName|Start")
}

/**
 * 结束记录事件
 * - 结束计时，计算耗时
 * - 输出日志
 */
fun stopRecordEvent(eventName: String) {
    if (eventName == currentEventName) {
        stopTime = System.currentTimeMillis()
        printlnDefault("$eventName|Stop|Time:${stopTime - startTime}ms")
    }
}

/**
 * 默认在日志前加上统一的前缀，方便过滤日志
 */
private fun printlnDefault(msg: String) {
    println("$LOG_TAG_DEFAULT_PREFIX$msg")
}