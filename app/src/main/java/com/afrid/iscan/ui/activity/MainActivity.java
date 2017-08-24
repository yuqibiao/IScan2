package com.afrid.iscan.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afrid.btprinter.BTPrinterManager;
import com.afrid.iscan.MyApplication;
import com.afrid.iscan.R;
import com.afrid.iscan.bean.json.UserInfo;
import com.afrid.iscan.gobal.Constant;
import com.afrid.iscan.ui.fragment.DepartmentsChoiceFragment;
import com.afrid.iscan.ui.fragment.PlaceOrderFragment;
import com.afrid.swingu.utils.SwingUManager;
import com.sunmi.adapter.SunmiScanManager;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.yyyu.baselibrary.utils.ActivityHolder;
import com.yyyu.baselibrary.utils.MySPUtils;
import com.yyyu.baselibrary.utils.MyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.content_main)
    RelativeLayout contentMain;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    private TextView tvFactoryName;
    private TextView tvUsername;
    private UserInfo userInfo;

    private static final int PLACE_ORDER =0;
    private SunmiScanManager sunmiScanManager;

    @Override
    public void beforeInit() {
        super.beforeInit();
        userInfo = (UserInfo) getIntent().getSerializableExtra(Constant.USER_INFO);
        sunmiScanManager = SunmiScanManager.getInstance();
        ((MyApplication)getApplication()).setUserInfo(userInfo);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);

        tvFactoryName = (TextView) navView.getHeaderView(0).findViewById(R.id.tv_factory_name);
        tvUsername = (TextView) navView.getHeaderView(0).findViewById(R.id.tv_username);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fl_content , new DepartmentsChoiceFragment());
        ft.commit();

       tvFactoryName.setText(userInfo.getXdCompany().getName());
       tvUsername.setText(userInfo.getUser().getName());

    }

    @Override
    protected void initListener() {

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id== R.id.nav_home){//首页
            toolbar.setTitle(resourceUtils.getStr(R.string.main_tb_title_manager));
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.fl_content , new DepartmentsChoiceFragment());
            ft.commit();
        }else if(id == R.id.nav_place_order){//下单
            placeOrder();
        }
        else if (id == R.id.nav_bt) {//设备链接
            BTDeviceScanActivity.startAction(this);
        }else if (id == R.id.nav_exit) {//注销账号
            MySPUtils.remove(this , Constant.USER_INFO);
            ActivityHolder.finishedAll();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_ORDER && resultCode == RESULT_OK){
            if (requestCode == SunmiScanManager.START_SCAN && data != null) {
                Bundle bundle = data.getExtras();
                ArrayList<HashMap<String, String>> result = (ArrayList<HashMap<String, String>>) bundle.getSerializable("data");
                Iterator<HashMap<String, String>> it = result.iterator();
                while (it.hasNext()) {
                    HashMap<String, String> hashMap = it.next();
                    Log.i("sunmi", hashMap.get("TYPE"));//这个是扫码的类型
                    Log.i("sunmi", hashMap.get("VALUE"));//这个是扫码的结果
                    //MyToast.showShort(this , "result-----"+result);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    List<Fragment> fragmentList = fragmentManager.getFragments();
                    for (Fragment fragment:fragmentList) {
                        if(fragment instanceof PlaceOrderFragment){
                            ((PlaceOrderFragment)fragment).setOrderData(hashMap.get("VALUE"));
                        }
                    }
                }

            }
        }
    }

    public void placeOrder(){
        toolbar.setTitle(resourceUtils.getStr(R.string.main_tb_title_place_order));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fl_content , new PlaceOrderFragment());
        ft.commit();
      /*  Intent intent = new Intent(this , CaptureActivity.class);
        startActivityForResult(intent , PLACE_ORDER);*/
        sunmiScanManager.toScanAtcForResult(this);
    }

    public static void startAction(Activity activity , UserInfo userInfo){
        Intent intent = new Intent(activity , MainActivity.class);
        intent.putExtra(Constant.USER_INFO, userInfo);
        activity.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwingUManager.getInstance(this).destroyReader();
        BTPrinterManager.getInstance(this).onDestoryPrinter();
    }

    long currentTime ;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else{
            if (System.currentTimeMillis() - currentTime > 2 * 1000) {
                MyToast.showShort(this, resourceUtils.getStr(R.string.main_exit_tip));
            } else {
                super.onBackPressed();
            }
            currentTime = System.currentTimeMillis();
        }

    }

}
