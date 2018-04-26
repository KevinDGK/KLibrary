package com.dgk.main.app

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.dgk.common.util.startTaskRecorder
import com.dgk.common.util.stopTaskRecorder

/**
 * Created by daigaokai on 2018/4/23.
 */
class KApplication : Application() {

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
    fun init() {

        initARouter()
    }

    /**
     * 初始化ARouter
     */
    fun initARouter() {
        startTaskRecorder(this,"初始化ARouter")
        ARouter.openDebug()
        ARouter.openLog()
        ARouter.init(this@KApplication)
        stopTaskRecorder(this,"初始化ARouter")
    }
}