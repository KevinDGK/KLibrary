package com.dgk.common.util.log

import android.content.Context
import android.os.Environment
import com.tencent.mars.xlog.Log
import com.tencent.mars.xlog.Xlog

/**
 * 日志工具类
 *
 *  目前，日志模块使用的是微信的终端跨平台组件Mars中的高性能日志模块xlog。
 *  - Mars(微信的终端跨平台组件)：https://github.com/Tencent/mars
 *  - Xlog(高性能日志模块)
 *    - 快速接入：https://github.com/Tencent/mars/wiki/Mars-Android-%E6%8E%A5%E5%8F%A3%E8%AF%A6%E7%BB%86%E8%AF%B4%E6%98%8E
 *    - Wiki：https://mp.weixin.qq.com/s/cnhuEodJGIbdodh0IxNeXQ
 *
 * Created by daigaokai on 2018/4/26.
 */

object KLog {

    init {
        System.loadLibrary("stlport_shared")
        System.loadLibrary("marsxlog")
    }

    // 默认在日志前加上统一的前缀，方便过滤日志
    const val LOG_TAG_DEFAULT_PREFIX = "KLog"

    fun init(context: Context, isDebug: Boolean) {

        val logPath = getFilePath(context)

        // this is necessary, or may cash for SIGBUS
        val cachePath = getCashPath(context)

        // 加密公钥，空字符串为不加密
        val PUB_KEY = ""

        // 文件名称为 nameprefix_yyyyMMdd.xlog
        val nameprefix = getNamePrefix()

        // init xlog
        if (isDebug) {
            Xlog.setConsoleLogOpen(true)    //控制台日志
            Xlog.appenderOpen(Xlog.LEVEL_DEBUG, Xlog.AppednerModeAsync, cachePath, logPath, nameprefix, PUB_KEY)
        } else {
            Xlog.setConsoleLogOpen(false)
            Xlog.appenderOpen(Xlog.LEVEL_INFO, Xlog.AppednerModeAsync, cachePath, logPath, nameprefix, PUB_KEY)
        }

        Log.setLogImp(Xlog())
    }

    /**
     * 将缓存中的日志写入到文件中
     * - 在上传日志之前可以调用一下该方法
     */
    fun flush(isSync: Boolean) {
        Log.appenderFlush(isSync)
    }

    /**
     * 文件SD卡保存路径
     */
    fun getFilePath(context: Context): String {

        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            KLogi("SD卡已经装载")
        }

        val packageName = context.packageName
        val SDCARD = Environment.getExternalStorageDirectory().absolutePath
        val logPath = "$SDCARD/$packageName/xlog"

        return logPath
    }

    /**
     * 在没有SD卡的情况下的文件保存路径
     */
    fun getCashPath(context: Context): String {
        return context.filesDir.toString() + "/xlog"
    }

    /**
     * 文件前缀
     */
    fun getNamePrefix(): String {
        return "log"
    }

    /**
     * 根据simpleName获取完整文件名
     */
    fun getFileName(simpleName: String): String {
        return "${getNamePrefix()}_$simpleName.xlog"
    }
}


fun KLogi(msg: String) {
    KLogi(null, msg)
}

fun KLogi(cls: Any?, msg: String) {
    if (cls == null) {
        com.tencent.mars.xlog.Log.i(KLog.LOG_TAG_DEFAULT_PREFIX, msg)
    } else {
        com.tencent.mars.xlog.Log.i(KLog.LOG_TAG_DEFAULT_PREFIX, "${cls.javaClass.simpleName}|$msg")
    }
}

fun KLoge(msg: String) {
    KLogi(null, msg)
}

fun KLoge(cls: Any?, msg: String) {
    if (cls == null) {
        com.tencent.mars.xlog.Log.i(KLog.LOG_TAG_DEFAULT_PREFIX, msg)
    } else {
        com.tencent.mars.xlog.Log.e(KLog.LOG_TAG_DEFAULT_PREFIX, "${cls.javaClass.simpleName}|$msg")
    }
}
