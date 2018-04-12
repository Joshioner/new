package com.blk.pharmacy.homePage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blk.R;
import com.blk.common.ToolBarSet;

/**
 * Created by lzx on 2018/3/23.
 */

public class drug_information_detail extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout drug_detail_linear;
    private RelativeLayout drug_picture_linear;
    private TextView drug_goods_textview;
    private ImageView drug_goods_imageview;
    private TextView drug_detail_textview;
    private ImageView drug_detail_imageview;
    private Button button_purchase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drug_information_detail);
        Window window = getWindow();
        ToolBarSet.setBar(window);
        initView();
        initEvent();
    }

    private void initView() {
        drug_detail_linear = (LinearLayout)findViewById(R.id.drug_detail_linear);
        drug_picture_linear = (RelativeLayout)findViewById(R.id.drug_picture_linear);
        drug_picture_linear.setVisibility(View.VISIBLE);
        drug_detail_linear.setVisibility(View.GONE);
        drug_goods_textview = (TextView) findViewById(R.id.drug_goods_textview);
        drug_goods_imageview = (ImageView) findViewById(R.id.drug_goods_imageview);
        drug_detail_textview = (TextView)findViewById(R.id.drug_detail_textview);
        drug_detail_imageview = (ImageView)findViewById(R.id.drug_detail_imageview);
        drug_goods_imageview.setVisibility(View.VISIBLE);
        drug_detail_imageview.setVisibility(View.GONE);
        button_purchase = (Button) findViewById(R.id.button_purchase);
    }

    private void initEvent() {
        drug_goods_textview.setOnClickListener(this);
        drug_detail_textview.setOnClickListener(this);
        button_purchase.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.drug_goods_textview:
                drug_picture_linear.setVisibility(View.VISIBLE);
                drug_detail_linear.setVisibility(View.GONE);
                drug_goods_imageview.setVisibility(View.VISIBLE);
                drug_detail_imageview.setVisibility(View.GONE);
                break;
            case R.id.drug_detail_textview:
                drug_picture_linear.setVisibility(View.GONE);
                drug_detail_linear.setVisibility(View.VISIBLE);
                drug_goods_imageview.setVisibility(View.GONE);
                drug_detail_imageview.setVisibility(View.VISIBLE);
                break ;
            case R.id.button_purchase:
                Intent intent = new Intent(drug_information_detail.this,firm_Order.class);
                drug_information_detail.this.finish();
                startActivity(intent);
                break;
        }

    }
}
