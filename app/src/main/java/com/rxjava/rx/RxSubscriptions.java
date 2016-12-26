package com.rxjava.rx;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


public class RxSubscriptions {

    private static CompositeSubscription mSubscriptions = new CompositeSubscription();

    public static boolean isUnsubscribed() {
        return mSubscriptions.isUnsubscribed();
    }

    public static void add(Subscription s) {
        if (s != null) {
            mSubscriptions.add(s);
        }
    }

    public static void remove(Subscription s) {
        if (s != null) {
            mSubscriptions.remove(s);
        }
    }

    public static void clear() {
        mSubscriptions.clear();
    }

    public static void unsubscribe() {
        mSubscriptions.unsubscribe();
    }

    /**
     * 是否有订阅者
     *
     * @return
     */
    public static boolean hasSubscriptions() {
        return mSubscriptions.hasSubscriptions();
    }
}
