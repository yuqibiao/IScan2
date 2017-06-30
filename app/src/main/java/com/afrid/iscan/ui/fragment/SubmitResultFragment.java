package com.afrid.iscan.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afrid.iscan.R;
import com.afrid.iscan.adapter.LinenAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 功能：提交扫描结果
 *
 * @author yu
 * @version 1.0
 * @date 2017/6/27
 */

public class SubmitResultFragment extends BaseFragment {


    @BindView(R.id.tv_hospital)
    TextView tvHospital;
    @BindView(R.id.tv_department)
    TextView tvDepartment;
    @BindView(R.id.tv_linen_type)
    TextView tvLinenType;
    @BindView(R.id.rv_order)
    RecyclerView rvOrder;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_submit_result;
    }

    @Override
    protected void initView() {
        tvHospital.setText("您所在的医院是：郑州省人民医院");
        tvDepartment.setText("要收布草的科室为：消化内科第一科");
        tvLinenType.setText("收货类型为：正常布草");

        rvOrder.setLayoutManager(new LinearLayoutManager(getContext()));
        rvOrder.setAdapter(new LinenAdapter(getContext()));

    }

    @Override
    protected void initListener() {

    }

    @OnClick(R.id.btn_submit)
    public void oprate(View view){
        //---TODO 确认收货

    }

}
