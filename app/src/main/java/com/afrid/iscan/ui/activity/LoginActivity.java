package com.afrid.iscan.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afrid.iscan.R;
import com.afrid.iscan.bean.Message;
import com.afrid.iscan.bean.OptUser;
import com.afrid.iscan.bean.XdCompany;
import com.afrid.iscan.bean.json.UserInfo;
import com.afrid.iscan.gobal.Constant;
import com.afrid.iscan.net.UrlApi;
import com.afrid.iscan.utils.Encryptor;
import com.afrid.iscan.utils.NetUtils;
import com.google.gson.Gson;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MySPUtils;
import com.yyyu.baselibrary.utils.MyToast;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 功能：登录界面
 *
 * @author yu
 * @version 1.0
 * @date 2017/5/9
 */

public class LoginActivity extends BaseActivity {


    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.et_factory_name)
    EditText etFactoryName;


    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        getSupportActionBar().setTitle(R.string.login);
    }

    @Override
    protected void initListener() {

    }

    @OnClick(R.id.btn_login)
    public void toLogin(View view) {

        String username = etUsername.getText().toString();
        String pwd = etPwd.getText().toString();
        final String factoryName = etFactoryName.getText().toString();

        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(factoryName)){
            MyToast.showShort(this , resourceUtils.getStr(R.string.lg_input_empty_tip));
            return;
        }

        showLoadDialog(resourceUtils.getStr(R.string.lg_loading_tip));

        final Gson gson = new Gson();

        OptUser optUser = new OptUser();
        optUser.setName(username);
        optUser.setPassWd( Encryptor.encrypt(pwd));

        XdCompany xdCompany = new XdCompany();
        xdCompany.setName(factoryName);

        Message message = new Message();
        message.setXdCompany(xdCompany);
        message.setJsonMessage(gson.toJson(optUser));

        final String  params = gson.toJson(message);

        new NetUtils().getData(UrlApi.LOGIN, params, new NetUtils.OnResultListener() {
            @Override
            public void onSuccess(String result) {
                hiddenLoadingDialog();
                MyLog.e(result);
                Message msg = gson.fromJson(result , Message.class);

                if(msg.getXdCompany()==null){
                    MyToast.showLong(LoginActivity.this , resourceUtils.getStr(R.string.lg_factory_name_error));
                    return ;
                }
                OptUser user =gson.fromJson(msg.getJsonMessage() , OptUser.class);
                if(user==null || TextUtils.isEmpty(user.getGroup())){
                    MyToast.showLong(LoginActivity.this , resourceUtils.getStr(R.string.lg_username_or_pwd_error));
                    return;
                }

                UserInfo userInfo = new UserInfo();
                userInfo.setXdCompany(msg.getXdCompany());
                userInfo.setUser(user);
                //---登录成功记住用户信息
                MySPUtils.put(LoginActivity.this , Constant.USER_INFO , gson.toJson(userInfo));
                MainActivity.startAction(LoginActivity.this , userInfo);
                finish();
            }

            @Override
            public void onFailed(String error) {
                MyToast.showLong(LoginActivity.this , resourceUtils.getStr(R.string.net_connect_error));
                hiddenLoadingDialog();
            }
        });
    }

    public static void startAction(Activity activity){
        Intent intent = new Intent(activity , LoginActivity.class);
        activity.startActivity(intent);
    }

}
