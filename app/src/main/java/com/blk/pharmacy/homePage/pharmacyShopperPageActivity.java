package com.blk.pharmacy.homePage;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.blk.R;
import com.blk.common.ToolBarSet;
import com.blk.pharmacy.homePage.Adapter.pharmacyShopperDrugListDetailBaseAdapter;
import com.blk.pharmacy.homePage.entity.pharmacy_shopper_drug_list_detail;

import java.util.ArrayList;
import java.util.List;

public class pharmacyShopperPageActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView drugListView;   //用于存放药物列表的listView;
    private List<pharmacy_shopper_drug_list_detail> pharmacyShopperDrugListDetailList;  //药物列表
    private pharmacyShopperDrugListDetailBaseAdapter pharmacyShopperDrugListBaseAdapter;  //适配器
    private PopupWindow drug_type_popupWindow;//药物类型弹出框
    private View ContentView;   //显示药物类型的视图
    private TabItem drug_type;  //药物感冒类型栏目
    private LinearLayout ganmao,kangyan,wuguan,pifu,changwei,fengshi,yingyang,shengjing,zhongyao; //药物类型
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharmacy_shopper_page);
        Window window = getWindow();
        ToolBarSet.setBar(window);
        //初始化控件
        initView();

        //初始化事件
        initEvent();


    }

    private void initEvent() {
        drugListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(pharmacyShopperPageActivity.this, drug_information_detail.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        drugListView = (ListView) findViewById(R.id.shopper_drug_list);  //listView初始化
        pharmacyShopperDrugListDetailList = new ArrayList<pharmacy_shopper_drug_list_detail>();   //药物列表初始化
        AddDrugList();
        pharmacyShopperDrugListBaseAdapter = new pharmacyShopperDrugListDetailBaseAdapter(pharmacyShopperPageActivity.this,pharmacyShopperDrugListDetailList);
        drugListView.setAdapter(pharmacyShopperDrugListBaseAdapter);


    }

    public void AddDrugList() {
        pharmacy_shopper_drug_list_detail DrugListDetail = new pharmacy_shopper_drug_list_detail();
        DrugListDetail.setDrugName("999 三九胃泰颗粒20X6袋");
        DrugListDetail.setDrugFunction("清热燥湿，行气活血，柔肝止痛");
        DrugListDetail.setPriceInt("￥16.");
        DrugListDetail.setPriceDouble("3");
        DrugListDetail.setPayNumber("21人付款");
        pharmacyShopperDrugListDetailList.add(DrugListDetail);
        pharmacyShopperDrugListDetailList.add(DrugListDetail);
        pharmacyShopperDrugListDetailList.add(DrugListDetail);
        pharmacyShopperDrugListDetailList.add(DrugListDetail);
        pharmacyShopperDrugListDetailList.add(DrugListDetail);
    }


    //初始化药物类型弹出框
    private void initPopupWindow() {
        ContentView = LayoutInflater.from(pharmacyShopperPageActivity.this).inflate(R.layout.popupwindow_drup_type, null);
        ganmao = (LinearLayout) ContentView.findViewById(R.id.linearLayout1);
        kangyan = (LinearLayout) ContentView.findViewById(R.id.linearLayout2);
        wuguan = (LinearLayout) ContentView.findViewById(R.id.linearLayout3);
        pifu = (LinearLayout) ContentView.findViewById(R.id.linearLayout4);
        changwei = (LinearLayout) ContentView.findViewById(R.id.linearLayout5);
        fengshi = (LinearLayout) ContentView.findViewById(R.id.linearLayout6);
        yingyang = (LinearLayout) ContentView.findViewById(R.id.linearLayout7);
        shengjing = (LinearLayout) ContentView.findViewById(R.id.linearLayout8);
        zhongyao = (LinearLayout) ContentView.findViewById(R.id.linearLayout9);
        drug_type_popupWindow = new PopupWindow(ContentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ContentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int[] location = new int[2];
        // 允许点击外部消失
        drug_type_popupWindow.setBackgroundDrawable(new BitmapDrawable());
        drug_type_popupWindow.setOutsideTouchable(true);
        drug_type_popupWindow.setFocusable(true);

//        // 获得位置
        ContentView.getLocationOnScreen(location);
        //popupWindow.showAtLocation(ContentView, Gravity.NO_GRAVITY, (location[0] + ContentView.getWidth() / 2) - popupWidth / 2, location[1] + popupHeight);
        drug_type_popupWindow.showAsDropDown(drug_type,-100,0);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {

        }
    }
}
