package com.dgk.klibrary.demo.design.component

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.dgk.R
import kotlinx.android.synthetic.main.demo_activity_card.*

class CardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_activity_card)

        setSupportActionBar(toolbar)
    }
}