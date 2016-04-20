package com.softfront.demo.core.model;

import android.text.TextUtils;

import com.softfront.demo.core.AppService;
import com.softfront.demo.until.ToastUntil;

/**
 * Created by nguyen.quang.tung on 4/14/2016.
 */
public class AppError {
    private int status_code;
    private String title;
    private String message;

    public AppError(int status_code, String title, String message) {
        this.status_code = status_code;
        this.title = title;
        if (this.status_code == AppService.STATUS_ERROR) {
            this.message = TextUtils.isEmpty(message) ? ToastUntil.getUnableConnection() : message;
        } else {
            this.message = status_code == AppService.ERROR_UNKNOW ? ToastUntil.getMessageInternetProblems() : message;
        }
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
