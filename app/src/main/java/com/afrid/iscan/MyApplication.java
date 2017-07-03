package com.afrid.iscan;

import android.app.Application;

import com.afrid.btprinter.BTPrinterManager;
import com.afrid.iscan.bean.HotelSource;
import com.afrid.iscan.bean.SpOffice;
import com.afrid.iscan.bean.XdSysUseArea;
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

    private HotelSource org;
    private XdSysUseArea area;
    private SpOffice dept;

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

    public HotelSource getOrg() {
        return org;
    }

    public void setOrg(HotelSource org) {
        this.org = org;
    }

    public XdSysUseArea getArea() {
        return area;
    }

    public void setArea(XdSysUseArea area) {
        this.area = area;
    }

    public SpOffice getDept() {
        return dept;
    }

    public void setDept(SpOffice dept) {
        this.dept = dept;
    }
}
