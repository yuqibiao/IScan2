package com.afrid.iscan.ui.fragment;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.afrid.btprinter.BTPrinterManager;
import com.afrid.iscan.MyApplication;
import com.afrid.iscan.R;
import com.afrid.iscan.adapter.DepartmentsAdapter;
import com.afrid.iscan.bean.Message;
import com.afrid.iscan.bean.OrgAreaDept;
import com.afrid.iscan.bean.SpOffice;
import com.afrid.iscan.bean.XdSysUseArea;
import com.afrid.iscan.bean.json.UserInfo;
import com.afrid.iscan.net.UrlApi;
import com.afrid.iscan.ui.activity.BaseActivity;
import com.afrid.iscan.ui.activity.TypeChoiceActivity;
import com.afrid.iscan.utils.LogicUtils;
import com.afrid.iscan.utils.NetUtils;
import com.afrid.swingu.utils.SwingUManager;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MyToast;
import com.yyyu.baselibrary.view.recyclerview.adapter.HeaderAndFooterWrapper;
import com.yyyu.baselibrary.view.recyclerview.listener.OnRvItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 功能：科室选择
 *
 * @author yu
 * @version 1.0
 * @date 2017/6/27
 */

public class DepartmentsChoiceFragment extends BaseFragment {

    @BindView(R.id.sp_hos_choice)
    Spinner spHosChoice;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.rv_departments)
    RecyclerView rvDepartments;
    private DepartmentsAdapter departmentsAdapter;
    private UserInfo userInfo;
    private Gson gson;
    private OrgAreaDept orgAreaDept;
    private List<String> areaNameList;
    private String checkedHos;//医院区域
    private String checkedDept;//科室
    private List<SpOffice> selectDept;
    private MyApplication myApplication;
    private KProgressHUD loadingDialog;

    @Override
    protected void beforeInit() {
        userInfo = ((MyApplication) getActivity().getApplication()).getUserInfo();
        myApplication = (MyApplication)getActivity().getApplication();
        gson = new Gson();
        loadingDialog = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("数据加载中...")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_departments_choice;
    }

    @Override
    protected void initView() {
        rvDepartments.setLayoutManager(new GridLayoutManager(getContext(), 3));
        departmentsAdapter = new DepartmentsAdapter(getContext() );
        rvDepartments.setAdapter(departmentsAdapter);

    }

    @Override
    protected void initListener() {

        /**
         *
         */
        spHosChoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                checkedHos = areaNameList.get(i).split(" ")[0];
                //---刷选出某一区域下的科室
                String areaId = orgAreaDept.getAreas().get(i).getPrimaryKey();
                myApplication.setArea(orgAreaDept.getAreas().get(i));
                List<SpOffice> deptList = orgAreaDept.getDepts();
                selectDept = new ArrayList<>();
                for (SpOffice dept : deptList){
                   if(dept.getParent().endsWith(areaId)){
                       selectDept.add(dept);
                   }
                }
                departmentsAdapter.setDeptList(selectDept);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /**
         * 科室选择
         */
        rvDepartments.addOnItemTouchListener(new OnRvItemClickListener(rvDepartments) {
            @Override
            public void onItemClick(View itemView, int position) {
                checkedDept = selectDept.get(position).getName();
                myApplication.setDept(selectDept.get(position));
                TypeChoiceActivity.startAction(getActivity() , checkedHos , checkedDept);
            }

            @Override
            public void onItemLongClick(View itemView, int position) {

            }
        });
    }

    @Override
    protected void initData() {

        loadingDialog.show();

        UserInfo userInfo = ((MyApplication) getActivity().getApplication()).getUserInfo();
        Message message = new Message();
        message.setXdCompany(userInfo.getXdCompany());
        message.setJsonMessage(gson.toJson(userInfo.getUser()));
        new NetUtils().getData(UrlApi.ORG_AREA_DEPT, gson.toJson(message), new NetUtils.OnResultListener() {

            @Override
            public void onSuccess(String result) {

                loadingDialog.dismiss();

                MyLog.e(result);
                orgAreaDept = gson.fromJson(result , OrgAreaDept.class);
                areaNameList = new ArrayList();
                for (XdSysUseArea area: orgAreaDept.getAreas() ) {
                    areaNameList.add(area.getParentName()+" "+area.getName());
                }
                ArrayAdapter<String> adapter=new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item , areaNameList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spHosChoice.setAdapter(adapter);
                List<SpOffice> deptList = orgAreaDept.getDepts();
                if (areaNameList.size()>0){
                    String areaId = orgAreaDept.getAreas().get(0).getPrimaryKey();
                    checkedHos =  areaNameList.get(0);
                    selectDept = new ArrayList<>();
                    for (SpOffice dept : deptList){
                        if(dept.getParent().endsWith(areaId)){
                            selectDept.add(dept);
                        }
                    }
                    departmentsAdapter.setDeptList(selectDept);
                }

            }

            @Override
            public void onFailed(String error) {
                loadingDialog.dismiss();
                if(getContext()!=null){
                    MyToast.showShort(getContext() , ""+error);
                }
            }
        });
    }
}
