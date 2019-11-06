package com.darleer.elgin.test.testrxjava.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.darleer.elgin.test.testrxjava.R;
import com.darleer.elgin.test.testrxjava.api.APIService;
import com.darleer.elgin.test.testrxjava.retrofit.RetrofitManager;

public class PmActivity extends AppCompatActivity {

    public String CITY_ID = "hefei";
    public String TOKEN = "5j1znBVAsnSf5xQyNQyq";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pm);
        getPmValue();
    }

    private void getPmValue()
    {
        APIService apiService = RetrofitManager.getRetrofit().create(APIService.class);
        apiService.pm25(CITY_ID,TOKEN);


    }
}
