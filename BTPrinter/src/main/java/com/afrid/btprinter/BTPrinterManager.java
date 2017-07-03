package com.afrid.btprinter;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.wpx.IBluetoothPrint;
import com.wpx.WPXMain;
import com.wpx.util.ConnCallBack;
import com.wpx.util.GeneralAttributes;
import com.wpx.util.WPXUtils;

import static android.content.ContentValues.TAG;
import static android.os.Looper.getMainLooper;

/**
 * 功能：蓝牙打印机管理
 *
 * @author yu
 * @version 1.0
 * @date 2017/6/30
 */

public class BTPrinterManager {

    private static Context mContext;

    private BTPrinterManager(){

    }

    private static class InstanceHolder{
         static BTPrinterManager INSTANCE = new BTPrinterManager();
    }

    public static BTPrinterManager getInstance(Context context){
        mContext = context;
        return InstanceHolder.INSTANCE;
    }

    public void printOne(String info){
        WPXMain.printCommand(GeneralAttributes.INSTRUCTIONS_GS_CHARACTER_SIZE_DEFUALT);
        WPXMain.printCommand(GeneralAttributes.INSTRUCTIONS_ESC_ALIGN_CENTER);
        IBluetoothPrint print = WPXMain.getBluetoothPrint();
        print.printOne(info);
    }

    /**
     * 打印文字(居中)
     * @param info
     */
    public void printText(String info){
        WPXMain.printCommand(GeneralAttributes.INSTRUCTIONS_GS_CHARACTER_SIZE_DEFUALT);
        WPXMain.printCommand(GeneralAttributes.INSTRUCTIONS_ESC_ALIGN_CENTER);
        IBluetoothPrint print = WPXMain.getBluetoothPrint();
        print.printText(info);
    }

    public void onDestoryPrinter(){
        disconnect();
    }

    /**
     * 判断是否连接
     * @return
     */
    public boolean isConnected(){
        return WPXMain.isConnected();
    }

    /**
     * 断开连接
     */
    public void disconnect(){
        WPXMain.disconnectDevice();
    }

    /**
     * 连接
     */
    public void connect(final BluetoothDevice device ){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isConntected = WPXMain.connectDevice(device.getAddress());
                handler.sendEmptyMessage(0);
            }
        }, 500);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==0){
                Toast.makeText(mContext , "蓝牙打印机连接成功！！" , Toast.LENGTH_SHORT);
            }
        }
    };

    public void initPrint(Application application){
        WPXMain.init(application);
    }

}
