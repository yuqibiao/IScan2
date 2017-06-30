package com.afrid.iscan.utils.bt;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yyyu.baselibrary.utils.MyLog;

/**
 * 功能：蓝牙接受广播
 *
 * @author yu
 * @version 1.0
 * @date 2017/6/29
 */

public class BTReceiver extends BroadcastReceiver {

    private static final String TAG = "BTReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        bluetoothDevice.getName();//蓝牙名称
        bluetoothDevice.getAddress();//蓝牙mac地址
        bluetoothDevice.getBluetoothClass().getDeviceClass();//蓝牙设备的类型
        MyLog.e(TAG , "===="+bluetoothDevice.getAddress());
    }

}
