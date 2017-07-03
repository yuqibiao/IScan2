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

    public static final String BASE_URL ="http://192.168.1.113:8080/SwingUserver/service-provider/app";//"http://fangkalaundryapptest.chinacloudsites.cn/service-provider/app";

    /**
     * 登录
     */
    public static final String LOGIN = BASE_URL + "/login";

    /**
     * 部门
     */
    public static final String ORG_AREA_DEPT = BASE_URL + "/org-area-dept";

    /**
     * 得到标签信息
     */
    public static final String GET_TAGS_INFO = BASE_URL + "/get-tags-info";

    /**
     * Normal wash receive
     */
    public static final String NORMAL_WASH_RECEIVE = BASE_URL + "/normal-wash-receive";

    /**
     * Special wash receive
     */
    public static final String SPECIAL_WASH_RECEIVE = BASE_URL + "/special-wash-receive";

    /**
     * Return wash receive
     */
    public static final String RETURN_WASH_RECEIVE = BASE_URL + "/return-wash-receive";

}
