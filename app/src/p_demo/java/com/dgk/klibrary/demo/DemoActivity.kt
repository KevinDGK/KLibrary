package com.dgk.klibrary.demo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.dgk.R
import com.dgk.klibrary.demo.other.TimerActivity
import com.dgk.klibrary.demo.rxjava.RxBus1Activity
import com.dgk.klibrary.demo.rxjava.RxJavaActivity
import com.dgk.klibrary.demo.view.DialogActivity
import kotlinx.android.synthetic.main.demo_activity_demo.*

@Route(path = "/demo/DemoActivity")
class DemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_activity_demo)

        setSupportActionBar(toolbar)

        btn_rxjava.setOnClickListener {
            startActivity(Intent(this@DemoActivity, RxJavaActivity::class.java))
        }

        btn_rxbus.setOnClickListener {
            startActivity(Intent(this@DemoActivity, RxBus1Activity::class.java))
        }

        btn_test.setOnClickListener {
            startActivity(Intent(this@DemoActivity, TestActivity::class.java))
        }

        btn_service.setOnClickListener {
            ARouter.getInstance().build("/demo/service/ServiceActivity").navigation()
        }

        btn_design.setOnClickListener{
            ARouter.getInstance().build("/demo/design/DesignActivity").navigation()
        }

        btn_dialog.setOnClickListener{
            startActivity(Intent(this@DemoActivity, DialogActivity::class.java))
        }

        btn_timer.setOnClickListener {
            startActivity(Intent(this@DemoActivity, TimerActivity::class.java))
        }
    }
}
