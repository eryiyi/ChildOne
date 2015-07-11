package com.xiaogang.ChildOne.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.xiaogang.ChildOne.HomeBabyApplication;
import com.xiaogang.ChildOne.R;
import com.xiaogang.ChildOne.data.AccountDATA;
import com.xiaogang.ChildOne.data.ErrorDATA;
import com.xiaogang.ChildOne.entity.Account;
import com.xiaogang.ChildOne.util.InternetURL;
import com.xiaogang.ChildOne.util.PhoneEnvUtil;

/**
 * Created by liuzwei on 2014/11/11.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private EditText username;//用户名
    private EditText password;//密码
    private TextView loginBtn;//登陆按钮
    private TextView forgetPass;//忘记密码
    private ProgressDialog progressDialog;

    private RequestQueue mRequestQueue;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        initView();

        mRequestQueue = Volley.newRequestQueue(this);
    }

    private void initView(){
        username = (EditText) findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.login_password);
        loginBtn = (TextView) findViewById(R.id.login_btn);
        forgetPass = (TextView) findViewById(R.id.login_forgetpass);

        loginBtn.setOnClickListener(this);

        forgetPass.setOnClickListener(this);
        if (username != null) {
            username.setText(getGson().fromJson(sp.getString("username", ""), String.class));
        }
        if (password!= null) {
            password.setText(getGson().fromJson(sp.getString("password", ""), String.class));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn://登陆按钮
                if (!PhoneEnvUtil.isNetworkConnected(mContext)){
                    Toast.makeText(mContext, R.string.check_network_isuse, Toast.LENGTH_SHORT).show();
                    return;
                }
                loginBtn.setClickable(false);
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("登录中...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        loginBtn.setClickable(true);
                    }
                });
                progressDialog.show();
                final String name = username.getText().toString();
                final String pass = password.getText().toString();

                if (name.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    loginBtn.setClickable(true);
                    return;
                }
                if (pass.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    loginBtn.setClickable(true);
                    return;
                }
                //组装请求url
                String uri = String.format(InternetURL.LOGIN_API + "?user_name=%s&password=%s", name, pass);
                StringRequest request = new StringRequest(
                        Request.Method.GET,
                        uri,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                Gson gson = new Gson();
                                try {
                                    AccountDATA data = gson.fromJson(s, AccountDATA.class);
                                    Account account = data.getData();
                                    saveAccount(name, pass, account);
                                    HomeBabyApplication.is_student = account.getIs_student();
                                    if ("1".equals(account.getIs_student())){//如果是家长登陆，选择身份
                                        Intent setIdentity = new Intent(LoginActivity.this, SelectIdentityActivity.class);
                                        setIdentity.putExtra(Constants.ACCOUNT_KEY, account);
                                        startActivity(setIdentity);
                                    }else {//老师登陆直接跳转到主页面
                                        Intent main = new Intent(LoginActivity.this, CenterActivity.class);
                                        startActivity(main);
                                        save(Constants.IDENTITY, "2");
                                    }
                                }catch (Exception e){
                                    ErrorDATA errorDATA = gson.fromJson(s, ErrorDATA.class);
                                    if (errorDATA.getMsg().equals("failed")){
                                        Toast.makeText(mContext, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                loginBtn.setClickable(true);
                                progressDialog.dismiss();
                            }
                        },new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                loginBtn.setClickable(true);
                                progressDialog.dismiss();
                                Toast.makeText(mContext, "请求超时，稍后重试", Toast.LENGTH_SHORT).show();
                            }
                });
                request.setRetryPolicy(new DefaultRetryPolicy(
                        5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                mRequestQueue.add(request);
                break;
            case R.id.login_forgetpass://忘记密码
                Intent forgetPass = new Intent(this, ForgetPassOneActivity.class);
                startActivity(forgetPass);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRequestQueue.cancelAll(this);
    }

    private void saveAccount(String username, String password, Account account){
        save(Constants.USERNAME_KEY, username);
        save(Constants.PASSWORD_KEY, password);
        save(Constants.ACCOUNT_KEY, account);
    }
}
