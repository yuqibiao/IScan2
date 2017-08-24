package com.afrid.iscan.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.afrid.iscan.R;
import com.sunmi.adapter.SunmiPrintManager;
import com.sunmi.adapter.SunmiScanManager;
import com.yyyu.baselibrary.utils.MyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class TestActivity extends AppCompatActivity {

    private SunmiPrintManager sunmiPrintManager;
    private SunmiScanManager sunmiScanManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        sunmiPrintManager = SunmiPrintManager.getInstance();
        sunmiScanManager = SunmiScanManager.getInstance();
    }

    public void printText(View view){
        sunmiPrintManager.printText("和呢呵呵呵呵呵呵呵\r\n1123123");
    }

    public void printOne(View view){
        sunmiPrintManager.printTwo("1234567890");
    }

    public void toScan(View view){
        sunmiScanManager.toScanAtcForResult(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SunmiScanManager.START_SCAN && data != null) {
            Bundle bundle = data.getExtras();
            ArrayList<HashMap<String, String>> result = (ArrayList<HashMap<String, String>>) bundle.getSerializable("data");
            Iterator<HashMap<String, String>> it = result.iterator();
            while (it.hasNext()) {
                HashMap<String, String> hashMap = it.next();
                Log.i("sunmi", hashMap.get("TYPE"));//这个是扫码的类型
                Log.i("sunmi", hashMap.get("VALUE"));//这个是扫码的结果
                MyToast.showLong(TestActivity.this ,hashMap.get("VALUE") );
            }

        }
    }
}
