package com.dgk.common.module

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.dgk.common.R

/**
 * Created by daigaokai on 2018/4/11.
 */
@Route(path = "/common/CommonActivity")
class CommonActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.common_activity_common)
    }
}