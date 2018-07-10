package com.dgk.common.util.timer

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dgk.common.util.log.KLogi

/**
 * 接收定时消息的本地广播接收者
 */
class TimerBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        KLogi("TimerBroadcastReceiver|onReceive")

        if (intent == null) {
            KLogi("The Intent is null!")
            return
        }

        TimerBroadcastObservable.receive()

        val intervalAtMills = intent.getLongExtra("intervalAtMills", -1)
        if (intervalAtMills <= 0) {
            KLogi("The intervalAtMills must be > 0!")
            return
        }
        val manager =  context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val nextIntent = Intent(context, TimerBroadcastReceiver::class.java)
        nextIntent.action = "com.dgk.common.util.ExactTimer"
        val pendingIntent = PendingIntent.getBroadcast(context,
                0,
                nextIntent,
                0)
        manager.setExact(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + intervalAtMills,
                pendingIntent)
    }
}