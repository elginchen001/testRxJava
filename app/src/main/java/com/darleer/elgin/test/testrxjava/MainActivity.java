package com.darleer.elgin.test.testrxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnTest = findViewById(R.id.btnTest);
        btnTest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int vID = v.getId();
        switch (vID)
        {
            case R.id.btnTest:
                testSubscribeAndObserveOn();
                break;
                default:
                    break;
        }
    }

    private void LogV(String info)
    {
        Log.v("TAG",info);
    }

    //region 测试repeatWhen操作符
    private  void doRxJavaTest()
    {
        io.reactivex.Observable.range(1,5).repeatWhen
                (
                        new Function<Observable<Object>, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(io.reactivex.Observable<Object> objectObservable) throws Exception {
                                return io.reactivex.Observable.timer(3, TimeUnit.SECONDS);
                            }
                        }
                )
                .subscribe(
                        new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                LogV(integer.toString());
                            }
                        }
                );

        try{
            Thread.sleep(12000);
        }catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    //endregion

    //region 测试RepeatUntil操作符
    private void doRxJavaRepeatUntilTest()
    {
        // 当getAsBoolean()返回false时，重复发射上游的observable；
        // 当getAsBoolean()返回true时，中止重复发射上游的Observable。
        final long startTimeMillis = System.currentTimeMillis();
        Observable.interval(500, TimeUnit.MILLISECONDS)
                .take(5)
                .repeatUntil(
                        new BooleanSupplier() {
                            @Override
                            public boolean getAsBoolean() throws Exception {
                                return System.currentTimeMillis()-startTimeMillis>10000;
                            }
                        }
                )
                .subscribe(
                        new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                LogV(aLong.toString());
                            }
                        }
                );
        try{
            Thread.sleep(12000);
        }catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    //endregion

    //region 测试defer操作符
    // defer一直等到有观察者订阅时，才创建Observable。且每个observable是最新的。
    private void testDefer()
    {
        Observable observable = createDeferObservable();
        observable.subscribe(
                new Consumer<String>() {
                    @Override
                    public void accept(String o) throws Exception {
                        LogV(o);
                    }
                }
        );
    }

    private Observable createDeferObservable()
    {
        return Observable.defer(
                new Callable<ObservableSource<? super String>>() {
                    @Override
                    public ObservableSource<? super String> call() throws Exception {
                        return Observable.just("Hello defer");
                    }
                }
        );
    }
    //endregion

    //region 测试interval
    // interval()每隔一段时间发射一个无限递增的整数序列
    private void testInterval()
    {
        Observable.interval(3,TimeUnit.SECONDS)
                .subscribe(
                        new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                LogV(aLong.toString());
                            }
                        }
                );
    }
    //endregion

    //region 测试timer
    // timer()创建一个在给定的时间段之后返回一个特殊值的Observable
    // 在延迟一段给定时间后，发射一个简单的数字0。
    private void testTimer()
    {
        Observable.timer(2,TimeUnit.SECONDS)
                .subscribe(
                        new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                LogV(aLong.toString());
                            }
                        }
                );
    }
    //endregion

    //region 测试scheduler
    // 在observeOn中开启新线程，用来转大写
    private void testScheduler()
    {
        Observable.just("aaa","bbb")
                .observeOn(Schedulers.newThread())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String s)
                    {
                        return s.toUpperCase();
                    }
                })
                .subscribeOn(Schedulers.single())
                .subscribe(
                        new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                LogV(s);
                            }
                        }
                );
    }
    //endregion

    //region 测试 单独使用subscribeOn
    private void testSubscribeOn() {
        Observable.create(
                new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter)
                    {
                        emitter.onNext("Hello");
                        emitter.onNext("World");
                    }
                }
        ).subscribeOn(Schedulers.newThread())
                .subscribe(
                        new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception
                        {
                            LogV(s);
                        }
                });
    }
    //endregion

    //region 多次调用subscribeOn和observeOn
    private void testSubscribeAndObserveOn()
    {
        Observable.just("Hello World")
                .subscribeOn(Schedulers.single())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String s)
                    {
                        s = s.toLowerCase();
                        return s;
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s)
                    {
                        s = s + "tony.";
                        return s;
                    }
                })
                .subscribeOn(Schedulers.computation())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s)
                    {
                        s = s + "it is a test";
                        return s;
                    }
                })
                .observeOn(Schedulers.newThread())
                .subscribe(
                        new Consumer<String>() {
                            @Override
                            public void accept(@NonNull String s) {
                                LogV(s);
                            }
                        });
    }
}
