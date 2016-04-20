package com.softfront.demo.core.model;

import com.softfront.demo.core.AppService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by nguyen.quang.tung on 4/14/2016.
 */
public abstract class AppCallBack<T> implements Callback<T> {

    public abstract void onSuccess(T response);

    public abstract void onFailure(AppError appError);

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            AppResponse<T> appResponse = (AppResponse<T>) response.body();
            if (appResponse.isSuccess()) {
                onSuccess(response.body());
            } else {
                onFailure(new AppError(appResponse.getStatus_code(),
                        appResponse.getTitle(), appResponse.getMessage()));
            }
        } else {
            onFailure(new AppError(AppService.ERROR_UNKNOW, "", ""));
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onFailure(new AppError(AppService.ERROR_UNKNOW, "",
                "You are having connection problems, please try again.")); // Fixed, because here have not context
    }
}
