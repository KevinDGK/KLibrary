package com.dgk.demo.performance

import android.app.usage.NetworkStatsManager
import android.net.TrafficStats
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.dgk.R
import com.dgk.common.util.KLogi
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.demo_activity_test.*
import java.math.BigDecimal
import java.util.concurrent.TimeUnit

/**
 * Created by daigaokai on 2018/4/26.
 */
@Route(path = "/demo/performance/NetworkActivity")
class NetworkActivity : AppCompatActivity() {

    private val mb = 1024.0 * 1024.0
    private val kb = 1024.0

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_activity_test)

//        disposables = Disposables()

        // 开始统计手机消耗的流量
        startCountPhoneTraffic()

        // 开始统计应用消耗的流量
        startCountAppTraffic()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    /**
     * 开始统计手机消耗的总流量
     * - TrafficStats.getTotalTxBytes() 获取手机启动后设备上行(上送)的总流量
     * - TrafficStats.getTotalRxBytes() 获取手机启动后设备下行(接收)的总流量
     */
    private fun startCountPhoneTraffic() {

        val mTvPhoneTraffic = TextView(this)
        ll_root.addView(mTvPhoneTraffic)

        var lastTotalTxB = -1L
        var lastTotalRxB = -1L

        val getNetworkSpeed = Observable.create<String> {

            val sb = StringBuilder()

            // 计算上行速率
            val totalTxB = TrafficStats.getTotalTxBytes()
            var txBInSecond = if (lastTotalTxB < 0) 0L else (totalTxB - lastTotalTxB)
            if (txBInSecond >= mb) {
                sb.append("Tx: ${formatDouble(txBInSecond / mb)}MB/s ")
            } else if (txBInSecond >= kb){
                sb.append("Tx: ${formatDouble(txBInSecond / kb)}KB/s ")
            } else {
                sb.append("Tx: ${formatDouble(txBInSecond * 1.0)}B/s ")
            }
            lastTotalTxB = totalTxB

            // 计算下行速率
            val totalRxB = TrafficStats.getTotalRxBytes()
            var rxBInSecond =  if (lastTotalRxB < 0) 0L else (totalRxB - lastTotalRxB)
            if (rxBInSecond >= mb) {
                sb.append("Rx: ${formatDouble(rxBInSecond / mb)}MB/s ")
            } else  if (rxBInSecond >= kb){
                sb.append("Rx: ${formatDouble(rxBInSecond / kb)}KB/s ")
            } else {
                sb.append("Rx: ${formatDouble(rxBInSecond * 1.0)}B/s ")
            }
            lastTotalRxB = totalRxB

            it.onNext(sb.toString())
        }

        Observable.interval(1, TimeUnit.SECONDS)
                .flatMap {
                    return@flatMap getNetworkSpeed
                }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (object : Observer<String> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposables.add(d)
                    }

                    override fun onNext(t: String) {
                        val speed = "实时网速：$t"
                        mTvPhoneTraffic.text = speed
                        KLogi(this@NetworkActivity, speed)
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
    }


    /**
     * 开始统计应用消耗的流量
     */
    private fun startCountAppTraffic() {


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val nsm = NetworkStatsManager()
//        } else {
//
//        }

    }

    fun formatDouble(num: Double): Double {
        return BigDecimal(num).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
    }
}