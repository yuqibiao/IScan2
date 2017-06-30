package com.afrid.iscan.ui.activity;

import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.afrid.btprinter.BTPrinterManager;
import com.afrid.iscan.R;
import com.afrid.iscan.bean.json.UserInfo;
import com.afrid.iscan.gobal.Constant;
import com.afrid.iscan.utils.bt.BTManager;
import com.afrid.swingu.utils.SwingUManager;
import com.google.gson.Gson;
import com.yyyu.baselibrary.utils.MySPUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;

/**
 * 功能：导航页
 *
 * @author yu
 * @version 1.0
 * @date 2017/6/30
 */

public class SplashActivity extends BaseActivity{

    @BindView(R.id.iv_splash)
    ImageView ivSplash;
    private BTManager btManager;
    private BTPrinterManager btPrinterManager;
    private SwingUManager swingUManager;


    @Override
    public void beforeInit() {
        super.beforeInit();
        btManager = BTManager.getInstance(this);
        swingUManager = SwingUManager.getInstance(this);
        btPrinterManager = BTPrinterManager.getInstance(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        //---刷新蓝牙设备（已绑定的）
        Set<BluetoothDevice> deviceSet =  btManager.getBoundsDevice();
        Iterator it=deviceSet.iterator();
        while(it.hasNext()){
            BluetoothDevice device = (BluetoothDevice) it.next();
            String deviceName = device.getName();
            if(deviceName.startsWith("NP")){
                btPrinterManager.connect( device);
            }else if(deviceName.startsWith("SwingU")){
                swingUManager.connectDevice(device);
            }
        }
    }

    @Override
    protected void afterInit() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_anime);
        ivSplash.setAnimation(animation);
        animation.start();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                String userInfoStr = (String ) MySPUtils.get(SplashActivity.this , Constant.USER_INFO , "");
                if(!TextUtils.isEmpty(userInfoStr)){
                    UserInfo userInfo = new Gson().fromJson(userInfoStr , UserInfo.class);
                    MainActivity.startAction(SplashActivity.this , userInfo);
                }else{
                    LoginActivity.startAction(SplashActivity.this);
                }
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
