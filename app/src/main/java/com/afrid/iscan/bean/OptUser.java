package com.afrid.iscan.bean;

import java.io.Serializable;

/**
 * Created by yuran on 2017/6/11.
 */

public class OptUser implements Serializable{
    private String name;
    private String passWd;
    private String group;

    public void setName(final String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setPassWd(final String passWd) { this.passWd = passWd; }
    public String getPassWd() {
        return passWd;
    }

    public void setGroup(final String group) {
        this.group = group;
    }
    public String getGroup() {
        return group;
    }
}
