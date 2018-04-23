package com.dgk.demo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.dgk.R
import com.dgk.demo.rxjava.RxJavaActivity
import kotlinx.android.synthetic.main.demo_activity_demo.*

@Route(path = "/demo/DemoActivity")
class DemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_activity_demo)

        btn_rxjava.setOnClickListener {
            startActivity(Intent(this@DemoActivity, RxJavaActivity::class.java))
        }
    }
}
