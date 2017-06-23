package com.afrid.iscan.ui.activity;

import com.afrid.iscan.R;

/**
 * 功能：登录界面
 *
 * @author yu
 * @version 1.0
 * @date 2017/5/9
 */

public class LoginActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        getSupportActionBar().setTitle(R.string.login);
    }

    @Override
    protected void initListener() {

    }
}
