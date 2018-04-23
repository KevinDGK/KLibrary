@file:JvmName("ExceptionUtil")
package com.dgk.common.util

/**
 * 异常处理相关
 * Created by daigaokai on 2018/4/23.
 */

/**
 * 输出异常信息，包含异常的Message和堆栈信息
 */
fun printThrowable(t: Throwable?) {
    println("The message of the throwable: ${t?.message}")
    println("The stack trace information related to the throwable:")
    val stackTrace = t?.stackTrace
    if (stackTrace != null && stackTrace.isNotEmpty()) {
        for (i in 0 until stackTrace.size) {
            println("\t${stackTrace[i]}")
        }
    }
}