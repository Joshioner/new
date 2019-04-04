package com.blk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blk.common.ToolBarSet;
import com.blk.common.entity.User;
import com.blk.common.util.AlterUtil;
import com.blk.common.util.ConfigUtil;
import com.blk.common.util.HttpCallbackListener;
import com.blk.common.util.HttpSendUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView forgetPassword,register;
    private Button loginButton;
    private SharedPreferences sharedPreferences;
    private EditText email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        ToolBarSet.setBar(window);
        //获取sharedPreferences中的userInfo信息
        sharedPreferences = getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        String userInfo = sharedPreferences.getString("userInfo",null);
        if ( userInfo != null && userInfo != ""){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            this.finish();
        }
        setContentView(R.layout.login);

        //初始化控件
        initView();

        //点击事件
        initEvent();

    }

    /**
     * 初始化控件
     */
    public void initView(){
        forgetPassword = (TextView) findViewById(R.id.forget_password);
        register = (TextView) findViewById(R.id.register);
        loginButton = (Button) findViewById(R.id.login_btn);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
    }

    public void initEvent(){
        //注册事件
        register.setOnClickListener(this);
        //登录验证
        loginButton.setOnClickListener(this);
    }
    /**
     * 重写点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.register:
                Intent registerIntent = new Intent(this,RegisterActivity.class);
                startActivity(registerIntent);
                break;
            case R.id.login_btn:
                login();
                break;
            default:
                break;
        }
    }
    //登陆
    private void login(){
        User user = new User();
        //邮箱号码
      if (TextUtils.isEmpty(email.getText())){
          AlterUtil.alterTextLong(LoginActivity.this,"邮箱号码不能为空");
          return;
      }
      user.setEmail(email.getText().toString());
        if (TextUtils.isEmpty(password.getText())){
            AlterUtil.alterTextLong(LoginActivity.this,"密码不能为空");
            return;
        }
        user.setPassword(password.getText().toString());
        //登陆地址
        String address = ConfigUtil.getServerAddress() + "/user/login";
        //转user对象转为json字符串
        HttpSendUtil.sendHttpRequest(address,JSONObject.toJSONString(user),new HttpCallbackListener(){
            @Override
            public void onFinish(String response) {
                JSONObject jsonObject = JSONObject.parseObject(response);
                int code = jsonObject.getIntValue("code");
                //登陆成功
                if (code == 0){
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    sharedPreferences = getSharedPreferences("userInfo",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    //将用户信息保存至SharedPreferences中
                    String userInfo = JSONObject.toJSONString(jsonObject.getObject("data",User.class));
                    editor.putString("userInfo",userInfo);
                    editor.commit();
                    LoginActivity.this.finish();
                    startActivity(intent);
                }else {
                    Looper.prepare();
                    AlterUtil.alterTextLong(LoginActivity.this,"邮箱或者密码不正确");
                    Looper.loop();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}
