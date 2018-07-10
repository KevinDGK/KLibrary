package com.dgk.common.util.timer

import java.lang.ref.WeakReference

/**
 * 定时广播的被观察者
 */
object TimerBroadcastObservable {

    /**
     * 持有的观察者对象
     * 由于Timer中持有Context，所以此处持有其弱引用，并且要求Timer使用完毕后必须关闭。
     */
    private var timer: ExactTimer? = null

    /**
     * 订阅
     */
    fun subscribe(timer: ExactTimer) {
        this.timer = timer
    }

    /**
     * 取消订阅
     */
    fun unsubscribe() {
        this.timer = null
    }

    /**
     * 接收到定时广播
     */
    fun receive() {
        notifyObserver()
    }

    /**
     * 通知观察者
     */
    private fun notifyObserver() {
        timer?.trigger()
    }
}