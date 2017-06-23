package com.afrid.iscan.net;

/**
 * 功能：网络请求API的统一管理类，和其它组件进行交互
 *
 * @author yyyu
 * @version 1.0
 * @date 2017/5/8
 */

public class APIMethodManager {

    private static final String TAG = "APIMethodManager";


    private APIMethodManager() {

    }

    private static class SingletonHolder {
        private static final APIMethodManager INSTANCE = new APIMethodManager();
    }

    public static APIMethodManager getInstance() {
        return SingletonHolder.INSTANCE;
    }



}