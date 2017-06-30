package com.afrid.iscan.ui.activity;

import android.app.Activity;
import android.content.Intent;

import com.afrid.iscan.R;

/**
 * 功能：
 *
 * @author yu
 * @version 1.0
 * @date 2017/6/30
 */

public class ConnectSuccessActivity extends BaseActivity{
    @Override
    public int getLayoutId() {
        return R.layout.activity_connect_success;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    public static void startAction(Activity activity){
        Intent intent = new Intent(activity , ConnectSuccessActivity.class);
        activity.startActivity(intent);
    }
}
