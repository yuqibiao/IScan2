package com.afrid.iscan;

import android.app.Application;

import com.afrid.btprinter.BTPrinterManager;
import com.afrid.iscan.bean.json.UserInfo;

/**
 * 功能：自定义Application
 *
 * @author yyyu
 * @version 1.0
 * @date 2017/5/8
 */

public class MyApplication extends Application{

    private UserInfo userInfo;

    @Override
    public void onCreate() {
        super.onCreate();
        BTPrinterManager.getInstance(getApplicationContext()).initPrint(this);
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
