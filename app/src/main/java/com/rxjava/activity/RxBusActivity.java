package com.rxjava.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.rxjava.R;
import com.rxjava.fragment.ObservableFragment;
import com.rxjava.fragment.SubscriberFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RxBusActivity extends AppCompatActivity {

    @Bind(R.id.observableframe)
    FrameLayout mObservableframe;
    @Bind(R.id.observer_frame)
    FrameLayout mObserverFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_bus);

        SubscriberFragment subscriberFragment = SubscriberFragment.newInstance();
        ObservableFragment observableFragment = ObservableFragment.newInstance();

        ButterKnife.bind(this);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.observableframe, observableFragment)
                .add(R.id.observer_frame, subscriberFragment)
                .commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
