package com.dgk.klibrary.demo.rxjava;

import com.dgk.common.util.log.KLogKt;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * 简单的RxBus
 * - 基于RxJava实现的事件总线，用于不同对象(组件)之间进行通信。
 */
public class RxBus1 {

    /**
     * 单例模式
     */
    public static RxBus1 getInstance() {
        return RxBusHolder.rxBus1;
    }

    /**
     * 使用静态内部类实现单例模式
     * - 静态内部类只有在第一次使用的时候才会加载，并且是线程安全的，所以
     * 当调用RxBus1.getInstance()方法时，会加载RxBusHolder内部类，同时创建RxBus1实例。
     */
    private static class RxBusHolder {
        private static RxBus1 rxBus1 = new RxBus1();
    }

    /**
     * 同时代表了一个观察者和一个被观察者，允许将单个源的多播事件传递给多个子观察者。
     */
    private Subject<Object> mSubject;

    private RxBus1() {
        /*
            创建一个PublishSubject实例，并且将其方法转变为线程安全的。
         */
        mSubject = PublishSubject.create().toSerialized();
    }

    /**
     * 发送事件
     */
    public void post(Object event) {
        KLogKt.KLogi("发送事件 " + event.getClass().getSimpleName());
        mSubject.onNext(event);
    }

    /**
     * 订阅事件
     */
    public <T> Observable<T> toObservable(Class<T> event) {
        KLogKt.KLogi("订阅事件 " + event.getSimpleName());
        return mSubject.ofType(event);
    }
}