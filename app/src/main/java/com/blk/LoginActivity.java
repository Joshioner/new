package com.blk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


import com.blk.common.ToolBarSet;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView forgetPassword,register;
    private Button loginButton;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        ToolBarSet.setBar(window);
//        sharedPreferences = getSharedPreferences("userInfo",Context.MODE_PRIVATE);
//        String accountName = sharedPreferences.getString("accountName","");
//        String password = sharedPreferences.getString("password","");
//        if ( (accountName != null && accountName != "") && (password != null && password != "")){
//            Intent intent = new Intent(this,MainActivity.class);
//            startActivity(intent);
//            this.finish();
//        }

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
                Intent loginIntent = new Intent(this,MainActivity.class);
                startActivity(loginIntent);
                break;
            default:
                break;
        }
    }
}
