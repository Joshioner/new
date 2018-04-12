package com.blk.pharmacy.homePage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ListView;

import com.blk.R;
import com.blk.common.ToolBarSet;
import com.blk.pharmacy.homePage.Adapter.drugListDetailBaseAdapter;
import com.blk.pharmacy.homePage.entity.drug_list_detail;

import java.util.ArrayList;
import java.util.List;

public class DrugListSearchActivity extends AppCompatActivity {

    private ListView drugListView;   //用于存放药物列表的listView;
    private List<drug_list_detail> drugListDetailList;  //药物列表
    private drugListDetailBaseAdapter drugListBaseAdapter;  //适配器
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharmacy_drug_page);
        Window window = getWindow();
        ToolBarSet.setBar(window);
        //初始化控件
        initView();

    }

    private void initView() {
        drugListView = (ListView) findViewById(R.id.drug_list);  //listView初始化
        drugListDetailList = new ArrayList<drug_list_detail>();   //药物列表初始化
        AddDrugList();
        drugListBaseAdapter = new drugListDetailBaseAdapter(DrugListSearchActivity.this,drugListDetailList);
        drugListView.setAdapter(drugListBaseAdapter);

    }

    public void AddDrugList() {
        drug_list_detail DrugListDetail = new drug_list_detail();
        DrugListDetail.setPharmacyName("奉生大药店");
        DrugListDetail.setDrugName("999 三九胃泰颗粒20X6袋");
        DrugListDetail.setDrugFunction("清热燥湿，行气活血，柔肝止痛");
        DrugListDetail.setPriceInt("￥16.");
        DrugListDetail.setPriceDouble("3");
        DrugListDetail.setPayNumber("21人付款");
        drugListDetailList.add(DrugListDetail);
        drugListDetailList.add(DrugListDetail);
        drugListDetailList.add(DrugListDetail);
        drugListDetailList.add(DrugListDetail);
        drugListDetailList.add(DrugListDetail);
    }
}
