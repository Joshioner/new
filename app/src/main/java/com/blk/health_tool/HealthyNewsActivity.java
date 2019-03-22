package com.blk.health_tool;

import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.blk.R;
import com.blk.common.ToolBarSet;
import com.blk.homePage.Adapter.HealthyNewsBaseAdapter;
import com.blk.medical_record.entity.HealthyNews;
import com.wx.goodview.GoodView;

import java.util.ArrayList;

public class HealthyNewsActivity extends AppCompatActivity {

    private ListView healthyNews_listView ;
    private ArrayList<HealthyNews> healthyNews_list;
    private HealthyNews healthyNews;
    private HealthyNewsBaseAdapter healthyNewsBaseAdapter;
    private GoodView goodView;
    private ImageView back_icon;  //返回按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        ToolBarSet.setBar(window);
        setContentView(R.layout.healthy_news_list);

        //初始化控件
        initView();
    }

    //初始化控件
    private void initView()
    {
        goodView = new GoodView(this);
        back_icon = (ImageView) findViewById(R.id.icon_back);
        healthyNews_listView = (ListView) findViewById(R.id.news_listView);
        //加载健康资讯
        new InitHealthyNewsListThread().execute();
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }



    class InitHealthyNewsListThread extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            healthyNews_list = new ArrayList<HealthyNews>();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String news_title = "孩子发烧，这三种物理降温才有效";
            String author_name = "作者：海棠";
            String news_content = "天一冷，感冒发烧的宝宝又增多了，本文盘点了几种常见的物理降温方法";
            healthyNews = new HealthyNews(1,news_title,author_name,news_content,"",0);
            healthyNews_list.add(healthyNews);
            healthyNews_list.add(healthyNews);
            healthyNews_list.add(healthyNews);
            healthyNews_list.add(healthyNews);
            healthyNewsBaseAdapter = new HealthyNewsBaseAdapter(HealthyNewsActivity.this, healthyNews_list,goodView);
            healthyNews_listView.setAdapter(healthyNewsBaseAdapter);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }

}
