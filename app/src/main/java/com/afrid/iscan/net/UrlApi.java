package com.afrid.iscan.net;

import java.util.Observable;

import retrofit2.http.POST;

/**
 * 功能：网络请求的API
 *
 * @author yyyu
 * @version 1.0
 * @date 2017/5/8
 */
public interface UrlApi {

    public static final String BASE_URL ="http://fangkalaundryapptest.chinacloudsites.cn/service-provider/app";

    public static final String LOGIN = BASE_URL + "/login";

}
