package com.blk.health_tool;


import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blk.R;
import com.blk.common.ToolBarSet;
import com.blk.common.entity.FamilyMember;
import com.blk.common.entity.HealthNews;
import com.blk.common.util.AlterUtil;
import com.blk.common.util.ConfigUtil;
import com.blk.common.util.HttpCallbackListener;
import com.blk.common.util.HttpRequestUtil;
import com.blk.common.util.WeiboDialogUtils;
import com.blk.homePage.Adapter.HealthyNewsBaseAdapter;
import com.blk.medical_record.Adapter.MemberManageBaseAdapter;
import com.blk.medical_record.MemberManageActivity;
import com.blk.medical_record.entity.HealthyNews;
import com.wx.goodview.GoodView;

import java.util.ArrayList;

public class HealthyNewsActivity extends AppCompatActivity {

    private ListView healthyNews_listView;
    private ArrayList<HealthNews> healthyNews_list;
    private HealthyNewsBaseAdapter healthyNewsBaseAdapter;
    private GoodView goodView;
    private ImageView back_icon;  //返回按钮
    private Dialog weiboDialogUtils;                         //加载框
    private SharedPreferences sharedPreferences;
    private com.blk.common.entity.User user;  //用户信息
    private final int Init_Adapter = 1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Init_Adapter:
                    //加载适配器并关闭加载框
                    healthyNews_listView.setAdapter(healthyNewsBaseAdapter);
                    WeiboDialogUtils.closeDialog(weiboDialogUtils);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        ToolBarSet.setBar(window);
        setContentView(R.layout.healthy_news_list);

        //初始化控件
        initView();
        //加载健康资讯
        new InitHealthyNewsListThread().execute();
    }

    //初始化控件
    private void initView() {
        goodView = new GoodView(this);
        back_icon = (ImageView) findViewById(R.id.icon_back);
        healthyNews_listView = (ListView) findViewById(R.id.news_listView);
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    class InitHealthyNewsListThread extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            weiboDialogUtils = WeiboDialogUtils.createLoadingDialog(HealthyNewsActivity.this,"加载中");
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
                        healthyNewsBaseAdapter = new HealthyNewsBaseAdapter(HealthyNewsActivity.this,healthyNews_list,goodView);
                        handler.sendMessage(message);
                    }else {
                        WeiboDialogUtils.closeDialog(weiboDialogUtils);
                        Looper.prepare();
                        AlterUtil.alterTextLong(HealthyNewsActivity.this,"获取健康资讯列表失败");
                        Looper.loop();
                    }
                }

                @Override
                public void onError(Exception e) {
                    WeiboDialogUtils.closeDialog(weiboDialogUtils);
                    Looper.prepare();
                    AlterUtil.alterTextLong(HealthyNewsActivity.this,"获取健康资讯列表失败");
                    Looper.loop();
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }

}
