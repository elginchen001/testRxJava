package com.darleer.elgin.test.testrxjava.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.darleer.elgin.test.testrxjava.R;
import com.darleer.elgin.test.testrxjava.api.APIService;
import com.darleer.elgin.test.testrxjava.model.CityModel;
import com.darleer.elgin.test.testrxjava.retrofit.RetrofitManager;
import com.safframework.utils.RxJavaUtils;

public class CityActivity extends AppCompatActivity implements View.OnClickListener{

    private String CITY_ID = "hefei";
    private Button btnRefresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int vID = v.getId();
        switch (vID)
        {
            case R.id.btnRefresh:
            {
                getCityStations();
            }
            default:
                break;
        }
    }

    private void getCityStations()
    {
        APIService apiService = RetrofitManager.getRetrofit().create(APIService.class);
        apiService.getCityStations(CITY_ID)
                .compose(RxJavaUtils.<CityModel>maybeToMain())
    }
}
