package com.afrid.iscan.ui.fragment;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.afrid.btprinter.BTPrinterManager;
import com.afrid.iscan.MyApplication;
import com.afrid.iscan.R;
import com.afrid.iscan.adapter.LinenAdapter;
import com.afrid.iscan.bean.Message;
import com.afrid.iscan.bean.OrgAreaDept;
import com.afrid.iscan.bean.ScanResult;
import com.afrid.iscan.bean.TagInfo;
import com.afrid.iscan.bean.WashDepartureInfo;
import com.afrid.iscan.bean.XdCompany;
import com.afrid.iscan.bean.XdSysUseArea;
import com.afrid.iscan.bean.json.UserInfo;
import com.afrid.iscan.net.UrlApi;
import com.afrid.iscan.ui.activity.BTDeviceScanActivity;
import com.afrid.iscan.ui.activity.MainActivity;
import com.afrid.iscan.utils.NetUtils;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
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
    //private BTPrinterManager btPrinterManager;
    private SunmiPrintManager btPrinterManager;
    private XdCompany xdCompany;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_palce_order;
    }

    @Override
    protected void beforeInit() {
        userInfo = ((MyApplication) getActivity().getApplication()).getUserInfo();
        xdSysUseArea = ((MyApplication) getActivity().getApplication()).getArea();
        xdCompany = ((MyApplication) getActivity().getApplication()).getUserInfo().getXdCompany();
        btPrinterManager = SunmiPrintManager.getInstance();
        container = new HashMap<>();
    }

    @Override
    protected void initView() {
        loadingDialog = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(resourceUtils.getStr(R.string.loading_data_from_net))
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

      /*  if(!BTPrinterManager.getInstance(getContext()).isConnected()){//判断打印机是否连接
            MyToast.showLong(getContext() , resourceUtils.getStr(R.string.device_print_disconnect));
            BTDeviceScanActivity.startAction(getActivity());
            return;
        }*/
        if(!SunmiPrintManager.getInstance().isConnect()){//判断打印机是否连接
            MyToast.showLong(getContext() , resourceUtils.getStr(R.string.device_print_disconnect));
        }

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
                    //打印条码
                    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    TimeZone TIME_ZONE = TimeZone.getTimeZone("Asia/Shanghai");
                    DATE_FORMAT.setTimeZone(TIME_ZONE);
                    String date = DATE_FORMAT.format(Calendar.getInstance(TIME_ZONE).getTime());
                    for (int i=0 ; i<2 ; i++){
                        btPrinterManager.printText(resourceUtils.getStr(R.string.place_order)+"");
                        btPrinterManager.printText(resourceUtils.getStr(R.string.place_date) + date + "");
                        btPrinterManager.printOne("123456789");
                        StringBuffer sbPrint  = new StringBuffer();
                        sbPrint.append("\r\n");
                        sbPrint.append(xdCompany.getName()+"\r\n"+"--------------------------------\r\n");
                        sbPrint.append("\r\n");
                        sbPrint.append(resourceUtils.getStr(R.string.place_user)+userInfo.getUser().getName());
                        sbPrint.append("\r\n");
                        sbPrint.append("\r\n");
                        for (ScanResult scanResult :mData ) {
                            sbPrint.append("类别："+scanResult.getTagName()+"--------"+resourceUtils.getStr(R.string.place_num)+scanResult.getTagNum()+"\r\n");
                        }
                        sbPrint.append("\r\n");
                        sbPrint.append("\r\n");
                        sbPrint.append("\r\n");
                        sbPrint.append("\r\n");
                        sbPrint.append(resourceUtils.getStr(R.string.place_customer_sign));
                        sbPrint.append("\r\n");
                        sbPrint.append("\r\n");
                        sbPrint.append("\r\n");
                        sbPrint.append(resourceUtils.getStr(R.string.place_arfid)+"\r\n");
                        sbPrint.append("\r\n");
                        sbPrint.append("\r\n");
                        sbPrint.append("\r\n");
                        sbPrint.append("\r\n");

                        btPrinterManager.printText(sbPrint.toString());
                    }

                    AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                            .setCancelable(false)
                            .setTitle(resourceUtils.getStr(R.string.place_alter_title))
                            .setMessage(resourceUtils.getStr(R.string.place_msg))
                            .setPositiveButton(resourceUtils.getStr(R.string.place_btn_pos), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ((MainActivity)getActivity()).placeOrder();
                                    btnPlace.setEnabled(true);
                                }
                            })
                            .setNegativeButton(resourceUtils.getStr(R.string.place_btn_neg), new DialogInterface.OnClickListener() {
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
                    MyToast.showShort(getContext(), resourceUtils.getStr(R.string.submit_error) );
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
