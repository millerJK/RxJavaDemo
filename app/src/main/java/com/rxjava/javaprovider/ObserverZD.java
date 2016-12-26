package com.rxjava.javaprovider;

import android.util.Log;

import java.util.Observable;
import java.util.Observer;


public class ObserverZD implements Observer {

    private static final String TAG = "ObserverZD";
    public SubjectZD mSubjectZD;

    public ObserverZD(SubjectZD mSubjectZD) {
        this.mSubjectZD = mSubjectZD;
        mSubjectZD.addObserver(this);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof SubjectZD) {
            Log.e(TAG, data.toString());
        }
    }

    public void removeObserver() {
        if (mSubjectZD != null)
            mSubjectZD.deleteObserver(this);
    }
}
