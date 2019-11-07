package com.darleer.elgin.test.testrxjava.activity;

import android.support.v4.util.Preconditions;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.darleer.elgin.test.testrxjava.R;
import com.darleer.elgin.test.testrxjava.api.APIService;
import com.darleer.elgin.test.testrxjava.model.PM25Model;
import com.darleer.elgin.test.testrxjava.retrofit.RetrofitManager;
import com.safframework.utils.RxJavaUtils;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class PmActivity extends AppCompatActivity {

    public String CITY_ID = "hefei";
    public String TOKEN = "5j1znBVAsnSf5xQyNQyq";

    private TextView txtQuality,txtPm25,txtPm25With24h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pm);
        initViews();

        getPmValue();
    }

    private void initViews()
    {
        this.txtQuality = findViewById(R.id.txtQuality);
        this.txtPm25 = findViewById(R.id.txtPm25);
        txtPm25With24h = findViewById(R.id.txtPm25With24h);
    }

    private void getPmValue()
    {
        APIService apiService = RetrofitManager.getRetrofit().create(APIService.class);
        apiService.pm25(CITY_ID,TOKEN)
                .compose(RxJavaUtils.<List<PM25Model>>maybeToMain())
                .filter(new Predicate<List<PM25Model>>() {
                    @Override
                    public boolean test(List<PM25Model> pm25Models) throws Exception {

                        return com.safframework.tony.common.utils.Preconditions.isNotBlank(pm25Models);
                    }
                })
                .flatMap(new Function<List<PM25Model>, MaybeSource<PM25Model>>() {
                    @Override
                    public MaybeSource<PM25Model> apply(List<PM25Model> pm25Models) throws Exception {

                        for (PM25Model model:pm25Models){

                            if ("南门".equals(model.position_name)) {
                                return Maybe.just(model);
                            }
                        }

                        return null;
                    }
                })
                .subscribe(new Consumer<PM25Model>() {
                    @Override
                    public void accept(PM25Model model) throws Exception {

                        if (model != null) {
                            txtQuality.setText("空气质量指数：" + model.quality);
                            txtPm25.setText("PM2.5 1小时内平均：" + model.pm2_5);
                            txtPm25With24h.setText("PM2.5 24小时滑动平均：" + model.pm2_5_24h);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.println(throwable.getMessage());
                    }
                });



    }
}
