package com.afrid.iscan.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.afrid.iscan.R;

public class SubmitResultActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_submit_result;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    public static void startAction(Activity activity){
        Intent intent = new Intent(activity , SubmitResultActivity.class);
        activity.startActivity(intent);
    }
}
