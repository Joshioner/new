package com.blk.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blk.MainActivity;
import com.blk.R;
import com.blk.common.entity.HealthNews;
import com.blk.common.util.AlterUtil;
import com.blk.common.util.ConfigUtil;
import com.blk.common.util.HttpCallbackListener;
import com.blk.common.util.HttpRequestUtil;
import com.blk.common.util.WeiboDialogUtils;
import com.blk.health_tool.HealthyNewsActivity;
import com.blk.homePage.Adapter.HealthyNewsBaseAdapter;
import com.blk.rollviewpager.RollPagerView;
import com.blk.rollviewpager.adapter.StaticPagerAdapter;
import com.wx.goodview.GoodView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/8/9.
 */

public class homePage_Fragment extends Fragment implements View.OnClickListener{
    private View view ;
    private RollPagerView mViewPager;

    private ListView healthy_news_listView;   //存放健康资讯的ListView
    private List<HealthNews> healthyNews_list;  //存放健康资讯的列表
    private HealthyNewsBaseAdapter healthy_news_BaseAdapter;   //适配器
    private RadioButton myCaseHistory,healthyTool,pharmacy,healthyServer; //首页的四个图标：我的病历、个人工具、居家药品、健康服务
    private GoodView goodView;
    private ImageView healthy_news_more;  //查看更多资讯
    private Dialog weiboDialogUtils;
    public homePage_Fragment(){
    }
    private final int Init_Adapter = 1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Init_Adapter:
                    //加载适配器并关闭加载框
                    healthy_news_listView.setAdapter(healthy_news_BaseAdapter);
                    WeiboDialogUtils.closeDialog(weiboDialogUtils);
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null){
            view=inflater.inflate(R.layout.home_page,container,false);
        }else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if(parent != null) {
                parent.removeView(view);
            }
        }

        mViewPager = (RollPagerView)view.findViewById(R.id.view_pager);
        //设置播放时间间隔
        mViewPager.setPlayDelay(2000);
       //设置滑动播放时间
        mViewPager.setAnimationDurtion(500);
       //设置适配器
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
        //查看更多健康资讯
        healthy_news_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),HealthyNewsActivity.class);
                startActivity(intent);
            }
        });
    }


    //初始化控件
    private void initView() {
        goodView = new GoodView(getActivity());
        healthy_news_listView = (ListView) view.findViewById(R.id.news_listView);
        healthy_news_more = (ImageView) view.findViewById(R.id.healthy_news_more);
        //加载健康资讯
        new InitHealthyNewsListThread().execute();

    }


    class InitHealthyNewsListThread extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            weiboDialogUtils = WeiboDialogUtils.createLoadingDialog(getActivity(),"加载中");
            healthyNews_list = new ArrayList<HealthNews>();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Message message = Message.obtain();
            message.what = Init_Adapter;
            String address = ConfigUtil.getServerAddress() + "/healthNews/getAllByCondition/" ;
            HttpRequestUtil.sendHttpRequest(address, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(response);
                    int code = jsonObject.getIntValue("code");
                    if (code == 0){
                        String data = jsonObject.getString("data");
                        JSONArray jsonArray = JSONArray.parseArray(data);
                        if (jsonArray.size() > 0){
                            for (int i = 0; i < jsonArray.size();i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                //健康资讯信息
                                HealthNews healthNews = new HealthNews();
                                healthNews.setNid(object.getIntValue("nid"));
                                healthNews.setAuthor(object.getString("author"));
                                healthNews.setContent(object.getString("content"));
                                healthNews.setCoverUrl(object.getString("coverUrl"));
                                healthNews.setTitle(object.getString("title"));
                                healthyNews_list.add(healthNews);
                            }
                        }
                        healthy_news_BaseAdapter = new HealthyNewsBaseAdapter(getActivity(),healthyNews_list,goodView);
                        handler.sendMessage(message);
                    }else {
                        WeiboDialogUtils.closeDialog(weiboDialogUtils);
                        Looper.prepare();
                        AlterUtil.alterTextLong(getActivity(),"获取健康资讯列表失败");
                        Looper.loop();
                    }
                }

                @Override
                public void onError(Exception e) {
                    WeiboDialogUtils.closeDialog(weiboDialogUtils);
                    Looper.prepare();
                    AlterUtil.alterTextLong(getActivity(),"获取健康资讯列表失败");
                    Looper.loop();
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }

    private void initRadio() {
         myCaseHistory = (RadioButton) view.findViewById(R.id.myCaseHistory);
         healthyTool = (RadioButton) view.findViewById(R.id.myTool);
         pharmacy = (RadioButton)view.findViewById(R.id.pharmacy);
         //healthyServer = (RadioButton)view.findViewById(R.id.healthyServer);

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
   //     healthyServer.setCompoundDrawables(null, home_icon_serve, null, null);
    }

    @Override
    public void onClick(View v) {
        //获取view的id
        int id = v.getId();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        Bundle bundle = new Bundle();
        switch (id)
        {
            //药房
            case R.id.pharmacy :
                intent = new Intent(getActivity(), com.blk.pharmacy.MainActivity.class);
                startActivity(intent);
                break;
            // 个人工具
            case R.id.myTool:
                bundle.putInt("data",2);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            // 我的病历
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
