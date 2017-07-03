package com.afrid.iscan.bean;

/**
 * 功能：扫描结果对应Tag信息Bean
 *
 * @author yu
 * @version 1.0
 * @date 2017/7/1
 */

public class ScanResult {

    private String tagName;
    private int tagNum;

    public ScanResult(){

    }

    public ScanResult(String tagName, int tagNum) {
        this.tagName = tagName;
        this.tagNum = tagNum;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public int getTagNum() {
        return tagNum;
    }

    public void setTagNum(int tagNum) {
        this.tagNum = tagNum;
    }
}
