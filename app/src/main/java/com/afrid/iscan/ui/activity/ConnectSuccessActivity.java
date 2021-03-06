package com.afrid.iscan.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.afrid.iscan.R;
import com.afrid.iscan.ui.activity.BaseActivity;
import com.afrid.iscan.utils.LogicUtils;
import com.afrid.swingu.utils.SwingUManager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 功能：设备连接成功界面
 *
 * @author yu
 * @version 1.0
 * @date 2017/6/27
 */

public class ConnectSuccessActivity extends BaseActivity {

    @BindView(R.id.tv_hospital)
    TextView tvHospital;
    @BindView(R.id.tv_department)
    TextView tvDepartment;
    @BindView(R.id.tv_linen_type)
    TextView tvLinenType;


    @Override
    public int getLayoutId() {
        return R.layout.activity_connect_success;
    }

    @Override
    protected void initView() {
        tvHospital.setText("您所在的医院是：郑州省人民医院");
        tvDepartment.setText("消化内科第一科");
        tvLinenType.setText("收获类型为：普通布草");
    }

    @Override
    protected void initListener() {

    }

    @OnClick({R.id.btn_start_scan, R.id.btn_stop_scan})
    public void oprate(View view) {
        switch (view.getId()) {
            case R.id.btn_start_scan:
                SwingUManager.getInstance(this).startReader();

                break;
            case R.id.btn_stop_scan:
                SwingUManager.getInstance(this).startReader();
                break;
        }
    }

    public static void startAction(Activity activity){
        Intent intent = new Intent(activity , com.afrid.iscan.ui.activity.ConnectSuccessActivity.class);
        activity.startActivity(intent);
    }

}
