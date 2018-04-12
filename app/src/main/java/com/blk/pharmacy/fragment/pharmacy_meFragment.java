package com.blk.pharmacy.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.blk.R;
import com.blk.pharmacy.homePage.Adapter.drugListDetailBaseAdapter;
import com.blk.pharmacy.homePage.entity.drug_list_detail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2018/3/19.
 */

public class pharmacy_meFragment extends Fragment  {
    private View view;
    private RadioGroup mid_bar;
    private RadioButton daishouhuo,shoucang,dianpu,dingdan;  //四个选择按钮--- 待收货，收藏夹，关注店铺，订单
//    private CustomViewPager viewPager;   //存放fragment的viewpageer
//
//    private List<Fragment> fragmentList;   //存放fragment的list
//
//    private MyFragmentPagerAdapter myFragmentPagerAdapter;  //适配器

    //几个代表页面的常量
//    public static final int PAGE_ONE = 0;
//    public static final int PAGE_TWO = 1;
//    public static final int PAGE_THREE = 2;
//    public static final int PAGE_FOUR = 3;

    private ListView meListView;   //用于存放待收货的listView;
    private List<drug_list_detail> drugListDetailList;  //药物列表
    private drugListDetailBaseAdapter drugListBaseAdapter;  //适配器

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.pharmacy_me,container,false);
        //初始化控件
        initView();
        //初始化Radio按钮
        initRadio();

        //初始化事件
        initEvent();
        return view;
    }

    private void initEvent() {
      //  viewPager.setOnTouchListener(this);
     //   mid_bar.setOnCheckedChangeListener(this);
    }

    private void initView() {
        mid_bar = (RadioGroup) view.findViewById(R.id.mid_bar);
        daishouhuo = (RadioButton) view.findViewById(R.id.daishouhuo);
        shoucang = (RadioButton) view.findViewById(R.id.shoucang);
        dianpu = (RadioButton) view.findViewById(R.id.dianpu);
        dingdan = (RadioButton) view.findViewById(R.id.dingdan);

        meListView = (ListView) view.findViewById(R.id.me_list);  //listView初始化
        drugListDetailList = new ArrayList<drug_list_detail>();   //药物列表初始化
        AddDrugList();
        drugListBaseAdapter = new drugListDetailBaseAdapter(getActivity(),drugListDetailList);
        meListView.setAdapter(drugListBaseAdapter);
//        viewPager= (CustomViewPager) view.findViewById(R.id.vpager_me);
//        fragmentList = new ArrayList<>();
//        fragmentList.add(new daishouhuoFragment());
//        fragmentList.add(new shoucangjiaFragment());
//        fragmentList.add(new dingdanFragment());
//        fragmentList.add(new dianpuFragment());
//
//
//
//        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(),fragmentList);
//        viewPager.setAdapter(myFragmentPagerAdapter);
//        viewPager.setCurrentItem(0);
//        daishouhuo.setChecked(true);


    }

    //待收货列表
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

    private void initRadio() {

        //定义底部标签图片大小和位置
        Drawable dai_shou_huo = getResources().getDrawable(R.drawable.daishouhuo);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        dai_shou_huo.setBounds(0, 0, 30, 30);
        //设置图片在文字的哪个方向
        daishouhuo.setCompoundDrawables(null, dai_shou_huo, null, null);

        //定义底部标签图片大小和位置
        Drawable shou_cang = getResources().getDrawable(R.drawable.shou_cang);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        shou_cang.setBounds(0, 0, 30, 30);
        //设置图片在文字的哪个方向
        shoucang.setCompoundDrawables(null, shou_cang, null, null);

        //定义底部标签图片大小和位置
        Drawable dian_pu = getResources().getDrawable(R.drawable.dian_pu);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        dian_pu.setBounds(0, 0, 30, 30);
        //设置图片在文字的哪个方向
        dianpu.setCompoundDrawables(null, dian_pu, null, null);


        //定义底部标签图片大小和位置
        Drawable ding_dan = getResources().getDrawable(R.drawable.ding_dan);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        ding_dan.setBounds(0, 0, 30, 30);
        //设置图片在文字的哪个方向
        dingdan.setCompoundDrawables(null, ding_dan, null, null);

    }

//    @Override
//    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//        switch (checkedId)
//        {
//            case R.id.daishouhuo:
//                viewPager.setCurrentItem(PAGE_ONE,false);
//                break;
//            case R.id.shoucang:
//                viewPager.setCurrentItem(PAGE_TWO,false);
//                break;
//            case R.id.dingdan:
//                viewPager.setCurrentItem(PAGE_THREE,false);
//                break;
//            case R.id.dianpu:
//                viewPager.setCurrentItem(PAGE_FOUR,false);
//                break;
//        }
//    }

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        return true;
//    }
}
