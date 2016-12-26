package com.rxjava.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.rxjava.javaprovider.ObserverZD;
import com.rxjava.javaprovider.SubjectZD;
import com.rxjava.R;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Subscriber<String> subscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSubcriber();

    }

    /**
     * 初始化观察者
     */
    private void initSubcriber() {

        subscriber = new Subscriber<String>() {

            /**
             * This method is invoked when the Subscriber and Observable have been connected but the Observable has
             * not yet begun to emit items or send notifications to the Subscriber. Override this method to add any
             * useful initialization to your subscription, for instance to initiate backpressure.
             */
            @Override
            public void onStart() {
                super.onStart();
                Log.e(TAG, "onStart() can write some initialized code here");
            }

            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted() message has run finished successfully");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError() cause s/0 :java.lang.arithmeticexception");
            }

            @Override
            public void onNext(String s) {
                Log.e(TAG, "onNext() message " + s + " has been accepted");
                if (s.equals("2")) {
                    int ints = Integer.valueOf(s) / 0;
                }
            }
        };
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    /**
     * 传统的观察者设计模式
     *
     * @param view
     */
    public void onClick1(View view) {
        SubjectZD subjectZD = new SubjectZD();
        ObserverZD observerZD = new ObserverZD(subjectZD);
        ObserverZD observerZD1 = new ObserverZD(subjectZD);
        subjectZD.notity("我是谁   " + System.currentTimeMillis());
    }


    /**
     * createMode 方法
     * 运行过程中出现异常：代码将不会继续向下执行
     *
     * @param view
     */
    public void createMode(View view) {

        //如果Observable使用create()方法的话，只有在observable中方法中调用 onCompleted() 才会执行onCompleted()方法
        // 如果observable 是用的是just 或者是 from 的话无需任何操作也会调用onCompleted()方法

        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("1");
                subscriber.onNext("2");
                subscriber.onNext("3");
                subscriber.onNext("4");
                subscriber.onCompleted();
            }
        });
        observable.subscribe(subscriber);
    }

    /**
     * justMode 模式
     * 运行过程中出现异常：代码将不会继续向下执行
     *
     * @param view
     */
    public void justMode(View view) {
        Observable<String> observable = Observable.just("1", "2", "3", "4", "5", "6", "7", "8");
        observable.subscribe(subscriber);
    }

    /**
     * fromMode 模式
     * 运行过程中出现异常：代码将不会继续向下执行
     *
     * @param view
     */
    public void fromMode(View view) {
        String[] ints = {"1", "2", "3", "4", "5"};
        Observable<String> observable = Observable.from(ints);
        observable.subscribe(subscriber);
    }


    public void actionMode(View view) {

        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG, s);
            }
        };

        Action0 onCompleteAction = new Action0() {
            @Override
            public void call() {
                Log.e(TAG, "action finish run success !");
            }
        };

        Action1<Throwable> onErrorAction = new Action1<Throwable>() {

            @Override
            public void call(Throwable throwable) {

            }
        };

        final Observable<String> observable = Observable
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext("1");
                        subscriber.onNext("2");
                        subscriber.onNext("3");
                        subscriber.onNext("4");
                        subscriber.onCompleted();
                    }
                });
        observable.subscribe(onNextAction, onErrorAction, onCompleteAction);
    }

    /**
     * 线程控制：immediate() 在当前线程  newThread总是启动新的线程 io()用于文件读写试用  mainThread ui线程。
     * AndroidScheculers.mainThread()
     * Schedulers.io() newThread() mainThread()
     *
     * @param view subscribeOn(): 指定 subscribe() 所发生的线程，即 Observable.OnSubscribe 被激活时所处的线程。或者叫做事件产生的线程。
     *             observeOn(): 指定 Subscriber 所运行在的线程。或者叫做事件消费的线程。
     */
    public void onSchedulerClick(View view) {
        Observable.just(1, 2, 3).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println("integer = " + integer);
                    }
                });
    }
}
