package com.dgk.demo.performance

import android.net.TrafficStats
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dgk.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.demo_activity_test.*
import java.util.concurrent.TimeUnit

/**
 * Created by daigaokai on 2018/4/26.
 */

class TestActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_activity_test)

    }

    override fun onResume() {
        super.onResume()

        val getNetworkSpeed = Observable.create<String> {

            TrafficStats.getTotalTxBytes()

        }

        Observable.interval(1, TimeUnit.SECONDS)
                .flatMap {
                    return@flatMap getNetworkSpeed
                }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    tv_network_speed.text = "实时网速：$it"
                }

    }
}