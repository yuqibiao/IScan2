package com.afrid.iscan.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afrid.iscan.R;
import com.afrid.swingu.utils.SwingUManager;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.view.WhewView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 功能：布草扫描界面（去除等待时间）
 *
 * @author yu
 * @version 1.0
 * @date 2017/6/27
 */

public class LinenScanActivity2 extends BaseActivity {

    @BindView(R.id.tv_hospital)
    TextView tvHospital;
    @BindView(R.id.tv_department)
    TextView tvDepartment;
    @BindView(R.id.tv_linen_type)
    TextView tvLinenType;
    @BindView(R.id.wv_scan)
     WhewView wvScan;

    private static final String TAG = "LinenScanActivity";

    private SwingUManager swingUManager;

    Set<String> tags = new HashSet<>();
    ArrayList<String> tagList = new ArrayList<>();
    private String hospital;
    private String dept;
    private int linenType;


    @Override
    public int getLayoutId() {
        return R.layout.activity_linen_scan2;
    }

    @Override
    public void beforeInit() {
        swingUManager = SwingUManager.getInstance(this);
        hospital = getIntent().getStringExtra("hospital");
        dept = getIntent().getStringExtra("dept");
        linenType = getIntent().getIntExtra("linenType", -1);
    }

    @Override
    protected void initView() {
        tvHospital.setText(resourceUtils.getStr(R.string.type_level1) + hospital);
        tvDepartment.setText(resourceUtils.getStr(R.string.type_level2)  + dept);
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
    }

    @Override
    protected void initListener() {
        swingUManager.setOnReadResultListener(new SwingUManager.OnReadResultListener() {
            @Override
            public void onRead(String tagId) {
                boolean isAdd = tags.add(tagId.substring(4));
                if (isAdd) {
                    MyLog.e(TAG, tagId.substring(4) + "   size" + tags.size());
                }
            }
        });
    }

    @OnClick(R.id.btn_start)
    public void start(View view) {
        swingUManager.resetReader();
        swingUManager.startReader();
        wvScan.start();
    }

    @OnClick(R.id.btn_stop)
    public void stop(View view) {
        swingUManager.stopReader();
        wvScan.stop();
        Iterator<String> iterator = tags.iterator();
        while (iterator.hasNext()) {
            tagList.add(iterator.next());
        }
        SubmitResultActivity.startAction(this, hospital, dept, linenType, tagList);
        finish();
    }


    public static void startAction(Activity activity, String hospital, String dept, int linenType) {
        Intent intent = new Intent(activity, LinenScanActivity2.class);
        intent.putExtra("hospital", hospital);
        intent.putExtra("dept", dept);
        intent.putExtra("linenType", linenType);
        activity.startActivity(intent);
    }

}
