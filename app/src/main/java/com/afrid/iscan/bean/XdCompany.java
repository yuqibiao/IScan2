package com.afrid.iscan.bean;

import java.io.Serializable;

/**
 * Created by yuran on 2017/6/11.
 */

public class XdCompany implements Serializable{
    private String name;

    public XdCompany() {
        name = null;
    }

    public XdCompany(String name) {
        this.name = name;
    }

    public void setName(final String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
