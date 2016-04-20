package com.softfront.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.softfront.demo.core.model.AppCallBack;
import com.softfront.demo.core.model.AppError;
import com.softfront.demo.core.model.AppResponse;
import com.softfront.demo.model.DataTest;
import com.softfront.demo.until.DialogUtil;
import com.softfront.demo.until.ToastUntil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;

public class MainActivity extends Activity {

    private Call mCallDataTest;

    @Bind(R.id.progressBar_id) ProgressBar mpProgressBar;
    @Bind(R.id.text_view_id) TextView mTextId;
    @Bind(R.id.text_view_name) TextView mTextName;
    @Bind(R.id.text_view_type) TextView mTextType;
    @Bind(R.id.text_view_data) TextView mTextData;

    public AppApplication getAppApplication() {
        return (AppApplication) getApplication();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        // Action get data from server
        doGetData();
    }

    @Override
    protected void onDestroy() {
        //   if (mCallDataTest != null) mCallDataTest.cancel();
        super.onDestroy();
    }

    /*Get data from server via API*/
    public void doGetData() {
        mpProgressBar.setVisibility(View.VISIBLE);
        mCallDataTest = getAppApplication().getAppService().getDataTest();
        mCallDataTest.enqueue(new AppCallBack<AppResponse<List<DataTest>>>() {
            @Override
            public void onSuccess(AppResponse<List<DataTest>> response) {
                mpProgressBar.setVisibility(View.GONE);
                if (response.getData() != null) {
                    List<DataTest> dataTestList = response.getData();
                    if (dataTestList.size() > 0) {
                        showDataOnScreen(dataTestList);
                    }
                } else {
                    ToastUntil.show("Response data is " + response.getData());
                }
            }

            @Override
            public void onFailure(AppError appError) {
                mpProgressBar.setVisibility(View.GONE);
                DialogUtil.appError(MainActivity.this, appError);
            }
        });
    }

    // Show data get from server to the screen
    private void showDataOnScreen(List<DataTest> dataTestList) {
        /* Test one element */
        DataTest dataTest = dataTestList.get(0);
        if (dataTest != null) {
            mTextId.setText(String.valueOf(dataTest.getId()).toString());
            mTextName.setText(dataTest.getName());
            mTextType.setText(String.valueOf(dataTest.isType()));
            mTextData.setText(dataTest.getData());
        }
    }
}
