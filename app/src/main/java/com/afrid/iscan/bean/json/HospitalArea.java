package com.afrid.iscan.bean.json;

/**
 * 功能：医院区域
 *
 * @author yu
 * @version 1.0
 * @date 2017/7/1
 */

public class HospitalArea {

    private int areaId;//区域id

    private String name;//医院+区域

    public HospitalArea(int areaId, String name) {
        this.areaId = areaId;
        this.name = name;
    }


}
