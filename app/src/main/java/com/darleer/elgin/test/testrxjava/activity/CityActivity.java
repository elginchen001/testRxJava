package com.darleer.elgin.test.testrxjava.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.darleer.elgin.test.testrxjava.R;

public class CityActivity extends AppCompatActivity implements View.OnClickListener{

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

    }
}
