package com.afrid.iscan.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.yyyu.baselibrary.utils.ResourceUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 功能：fragment的基类
 *
 * @author yyyu
 * @version 1.0
 * @date 2017/03/15
 */
public abstract class BaseFragment extends Fragment {

    protected Gson mGson;
    private View rootView;
    private Unbinder mUnbind;
    protected ResourceUtils resourceUtils;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.from(getActivity()).inflate(getLayoutId(), container, false);
        mUnbind = ButterKnife.bind(this, rootView);
        mGson = new Gson();
        resourceUtils = ResourceUtils.getInstance(getContext());
        init();
        return rootView;
    }

    private void init() {
        beforeInit();
        initView();
        initListener();
        initData();
        afterInit();
    }

    /**
     * 得到fragment布局文件id的钩子方法
     */
    public abstract int getLayoutId();

    /**
     * 初始化之前：进行一些变量的初始化
     */
    protected void beforeInit() {
    }

    /**
     * 初始化View
     */
    protected abstract void initView();

    /**
     * 初始化监听
     */
    protected abstract void initListener();

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    protected void afterInit() {
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbind.unbind();
    }

}