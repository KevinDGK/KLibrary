package com.dgk.demo.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.dgk.common.util.KLoge
import com.dgk.common.util.KLogi
import com.dgk.common.util.ToastManager
import com.dgk.main.app.KApplication

/**
 * 服务
 * - 普通的Service不能做耗时操作，需要开子线程或者使用IntentService
 * Created by daigaokai on 2018/4/26.
 */

class Service01 : Service() {

    var binder = Binder01()
    override fun onCreate() {
        super.onCreate()
        KLogi(this, "onCreate")
    }


    override fun onBind(intent: Intent?): IBinder {
        KLogi(this, "onBind")
        ToastManager.toast(KApplication.getCtx(), "Service绑定成功")
        return binder
    }


    override fun onUnbind(intent: Intent?): Boolean {
        KLogi(this, "onUnbind")
        return super.onUnbind(intent)
    }
    override fun onDestroy() {
        super.onDestroy()
        KLogi(this, "onDestroy")
    }

    class Binder01 : Binder() {

        fun getDataFromService() : String {
            return "ServiceData"
        }

    }
}