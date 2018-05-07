package com.dgk.demo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.dgk.R
import kotlinx.android.synthetic.main.demo_activity_demo.*

@Route(path = "/demo/DemoActivity")
class DemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_activity_demo)

        setSupportActionBar(toolbar)

        btn_rxjava.setOnClickListener {
            ARouter.getInstance().build("/demo/rxjava/RxJavaActivity").navigation()
        }

        btn_test.setOnClickListener {
            ARouter.getInstance().build("/demo/performance/NetworkActivity").navigation()
        }

        btn_service.setOnClickListener {
            ARouter.getInstance().build("/demo/service/ServiceActivity").navigation()
        }

        btn_design.setOnClickListener{
            ARouter.getInstance().build("/demo/design/DesignActivity").navigation()
        }
    }
}
