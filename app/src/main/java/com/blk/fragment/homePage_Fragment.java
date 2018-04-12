package com.blk.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;

import com.blk.MainActivity;
import com.blk.R;
import com.blk.medical_record.Adapter.healthyNewsBaseAdapter;
import com.blk.medical_record.entity.healthy_news;
import com.blk.rollviewpager.RollPagerView;
import com.blk.rollviewpager.adapter.StaticPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/8/9.
 */

public class homePage_Fragment extends Fragment implements View.OnClickListener{
    private View view ;
    private RollPagerView mViewPager;
    private healthy_news healthyNews;   //健康资讯对象
    private ListView healthy_news_listView;   //存放健康资讯的ListView
    private List<healthy_news> healthy_news_list;  //存放健康资讯的列表
    private healthyNewsBaseAdapter healthy_news_BaseAdapter;   //适配器
    private RadioButton myCaseHistory,healthyTool,pharmacy,healthyServer; //首页的四个图标：我的病历、个人工具、居家药品、健康服务

    public homePage_Fragment(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.home_page,container,false);
        mViewPager = (RollPagerView)view.findViewById(R.id.view_pager);
        mViewPager.setAdapter(new ImageNormalAdapter());
        initRadio();
        //初始化控件
        initView();
       //事件
        initEvent();
        return view;
    }

    private void initEvent() {
        //图标 -- 我的病历  点击事件
        myCaseHistory.setOnClickListener(this);
        //图标 -- 居家药房  点击事件
        pharmacy.setOnClickListener(this);
        //图标 -- 个人工具  点击事件
        healthyTool.setOnClickListener(this);
    }


    //初始化控件
    private void initView()
    {
        healthy_news_listView = (ListView) view.findViewById(R.id.news_listView);
        healthy_news_list = new ArrayList<healthy_news>();
        AddHealthyNewsList();
        healthy_news_BaseAdapter = new healthyNewsBaseAdapter(getActivity(),healthy_news_list);
        healthy_news_listView.setAdapter(healthy_news_BaseAdapter);
    }

    private void AddHealthyNewsList()
    {
        String news_title = "孩子发烧，这三种物理降温才有效";
        String author_name = "作者：海棠";
        String news_content = "天一冷，感冒发烧的宝宝又增多了，本文盘点了几种常见的物理降温方法";
        healthyNews = new healthy_news(news_title,author_name,news_content);
        healthy_news_list.add(healthyNews);
        healthy_news_list.add(healthyNews);
        healthy_news_list.add(healthyNews);
        healthy_news_list.add(healthyNews);

    }


    private void initRadio() {
         myCaseHistory = (RadioButton) view.findViewById(R.id.myCaseHistory);
         healthyTool = (RadioButton) view.findViewById(R.id.myTool);
         pharmacy = (RadioButton)view.findViewById(R.id.pharmacy);
         healthyServer = (RadioButton)view.findViewById(R.id.healthyServer);

        //定义底部标签图片大小和位置
        Drawable home_icon_mycasehistory = getResources().getDrawable(R.mipmap.home_icon_mycasehistory);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        home_icon_mycasehistory.setBounds(0, 0, 30, 40);
        //设置图片在文字的哪个方向
        myCaseHistory.setCompoundDrawables(null, home_icon_mycasehistory, null, null);
//
        //定义底部标签图片大小和位置
        Drawable home_icon_mytool = getResources().getDrawable(R.mipmap.home_icon_mytool);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        home_icon_mytool.setBounds(0, 0, 30, 40);
        //设置图片在文字的哪个方向
        healthyTool.setCompoundDrawables(null, home_icon_mytool, null, null);
//
        //定义底部标签图片大小和位置
        Drawable home_icon_pharmacy = getResources().getDrawable(R.mipmap.home_icon_pharmacy);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        home_icon_pharmacy.setBounds(0, 0, 30, 40);
        //设置图片在文字的哪个方向
        pharmacy.setCompoundDrawables(null, home_icon_pharmacy, null, null);
//
        //定义底部标签图片大小和位置
        Drawable home_icon_serve = getResources().getDrawable(R.mipmap.home_icon_serve);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        home_icon_serve.setBounds(0, 0, 30, 40);
        //设置图片在文字的哪个方向
        healthyServer.setCompoundDrawables(null, home_icon_serve, null, null);
    }

    @Override
    public void onClick(View v) {
        //获取view的id
        int id = v.getId();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        Bundle bundle = new Bundle();
        switch (id)
        {
            case R.id.pharmacy :
                intent = new Intent(getActivity(), com.blk.pharmacy.MainActivity.class);
                startActivity(intent);
                break;
            case R.id.myTool:
                bundle.putInt("data",2);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.myCaseHistory:
                bundle.putInt("data",1);
                intent.putExtras(bundle);
                startActivity(intent);
                break;


        }
    }


    private class ImageNormalAdapter extends StaticPagerAdapter {
        int[] imgs = new int[]{
                R.mipmap.home_banner,
                R.mipmap.home_bg_left,
                R.mipmap.home_banner,
                R.mipmap.home_bg_left,
                R.mipmap.home_banner,
        };

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view1 = new ImageView(container.getContext());
            view1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            view1.setImageResource(imgs[position]);
            return view1;
        }


        @Override
        public int getCount() {
            return imgs.length;
        }
    }

}
