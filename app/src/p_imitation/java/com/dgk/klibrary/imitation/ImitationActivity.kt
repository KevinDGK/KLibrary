package com.dgk.klibrary.imitation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.dgk.R

/**
 * Created by daigaokai on 2018/4/11.
 */
@Route(path = "/imitation/ImitationActivity")
class ImitationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.imitation_activity_imitation)
    }
}