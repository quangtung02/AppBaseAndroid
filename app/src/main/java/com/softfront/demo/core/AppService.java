package com.softfront.demo.core;

import com.softfront.demo.core.model.AppResponse;
import com.softfront.demo.model.DataTest;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * Created by nguyen.quang.tung on 4/14/2016.
 */
public interface AppService {
    int ERROR_UNKNOW = 0;
    int STATUS_ERROR = 600;

    @GET("api/v2/api_test/")
    Call<AppResponse<List<DataTest>>> getDataTest();

    @Multipart
    @POST("upload_file")
    Call<AppResponse<List<DataTest>>> upload(@Part("description") RequestBody description, @PartMap Map<String, MultipartBody.Part> files);

    // In addition, you can put the POST, PUT, QUERY,... methods in here
}
