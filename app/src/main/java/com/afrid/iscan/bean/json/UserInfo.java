package com.afrid.iscan.bean.json;

import com.afrid.iscan.bean.OptUser;
import com.afrid.iscan.bean.XdCompany;

import java.io.Serializable;

/**
 * 功能：登录返回
 *
 * @author yu
 * @version 1.0
 * @date 2017/6/30
 */

public class UserInfo implements Serializable{

    private XdCompany xdCompany;

    private OptUser user;

    public UserInfo(){

    }

    public XdCompany getXdCompany() {
        return xdCompany;
    }

    public void setXdCompany(XdCompany xdCompany) {
        this.xdCompany = xdCompany;
    }

    public OptUser getUser() {
        return user;
    }

    public void setUser(OptUser user) {
        this.user = user;
    }
}
