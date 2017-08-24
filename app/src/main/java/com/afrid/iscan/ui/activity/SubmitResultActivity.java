package com.afrid.iscan.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
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
import com.afrid.iscan.utils.NetUtils;
import com.google.gson.reflect.TypeToken;
import com.sunmi.adapter.SunmiPrintManager;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MyToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.OnClick;

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
    //---adapter 数据集合
    private List<ScanResult> mData;
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
    //private BTPrinterManager btPrinterManager;
    private SunmiPrintManager btPrinterManager;


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
        btPrinterManager = SunmiPrintManager.getInstance();
    }

    @Override
    protected void initView() {
        tvHospital.setText(resourceUtils.getStr(R.string.type_level1)+hospital);
        tvDepartment.setText(resourceUtils.getStr(R.string.type_level2)+dept);
        tvLinenType.setText(resourceUtils.getStr(R.string.scan_linen_type)+linenType);
        switch (linenType) {
            case 0:
                tvLinenType.setText(resourceUtils.getStr(R.string.scan_linen_type_normal));
                break;
            case 1:
                tvLinenType.setText(resourceUtils.getStr(R.string.scan_linen_type_special));
                break;
            case 2:
                tvLinenType.setText(resourceUtils.getStr(R.string.scan_linen_type_return));
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
        showLoadDialog(resourceUtils.getStr(R.string.loading_data_from_net));
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
                   /* if (tagInfo.getOrgName().equals(hospital)) {//---只留所选医院的标签
                        if (!container.containsKey(tagInfo.getObjName())) {
                            container.put(tagInfo.getObjName(), new ArrayList<TagInfo>());
                        }
                        container.get(tagInfo.getObjName()).add(tagInfo);
                    }else{//---其他医院/未识别 标签
                        outOfRangeTagList.add(tagInfo);
                    }*/
                    if (!container.containsKey(tagInfo.getObjName())) {
                        container.put(tagInfo.getObjName(), new ArrayList<TagInfo>());
                    }
                    container.get(tagInfo.getObjName()).add(tagInfo);
                }
                mData = new ArrayList<>();
                Iterator iterator = container.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, List<TagInfo>> entry = (Map.Entry<String, List<TagInfo>>) iterator.next();
                    String tagName = entry.getKey();
                    int tagNum = entry.getValue().size();
                    mData.add(new ScanResult(tagName, tagNum));
                }
                hiddenLoadingDialog();
                adapter.setmData(mData);
                tvUselessTag.setText(resourceUtils.getStr(R.string.useless_tag)+(tagList.size()-tagInfoList.size()));
                tvOutOfRange.setText(resourceUtils.getStr(R.string.out_of_range_tag)+outOfRangeTagList.size());
            }

            @Override
            public void onFailed(String error) {
                MyToast.showShort(SubmitResultActivity.this , error);
                hiddenLoadingDialog();
            }
        });
    }

    @OnClick(R.id.btn_submit)
    public void operate(View view) {//-----确认收货

      /*  if(!BTPrinterManager.getInstance(this).isConnected()){//判断打印机是否连接
            MyToast.showLong(this , resourceUtils.getStr(R.string.device_print_disconnect));
            BTDeviceScanActivity.startAction(this);
            return;
        }*/
        if(!SunmiPrintManager.getInstance().isConnect()){//判断打印机是否连接
            MyToast.showLong(this , resourceUtils.getStr(R.string.device_print_disconnect));
        }

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
            final String uid = "123456789";//UUID.randomUUID().toString();
            MyApplication myApplication = (MyApplication)getApplication();
            Message message = new Message();
            final XdCompany xdCompany = ((MyApplication) getApplication()).getUserInfo().getXdCompany();
            message.setXdCompany(xdCompany);
            final UserInfo userInfo = myApplication.getUserInfo();
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
                        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        TimeZone TIME_ZONE = TimeZone.getTimeZone("Asia/Shanghai");
                        DATE_FORMAT.setTimeZone(TIME_ZONE);
                        String date = DATE_FORMAT.format(Calendar.getInstance(TIME_ZONE).getTime());
                        for (int i=0 ; i<2 ; i++){
                            btPrinterManager.printText(resourceUtils.getStr(R.string.submit_order)+"");
                            btPrinterManager.printText(resourceUtils.getStr(R.string.submit_date) + date + "");
                            btPrinterManager.printOne(uid);
                            StringBuffer sbPrint  = new StringBuffer();
                            switch (linenType) {//-----打印布草类型
                                case 0:
                                    sbPrint.append(resourceUtils.getStr(R.string.scan_linen_type_normal)+"\r\n");
                                    break;
                                case 1:
                                    sbPrint.append(resourceUtils.getStr(R.string.scan_linen_type_special)+"\r\n");
                                    break;
                                case 2:
                                    sbPrint.append(resourceUtils.getStr(R.string.scan_linen_type_return)+"\r\n");
                                    break;
                            }
                            sbPrint.append("\r\n");
                            sbPrint.append(xdCompany.getName()+"\r\n"+"--------------------------------\r\n");
                            sbPrint.append("\r\n");
                            sbPrint.append(resourceUtils.getStr(R.string.submit_user)+userInfo.getUser().getName());
                            sbPrint.append("\r\n");
                            sbPrint.append("\r\n");
                            sbPrint.append(resourceUtils.getStr(R.string.submit_level1)+hospital+"\r\n");
                            sbPrint.append(resourceUtils.getStr(R.string.submit_level2)+dept+"\r\n");
                            sbPrint.append("\r\n");
                            for (ScanResult scanResult :mData ) {
                                sbPrint.append(resourceUtils.getStr(R.string.submit_type)+scanResult.getTagName()+"--------"+resourceUtils.getStr(R.string.submit_num)+scanResult.getTagNum()+"\r\n");
                            }
                            sbPrint.append("\r\n");
                            sbPrint.append("\r\n");
                            sbPrint.append("\r\n");
                            sbPrint.append("\r\n");
                            sbPrint.append(resourceUtils.getStr(R.string.submit_customer_sign));
                            sbPrint.append("\r\n");
                            sbPrint.append("\r\n");
                            sbPrint.append("\r\n");
                            sbPrint.append(resourceUtils.getStr(R.string.submit_arfid)+"\r\n");
                            sbPrint.append("\r\n");
                            sbPrint.append("\r\n");
                            sbPrint.append("\r\n");
                            sbPrint.append("\r\n");

                            btPrinterManager.printText(sbPrint.toString());
                        }
                        AlertDialog alertDialog = new AlertDialog.Builder(SubmitResultActivity.this)
                                .setCancelable(false)
                                .setTitle(resourceUtils.getStr(R.string.submit_alter_title))
                                .setMessage(resourceUtils.getStr(R.string.submit_alter_msg))
                                .setPositiveButton(resourceUtils.getStr(R.string.submit_alter_pos), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                })
                              /*  .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                })*/
                                .create();
                        alertDialog.show();
                    }else{
                        MyToast.showShort(SubmitResultActivity.this , resourceUtils.getStr(R.string.submit_error));
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
