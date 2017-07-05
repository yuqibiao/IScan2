package com.afrid.iscan.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.afrid.iscan.MyApplication;
import com.afrid.iscan.R;
import com.afrid.iscan.adapter.LinenAdapter;
import com.afrid.iscan.bean.Message;
import com.afrid.iscan.bean.OrgAreaDept;
import com.afrid.iscan.bean.ScanResult;
import com.afrid.iscan.bean.SpOffice;
import com.afrid.iscan.bean.TagInfo;
import com.afrid.iscan.bean.WashDepartureInfo;
import com.afrid.iscan.bean.XdSysUseArea;
import com.afrid.iscan.bean.json.UserInfo;
import com.afrid.iscan.net.UrlApi;
import com.afrid.iscan.ui.activity.MainActivity;
import com.afrid.iscan.utils.NetUtils;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 功能：下单界面
 *
 * @author yu
 * @version 1.0
 * @date 2017/7/5
 */

public class PlaceOrderFragment extends BaseFragment {

    private static final java.lang.String TAG = "PlaceOrderFragment";
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.sp_hos_choice)
    Spinner spHosChoice;
    @BindView(R.id.rv_scan_result)
    RecyclerView rvScanResult;
    @BindView(R.id.btn_place)
    Button btnPlace;

    private OrgAreaDept orgAreaDept;
    private KProgressHUD loadingDialog;
    private UserInfo userInfo;
    private List<String> areaNameList;
    private LinenAdapter adapter;
    //--- key：标签类型名 value：标签信息集合
    private HashMap<String, List<TagInfo>> container;
    //给adapter用的数据集合
    private List<ScanResult> mData;
    //通过条码查到的数据集合
    private List<TagInfo> tagInfoList;
    private XdSysUseArea xdSysUseArea;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_palce_order;
    }

    @Override
    protected void beforeInit() {
        userInfo = ((MyApplication) getActivity().getApplication()).getUserInfo();
        xdSysUseArea = ((MyApplication) getActivity().getApplication()).getArea();
        container = new HashMap<>();
    }

    @Override
    protected void initView() {
        loadingDialog = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("数据加载中...")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        rvScanResult.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LinenAdapter(getContext());
        rvScanResult.setAdapter(adapter);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        super.initData();
        Message message = new Message();
        message.setXdCompany(userInfo.getXdCompany());
        message.setJsonMessage(mGson.toJson(userInfo.getUser()));
        new NetUtils().getData(UrlApi.ORG_AREA_DEPT, mGson.toJson(message), new NetUtils.OnResultListener() {
            @Override
            public void onSuccess(String result) {
                loadingDialog.dismiss();
                MyLog.e(result);
                orgAreaDept = mGson.fromJson(result, OrgAreaDept.class);
                areaNameList = new ArrayList();
                for (XdSysUseArea area : orgAreaDept.getAreas()) {
                    areaNameList.add(area.getParentName() + " " + area.getName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, areaNameList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spHosChoice.setAdapter(adapter);
            }

            @Override
            public void onFailed(String error) {
                loadingDialog.dismiss();
                if (getContext() != null) {
                    MyToast.showShort(getContext(), "" + error);
                }
            }

        });
    }


    public void setOrderData(String oneCodeStr) {
        Message message = new Message();
        message.setXdCompany(userInfo.getXdCompany());
        message.setJsonMessage(mGson.toJson(userInfo.getUser()));
        message.setJsonMessage(oneCodeStr);
        new NetUtils().getData(UrlApi.BARCODE_TAGS_INFO, mGson.toJson(message), new NetUtils.OnResultListener() {
                    @Override
                    public void onSuccess(String result) {
                        btnPlace.setEnabled(true);
                        MyLog.e(TAG, "result===" + result);
                        TypeToken<List<TagInfo>> tagListToken = new TypeToken<List<TagInfo>>() {
                        };
                        tagInfoList = mGson.fromJson(result, tagListToken.getType());
                        for (TagInfo tagInfo : tagInfoList) {
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
                        adapter.setmData(mData);

                    }

                    @Override
                    public void onFailed(String error) {
                        if (getContext() != null) {
                            MyToast.showShort(getContext(), "" + error);
                        }
                    }
                }
        );
    }

    @OnClick(R.id.btn_place)
    public void placeOrder(View view){

        btnPlace.setEnabled(false);
        loadingDialog.show();
        Message message = new Message();
        message.setXdCompany(userInfo.getXdCompany());
        WashDepartureInfo washDepartureInfo = new WashDepartureInfo();
        washDepartureInfo.setTags(tagInfoList);
        washDepartureInfo.setArea(xdSysUseArea);
        washDepartureInfo.setUser(userInfo.getUser());
        message.setJsonMessage(mGson.toJson(washDepartureInfo));
        new NetUtils().getData(UrlApi.CLEAN_LINEN_DEPARTURE, mGson.toJson(message), new NetUtils.OnResultListener() {
            @Override
            public void onSuccess(String result) {
                loadingDialog.dismiss();
                if (result.equals("1")){
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                            .setCancelable(false)
                            .setTitle("结果")
                            .setMessage("发货成功！！！")
                            .setPositiveButton("继续发货", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ((MainActivity)getActivity()).palceOrder();
                                    btnPlace.setEnabled(true);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            })
                            .create();
                    alertDialog.show();
                    //必须在show后调用
                    Button button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    button.setTextColor(getResources().getColor(R.color.colorPrimary));
                }else{
                    MyToast.showShort(getContext(), "提交失败！！" );
                    btnPlace.setEnabled(true);
                }
            }

            @Override
            public void onFailed(String error) {
                loadingDialog.dismiss();
                if (getContext() != null) {
                    MyToast.showShort(getContext(), "" + error);
                }
            }
        });
    }

}
