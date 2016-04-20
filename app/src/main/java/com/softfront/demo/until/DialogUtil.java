package com.softfront.demo.until;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.softfront.demo.R;
import com.softfront.demo.core.model.AppError;

import java.util.HashMap;

/**
 * Created by nguyen.quang.tung on 4/14/2016.
 */
public class DialogUtil {
    public interface onEventOk {
        void onOk();
    }

    public interface onEventCancel {
        void onCancel();
    }

    private static AlertDialog.Builder alertBuilder;

    private static HashMap<Integer, Boolean> cacheDilog = new HashMap<>();

    public static void appError(final Activity activity, final AppError appError) {
        appError(activity, appError, null);
    }


    public static void appError(final Activity activity, final AppError appError, final onEventOk onEventOk) {
        appError(activity, appError, onEventOk, null);
    }

    public static void appError(final Activity activity, final AppError appError, final onEventOk onEventOk, DialogInterface.OnDismissListener onDismissListener) {
        if (activity == null) return;
        else if (activity.isFinishing()) return;

        boolean ok = cacheDilog.containsKey(Integer.valueOf(appError.getStatus_code()));
        if (ok) return;

        String strMsg = appError.getMessage();
        String btnOk = activity.getString(R.string.btn_ok);

        if (appError.getStatus_code() <= 600) {
            strMsg = TextUtils.isEmpty(appError.getMessage()) ? activity.getString(R.string.unable_connection) : appError.getMessage();
        }
        cacheDilog.put(Integer.valueOf(appError.getStatus_code()), true);

    }

    public static void showDialogNotify(final Activity activity,
                                        final boolean isCancel,
                                        final String title,
                                        final String message,
                                        final String strOk,
                                        final onEventOk onEventOk,
                                        final String strCancel,
                                        final onEventCancel onEventCancel) {
        if (activity == null || activity.isFinishing()) return;

        alertBuilder = new AlertDialog.Builder(activity);
        alertBuilder.setCancelable(isCancel);
        alertBuilder.setTitle(title);
        alertBuilder.setMessage(message);
        if (!TextUtils.isEmpty(strOk)) {
            alertBuilder.setPositiveButton(strOk, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (onEventOk != null) onEventOk.onOk();
                }
            });
        }

        if (!TextUtils.isEmpty(strCancel)) {
            alertBuilder.setPositiveButton(strCancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (onEventCancel != null) onEventCancel.onCancel();
                }
            });
        }
        alertBuilder.show();
    }


    public static void showDialogNotify(final Activity activity,
                                        final boolean isCancel,
                                        final String title,
                                        final String message,
                                        final String strOk,
                                        final onEventOk onEventOk,
                                        final String strCancel,
                                        final onEventCancel onEventCancel,
                                        final DialogInterface.OnDismissListener onDismissListener) {
        showDialogNotify(activity, isCancel, title, message, strOk, onEventOk, strCancel, onEventCancel);

        if (onDismissListener != null) alertBuilder.setOnDismissListener(onDismissListener);
        alertBuilder.show();
    }
}
