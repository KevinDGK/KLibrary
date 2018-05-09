package com.dgk.klibrary.demo.design

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.dgk.R
import com.dgk.klibrary.demo.design.component.BottomSheetActivity
import com.dgk.klibrary.demo.design.component.ButtonActivity
import com.dgk.klibrary.demo.design.component.CardActivity
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

        btn_button.setOnClickListener {
            startActivity(Intent(this@DesignActivity, ButtonActivity::class.java))
        }

        btn_card.setOnClickListener {
            startActivity(Intent(this@DesignActivity, CardActivity::class.java))
        }
    }
}