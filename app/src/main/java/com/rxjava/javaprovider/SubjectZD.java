package com.rxjava.javaprovider;

import java.util.Observable;


public class SubjectZD extends Observable {

    public void notity(String name) {
        setChanged();
        notifyObservers(name);
    }
}
