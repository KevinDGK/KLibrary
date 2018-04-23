package com.dgk.demo.rxjava

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.alibaba.fastjson.JSON
import com.dgk.R
import com.dgk.common.util.printThrowable
import com.dgk.demo.okhttp.OkHttpUtil
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.Exceptions
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.demo_activity_rxjava.*
import okhttp3.*
import java.io.IOException
import java.lang.NullPointerException
import io.reactivex.ObservableSource
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.functions.BiFunction


/**
 * RxJava
 * 版本：RxJava 2.x
 * 简介：响应式编程
 *
 * 两种观察者模式：
 * 1. Observable - Observer（不支持背压）
 * 2. Flowable - Subscriber（支持背压）
 *
 * Created by daigaokai on 2018/4/16.
 */
class RxJavaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_activity_rxjava)

        btn_test_01.setOnClickListener {
            rxJavaTest01()
        }
        btn_test_02.setOnClickListener {
            rxJavaTest02()
        }
        btn_test_03.setOnClickListener {
            rxJavaTest03()
        }

        btn_test_04.setOnClickListener {
            rxJavaTest04()
        }

        btn_test_05.setOnClickListener {
            rxJavaTest05()
        }

        btn_test_06.setOnClickListener {
            rxJavaTest06()
        }

        // 设置RxJava全局的ErrorHandler，来接收Rxjava中onXXX()抛出的未捕捉的异常，详见RxJavaPlugins.onError()方法。
        RxJavaPlugins.setErrorHandler(object : Consumer<Throwable> {
            override fun accept(t: Throwable?) {
                println("RxJavaPlugins#errorHandler.onError()")
                // 输出任务栈信息
                printThrowable(t)
            }
        })
    }

    /**
     * 基本使用
     * 第一步：创建被观察者Observable
     * 第二步：创建观察者Observer
     * 第三部：建立订阅关系
     */
    private fun rxJavaTest01() {
        println("====== rxJavaTest01 ======")

        var disposable: Disposable? = null

        // 1.构建被观察者Observable
        Observable.create<Int>(object : ObservableOnSubscribe<Int> {
            override fun subscribe(emitter: ObservableEmitter<Int>) {
                println("emitter.onNext(1)")
                emitter.onNext(1)
                println("emitter.onNext(2)")
                emitter.onNext(2)
                println("emitter.onNext(3)")
                emitter.onNext(3)
                println("emitter.onComplete()")
                emitter.onComplete()        // 发射一个onComplete()事件，表示结束，下游的观察者就会取消订阅，不会再接收信息
                println("emitter.onNext(4)")
                emitter.onNext(4)
            }

            // 3.订阅
        }).subscribe(object : Observer<Int> {       // 2.构建观察者Observer
            override fun onComplete() {
                println("onComplete")
            }

            override fun onSubscribe(d: Disposable) {
                /*
                    首先打印的是该日志，因为观察者和被观察者首先会建立订阅关系，然后被观察者才会生产事件。
                    在该方法中可以获取到Disposable对象，可以用于取消订阅关系，即可以使得被观察者不再接收上游事件。
                 */
                println("onSubscribe")
                disposable = d
            }

            override fun onNext(t: Int) {
                println("onNext, t=$t")
//                if (t >= 2) {
//                    println("dispose")
                // 被观察者可以主动取消订阅，即不再接收上游emitter发送的任何事件
//                    disposable?.dispose()
//                }
            }

            override fun onError(e: Throwable) {
                println("onError")
                e.printStackTrace()
            }
        })
    }

    /**
     * 使用场景：网络请求，解析响应数据，根据结果刷新UI
     */
    private fun rxJavaTest02() {
        println("====== rxJavaTest02 ======")

        Observable.create(object : ObservableOnSubscribe<String> {
            override fun subscribe(emitter: ObservableEmitter<String>) {
                println("Observable: subscribe, 进行网络请求")

                val URL_GET = "http://192.168.0.103:8080/mydemo/test/get"

                OkHttpUtil.getAsync(URL_GET, object : Callback {
                    override fun onFailure(call: Call?, e: IOException?) {
                        println("onFailure: $e")
                        emitter.onError(Exception("onFailure: ${e?.message ?: "null"}"))
                    }

                    override fun onResponse(call: Call?, response: Response?) {
                        val result = response?.body()?.string()
                        println("onResponse: response.body = $result")
                        if (result.isNullOrEmpty()) {
                            emitter.tryOnError(Exception("onFailure: 返回数据为空"))
                        } else {
                            emitter.onNext(result!!)
                        }
                    }
                })
            }
        }).map(object : Function<String, ResponseBean> {    // map()操作符
            override fun apply(t: String): ResponseBean {
                println("map: apply, 数据解析 t=$t")
                val result = JSON.parseObject(t, ResponseBean::class.java)

                // code不为0表示本次业务请求失败，直接error，会走Observer的onError方法
                if (result.code != 0) {

                    // 使用Kotlin直接抛出RuntimeException运行时异常，该异常都会被抛到OnError()
                    error("请求失败, code=${result.code}")

                    // 也可以使用RxJava抛出RuntimeException运行时异常
//                    Exceptions.propagate(Exception("请求失败"))
                }
                return result
            }
        }).doOnNext(object : Consumer<ResponseBean> {
            override fun accept(t: ResponseBean?) {
                println("doOnNext: accept, 将数据保存到本地数据库")
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Any> {
                    override fun onComplete() {
                        println("Observer: onComplete")
                    }

                    override fun onSubscribe(d: Disposable) {
                        println("Observer: onSubscribe")
                    }

                    override fun onNext(t: Any) {
                        println("Observer: onNext")
                    }

                    override fun onError(e: Throwable) {
                        println("Observer: onError")
                        e.printStackTrace()
                    }
                })
    }

    /**
     * RxJava异常
     */
    private fun rxJavaTest03() {
        println("====== rxJavaTest03 ======")

        Observable.create(object : ObservableOnSubscribe<Int> {
            override fun subscribe(emitter: ObservableEmitter<Int>) {

                for (i in 1..5) {
                    println("emitter.onNext($i)")
                    emitter.onNext(i)
                    Thread.sleep(2000)
                }

                // 直接抛出onError事件，可以在Observer的onError()中处理
//                emitter.onError(NullPointerException("There is a null param!!!"))

                emitter.onNext(10)
                emitter.onComplete()
            }
        }).map {
            println("map, value=$it")
            if (it >= 3) {

                /*
                    在此处抛出的异常会被传送到Scheduler或者Executor，
                    或者路由到Rxjavaplugins #onError() 或者 Thread.UncaughtExceptionHandler#uncaughtException(Thread, Throwable)的handler进行处理。
                    所以不一定会导致崩溃。
                 */

                // RxJava抛异常的方法，异常类型为RuntimeException
//                Exceptions.propagate(NullPointerException("RxJava: There is a null param!!!"))

                // Kotlin抛出异常的方法，异常类型为IllegalStateException，RuntimeException的子类
//                error("Kotlin: There is a null param!!!")
            }
            return@map it * it
        }.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Int> {
                    override fun onComplete() {
                        println("Observer: onComplete")
                    }

                    override fun onSubscribe(d: Disposable) {
                        println("Observer: onSubscribe")
                    }

                    override fun onNext(t: Int) {
                        println("Observer: onNext, value=$t")
                        /*
                            在onXXX()中抛出异常：
                            - 如果设置了RxJava全局的异常处理的handler，即RxJavaPlugins.setErrorHandler()，那么在此处抛出的异常会送到RxJavaPlugins.onError()方法处理；
                            - 如果没有设置，就会直接抛给该线程或者线程组，如果该线程没有设置处理异常的Handler，即defaultUncaughtExceptionHandler，那么就会造成程序崩溃。
                         */
                        if (t >= 9) {
                            error("Kotlin: There is a null param!!!")
                        }
                    }

                    override fun onError(e: Throwable) {
                        println("Observer: onError")
                        e.printStackTrace()
                    }
                })
    }

    /**
     * concat 依次轮流发射数据，并且只有前一个调用了onComplete()后一个才会执行
     */
    private fun rxJavaTest04() {
        println("====== rxJavaTest04 ======")

        // source1: 模拟从内存中获取
        val source1 = Observable.create(object : ObservableOnSubscribe<String> {
            override fun subscribe(emitter: ObservableEmitter<String>) {
                println("ObservableSource1 Thread: ${Thread.currentThread()}")

                var data = "data from cache"

                if (data.isNullOrEmpty()) {
                    /*
                        如果缓存中没有我们要的数据，则直接调用onComplete()，
                        此时会触发下一个Observable的subscribe
                     */
                    emitter.onComplete()
                } else {
                    /*
                        如果缓存中有我们要的数据，就不再进行网络请求，直接调用onNext()
                        然后在onNext()中处理数据。取消订阅
                     */
                    emitter.onNext(data)
                }
            }
        })

        // source2: 模拟从内存中获取
        val source2 = Observable.create(object : ObservableOnSubscribe<String> {
            override fun subscribe(emitter: ObservableEmitter<String>) {
                println("ObservableSource2 Thread: ${Thread.currentThread()}")

                var data = "data from net"

                emitter.onNext(data)
                emitter.onComplete()
            }
        })

        var disposable: Disposable? = null

        Observable.concat(source1, source2)
                .map {
                    println("Map Thread: ${Thread.currentThread()}")
                    println("map: it=$it")
                    return@map it
                }.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<String> {
                    override fun onComplete() {
                        println("Observer: onComplete")
                    }

                    override fun onSubscribe(d: Disposable) {
                        println("Observer: onSubscribe")
                        disposable = d
                    }

                    override fun onNext(t: String) {
                        println("Observer: onNext, value=$t, Thread=${Thread.currentThread()})")
                        disposable?.dispose()
                    }

                    override fun onError(e: Throwable) {
                        println("Observer: onError")
                        e.printStackTrace()
                    }
                })
    }

    /**
     * flatMap 基本使用
     * - 展开流：对Observable发射的数据流进行变换操作，然后再发射出去
     */
    private fun rxJavaTest05() {

        Observable.create(object : ObservableOnSubscribe<Int> {
            override fun subscribe(emitter: ObservableEmitter<Int>) {
                for (i in 0..9) {
//                    println("emitter.onNext(${i*3})")   // 发射 0, 3, 6, 9, 12
                    emitter.onNext(i*3)
//                    Thread.sleep(1000)
                }
                emitter.onComplete()
            }
        }).flatMap(object : Function<Int, ObservableSource<Int>>{
            override fun apply(t: Int): ObservableSource<Int> {
                return Observable.create {
                    // 将t转换成t、t+1、t+2发射出去，即
                    it.onNext(t)
                    it.onNext(t + 1)
                    it.onNext(t + 2)
                    it.onComplete()
                }
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Int> {
                    override fun onComplete() {
                        println("Observer: onComplete")
                    }

                    override fun onSubscribe(d: Disposable) {
                        println("Observer: onSubscribe")
                    }

                    override fun onNext(t: Int) {
                        println("Observer: onNext, value=$t")
                    }

                    override fun onError(e: Throwable) {
                        println("Observer: onError")
                        e.printStackTrace()
                    }
                })
    }

    /**
     * zip：依次执行Observable.subscribe()方法，并将发射的所有数据经过BiFunction()组合起来，再发射出去
     */
    private fun rxJavaTest06() {

        // 模拟网络请求1
        val source1 = Observable.create(object : ObservableOnSubscribe<String> {
            override fun subscribe(emitter: ObservableEmitter<String>) {
                println("source1: subscribe")
                Thread.sleep(3000)
                println("source1: emitter")
                emitter.onNext("Kevin")
                emitter.onComplete()
            }
        })

        // 模拟网络请求2
        val source2 = Observable.create(object : ObservableOnSubscribe<Int> {
            override fun subscribe(emitter: ObservableEmitter<Int>) {
                println("source2: subscribe")
                Thread.sleep(5000)
                println("source2: emitter")
                emitter.onNext(18)
                emitter.onComplete()
            }
        })

        // 合并两个网络请求的结果，转为String向下发送
        Observable.zip(source1, source2, object : BiFunction<String, Int, String>{
            override fun apply(t1: String, t2: Int): String {
                println("zip: apply")
                return "$t1 is $t2 years old!"
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<String> {
                    override fun onComplete() {
                        println("Observer: onComplete")
                    }

                    override fun onSubscribe(d: Disposable) {
                        println("Observer: onSubscribe")
                    }

                    override fun onNext(t: String) {
                        println("Observer: onNext, value=$t")
                    }

                    override fun onError(e: Throwable) {
                        println("Observer: onError")
                        e.printStackTrace()
                    }
                })
    }
}