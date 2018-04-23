package com.dgk.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.dgk.R
import kotlinx.android.synthetic.main.main_activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_main)

        btn_demo.setOnClickListener {
            println("DemoActivity")
            ARouter.getInstance().build("/demo/DemoActivity").navigation()
        }

        btn_imitation.setOnClickListener {
            println("ImitationActivity")
            ARouter.getInstance().build("/imitation/ImitationActivity").navigation()
        }

        btn_common.setOnClickListener {
            println("CommonActivity")
            ARouter.getInstance().build("/common/CommonActivity").navigation()
        }
    }
}
