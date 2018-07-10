package com.dgk.klibrary.demo.rxjava

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v7.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.fastjson.JSON
import com.dgk.R
import com.dgk.common.util.log.KLogi
import com.dgk.common.util.printThrowable
import com.dgk.common.util.startTaskRecorder
import com.dgk.common.util.stopTaskRecorder
import com.dgk.common.util.http.OkHttpUtil
import com.dgk.klibrary.main.app.CONFIG_SERVER_URL
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.demo_activity_rxjava.*
import okhttp3.*
import java.io.IOException
import io.reactivex.functions.*
import io.reactivex.functions.Function
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.concurrent.TimeUnit

/**
 * RxJava
 * 版本：RxJava 2.x
 * 简介：响应式编程
 *
 * 两种观察者模式：
 * 1. Observable - Observer（不支持背压）
 * 2. Flowable - Subscriber（支持背压）
 *
 *
 * 资源：
 * - 源码是最好的老师；
 * - RxJava 2.x 中文Demo：https://github.com/nanchen2251/RxJava2Examples
 * - RxJava GitHub：https://github.com/ReactiveX/RxJava
 *
 * Created by daigaokai on 2018/4/16.
 */
class RxJavaActivity : AppCompatActivity() {

    private data class ResponseBean(var code: Int = -1, var message: String = "")

    /**
     * 在界面关闭、获取数据成功、或者达到一定次数和时间之后就需要取消轮询，否则界面关闭后线程仍然在执行，容易造成崩溃。
     */
    var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_activity_rxjava)

        // 将布局中的ToolBar替换系统的Toolbar
        setSupportActionBar(toolbar)

        btn_test_observable.setOnClickListener {
            rxJavaTestObservableAndObserver()
        }

        btn_test_single.setOnClickListener {
            rxJavaTestSingle()
        }

        btn_test_flowable.setOnClickListener {
            rxJavaTestFlowable()
        }

        btn_test_create.setOnClickListener {
            rxJavaTestCreate()
        }

        btn_test_exception.setOnClickListener {
            rxJavaTestException()
        }
        btn_test_concat.setOnClickListener {
            rxJavaTestConcat()
        }

        btn_test_zip.setOnClickListener {
            rxJavaTestZip()
        }

        btn_test_map.setOnClickListener {
            rxJavaTestMap()
        }

        btn_test_flatmap.setOnClickListener {
            rxJavaTestFlatMap()
        }

        btn_test_concatmap.setOnClickListener {
            rxJavaTestConcatMap()
        }

        btn_test_interval.setOnClickListener {
            rxJavaTestInterval()
        }

        btn_test_distinct.setOnClickListener {
            rxJavaTestDistinct()
        }

        btn_test_filter.setOnClickListener {
            rxJavaTestFilter()
        }

        btn_test_buffer.setOnClickListener {
            rxJavaTestBuffer()
        }

        btn_test_do_on_next.setOnClickListener {
            rxJavaTestDoOnNext()
        }

        btn_test_just.setOnClickListener {
            rxJavaTestJust()
        }

        // 设置RxJava全局的ErrorHandler，来接收Rxjava中onXXX()抛出的未捕捉的异常，详见RxJavaPlugins.onError()方法。
        RxJavaPlugins.setErrorHandler(object : Consumer<Throwable> {
            override fun accept(t: Throwable?) {
                KLogi("RxJavaPlugins#errorHandler.onError()")
                // 输出任务栈信息
                printThrowable(t)
            }
        })
    }

    override fun onStop() {
        super.onStop()

//        /*
//            当界面关闭的时候，一定要将一些耗时较长、轮询等被观察者取消订阅，
//            否则，当界面关闭后，有可能还在发射事件，并走到了观察者的onNext()方法中，
//            容易造成崩溃或者内存泄漏。
//        */
//        disposable?.let {
//            it.dispose()
//            stopTaskRecorder(this, "rxJavaTestInterval")
//        }
    }

    override fun onDestroy() {
        super.onDestroy()

        /*
            当界面关闭的时候，一定要将一些耗时较长、轮询等被观察者取消订阅，
            否则，当界面关闭后，有可能还在发射事件，并走到了观察者的onNext()方法中，
            容易造成崩溃或者内存泄漏。
        */
        disposable?.let {
            it.dispose()
            stopTaskRecorder(this, "rxJavaTestInterval")
        }
    }

    /**
     * 基本使用
     * 第一步：创建被观察者Observable
     * 第二步：创建观察者Observer
     * 第三步：建立订阅关系
     */
    private fun rxJavaTestObservableAndObserver() {
        KLogi("====== rxJavaTestObservableAndObserver ======")

        var disposable: Disposable? = null

        // 1.构建被观察者Observable
        Observable.create<Int>(object : ObservableOnSubscribe<Int> {
            override fun subscribe(emitter: ObservableEmitter<Int>) {
                KLogi("emitter.onNext(1)")
                emitter.onNext(1)
                KLogi("emitter.onNext(2)")
                emitter.onNext(2)
                KLogi("emitter.onNext(3)")
                emitter.onNext(3)
                KLogi("emitter.onComplete()")
                emitter.onComplete()        // 发射一个onComplete()事件，表示结束，下游的观察者就会取消订阅，不会再接收信息
                KLogi("emitter.onNext(4)")
                emitter.onNext(4)
            }

            // 3.订阅
        }).subscribe(object : Observer<Int> {       // 2.构建观察者Observer
            override fun onComplete() {
                KLogi("onComplete")
            }

            override fun onSubscribe(d: Disposable) {
                /*
                    首先打印的是该日志，因为观察者和被观察者首先会建立订阅关系，然后被观察者才会生产事件。
                    在该方法中可以获取到Disposable对象，可以用于取消订阅关系，即可以使得被观察者不再接收上游事件。
                 */
                KLogi("onSubscribe")
                disposable = d
            }

            override fun onNext(t: Int) {
                KLogi("onNext, t=$t")
//                if (t >= 2) {
//                    KLogi("dispose")
                // 被观察者可以主动取消订阅，即不再接收上游emitter发送的任何事件
//                    disposable?.dispose()
//                }
            }

            override fun onError(e: Throwable) {
                KLogi("onError")
                e.printStackTrace()
            }
        })
    }

    /**
     * Single
     * - 仅发射单个数据的被观察者和观察者模式。
     * 和Observable／Observer基本相同，唯一区别是没有onComplete事件。
     */
    private fun rxJavaTestSingle() {

        KLogi(this, "rxJavaTestSingle")

        Single.create(object : SingleOnSubscribe<Int> {
            override fun subscribe(emitter: SingleEmitter<Int>) {
                // 由于仅发射一个数据(事件)，所以只会调用onSuccess或者onError，没有onComplete和onNext。
                emitter.onSuccess(1)
//                emitter.onError()
            }
        }).subscribe(object : SingleObserver<Int> {
            override fun onSuccess(t: Int) {
                KLogi("onSuccess: $t")
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onError(e: Throwable) {
            }
        })

    }

    /**
     * Flowable/Subscriber
     * - 背压，详细可以参考文章 https://www.jianshu.com/p/ff8167c1d191
     */
    private fun rxJavaTestFlowable() {

        KLogi(this, "rxJavaTestFlowable")

        Flowable.create(object : FlowableOnSubscribe<Int> {
            override fun subscribe(emitter: FlowableEmitter<Int>) {
                emitter.onNext(1)
                emitter.onNext(2)
                emitter.onComplete()
            }
        }, BackpressureStrategy.BUFFER)
                .subscribe(object : Subscriber<Int> {
                    override fun onComplete() {
                        KLogi("onComplete")
                    }

                    override fun onSubscribe(s: Subscription?) {
                        s?.request(Long.MAX_VALUE)
                    }

                    override fun onNext(t: Int?) {
                        KLogi("onNext: $t")
                    }

                    override fun onError(t: Throwable?) {
                        t?.printStackTrace()
                    }
                })

    }

    /**
     * create 创建
     */
    private fun rxJavaTestCreate() {

        KLogi(this, "rxJavaTestCreate")

        Observable.create(object : ObservableOnSubscribe<Int> {
            override fun subscribe(emitter: ObservableEmitter<Int>) {
                emitter.onNext(1)
                emitter.onNext(2)
                emitter.onNext(3)
                emitter.onComplete()
            }
        }).subscribe(object : Observer<Int> {
            override fun onSubscribe(d: Disposable) {
                // 订阅
            }

            override fun onNext(t: Int) {
                // 接收数据流-发射的事件
                KLogi("onNext: $t")
            }

            override fun onError(e: Throwable) {
                // 接收异常
            }

            override fun onComplete() {
                // 结束
            }
        })
    }

    /**
     * RxJava异常
     */
    private fun rxJavaTestException() {
        KLogi("====== rxJavaTestException ======")

        Observable.create(object : ObservableOnSubscribe<Int> {
            override fun subscribe(emitter: ObservableEmitter<Int>) {

                for (i in 1..5) {
                    KLogi("emitter.onNext($i)")
                    emitter.onNext(i)
                    Thread.sleep(2000)
                }

                // 直接抛出onError事件，可以在Observer的onError()中处理
//                emitter.onError(NullPointerException("There is a null param!!!"))

                emitter.onNext(10)
                emitter.onComplete()
            }
        }).map {
            KLogi("cacheMap, value=$it")
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
                        KLogi("Observer: onComplete")
                    }

                    override fun onSubscribe(d: Disposable) {
                        KLogi("Observer: onSubscribe")
                    }

                    override fun onNext(t: Int) {
                        KLogi("Observer: onNext, value=$t")
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
                        KLogi("Observer: onError")
                        e.printStackTrace()
                    }
                })
    }

    /**
     * concat: 连接发射器
     * - 依次轮流发射数据，并且只有前一个调用了onComplete()后一个才会执行
     */
    private fun rxJavaTestConcat() {
        KLogi("====== rxJavaTestConcat ======")

        // 例1. 简单使用，会连接两个被观察者，被观察者1发射完数据后，被观察者2就会继续发射数据
        Observable.concat(Observable.just(1, 2, 3), Observable.just(4, 5, 6))
                .subscribe(object : Consumer<Int> {
                    override fun accept(t: Int?) {
                        KLogi("accept, t=$t")
                    }
                })

        // 例2. 模拟先从缓存中读取数据，如果缓存没有再进行网络请求，向服务器获取数据

        // source1: 模拟从内存中获取
        val source1 = Observable.create(object : ObservableOnSubscribe<String> {
            override fun subscribe(emitter: ObservableEmitter<String>) {
                KLogi("ObservableSource1 Thread: ${Thread.currentThread()}")

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
                KLogi("ObservableSource2 Thread: ${Thread.currentThread()}")

                var data = "data from net"

                emitter.onNext(data)
                emitter.onComplete()
            }
        })

        var disposable: Disposable? = null

        //
        Observable.concat(source1, source2)
                .map {
                    KLogi("Map Thread: ${Thread.currentThread()}")
                    KLogi("cacheMap: it=$it")
                    return@map it
                }.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<String> {
                    override fun onComplete() {
                        KLogi("Observer: onComplete")
                    }

                    override fun onSubscribe(d: Disposable) {
                        KLogi("Observer: onSubscribe")
                        disposable = d
                    }

                    override fun onNext(t: String) {
                        KLogi("Observer: onNext, value=$t, Thread=${Thread.currentThread()})")
                        disposable?.dispose()
                    }

                    override fun onError(e: Throwable) {
                        KLogi("Observer: onError")
                        e.printStackTrace()
                    }
                })
    }

    /**
     * zip：合并发射器
     * - 合并Observable发射的数据，通过BiFunction()组合起来，再发射出去。最终发射数量以最少的为准。
     */
    private fun rxJavaTestZip() {

        KLogi("====== rxJavaTestZip ======")

        /*
            例1.简单使用，会依次将两个被观察者发射的事件两两合并，然后发射出去，没有配对的数据不会发射。
            输出数据应该是 1A 2B 3C，由于4和5没有配对数据，所以不会再次发射。
         */
        val source1 = Observable.create(object : ObservableOnSubscribe<Int> {
            override fun subscribe(emitter: ObservableEmitter<Int>) {
                emitter.onNext(1)
                emitter.onNext(2)
                emitter.onNext(3)
                emitter.onNext(4)
                emitter.onNext(5)
            }
        })
        val source2 = Observable.create(object : ObservableOnSubscribe<String> {
            override fun subscribe(emitter: ObservableEmitter<String>) {
                emitter.onNext("A")
                emitter.onNext("B")
                emitter.onNext("C")
            }
        })
        Observable.zip(source1, source2, object : BiFunction<Int, String, String> {
            override fun apply(t1: Int, t2: String): String {
                return "$t1$t2"
            }
        }).subscribe(object : Consumer<String> {
            override fun accept(t: String?) {
                KLogi("accept: $t")
            }
        })

        /*
            例2.实际应用，连续进行多个网络请求获取数据，然后统一刷新UI。
         */

        // 模拟网络请求1
        val source3 = Observable.create(object : ObservableOnSubscribe<String> {
            override fun subscribe(emitter: ObservableEmitter<String>) {
                KLogi("source1: subscribe")
                Thread.sleep(3000)
                KLogi("source1: emitter")
                emitter.onNext("Kevin")
                emitter.onComplete()
            }
        })

        // 模拟网络请求2
        val source4 = Observable.create(object : ObservableOnSubscribe<Int> {
            override fun subscribe(emitter: ObservableEmitter<Int>) {
                KLogi("source2: subscribe")
                Thread.sleep(5000)
                KLogi("source2: emitter")
                emitter.onNext(18)
                emitter.onComplete()
            }
        })

        // 合并两个网络请求的结果，转为String向下发送
        Observable.zip(source3, source4, object : BiFunction<String, Int, String> {
            override fun apply(t1: String, t2: Int): String {
                KLogi("zip: apply")
                return "$t1 is $t2 years old!"
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<String> {
                    override fun onComplete() {
                        KLogi("Observer: onComplete")
                    }

                    override fun onSubscribe(d: Disposable) {
                        KLogi("Observer: onSubscribe")
                    }

                    override fun onNext(t: String) {
                        KLogi("Observer: onNext, value=$t")
                    }

                    override fun onError(e: Throwable) {
                        KLogi("Observer: onError")
                        e.printStackTrace()
                    }
                })
    }

    /**
     * Map 数据变换
     * - 使用场景：网络请求，解析响应数据，根据结果刷新UI
     */
    private fun rxJavaTestMap() {
        KLogi("====== rxJavaTestMap ======")

        Observable.create(object : ObservableOnSubscribe<String> {
            override fun subscribe(emitter: ObservableEmitter<String>) {
                KLogi("Observable: subscribe, 进行网络请求")

                OkHttpUtil.getAsync(CONFIG_SERVER_URL + "get", null, object : Callback {
                    override fun onFailure(call: Call?, e: IOException?) {
                        KLogi("onFailure: $e")
                        emitter.onError(Exception("onFailure: ${e?.message ?: "null"}"))
                    }

                    override fun onResponse(call: Call?, response: Response?) {
                        val result = response?.body()?.string()
                        KLogi("onResponse: response.body = $result")
                        if (result.isNullOrEmpty()) {
                            emitter.tryOnError(Exception("onFailure: 返回数据为空"))
                        } else {
                            emitter.onNext(result!!)
                        }
                    }
                })
            }
        }).map(object : Function<String, ResponseBean> {    // cacheMap()操作符
            override fun apply(t: String): ResponseBean {
                KLogi("cacheMap: apply, 数据解析 t=$t")
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
                KLogi("doOnNext: accept, 将数据保存到本地数据库")
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Any> {
                    override fun onComplete() {
                        KLogi("Observer: onComplete")
                    }

                    override fun onSubscribe(d: Disposable) {
                        KLogi("Observer: onSubscribe")
                    }

                    override fun onNext(t: Any) {
                        KLogi("Observer: onNext")
                    }

                    override fun onError(e: Throwable) {
                        KLogi("Observer: onError")
                        e.printStackTrace()
                    }
                })
    }

    /**
     * flatMap 基本使用
     * - 展开流：对Observable发射的数据流进行变换操作，然后再发射出去
     */
    private fun rxJavaTestFlatMap() {

        KLogi("====== rxJavaTestFlatMap ======")

        /*
           例1.简单使用，可以将ObservableA转换成ObservableB，无序
           输出:
             The value is 0
             The value is 1
             The value is 5
             The value is 3
             The value is 4
             The value is 2
        */
        Observable.create(object : ObservableOnSubscribe<Int> {
            override fun subscribe(emitter: ObservableEmitter<Int>) {
                for (i in 0..5) {
                    emitter.onNext(i)
                }
                emitter.onComplete()
            }
        }).flatMap(object : Function<Int, ObservableSource<String>> {
            override fun apply(t: Int): ObservableSource<String> {
                // 将Int转换成String，增加延迟，方便明显的看出无序
                return Observable.just("The value is $t").delay(Math.round(1000 * Math.random()), TimeUnit.MILLISECONDS)
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Consumer<String> {
                    override fun accept(t: String?) {
                        KLogi("accept: $t")
                    }
                })
    }


    /**
     * concatMap
     * - 和flatMap唯一的区别就是有序的
     */
    private fun rxJavaTestConcatMap() {

        KLogi("====== rxJavaTestConcatMap ======")

        /*
           例1.简单使用，可以将ObservableA转换成ObservableB，有序
           输出:
             The value is 0
             The value is 1
             The value is 2
             The value is 3
             The value is 4
             The value is 5
        */
        Observable.create(object : ObservableOnSubscribe<Int> {
            override fun subscribe(emitter: ObservableEmitter<Int>) {
                for (i in 0..5) {
                    emitter.onNext(i)
                }
                emitter.onComplete()
            }
        }).concatMap(object : Function<Int, ObservableSource<String>> {
            override fun apply(t: Int): ObservableSource<String> {
                // 将Int转换成String，增加延迟，方便明显的看出无序
                return Observable.just("The value is $t").delay(Math.round(1000 * Math.random()), TimeUnit.MILLISECONDS)
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Consumer<String> {
                    override fun accept(t: String?) {
                        KLogi("accept: $t")
                    }
                })
    }

    /**
     * interval 间隔时间执行操作
     * - 轮询等心跳间隔任务
     */
    private fun rxJavaTestInterval() {

        startTaskRecorder(this, "rxJavaTestInterval")

        // 模拟网络请求获取数据
        val getDataFromServer = Observable.create(object : ObservableOnSubscribe<String> {
            override fun subscribe(emitter: ObservableEmitter<String>) {
                KLogi("Observable: subscribe, 进行网络请求")

                OkHttpUtil.getAsync(CONFIG_SERVER_URL + "get", null, object : Callback {
                    override fun onFailure(call: Call?, e: IOException?) {
                        KLogi("onFailure: $e")
                        emitter.onError(Exception("onFailure: ${e?.message ?: "null"}"))
                    }

                    override fun onResponse(call: Call?, response: Response?) {
                        val result = response?.body()?.string()
                        KLogi("onResponse: response.body = $result")
                        if (result.isNullOrEmpty()) {
                            emitter.tryOnError(Exception("onFailure: 返回数据为空"))
                        } else {
                            emitter.onNext(result!!)
                        }
                    }
                })
            }
        })

        /*
            每隔2s轮询一次，对于这种重复执行的任务一定要在适当的时候结束掉(达到一定次数、时间或者关闭界面的时候)，否则会造成内存泄漏和崩溃。
            - initialDelay: 首次执行延时，可不传
            - period: 时间间隔
            - timeUnit: 时间单位
         */
        Observable.interval(0, 2, TimeUnit.SECONDS)
        .flatMap(object : Function<Long, ObservableSource<String>> {
            override fun apply(t: Long): ObservableSource<String> {
                KLogi("apply, t=$t")
                return getDataFromServer
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<String> {
                    override fun onComplete() {
                        KLogi("Observer: onComplete")
                    }

                    override fun onSubscribe(d: Disposable) {
                        KLogi("Observer: onSubscribe")
                        /*
                            由于该操作相当于一个定时任务，一直间隔执行，所以当Activity销毁的时候该操作还未停止。
                            所以我们需要在Activity销毁的时候就停止该操作，即适当的时候调用dispose.dispose()停止订阅。
                         */
                        disposable = d
                    }

                    override fun onNext(t: String) {
                        KLogi("Observer: onNext, value=$t")
                    }

                    override fun onError(e: Throwable) {
                        KLogi("Observer: onError")
                        e.printStackTrace()
                    }
                })

    }

    /**
     * distinct 去重
     */
    private fun rxJavaTestDistinct() {

        KLogi("====== rxJavaTestDistinct ======")

        /*
            例1.简单使用，distinct会将发射的item去重
            输出结果：1,2,3,4,5
         */
        Observable.just(1, 1, 2, 2, 3, 4, 5)
                .distinct()
                .subscribe(object : Consumer<Int> {
                    override fun accept(t: Int?) {
                        KLogi("accept: $t")
                    }
                })
    }

    /**
     * filter 过滤器
     */
    private fun rxJavaTestFilter() {

        startTaskRecorder(this, "rxJavaTestFilter")

        /*
            例1.简单使用，过滤出符合条件的Item
            输出结果：100,234
         */
        Observable.just(1, -1, 100, 234, 9)
                .filter(object : Predicate<Int> {
                    override fun test(t: Int): Boolean {
                        return t >= 100
                    }
                })
                .subscribe(
                        { t -> KLogi("accept: $t") },     // onNext
                        { },        // onError
                        { stopTaskRecorder(this, "rxJavaTestFilter") }    // onComplete
                )
    }

    /**
     * buffer 缓冲区
     */
    private fun rxJavaTestBuffer() {
        startTaskRecorder(this, "rxJavaTestBuffer")

        /*
            例1.简单使用，每次最多发射count个，步长skip
            输出结果：[1,2,3,4]    [3,4,5,6]    [5,6,7,8]   [7,8,9,10]   [9,10]
         */
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .buffer(4, 2)
                .subscribe(
                        { t -> KLogi("accept: $t") },     // onNext
                        { },        // onError
                        { stopTaskRecorder(this, "rxJavaTestBuffer") }    // onComplete
                )
    }

    /**
     * doOnNext
     * - 用于对数据流做一些处理，比如保存数据，该方法没有返回值，即不会对整个数据流有影响
     */
    private fun rxJavaTestDoOnNext() {

        data class Book(var name: String)

        /*
            例1. 先执行map1做数据转换，然后将发射的数据传给doOnNext用于数据保存或输出，然后执行map2做数据转换，然后传递给onNext()。
            需要注意的是，如果发射的数据是引用类型，那么如果在doOnNext中做了更改，那么doNext()收到的就是新值。
            输出：
                doOnNext: Kotlin-map1-doOnNext
                accept: Kotlin-map1-doOnNext-map2
         */
        Observable.just(Book("Kotlin")).map {
            it.name = it.name + "-map1"
            it
        }
                .doOnNext {
                    it.name = it.name + "-doOnNext"     // 虽然it是常量，但是它是引用类型，虽然引用没变，但是这个Book对象的name变了
                    KLogi("doOnNext: ${it.name}")
                }
                .map {
                    it.name = it.name + "-map2"
                    it
                }
                .subscribe(
                        { KLogi("accept: ${it.name}") },    // onNext()
                        { it.printStackTrace() },                 // onError()
                        { KLogi("onComplete") })            // onComplete()

        /*
            输出：
                doOnNext: 1
                accept: 1
         */
        Observable.just(0)
                .map {
                    it + 1      // 0->1，并将新的数据返回，向下游传递
                }
                .doOnNext {
                    // 该函数并没有返回值，而且it是常量
                    it + 1
                    KLogi("doOnNext: $it")
                }
                .subscribe {
                    KLogi("accept: $it")
                }

    }

    /**
     * Just 简单使用
     */
    private fun rxJavaTestJust() {

        KLogi(this, "rxJavaTestJust")

        Observable.just(1, 2, 3, 4, 5)
                .skip(1)    // 跳过前count个
                .take(3)    // 最多接收count个
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    KLogi("onNext: $it")
                }
    }
}