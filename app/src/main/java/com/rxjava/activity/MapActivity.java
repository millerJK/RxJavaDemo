package com.rxjava.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rxjava.R;
import com.rxjava.entity.Course;
import com.rxjava.entity.Student;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 关于map  flatMap  针对时间序列的处理和在发送
 * Rxjava 中Map和flatMap 都是基于同一个变换方法lift()
 */
public class MapActivity extends AppCompatActivity {

    private static final String TAG = "MapActivity";
    private LinearLayout mLinearLay;

    Integer[] res = {R.drawable.ic_launcher, R.drawable.live_head, R.drawable.ic_launcher};
    private Observable<Integer> observable;
    private Subscriber<Drawable> subscriber;

    private Observable<Student> flatObservable;
    private Subscriber<Course> flatSubsriber;

    private ArrayList<Student> mStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mLinearLay = (LinearLayout) findViewById(R.id.two_line);
        observable = Observable.from(res);

        initStudents();

    }

    private void initStudents() {
        mStudents = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Student stu = new Student();
            stu.setName("course" + i);
            List<Course> courses = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                Course course = new Course();
                course.setCourseName(stu.getName() + "课程" + j);
                courses.add(course);
            }
            stu.setCourses(courses);
            mStudents.add(stu);
        }
    }

    /**
     * map API
     * Map() Fun（） 方法只是转换成为一个变量
     *
     * @param view
     */
    public void mapClick(View view) {

        subscriber = new Subscriber<Drawable>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Drawable drawable) {
                ImageView imageView = new ImageView(MapActivity.this);
                mLinearLay.addView(imageView);
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                lp.width = 200;
                lp.height = 200;
                imageView.setLayoutParams(lp);
                imageView.setImageDrawable(drawable);
            }
        };

        observable.from(res)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Integer, Drawable>() {
                    @Override
                    public Drawable call(Integer integer) {
                        return getResources().getDrawable(integer);
                    }
                }).subscribe(subscriber);

    }


    /**
     * flatMap API
     * flatMap则是转换成为一个Observable 对象
     *
     * @param view
     */
    public void flatMapClick(View view) {

        flatSubsriber = new Subscriber<Course>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Course course) {
                Log.e(TAG, course.getCourseName());
            }
        };

        flatObservable.from(mStudents)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Student, Observable<Course>>() {
                    @Override
                    public Observable<Course> call(Student student) {
                        return Observable.from(student.getCourses());
                    }
                }).subscribe(flatSubsriber);
    }


    public void turnThree(View view) {
        Intent intent = new Intent(this, RxBusActivity.class);
        startActivity(intent);
    }

    //// FIXME: 2016/12/26 对于lift方法没有理解
    public void lift() {

    }

}
