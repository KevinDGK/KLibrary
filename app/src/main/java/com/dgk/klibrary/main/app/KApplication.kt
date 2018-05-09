package com.dgk.klibrary.main.app

import android.app.Application
import android.support.multidex.MultiDexApplication
import com.alibaba.android.arouter.launcher.ARouter
import com.dgk.BuildConfig
import com.dgk.common.util.http.OkHttpUtil
import com.dgk.common.util.log.KLog
import com.dgk.common.util.startTaskRecorder
import com.dgk.common.util.stopTaskRecorder

/**
 * Created by daigaokai on 2018/4/23.
 */
class KApplication : MultiDexApplication() {

    companion object {
        var application: KApplication? = null

        fun getCtx() : Application{
            return application!!
        }
    }

    override fun onCreate() {
        super.onCreate()

        application = this

        init()
    }

    /**
     * 初始化
     */
    private fun init() {

        // 初始化日志模块
        initLog()

        // 初始化网络模块
        initHttp()

        // 初始化ARouter
        initARouter()
    }

    /**
     * 初始化网络模块
     */
    private fun initHttp() {
        startTaskRecorder(this,"初始化网络模块")
        OkHttpUtil.SERVER_URL = CONFIG_SERVER_URL
        stopTaskRecorder(this,"初始化网络模块")
    }

    /**
     * 初始化ARouter
     */
    private fun initARouter() {
        startTaskRecorder(this,"初始化ARouter")
        ARouter.openDebug()
        ARouter.openLog()
        ARouter.init(this)
        stopTaskRecorder(this,"初始化ARouter")
    }

    /**
     * 初始化日志模块
     * - 日志模块为KLog，内部使用的是XLog
     */
    private fun initLog() {
        startTaskRecorder(this,"初始化日志模块")
        KLog.init(this, BuildConfig.DEBUG)
        stopTaskRecorder(this,"初始化日志模块")
    }
}