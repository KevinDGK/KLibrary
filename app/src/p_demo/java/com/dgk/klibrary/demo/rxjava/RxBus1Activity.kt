package com.dgk.klibrary.demo.rxjava

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.dgk.R
import com.dgk.klibrary.demo.rxjava.event.BackgroudChangeEvent
import com.dgk.klibrary.demo.rxjava.event.TextChangeEvent
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_rx_bus_1.*
import java.util.*

class RxBus1Activity : AppCompatActivity() {

    private var flag = 0

    private var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx_bus_1)
        setSupportActionBar(toolbar)

        btn_send_text.setOnClickListener {
            // 发送内容改变事件
            RxBus1.getInstance().post(TextChangeEvent("The event is ${++flag}."))
        }

        // 订阅内容改变事件
        compositeDisposable.add(RxBus1.getInstance().toObservable(TextChangeEvent::class.java)
                .subscribe {
                    tv_content.text = it.content
                })

        btn_send_color.setOnClickListener {
            // 发送背景颜色改变事件
            RxBus1.getInstance().post(BackgroudChangeEvent(Color.rgb(Random().nextInt(255),
                    Random().nextInt(255),
                    Random().nextInt(255))))
        }

        // 订阅背景颜色事件
        compositeDisposable.add(RxBus1.getInstance().toObservable(BackgroudChangeEvent::class.java)
                .subscribe {
                    tv_content.setBackgroundColor(it.color)
                })
    }

    override fun onDestroy() {
        super.onDestroy()

        // 必须要取消订阅，否则会造成内存泄漏
        compositeDisposable.dispose()
    }
}