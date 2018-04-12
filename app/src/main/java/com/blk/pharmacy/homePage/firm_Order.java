package com.blk.pharmacy.homePage;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.blk.R;
import com.blk.common.CommomDialog;
import com.blk.common.ToolBarSet;

/**
 * Created by lzx on 2018/3/27.
 */

public class firm_Order extends AppCompatActivity {
    private Button submit_order;  //提交订单按钮
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_firm);
        Window window = getWindow();
        ToolBarSet.setBar(window);
        initview();
        initEvent();
    }
    private void initview() {
        submit_order = (Button) findViewById(R.id.submit_order);
    }
    private void initEvent() {
        submit_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new CommomDialog(firm_Order.this, R.style.dialog, "您确定提交订单么？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        //不保存，确认退出
                        if (confirm) {
                            //订单提交成功，对话框消失，跳转到病历详情页面
                            dialog.dismiss();
                            Intent confirmIntent = new Intent(firm_Order.this, drug_information_detail.class);
                            firm_Order.this.finish();
                            Toast.makeText(firm_Order.this, "下单成功", Toast.LENGTH_SHORT).show();
                            startActivity(confirmIntent);
                        }
                        //取消退出
                        else {
                            dialog.dismiss();
                        }

                    }
                }).setTitle("提示").show();
            }

        });

    }
}
