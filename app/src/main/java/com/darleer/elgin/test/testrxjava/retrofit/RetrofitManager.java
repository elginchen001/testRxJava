package com.darleer.elgin.test.testrxjava.retrofit;

import com.darleer.elgin.test.testrxjava.api.APIService;
import com.safframework.http.interceptor.LoggingInterceptor;

import java.util.concurrent.TimeUnit;
import java.util.logging.LoggingPermission;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private static Retrofit mRetrofit;
    public static Retrofit getRetrofit()
    {
        if(mRetrofit==null)
        {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.writeTimeout(30*1000, TimeUnit.MILLISECONDS);
            builder.writeTimeout(20*1000, TimeUnit.MILLISECONDS);
            builder.connectTimeout(15*1000,TimeUnit.MILLISECONDS);

            LoggingInterceptor loggingInterceptor = new LoggingInterceptor.Builder()
                    .loggable(true)
                    .request()
                    .requestTag("Request")
                    .response()
                    .requestTag("Response")
                    .build();

            OkHttpClient okHttpClient = builder.build();
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(APIService.API_BASE_SERVER_URL)
                    .addConverterFactory(FastJsonConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return mRetrofit;
    }
}
