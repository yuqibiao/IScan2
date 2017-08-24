package com.sunmi.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.widget.Toast;

import com.sunmi.printerhelper.utils.AidlUtil;

/**
 * 功能：sunmi打印相关api再次封装
 *
 * @author yu
 * @version 1.0
 * @date 2017/8/24
 */
public class SunmiPrintManager {

    private static SunmiPrintManager ourInstance = new SunmiPrintManager();
    private final AidlUtil aidlUtil;

    public static SunmiPrintManager getInstance() {
        return ourInstance;
    }

    private SunmiPrintManager() {
        aidlUtil = AidlUtil.getInstance();
    }

    /**
     * 初始化打印机
     */
    public void initPrinter() {
       aidlUtil.initPrinter();
    }

    /**
     * 连接服务
     *
     * @param context context
     */
    public void connectPrinterService(Context context) {
       aidlUtil.connectPrinterService(context);
    }

    /**
     * 断开服务
     *
     * @param context context
     */
    public void disconnectPrinterService(Context context) {
       aidlUtil.disconnectPrinterService(context);
    }

    public boolean isConnect() {
       return aidlUtil.isConnect();
    }


    public void printOne(String data){
        printOne(data , 8,115 , 2 , 2);
    }

    public void printTwo(String data){
        printTwo(data , 8 ,3);
    }

    /**
     * 打印二维码
     *
     * @param data
     * @param modulesize
     * @param errorlevel
     */
    public void printTwo(String data, int modulesize, int errorlevel){
        aidlUtil.printQr(data , modulesize , errorlevel);
    }

    /**
     *打印条形码
     *
     * @param data
     * @param symbology
     * @param height
     * @param width
     * @param textposition
     */
    public void printOne(String data, int symbology, int height, int width, int textposition){
        aidlUtil.printBarCode(data , symbology , height ,width , textposition);
    }

    public void printText(String into){
        printText(into , 24 , false , false);
    }

    /**
     * 打印文字
     *
     * @param info
     * @param size
     * @param isBold
     * @param isUnderline
     */
    public void printText(String info , float size , boolean isBold , boolean isUnderline ){
        aidlUtil.printText(info , size , isBold ,isUnderline);
    }

}
