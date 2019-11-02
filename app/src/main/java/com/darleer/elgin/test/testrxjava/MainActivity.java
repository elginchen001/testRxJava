package com.darleer.elgin.test.testrxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
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
                testFlatMap();
                break;
                default:
                    break;
        }
    }

    private void LogV(String info)
    {
        Log.v("TAG",info);
    }

    //region 测试map()和flatMap()方法
    private User initUserData()
    {
        User user = new User();
        User.Address address1 = new User.Address("chang jiang Load","Hefei");
        User.Address address2 = new User.Address("huai ning Load","beijing");
        user.AddressList = new ArrayList<>();
        user.AddressList.add(address1);
        user.AddressList.add(address2);
        return user;
    }

    /**
     * 将User.AddressList转为List<User.Address>然后返回</>
     */
    private void testMap()
    {
        User user = initUserData();
        Observable.just(user)
                .map(new Function<User, List<User.Address>>() {
                    @Override
                    public List<User.Address> apply(User user)
                    {
                        return user.AddressList;
                    }
                })
                .subscribe(new Consumer<List<User.Address>>() {
                    @Override
                    public void accept(List<User.Address> addresses) throws Exception {
                        for( User.Address addr :addresses)
                        {
                            LogV(addr.street);
                        }
                    }
                });
    }

    /**
     * 使用fromIterable()逐次发射list中的数据，然后flatMap合并这些Observable发射的数据，
     * 最后将合并后的结果当做它自己的数据序列发射。
     */
    private void testFlatMap()
    {
        User user = initUserData();
        Observable.just(user)
                .flatMap(new Function<User, ObservableSource<User.Address>>() {
                    @Override
                    public ObservableSource<User.Address> apply(User user) throws Exception {
                        return Observable.fromIterable(user.AddressList);
                    }
                })
                .subscribe(new Consumer<User.Address>() {
                    @Override
                    public void accept(User.Address address) throws Exception {
                        LogV(address.street);
                    }
                });
    }
    //endregion

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
        Observable.just("1","2","3","4","5")
                .subscribeOn(Schedulers.single())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String s)
                    {
                        s = s.toLowerCase();
                        s = s+":"+Thread.currentThread().getName();
                        LogV(s);
                        return s;
                    }
                })
                .skip(1)
                .observeOn(Schedulers.io())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s)
                    {
                        s = s+":"+Thread.currentThread().getName();
                        LogV(s);
                        return s;
                    }
                })
                .subscribeOn(Schedulers.computation())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s)
                    {
                        s = s+":"+Thread.currentThread().getName();
                        LogV(s);
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
    //endregion
}
