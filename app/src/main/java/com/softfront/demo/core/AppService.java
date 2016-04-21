package com.softfront.demo.core;

import com.softfront.demo.core.model.AppResponse;
import com.softfront.demo.model.DataTest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by nguyen.quang.tung on 4/14/2016.
 */
public interface AppService {
    int ERROR_UNKNOW = 0;
    int STATUS_ERROR = 600;

    @GET("api/v2/api_test/")
    Call<AppResponse<List<DataTest>>> getDataTest();

    @GET("api/v2/api_test/{appId}")
    Call<AppResponse<List<DataTest>>> getDataApp(@Path("id") String id);

    @POST("api/v2/api_test/{appId}")
    Call<AppResponse<DataTest>> createApp(@Body DataTest dataTest);

    // In addition, you can put the POST, PUT, QUERY,... methods in here

}
