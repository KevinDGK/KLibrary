package com.dgk.main

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.dgk.common.util.startRecordEvent
import com.dgk.common.util.stopRecordEvent

/**
 * Created by daigaokai on 2018/4/23.
 */
class KApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        init()
    }

    /**
     * 初始化
     */
    fun init() {

        initARouter()
    }

    /**
     * 初始化ARouter
     */
    fun initARouter() {
        startRecordEvent("初始化ARouter")
        ARouter.openDebug()
        ARouter.openLog()
        ARouter.init(this@KApplication)
        stopRecordEvent("初始化ARouter")
    }
}