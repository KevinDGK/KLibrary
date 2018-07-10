package com.dgk.common.util.timer

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager
import com.dgk.common.util.log.KLogi

/**
 * 精准定时器
 * - 使用系统时钟 AlarmManager 实现，定时ns后发送广播，接收到广播后再次定时ns后发送广播，同时
 * 通过观察者被观察者模式通知 ExactTimer 对象，再回调给使用者。
 */
class ExactTimer(private val context: Context,
                 private val intervalAtMills: Long,
                 private val callback: Callback) : TimerBroadcastObserver {

    private lateinit var manager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    private var lbm: LocalBroadcastManager = LocalBroadcastManager.getInstance(context)
    private lateinit var receiver: TimerBroadcastReceiver

    fun start() {
        KLogi("ExactTimer|start")

        // 订阅定时广播
        TimerBroadcastObservable.subscribe(this@ExactTimer)

        manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        receiver = TimerBroadcastReceiver()
        lbm.registerReceiver(receiver, IntentFilter())

        // 定时 intervalAtMills 后发送广播 TimerBroadcastReceiver
        val intent = Intent(context, TimerBroadcastReceiver::class.java)
        intent.action = "com.dgk.common.util.ExactTimer"
        intent.putExtra("intervalAtMills",intervalAtMills)
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        manager.setExact(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + intervalAtMills,
                pendingIntent)
    }

    fun stop() {
        KLogi("ExactTimer|stop")
        TimerBroadcastObservable.unsubscribe()
        lbm.unregisterReceiver(receiver)
        manager.cancel(pendingIntent)
    }

    /**
     * 触发定时操作
     */
    override fun trigger() {
        callback.run()
    }

    interface Callback {
        fun run()
    }
}