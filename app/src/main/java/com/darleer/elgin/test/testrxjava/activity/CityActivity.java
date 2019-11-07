package com.darleer.elgin.test.testrxjava.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.darleer.elgin.test.testrxjava.R;
import com.darleer.elgin.test.testrxjava.api.APIService;
import com.darleer.elgin.test.testrxjava.model.CityModel;
import com.darleer.elgin.test.testrxjava.model.StationModel;
import com.darleer.elgin.test.testrxjava.retrofit.RetrofitManager;
import com.safframework.tony.common.utils.Preconditions;
import com.safframework.utils.RxJavaUtils;

import java.util.List;
import java.util.Observable;

import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CityActivity extends AppCompatActivity implements View.OnClickListener{

    private String CITY_ID = "hefei";
    private String TOKEN = "5j1znBVAsnSf5xQyNQyq";

    private Button btnRefresh;
    private TextView txtCityName;
    private ListView lvStation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(this);
        txtCityName = findViewById(R.id.txtCityName);
        lvStation = findViewById(R.id.lvStation);
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
        io.reactivex.Observable<CityModel> observable = apiService.getCity(CITY_ID,TOKEN).subscribeOn(Schedulers.io());
                observable
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                        new Consumer<CityModel>() {
                            @Override
                            public void accept(CityModel model) {
                                if (model != null) {
                                    txtCityName.setText(model.getCity());
                                    setListView(model);
                                }
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.v("TAG",throwable.getMessage());
                            }
                        }
                );

    }

    private void setListView(CityModel model)
    {
        String[] stationData = new String[]{};
        List<CityModel.StationsBean> stations = model.getStations();
        if(stations!=null&&stations.size()>0)
        {
            for (CityModel.StationsBean station : stations) {
                int i = 0;
                stationData[i] = station.getStation_name();
                i++;
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.listview_citystation_item,stationData);
        lvStation.setAdapter(adapter);

    }
}
