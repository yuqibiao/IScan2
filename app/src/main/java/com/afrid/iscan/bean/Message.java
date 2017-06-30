package com.afrid.iscan.bean;

/**
 * Created by Alex on 2017/3/24.
 */

public class Message {
    private XdCompany xdCompany;
    private String jsonMessage;

    public Message() {
        xdCompany = null;
        jsonMessage = null;
    }

    public Message(final XdCompany xdCompany, final String jsonMessage) {
        this.xdCompany = xdCompany;
        this.jsonMessage = jsonMessage;
    }


    public void setXdCompany(XdCompany xdCompany) {
        this.xdCompany = xdCompany;
    }
    public XdCompany getXdCompany() {
        return xdCompany;
    }

    public void setJsonMessage(String jsonMessage) {
        this.jsonMessage = jsonMessage;
    }
    public String getJsonMessage() {
        return jsonMessage;
    }
}
