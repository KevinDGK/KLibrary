@file:JvmName("TaskRecorder")

package com.dgk.common.util

/**
 * 任务日志工具类
 * - 记录任务执行的日志
 * - 计算任务耗时
 * Created by daigaokai on 2018/4/23.
 */

/**
 * 任务
 *
 * @param name 任务名称
 * @param startTime 任务开始时间
 */
data class TaskEvent(val name: String? = "", val startTime: Long = 0L)

const val MAX_TASK_EVENT_CACHE = 10

/**
 * 任务缓存器
 */
val cacheMap = mutableMapOf<String, TaskEvent>()

/**
 * 开始记录任务
 */
@Deprecated(message = "建议使用另一个带类名标记的方法",
        replaceWith = ReplaceWith("startTaskRecorder(null, taskName)"))
fun startTaskRecorder(taskName: String) {
    startTaskRecorder(null, taskName)
}

/**
 * 开始记录任务
 * - 开始计时
 * - 输出日志
 * @param cls 类
 * @param taskName 任务名称，不可为null
 */
fun startTaskRecorder(cls: Any?, taskName: String) {

    // 任务名称不允许为""
    if (taskName.isEmpty()) return

    // 如果任务缓存器长度达到阈值则不加入集合
    if (cacheMap.size >= MAX_TASK_EVENT_CACHE) {
        KLoge("警告|任务缓存器Size达到阈值：$MAX_TASK_EVENT_CACHE")
        return
    }

    // 缓存的任务名 = 类名 + 任务名
    val realTaskName = if (cls == null) {
        taskName
    } else {
        "${cls.javaClass.simpleName}|$taskName"
    }

    // 如果任务不在缓存器中则加入缓存器，否则仅提示
    if (realTaskName !in cacheMap) {
        cacheMap[realTaskName] = TaskEvent(realTaskName, System.currentTimeMillis())
        KLogi("$realTaskName|开始")
    } else {
        KLoge("警告|$realTaskName 已经在任务缓存器中!")
    }
}

/**
 * 结束记录任务
 */
@Deprecated(message = "建议使用另一个带类名标记的方法",
        replaceWith = ReplaceWith("stopTaskRecorder(null, taskName)"))
fun stopTaskRecorder(taskName: String) {
    stopTaskRecorder(null, taskName)
}

/**
 * 结束记录任务
 * - 结束计时，计算耗时
 * - 输出日志
 */
fun stopTaskRecorder(cls: Any?, taskName: String) {

    // 缓存的任务名 = 类名 + 任务名
    val realTaskName = if (cls == null) {
        taskName
    } else {
        "${cls.javaClass.simpleName}|$taskName"
    }

    // 如果任务在缓存器中则取出，否则进提示
    if (realTaskName in cacheMap) {
        val stopTime = System.currentTimeMillis()
        val startTime = cacheMap[realTaskName]?.startTime ?: (stopTime + 1)
        cacheMap.remove(realTaskName)
        KLogi("$realTaskName|结束|耗时:${stopTime - startTime}ms")
    } else {
        KLoge("警告|$realTaskName 不在任务缓存器中!")
    }
}

/**
 * 清空缓存
 */
fun clearTaskCacheMap() {
    cacheMap.clear()
}