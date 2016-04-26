package com.softfront.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.softfront.demo.core.model.AppCallBack;
import com.softfront.demo.core.model.AppError;
import com.softfront.demo.core.model.AppResponse;
import com.softfront.demo.model.DataTest;
import com.softfront.demo.ui.OnLoadMoreListener;
import com.softfront.demo.ui.adapter.DataTestAdapter;
import com.softfront.demo.until.DialogUtil;
import com.softfront.demo.until.PermissionUntil;
import com.softfront.demo.until.ToastUntil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;

public class MainActivity extends Activity {

    public static final int REQUEST_CODE_ASK_SINGLE_PERMISSION = 1;
    public static final int REQUEST_CODE_ASK_MULTI_PERMISSION = 2;

    private Call mCallDataTest;
    private DataTestAdapter dataTestAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<DataTest> dataTestList;
    protected Handler handler;

    private List<String> listPermission = new ArrayList<String>();

    @Bind(R.id.text_view_no_record)
    TextView mTxtNoRecord;
    @Bind(R.id.my_recycler_view)
    RecyclerView mRecyclerView;

    public AppApplication getAppApplication() {
        return (AppApplication) getApplication();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        dataTestList = new ArrayList<DataTest>();
        handler = new Handler();
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        dataTestAdapter = new DataTestAdapter(getApplicationContext(), dataTestList, mRecyclerView);
        mRecyclerView.setAdapter(dataTestAdapter);
        // Action get data from server
        doGetData();

        dataTestAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                dataTestList.add(null);
                dataTestAdapter.notifyItemInserted(dataTestList.size() - 1);
                doGetData();
            }
        });
        // TODO: 4/21/2016 this is sample code check permission. Now, no need import 
        // Check single permission
/*        if (PermissionUntil.checkSinglePermissionGranted(this, PermissionUntil.CAMERA,
                REQUEST_CODE_ASK_SINGLE_PERMISSION, new PermissionUntil.ShowRationaleCallback() {
                    @Override
                    public void needShowRationale(final int requestCode) {
                        showMessageOkCancel(getResources().getString(R.string.permission_access_msg) + " Camera ", new DatePickerDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PermissionUntil.createRequestSinglePermissions(MainActivity.this,
                                        PermissionUntil.CAMERA, REQUEST_CODE_ASK_SINGLE_PERMISSION);
                            }
                        });
                        return;
                    }
                })) ;

        // Check Multiple permission
        PermissionUntil.checkMultiplePermissionGranted(this, listPermission,
                REQUEST_CODE_ASK_MULTI_PERMISSION, new PermissionUntil.ShowRationaleCallback() {
                    @Override
                    public void needShowRationale(int requestCode) {
                        showMessageOkCancel(getResources().getString(R.string.permission_access_msg) + " " + listPermission.get(0),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        PermissionUntil.createRequestMultiplePermissions(MainActivity.this,
                                                listPermission, REQUEST_CODE_ASK_MULTI_PERMISSION);
                                    }
                                });
                    }
                });*/
    }

    @Override
    protected void onDestroy() {
        if (mCallDataTest != null) mCallDataTest.cancel();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_SINGLE_PERMISSION:
                if (PermissionUntil.isGrantedForResult(grantResults)) {
                    // TODO: 4/21/2016
                } else {
                    // TODO: 4/20/2016
                }
                break;
            case REQUEST_CODE_ASK_MULTI_PERMISSION:
                // TODO: 4/21/2016
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /*Get data from server via API*/
    public void doGetData() {
        mRecyclerView.setVisibility(View.GONE);
        mTxtNoRecord.setVisibility(View.GONE);
        mCallDataTest = getAppApplication().getAppService().getDataTest();
        mCallDataTest.enqueue(new AppCallBack<AppResponse<List<DataTest>>>() {
            @Override
            public void onSuccess(AppResponse<List<DataTest>> response) {
                if (response.getData() != null) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    List<DataTest> listData = response.getData();
                    if (listData.size() > 0) {
                        Log.d("AAA", "dataList size: " + dataTestList.size());
                        if (dataTestList.size() > 0) {
                            dataTestList.remove(dataTestList.size() - 1);
                            dataTestAdapter.notifyItemRemoved(dataTestList.size());
                        }
                        dataTestList.addAll(listData);
                        dataTestAdapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUntil.show("Response data is " + response.getData());
                    mTxtNoRecord.setVisibility(View.VISIBLE);
                }
                dataTestAdapter.setLoaded();
            }

            @Override
            public void onFailure(AppError appError) {
                mTxtNoRecord.setVisibility(View.VISIBLE);
                DialogUtil.appError(MainActivity.this, appError);
            }
        });
    }

    // Show data get from server to the screen
    private void showDataOnScreen(List<DataTest> dataTestList) {
        /* Test for one element */
        DataTest dataTest = dataTestList.get(0);
        if (dataTest != null) {
        }
    }

    public void showMessageOkCancel(String message, DialogInterface.OnClickListener clickListener) {
        AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
        alBuilder.setMessage(message);
        alBuilder.setPositiveButton(getResources().getString(R.string.btn_ok), clickListener);
        alBuilder.setNegativeButton(getResources().getString(R.string.btn_cancel), null);
        alBuilder.create();
        alBuilder.show();
    }
}
