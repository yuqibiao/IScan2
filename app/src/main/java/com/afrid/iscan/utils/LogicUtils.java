package com.afrid.iscan.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.afrid.iscan.R;

/**
 * 功能：一些工具类
 *
 * @author yu
 * @version 1.0
 * @date 2017/6/27
 */

public class LogicUtils {


    /**
     * 添加fragment
     *
     * @param activity
     * @param fragment
     * @param isAddToBAckStack
     */
    public static void addFragment(FragmentActivity activity , Fragment fragment , boolean isAddToBAckStack){
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fl_content , fragment);
        if(isAddToBAckStack){
            ft.addToBackStack(null);
        }
        ft.commit();
    }

}
