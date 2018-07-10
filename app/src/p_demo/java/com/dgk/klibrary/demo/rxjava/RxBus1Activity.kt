package com.dgk.klibrary.demo.rxjava

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.dgk.R
import com.dgk.klibrary.demo.rxjava.event.TextChangeEvent
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_rx_bus_1.*

class RxBus1Activity : AppCompatActivity() {

    private var flag = 0

    private var subscribe: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx_bus_1)
        setSupportActionBar(toolbar)

        btn_send.setOnClickListener {
            // 发送事件
            RxBus1.getInstance().post(TextChangeEvent("The event is ${++flag}."))
        }

        // 订阅事件
        subscribe = RxBus1.getInstance().toObservable(TextChangeEvent::class.java)
                .subscribe {
                    tv_content.text = it.content
                }
    }

    override fun onDestroy() {
        super.onDestroy()

        // 必须要取消订阅，否则会造成内存泄漏
        subscribe?.dispose()
    }
}