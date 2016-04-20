package com.softfront.demo.until;

import android.content.Context;
import android.widget.Toast;

import com.softfront.demo.R;

/**
 * Created by nguyen.quang.tung on 4/14/2016.
 */
public class ToastUntil {

    private static Context context;

    public static void init(Context context) {
        ToastUntil.context = context;
    }

    public static void show(String string, int length) {
        Toast.makeText(context, string, length).show();
    }

    public static void show(String string) {
        ToastUntil.show(string, Toast.LENGTH_SHORT);
    }

    public static void show(int strId) {
        ToastUntil.show(context.getString(strId), Toast.LENGTH_SHORT);
    }

    public static void show(int strId, int length) {
        ToastUntil.show(context.getString(strId), length);
    }

    public static String getMessageInternetProblems() {
        return context.getString(R.string.internet_unable_connection);
    }

    public static String getUnableConnection() {
        return context.getString(R.string.unable_connection);
    }
}
