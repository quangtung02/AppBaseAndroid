package com.softfront.demo.until;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nguyen.quang.tung on 4/21/2016.
 */
public class PermissionUntil {

    public static final String CAMERA = Manifest.permission.CAMERA;
    public static final String READ_CONTACTS = Manifest.permission.READ_CONTACTS;
    public static final String WRITE_CONTACTS = Manifest.permission.WRITE_CONTACTS;
    public static final String[] listPermission = new String[]{CAMERA, READ_CONTACTS, WRITE_CONTACTS};


    public interface ShowRationaleCallback {

        //Call this method if we should show rationale
        void needShowRationale(final int requestCode);
    }

    public static final boolean checkSinglePermissionGranted(final Activity activity, final String permission,
                                                       final int requestCode,
                                                       final ShowRationaleCallback showRationaleCallback) {
        boolean isGranted = true;

        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            isGranted = false;

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                if (showRationaleCallback != null)
                    showRationaleCallback.needShowRationale(requestCode);
            } else {
                createRequestSinglePermissions(activity, permission, requestCode);
            }
        }
        return isGranted;
    }

    public static void createRequestSinglePermissions(Activity activity, final String permission, int requestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
    }

    /*
    * return true if granted
    * */
    public static boolean isGrantedForResult(int[] grantResults) {
        return grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }

    public static void checkMultiplePermissionGranted(final Activity activity, final List<String> permissionList,
                                               final int requestCode,
                                               final ShowRationaleCallback showRationaleCallback) {
        List<String> permissionNeed = new ArrayList<String>();

        for (int i = 0; i < listPermission.length; i++) {
            if (!addPermissionList(activity, permissionList, listPermission[i])) {
                permissionNeed.add(listPermission[i]);
            }
        }

        if (permissionList.size() > 0) {
            if (permissionNeed.size() > 0) {
                // Check rationale
                if (showRationaleCallback != null)
                    showRationaleCallback.needShowRationale(requestCode);
            } else {
                createRequestMultiplePermissions(activity, permissionList, requestCode);
            }
        }
    }

    public static void createRequestMultiplePermissions(Activity activity, final List<String> permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions.toArray(new String[permissions.size()]), requestCode);
    }

    public static boolean addPermissionList(Activity activity, List<String> listPermission, String permission) {
        if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            listPermission.add(permission);
            // Check show rationale
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission))
                return false;
        }
        return true;
    }
}
