package com.blk.pharmacy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.blk.R;
import com.blk.pharmacy.homePage.Adapter.pharmacyListDetailBaseAdapter;
import com.blk.pharmacy.homePage.entity.pharmacy_list_detail;
import com.blk.pharmacy.homePage.pharmacySearchActivity;
import com.blk.pharmacy.homePage.pharmacyShopperPageActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2018/3/19.
 */

public class pharmacy_homePageFragment extends Fragment implements View.OnClickListener{
    private View view;
    private TabLayout tabLayout;
    private ListView pharmacyListView ;    //显示药店的ListView
    private List<pharmacy_list_detail> pharmacy_list;  //显示药店的列表
    private pharmacyListDetailBaseAdapter pharmacy_list_detailBaseAdapter;  //适配器
    private ImageView pharmacySearchBox; //药房药物查询框

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.pharmacy_homepage,container,false);
        //初始化控件
        initView();
        //事件
        initEvent();
        return view;
    }

    private void initEvent() {
        pharmacySearchBox.setOnClickListener(this);
        pharmacyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), pharmacyShopperPageActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        pharmacyListView = (ListView) view.findViewById(R.id.pharmacy_list);
        pharmacy_list = new ArrayList<pharmacy_list_detail>();
        AddPharmacyList();
        pharmacy_list_detailBaseAdapter = new pharmacyListDetailBaseAdapter(getActivity(),pharmacy_list);
        pharmacyListView.setAdapter(pharmacy_list_detailBaseAdapter);
        pharmacySearchBox = (ImageView) view.findViewById(R.id.pharmacySearchBox);
    }
    public void AddPharmacyList()
    {
        pharmacy_list_detail pharmacyListDetail = new pharmacy_list_detail();
        pharmacyListDetail.setAverage("人均￥38");
        pharmacyListDetail.setDistance("853m");
        pharmacyListDetail.setDistribution("配送￥8");
        pharmacyListDetail.setPharmacyName("奉生大药房");
        pharmacyListDetail.setSend("起送￥0");
        pharmacyListDetail.setTime("30分钟");
        pharmacy_list.add(pharmacyListDetail);
        pharmacy_list.add(pharmacyListDetail);
        pharmacy_list.add(pharmacyListDetail);
        pharmacy_list.add(pharmacyListDetail);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent;
        switch (id)
        {
            case R.id.pharmacySearchBox:
                intent = new Intent(getActivity(),pharmacySearchActivity.class);
                startActivity(intent);

        }
    }
}
