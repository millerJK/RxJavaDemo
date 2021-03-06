package com.rxjava.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rxjava.R;
import com.rxjava.TUtil;
import com.rxjava.event.Event;
import com.rxjava.event.EventSticky;
import com.rxjava.event.PictureEvent;
import com.rxjava.rx.RxBus;
import com.rxjava.rx.RxSubscriptions;

import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;


/**
 * 观察者(订阅者)
 */
public class SubscriberFragment extends Fragment {

    private static final String TAG = "RxBus";

    private TextView mTvResult, mTvResultSticky;
    private Button mBtnSubscribeSticky;
    private ImageView mImageView;

    private Subscription mRxSub, mRxSubSticky, mPicSubscription;


    public static SubscriberFragment newInstance() {
        return new SubscriberFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSubscription();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscriber, container, false);
        initView(view);
        return view;
    }


    private void initSubscription() {

        RxSubscriptions.remove(mPicSubscription);
        mPicSubscription = RxBus.getInstance()
                .toObservable(PictureEvent.class)
                .subscribe(new Action1<PictureEvent>() {
                    @Override
                    public void call(PictureEvent pictureEvent) {
                        mImageView.setImageResource(pictureEvent.img_id);
                    }
                });
        RxSubscriptions.add(mPicSubscription);
    }

    private void initView(View view) {
        mTvResult = (TextView) view.findViewById(R.id.tv_result);
        mTvResultSticky = (TextView) view.findViewById(R.id.tv_resultSticky);
        mBtnSubscribeSticky = (Button) view.findViewById(R.id.btn_subscribeSticky);
        mImageView = (ImageView) view.findViewById(R.id.ic_launcher_show);

        // 订阅普通RxBus事件
        subscribeEvent();
        TUtil.showShort(getActivity(), R.string.rxbus);

        mBtnSubscribeSticky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 订阅Sticky事件
                subscribeEventSticky();
            }
        });
    }

    private void subscribeEvent() {
        RxSubscriptions.remove(mRxSub);
        mRxSub = RxBus.getInstance().toObservable(Event.class)
                .map(new Func1<Event, Event>() {
                    @Override
                    public Event call(Event event) {
                        // 变换等操作
                        return event;
                    }
                }).subscribe(new Action1<Event>() {
                    @Override
                    public void call(Event myEvent) {
                        Log.i(TAG, "onNext--->" + myEvent.event);
                        String str = mTvResult.getText().toString();
                        mTvResult.setText(TextUtils.isEmpty(str) ? String.valueOf(myEvent.event) : str + ", " + myEvent.event);
                    }
                });
        RxSubscriptions.add(mRxSub);

        TUtil.showShort(getActivity(), R.string.resubscribe);
    }


    private void subscribeEventSticky() {
        if (mRxSubSticky != null && !mRxSubSticky.isUnsubscribed()) {
            //已经订阅
            mTvResultSticky.setText("");
            RxSubscriptions.remove(mRxSubSticky);

            mBtnSubscribeSticky.setText(R.string.subscribeSticky);
            TUtil.showShort(getActivity(), R.string.unsubscribeSticky);
        } else {
            //尚未订阅
            mRxSubSticky = RxBus.getInstance().toStickObservable(EventSticky.class)
                    // 建议在Sticky时,在操作符内主动try,catch
                    .map(new Func1<EventSticky, EventSticky>() {
                        @Override
                        public EventSticky call(EventSticky eventSticky) {
                            try {
                                // 变换操作
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return eventSticky;
                        }
                    }).subscribe(new Action1<EventSticky>() {
                        @Override
                        public void call(EventSticky eventSticky) {
                            Log.i(TAG, "onNext--Sticky-->" + eventSticky.event);

                            String str = mTvResultSticky.getText().toString();
                            mTvResultSticky.setText(TextUtils.isEmpty(str) ? String.valueOf(eventSticky.event) : str + ", " + eventSticky.event);
                        }
                    });
            RxSubscriptions.add(mRxSubSticky);

            mBtnSubscribeSticky.setText(R.string.unsubscribeSticky);
            TUtil.showShort(getActivity(), R.string.subscribeSticky);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 从CompositeSubscription中移除取消订阅事件,防止内存泄漏
        RxSubscriptions.remove(mRxSub);
        RxSubscriptions.remove(mRxSubSticky);
        RxSubscriptions.remove(mPicSubscription);


    }
}
