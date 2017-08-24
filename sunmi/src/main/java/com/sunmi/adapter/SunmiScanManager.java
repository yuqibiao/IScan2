package com.sunmi.adapter;

import android.app.Activity;
import android.content.Intent;

/**
 * 功能：sunmi扫描相关
 *
 * @author yu
 * @version 1.0
 * @date 2017/8/24
 */
public class SunmiScanManager {

    public static final int START_SCAN = 1001;

    private static SunmiScanManager ourInstance = new SunmiScanManager();

    public static SunmiScanManager getInstance() {
        return ourInstance;
    }

    private SunmiScanManager() {
    }


    public  void toScanAtcForResult(Activity activity) {
        Intent intent = new Intent("com.summi.scan");
        intent.setPackage("com.sunmi.sunmiqrcodescanner");
        intent.putExtra("CURRENT_PPI", 0X0003);//当前分辨率
        //M1和V1的最佳是800*480,PPI_1920_1080 = 0X0001;PPI_1280_720 =
        //0X0002;PPI_BEST = 0X0003;
        intent.putExtra("PLAY_SOUND", true);// 扫描完成声音提示  默认true
        intent.putExtra("PLAY_VIBRATE", false);
        //扫描完成震动,默认false，目前M1硬件支持震动可用该配置，V1不支持
        intent.putExtra("IDENTIFY_INVERSE_QR_CODE", true);// 识别反色二维码，默认true
        intent.putExtra("IDENTIFY_MORE_CODE", false);// 识别画面中多个二维码，默认false
        intent.putExtra("IS_SHOW_SETTING", true);// 是否显示右上角设置按钮，默认true
        intent.putExtra("IS_SHOW_ALBUM", true);// 是否显示从相册选择图片按钮，默认true
        activity.startActivityForResult(intent, START_SCAN);

    }
}
