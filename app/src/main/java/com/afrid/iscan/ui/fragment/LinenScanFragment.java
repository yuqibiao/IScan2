package com.afrid.iscan.ui.fragment;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afrid.iscan.R;
import com.afrid.iscan.ui.activity.SubmitResultActivity;
import com.afrid.iscan.utils.LogicUtils;
import com.afrid.swingu.utils.SwingUManager;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 功能：布草扫描界面
 *
 * @author yu
 * @version 1.0
 * @date 2017/6/27
 */

public class LinenScanFragment extends BaseFragment {

    @BindView(R.id.tv_hospital)
    TextView tvHospital;
    @BindView(R.id.tv_department)
    TextView tvDepartment;
    @BindView(R.id.tv_linen_type)
    TextView tvLinenType;
    @BindView(R.id.tv_tip_before)
    TextView tvTipBefore;
    @BindView(R.id.tv_tip_left)
    TextView tvTipLeft;
    @BindView(R.id.tv_tip_top_center)
    TextView tvTipTopCenter;
    @BindView(R.id.tv_tip_right)
    TextView tvTipRight;
    @BindView(R.id.tv_tip_behind)
    TextView tvTipBehind;
    @BindView(R.id.btn_next)
    Button btnNext;
    /**
     * 0：前   1：左  2：后 3：右 4：顶
     * 5：复核前  6：复核左 7：复核右 8：复核右 9：复核顶
     */
    private int scanStep = 0;

    public  final static int SPACE = 10;
    private int timeSpace = SPACE;
    private static final long TIME = 1000;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_linen_scan;
    }

    @Override
    protected void initView() {
        tvHospital.setText("您所在的医院是：郑州省人民医院");
        tvDepartment.setText("要收布草的科室为：消化内科第一科");
        tvLinenType.setText("收货类型为：正常布草");
    }

    @Override
    protected void initListener() {

    }

    @OnClick(R.id.btn_next)
    public void oprate(View view) {
        btnNext.setText("");
        tvTipBefore.setText("");
        tvTipLeft.setText("");
        tvTipBehind.setText("");
        tvTipRight.setText("");
        tvTipTopCenter.setText("布草车");
        switch (scanStep) {
            case 0://前
                SwingUManager.getInstance(getContext()).startReader();
                tvTipBefore.setTextColor(getResources().getColor(R.color.colorAccent));
                tvTipBefore.setText("请将扫描仪放在\r\n布草车前面");
                break;
            case 1://左
                tvTipLeft.setTextColor(getResources().getColor(R.color.colorAccent));
                tvTipLeft.setText("请将扫描仪放在\r\n布草车左边");
                break;
            case 2://后
                tvTipBehind.setTextColor(getResources().getColor(R.color.colorAccent));
                tvTipBehind.setText("请将扫描仪放在\r\n布草车后面");
                break;
            case 3://右
                tvTipRight.setTextColor(getResources().getColor(R.color.colorAccent));
                tvTipRight.setText("请将扫描仪放在\r\n布草车左边");
                break;
            case 4://顶部
                tvTipTopCenter.setTextColor(getResources().getColor(R.color.colorAccent));
                tvTipTopCenter.setText("布草车 \r\n\r\n请将扫描仪放在\r\n布草车顶部");
                break;
            case 5://复核前
                tvTipBefore.setTextColor(getResources().getColor(R.color.colorAccent));
                tvTipBefore.setText("复核\r\n布草车前面");
                break;
            case 6://复核左
                tvTipLeft.setTextColor(getResources().getColor(R.color.colorAccent));
                tvTipLeft.setText("复核\r\n布草车左边");
                break;
            case 7://复核后
                tvTipBehind.setTextColor(getResources().getColor(R.color.colorAccent));
                tvTipBehind.setText("复核\r\n布草车后面");
                break;
            case 8://复核右
                tvTipRight.setTextColor(getResources().getColor(R.color.colorAccent));
                tvTipRight.setText("复核\r\n布草车左边");
                break;
            case 9://复核顶部
                tvTipTopCenter.setTextColor(getResources().getColor(R.color.colorAccent));
                tvTipTopCenter.setText("复核 \r\n\r\n请将扫描仪放在\r\n布草车顶部");
                break;
        }
        if (scanStep>9){//TODO 结束扫描
            SubmitResultActivity.startAction(getActivity());
        }else{
            mHandler.postDelayed(runnable, 0);
        }
    }

    private Handler mHandler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                timeSpace--;
                mHandler.postDelayed(this, TIME);
                if (timeSpace < 0) {//结束
                    mHandler.removeCallbacks(this);
                    btnNext.setEnabled(true);
                    timeSpace = SPACE;
                    btnNext.setText("下一步");
                    scanStep++;
                    if(scanStep>9){
                        btnNext.setText("结束扫描");
                    }
                } else {
                    btnNext.setEnabled(false);
                    btnNext.setText(timeSpace + "秒");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

}
