package com.rxjava.typicalObserverModel;


public class Observer {

    private Subject mSubject;

    public Observer(Subject mSubject) {
        this.mSubject = mSubject;
    }

    public void update(String name) {
        System.out.println(System.currentTimeMillis()+"接收到了+ " + name);
    }

    public void attachSubject() {
        if (mSubject != null)
            mSubject.attach(this);
    }

    public void deAttachSubject() {
        if (mSubject != null)
            mSubject.deAttach(this);
    }
}
