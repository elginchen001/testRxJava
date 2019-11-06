package com.darleer.elgin.test.testrxjava.api;

import com.darleer.elgin.test.testrxjava.model.PM10Model;
import com.darleer.elgin.test.testrxjava.model.PM25Model;
import com.darleer.elgin.test.testrxjava.model.SO2Model;

import java.util.List;

import io.reactivex.Maybe;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {
    String API_BASE_SERVER_URL = "http://www.pm25.in/";

    @GET("api/querys/pm2_5.json")
    Maybe<List<PM25Model>> pm25(@Query("city") String cityId, @Query("token") String token);

    @GET("api/querys/pm10.json")
    Maybe<List<PM10Model>> pm10(@Query("city") String cityId, @Query("token") String token);

    @GET("api/querys/so2.json")
    Maybe<List<SO2Model>> so2(@Query("city") String cityId, @Query("token") String token);
}
