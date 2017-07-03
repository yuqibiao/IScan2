package com.afrid.iscan.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.afrid.btprinter.BTPrinterManager;
import com.afrid.iscan.R;
import com.afrid.iscan.ui.activity.BaseActivity;
import com.afrid.swingu.utils.SwingUManager;
import com.yyyu.baselibrary.utils.MyToast;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 功能：布草类型选择
 *
 * @author yu
 * @version 1.0
 * @date 2017/6/27
 */

public class TypeChoiceActivity extends BaseActivity {


    @BindView(R.id.tv_hospital)
    TextView tvHospital;
    @BindView(R.id.tv_department)
    TextView tvDepartment;
    private String hos;
    private String dept;

    @Override
    public int getLayoutId() {
        return R.layout.activity_type_choice;
    }

    @Override
    public void beforeInit() {
        hos = getIntent().getStringExtra("hos");
        dept = getIntent().getStringExtra("dept");
    }

    @Override
    protected void initView() {
        tvHospital.setText("您所在的医院是："+hos);
        tvDepartment.setText(""+dept);
    }

    @Override
    protected void initListener() {

    }

    @OnClick({R.id.btn_normal, R.id.btn_special, R.id.btn_return, R.id.btn_stop})
    public void oprate(View view) {
        if(!BTPrinterManager.getInstance(this).isConnected()){
            MyToast.showShort(this , "蓝牙打印机未链接！！");
            BTDeviceScanActivity.startAction(this);
            return;
        }
        if(!SwingUManager.getInstance(this).isConncted()){
            MyToast.showShort(this , "手持机未链接！！");
            BTDeviceScanActivity.startAction(this);
            return;
        }
        switch (view.getId()) {
            //---TODO 判断设备是否连接成功
            case R.id.btn_normal:
                LinenScanActivity.startAction(this , hos , dept,0 );
                break;
            case R.id.btn_special:
                LinenScanActivity.startAction(this , hos , dept,1 );
                break;
            case R.id.btn_return:
                LinenScanActivity.startAction(this , hos , dept,2);
                break;
            case R.id.btn_stop:
                break;
        }
    }

    public static void startAction(Activity activity , String hos , String dept){
        Intent intent = new Intent(activity , TypeChoiceActivity.class);
        intent.putExtra("hos" , hos);
        intent.putExtra("dept" , dept);
        activity.startActivity(intent);
    }

}
