package com.afrid.iscan.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
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
import com.afrid.swingu.utils.SwingUManager;
import com.yyyu.baselibrary.utils.ActivityHolder;
import com.yyyu.baselibrary.utils.MySPUtils;
import com.yyyu.baselibrary.utils.MyToast;

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

    @Override
    public void beforeInit() {
        userInfo = (UserInfo) getIntent().getSerializableExtra(Constant.USER_INFO);
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
            toolbar.setTitle("布草管理系统");
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.fl_content , new DepartmentsChoiceFragment());
            ft.commit();
        }else if (id == R.id.nav_bt) {//设备链接
            BTDeviceScanActivity.startAction(this);
        } else if (id == R.id.nav_order) {

        }  else if (id == R.id.nav_exit) {//注销账号
            MySPUtils.remove(this , Constant.USER_INFO);
            ActivityHolder.finishedAll();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void startAction(Activity activity , UserInfo userInfo){
        Intent intent = new Intent(activity , MainActivity.class);
        intent.putExtra(Constant.USER_INFO, userInfo);
        activity.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwingUManager.getInstance(this).destoryReader();
        BTPrinterManager.getInstance(this).onDestoryPrinter();
    }

    long currentTime ;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
            if (System.currentTimeMillis() - currentTime > 2 * 1000) {
                MyToast.showShort(this, "再按一次退出系统");
            } else {
                super.onBackPressed();
            }
            currentTime = System.currentTimeMillis();
        }

    }

}
