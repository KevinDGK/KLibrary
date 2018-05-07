package com.dgk.demo.design.component

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dgk.R
import kotlinx.android.synthetic.main.demo_activity_button.*

class ButtonActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_activity_button)

        setSupportActionBar(toolbar)

    }

}