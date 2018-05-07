package com.dgk.demo.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.dgk.R
import com.dgk.common.util.KLoge
import com.dgk.common.util.KLogi
import com.dgk.common.util.ToastManager
import com.dgk.main.app.KApplication
import kotlinx.android.synthetic.main.demo_activity_service.*

/**
 * Created by daigaokai on 2018/4/26.
 */
@Route(path = "/demo/service/ServiceActivity")
class ServiceActivity : AppCompatActivity() {

    var serviceConnection:ServiceConnection ? = null
    var binder: Service01.Binder01? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.demo_activity_service)

        toolbar.title = "Service"
        setSupportActionBar(toolbar)

        // 绑定服务
        btn_bind.setOnClickListener{
            if (serviceConnection == null) {
                serviceConnection = object : ServiceConnection {
                    override fun onServiceDisconnected(name: ComponentName?) {
                        KLoge(this@ServiceActivity,"onServiceDisconnected")

                        // Service断开连接，需要做处理

                    }

                    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                        KLoge(this@ServiceActivity,"onServiceConnected")
                        binder = service as? Service01.Binder01

                        binder?.linkToDeath(object :IBinder.DeathRecipient {
                            override fun binderDied() {
                                KLoge(this@ServiceActivity,"binderDied")
                            }
                        }, 0)
                    }
                }
                bindService(Intent(this@ServiceActivity, Service01::class.java), serviceConnection, Context.BIND_AUTO_CREATE)
            } else {
                ToastManager.toast(KApplication.getCtx(), "Service已经绑定成功")
            }
        }

        // 解绑服务
        btn_unbind.setOnClickListener {
            if (serviceConnection != null) {
                unbindService(serviceConnection)
                serviceConnection = null
                binder = null
            } else {
                KLoge(this@ServiceActivity,"Service未绑定")
            }
        }

        // Activity调用服务方法
        btn_activity_invoke_service.setOnClickListener {
            if (binder == null) {
                KLoge(this@ServiceActivity,"Service未绑定")
            } else {
                if (binder!!.isBinderAlive) {
                    KLogi(this@ServiceActivity,"${binder?.getDataFromService()}")
                } else {
                    KLoge(this@ServiceActivity,"Service已经死亡")
                }
            }
        }
    }
}