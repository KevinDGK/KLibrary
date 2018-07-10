package com.dgk.klibrary.demo.other

import android.support.v7.app.AppCompatActivity
import com.dgk.R
import com.dgk.common.util.log.KLogi
import kotlinx.android.synthetic.main.demo_activity_timer.*
import java.util.*
import android.os.*
import com.dgk.common.util.timer.ExactTimer


class TimerActivity : AppCompatActivity() {

    private val HEART_BEAT_HANDLER = 1000
    private val HEART_BEAT_TIMER = 1001
    private val HEART_BEAT_INTERVAL = 3000L

    private var lastSendTime = 0L

    private val handler = Handler(Looper.getMainLooper()) {

        when (it.what) {
            HEART_BEAT_HANDLER -> {
                val currentTime = System.currentTimeMillis()
                KLogi("Handler 心跳间隔：${currentTime - lastSendTime}")
                lastSendTime = currentTime
                sendHeartBeat()
            }
            HEART_BEAT_TIMER -> {
                val currentTime = System.currentTimeMillis()
                KLogi("Timer + Handler 心跳间隔：${currentTime - lastSendTime}")
                lastSendTime = currentTime
            }
            else -> KLogi("unknown message.what : ${it.what}")
        }
        return@Handler true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.demo_activity_timer)

        setSupportActionBar(toolbar)

        btn_handler_start.setOnClickListener {
            KLogi("开启Handler定时")
            lastSendTime = System.currentTimeMillis()
            handler.sendEmptyMessage(HEART_BEAT_HANDLER)
        }

        btn_handler_stop.setOnClickListener {
            KLogi("结束Handler定时")
            handler.removeMessages(HEART_BEAT_HANDLER)
        }

        btn_timer_start.setOnClickListener {
            KLogi("开启Timer+Handler定时")
            startTimerTask()
        }

        btn_timer_stop.setOnClickListener {
            KLogi("结束Timer+Handler定时")
            timer.cancel()
        }

        var exactTimer: ExactTimer? = null

        btn_alarm_manager_start.setOnClickListener {
            KLogi("开启ExactTimer定时")

            var lastSendTime = System.currentTimeMillis()

            exactTimer = ExactTimer(this.applicationContext, 5000L, object : ExactTimer.Callback {
                override fun run() {
                    val currentTime = System.currentTimeMillis()
                    KLogi("ExactTimer 心跳间隔：${currentTime - lastSendTime}")
                    lastSendTime = currentTime
                }
            })
            exactTimer?.start()
        }

        btn_alarm_manager_stop.setOnClickListener {
            KLogi("关闭 ExactTimer 定时")
            exactTimer?.stop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    private fun sendHeartBeat() {
        handler.sendEmptyMessageDelayed(HEART_BEAT_HANDLER, HEART_BEAT_INTERVAL)
    }

    private var timer: Timer = Timer()
    private fun startTimerTask() {
        lastSendTime = System.currentTimeMillis()
        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.sendEmptyMessage(HEART_BEAT_TIMER)
            }
        }, 0, HEART_BEAT_INTERVAL)
    }
}