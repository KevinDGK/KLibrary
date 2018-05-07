package com.dgk.demo.design

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.dgk.R
import com.dgk.demo.design.component.BottomSheetActivity
import kotlinx.android.synthetic.main.demo_activity_design.*

/**
 * Created by daigaokai on 2018/5/6.
 */
@Route(path = "/demo/design/DesignActivity")
class DesignActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_activity_design)

        setSupportActionBar(toolbar)

        btn_bottom_sheet.setOnClickListener {
            startActivity(Intent(this@DesignActivity, BottomSheetActivity::class.java))
        }
    }
}