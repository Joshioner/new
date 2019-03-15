package com.blk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;


import com.blk.common.ToolBarSet;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView forgetPassword,register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        ToolBarSet.setBar(window);
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
    }

    public void initEvent(){
        //注册事件
        register.setOnClickListener(this);
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
            default:
                break;
        }
    }
}
