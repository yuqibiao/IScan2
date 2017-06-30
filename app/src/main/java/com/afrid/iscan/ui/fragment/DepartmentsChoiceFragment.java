package com.afrid.iscan.ui.fragment;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.afrid.btprinter.BTPrinterManager;
import com.afrid.iscan.MyApplication;
import com.afrid.iscan.R;
import com.afrid.iscan.adapter.DepartmentsAdapter;
import com.afrid.iscan.bean.json.UserInfo;
import com.afrid.iscan.ui.activity.TypeChoiceActivity;
import com.afrid.iscan.utils.LogicUtils;
import com.afrid.swingu.utils.SwingUManager;
import com.yyyu.baselibrary.utils.MyToast;
import com.yyyu.baselibrary.view.recyclerview.adapter.HeaderAndFooterWrapper;
import com.yyyu.baselibrary.view.recyclerview.listener.OnRvItemClickListener;

import butterknife.BindView;

/**
 * 功能：科室选择
 *
 * @author yu
 * @version 1.0
 * @date 2017/6/27
 */

public class DepartmentsChoiceFragment extends BaseFragment {

    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.rv_departments)
    RecyclerView rvDepartments;
    private DepartmentsAdapter departmentsAdapter;
    private UserInfo userInfo;

    @Override
    protected void beforeInit() {
        userInfo = ((MyApplication)getActivity().getApplication()).getUserInfo();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_departments_choice;
    }

    @Override
    protected void initView() {
        rvDepartments.setLayoutManager(new GridLayoutManager(getContext() , 3));
        departmentsAdapter = new DepartmentsAdapter(getContext());
        rvDepartments.setAdapter(departmentsAdapter);
    }

    @Override
    protected void initListener() {
        rvDepartments.addOnItemTouchListener(new OnRvItemClickListener(rvDepartments) {
            @Override
            public void onItemClick(View itemView, int position) {
                TypeChoiceActivity.startAction(getActivity());
            }
            @Override
            public void onItemLongClick(View itemView, int position) {

            }
        });
    }

}
