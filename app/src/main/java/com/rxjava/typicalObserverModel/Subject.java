package com.rxjava.typicalObserverModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 被观察者
 */
public class Subject {

    private List<Observer> mObservers = new ArrayList<>();

    /**
     * 绑定被观察者
     * @param observer 观察者
     */
    public void attach(Observer observer) {
        mObservers.add(observer);
    }

    /**
     * 解绑定
     * @param observer
     */
    public void deAttach(Observer observer) {
        mObservers.remove(observer);
    }

    /**
     * 发送通知
     * @param name
     */
    public void notityAll(String name) {
        for (Observer observer : mObservers) {
            observer.update(name);
        }
    }
}
