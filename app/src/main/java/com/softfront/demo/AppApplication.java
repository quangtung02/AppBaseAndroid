package com.softfront.demo;

import android.app.Application;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.softfront.demo.core.AppConst;
import com.softfront.demo.core.AppService;
import com.softfront.demo.core.model.AppDerializer;
import com.softfront.demo.core.model.AppResponse;
import com.softfront.demo.core.model.BooleanSerializer;
import com.softfront.demo.until.SSLSocketFactoryUntil;
import com.softfront.demo.until.ToastUntil;

import java.io.IOException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by nguyen.quang.tung on 4/14/2016.
 */
public class AppApplication extends Application {

    private static AppService appService;

    @Override
    public void onCreate() {
        super.onCreate();

        ToastUntil.init(this);
        initAPI();
    }

    public AppService getAppService() {
        return appService;
    }

    public void initAPI() {
        BooleanSerializer booleanSerializer = new BooleanSerializer();
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(AppResponse.class, new AppDerializer())
                .registerTypeAdapter(Boolean.class, booleanSerializer)
                .registerTypeAdapter(boolean.class, booleanSerializer)
                .create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(SSLSocketFactoryUntil.createSSLSocketFactory(getApplicationContext()))
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request requestWithHeader = request.newBuilder()
                                .header("X-APP-VERSION", BuildConfig.VERSION_NAME) // Request parameter header
                                .build();

                        Response response = chain.proceed(requestWithHeader);
                        return response;
                    }
                }).build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConst.BASE_URL) // Url: connect to server
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        appService = retrofit.create(AppService.class);
    }
}
