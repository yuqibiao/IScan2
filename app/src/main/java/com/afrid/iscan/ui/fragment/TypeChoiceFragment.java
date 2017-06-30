package com.afrid.iscan.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afrid.btprinter.BTPrinterManager;
import com.afrid.iscan.R;
import com.afrid.iscan.ui.activity.BTDeviceScanActivity;
import com.afrid.iscan.ui.activity.LineScanActivity;
import com.afrid.iscan.utils.LogicUtils;
import com.afrid.swingu.utils.SwingUManager;
import com.yyyu.baselibrary.utils.MyToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 功能：布草类型选择
 *
 * @author yu
 * @version 1.0
 * @date 2017/6/27
 */

public class TypeChoiceFragment extends BaseFragment {


    @BindView(R.id.tv_hospital)
    TextView tvHospital;
    @BindView(R.id.tv_department)
    TextView tvDepartment;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_type_choice;
    }

    @Override
    protected void initView() {
        tvHospital.setText("您所在的医院是：郑州省人民医院");
        tvDepartment.setText("消化内科第一科");
    }

    @Override
    protected void initListener() {

    }

    @OnClick({R.id.btn_normal, R.id.btn_special, R.id.btn_return, R.id.btn_stop})
    public void oprate(View view) {
        if(!BTPrinterManager.getInstance(getContext()).isConnected()){
            MyToast.showShort(getContext() , "蓝牙打印机未链接！！");
            BTDeviceScanActivity.startAction(getActivity());
            return;
        }
        if(!SwingUManager.getInstance(getContext()).isConncted()){
            MyToast.showShort(getContext() , "手持机未链接！！");
            BTDeviceScanActivity.startAction(getActivity());
            return;
        }
        switch (view.getId()) {
            //---TODO 判断设备是否连接成功
            case R.id.btn_normal:
                LineScanActivity.startAction(getActivity());
                break;
            case R.id.btn_special:
                break;
            case R.id.btn_return:
                break;
            case R.id.btn_stop:
                break;
        }
    }

}
