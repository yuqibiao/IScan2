package com.afrid.iscan.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afrid.btprinter.BTPrinterManager;
import com.afrid.iscan.MyApplication;
import com.afrid.iscan.R;
import com.afrid.iscan.adapter.LinenAdapter;
import com.afrid.iscan.bean.Message;
import com.afrid.iscan.bean.ScanResult;
import com.afrid.iscan.bean.TagInfo;
import com.afrid.iscan.bean.WashReceiveInfo;
import com.afrid.iscan.bean.XdCompany;
import com.afrid.iscan.bean.json.UserInfo;
import com.afrid.iscan.net.UrlApi;
import com.afrid.iscan.ui.activity.BaseActivity;
import com.afrid.iscan.utils.NetUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

public class SubmitResultActivity extends BaseActivity {

    private String TAG = "SubmitResultActivity";

    @BindView(R.id.tv_hospital)
    TextView tvHospital;
    @BindView(R.id.tv_department)
    TextView tvDepartment;
    @BindView(R.id.tv_linen_type)
    TextView tvLinenType;
    @BindView(R.id.tv_useless_tag)
    TextView tvUselessTag;
    @BindView(R.id.tv_out_of_range)
    TextView tvOutOfRange;
    @BindView(R.id.rv_order)
    RecyclerView rvOrder;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    //---TAG UID 集合
    private ArrayList<String> tagList;
    //--- key：标签类型名 value：标签信息集合
    private HashMap<String, List<TagInfo>> container;
    //---非本科室标签
    private List<TagInfo> outOfRangeTagList = new ArrayList<>();
    //---无用标签
    private List<TagInfo> uselessTagList = new ArrayList<>();
    private LinenAdapter adapter;
    private String hospital;
    private int linenType;
    private String dept;

    @Override
    public int getLayoutId() {
        return R.layout.activity_submit_result;
    }

    @Override
    public void beforeInit() {
        tagList = getIntent().getStringArrayListExtra("tagList");
        dept = getIntent().getStringExtra("dept");
        hospital = getIntent().getStringExtra("hospital");
        linenType = getIntent().getIntExtra("linenType", -1);
        container = new HashMap<>();
    }

    @Override
    protected void initView() {
        tvHospital.setText("您所在的医院是："+hospital);
        tvDepartment.setText("要收布草的科室为："+dept);
        tvLinenType.setText("收货类型为："+linenType);
        switch (linenType) {
            case 0:
                tvLinenType.setText("收货类型为：正常布草" );
                break;
            case 1:
                tvLinenType.setText("收货类型为：特殊布草" );
                break;
            case 2:
                tvLinenType.setText("收货类型为：返厂布草" );
                break;
        }
        rvOrder.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LinenAdapter(this);
        rvOrder.setAdapter(adapter);

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        showLoadDialog("数据加载中....");
        Message message = new Message();
        XdCompany xdCompany = ((MyApplication) getApplication()).getUserInfo().getXdCompany();
        message.setJsonMessage(mGson.toJson(tagList));
        message.setXdCompany(xdCompany);
        String params = mGson.toJson(message);
        new NetUtils().getData(UrlApi.GET_TAGS_INFO, params, new NetUtils.OnResultListener() {
            @Override
            public void onSuccess(String result) {
                TypeToken<List<TagInfo>> tagListToken = new TypeToken<List<TagInfo>>() {
                };
                List<TagInfo> tagInfoList = mGson.fromJson(result, tagListToken.getType());
                MyLog.e(TAG, "tagInfoList==" + tagInfoList.size());
                for (TagInfo tagInfo : tagInfoList) {
                    if (tagInfo.getOrgName().equals(hospital)) {//---只留所选医院的标签
                        if (!container.containsKey(tagInfo.getObjName())) {
                            container.put(tagInfo.getObjName(), new ArrayList<TagInfo>());
                        }
                        container.get(tagInfo.getObjName()).add(tagInfo);
                    }else{//---其他医院/未识别 标签
                        outOfRangeTagList.add(tagInfo);
                    }
                }
                List<ScanResult> mData = new ArrayList<>();
                Iterator iterator = container.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, List<TagInfo>> entry = (Map.Entry<String, List<TagInfo>>) iterator.next();
                    String tagName = entry.getKey();
                    int tagNum = entry.getValue().size();
                    mData.add(new ScanResult(tagName, tagNum));
                }
                hidenLoadingDialog();
                adapter.setmData(mData);
                tvUselessTag.setText("无用标签的数量有："+(tagList.size()-tagInfoList.size())+"个");
                tvOutOfRange.setText("非本科室的标签数为："+outOfRangeTagList.size()+"个");
            }

            @Override
            public void onFailed(String error) {
                MyToast.showShort(SubmitResultActivity.this , error);
                hidenLoadingDialog();
            }
        });
    }

    @OnClick(R.id.btn_submit)
    public void operate(View view) {
        btnSubmit.setEnabled(false);
        //---得到选中的
        List<String> checkedList = adapter.getCheckedPosition();
        List<TagInfo> temp = new ArrayList<>();
        for (String  key : checkedList){
            temp.addAll(container.get(key));
        }

        String url = "";
        switch (linenType) {
            case 0:
                url = UrlApi.NORMAL_WASH_RECEIVE;
                break;
            case 1:
                url = UrlApi.SPECIAL_WASH_RECEIVE;
                break;
            case 2:
                url = UrlApi.RETURN_WASH_RECEIVE;
                break;
        }
        if(!TextUtils.isEmpty(url)){
            final String uid = UUID.randomUUID().toString();
            MyApplication myApplication = (MyApplication)getApplication();
            Message message = new Message();
            XdCompany xdCompany = ((MyApplication) getApplication()).getUserInfo().getXdCompany();

            message.setXdCompany(xdCompany);

            UserInfo userInfo = myApplication.getUserInfo();
            WashReceiveInfo washReceiveInfo = new WashReceiveInfo();
            washReceiveInfo.setUser(userInfo.getUser());
            washReceiveInfo.setArea(myApplication.getArea());
            washReceiveInfo.setTags(temp);
            washReceiveInfo.setDept(myApplication.getDept());
            washReceiveInfo.setBarCode(uid);

            message.setJsonMessage(mGson.toJson(washReceiveInfo));

            String param = mGson.toJson(message);

            MyLog.e(TAG , "param==="+param);

            new NetUtils().getData(url,param , new NetUtils.OnResultListener() {
                @Override
                public void onSuccess(String result) {
                    MyLog.e(TAG , "result"+result);
                    if(result.equals("1")){
                        //打印条码
                        BTPrinterManager.getInstance(SubmitResultActivity.this).printOne(uid);
                        finish();
                    }else{
                        MyToast.showShort(SubmitResultActivity.this , "内部错误，请重新提交");
                        btnSubmit.setEnabled(true);
                    }
                }

                @Override
                public void onFailed(String error) {
                    btnSubmit.setEnabled(true);
                    MyToast.showShort(SubmitResultActivity.this , error);
                }
            });
        }
    }


    public static void startAction(Activity activity, String hospital, String dept , int linenType, ArrayList<String> tagList) {
        Intent intent = new Intent(activity, com.afrid.iscan.ui.activity.SubmitResultActivity.class);
        intent.putStringArrayListExtra("tagList", tagList);
        intent.putExtra("dept" , dept);
        intent.putExtra("hospital", hospital);
        intent.putExtra("linenType", linenType);
        activity.startActivity(intent);
    }

}
