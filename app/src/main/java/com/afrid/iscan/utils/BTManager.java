package com.afrid.iscan.utils;

import android.bluetooth.BluetoothDevice;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 功能：蓝牙相关工具类
 *
 * @author yu
 * @version 1.0
 * @date 2017/6/23
 */

public class BTManager {

    /**
     * 绑定蓝牙设备
     *
     * @param device
     * @return
     */
    public static boolean bondDevice(BluetoothDevice device) {
        try {
            if(device != null) {
                if(device.getBondState() == 12) {
                    return true;
                } else {
                    Method e = BluetoothDevice.class.getMethod("createBond", new Class[0]);
                    e.invoke(device, new Object[0]);
                    return true;
                }
            } else {
                return false;
            }
        } catch (NoSuchMethodException var2) {
            var2.printStackTrace();
            return false;
        } catch (IllegalAccessException var3) {
            var3.printStackTrace();
            return false;
        } catch (IllegalArgumentException var4) {
            var4.printStackTrace();
            return false;
        } catch (InvocationTargetException var5) {
            var5.printStackTrace();
            return false;
        }
    }


}
