package com.blk.health_tool;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blk.R;
import com.blk.common.ToolBarSet;
import com.blk.common.entity.FamilyMember;
import com.blk.common.entity.HealthNews;
import com.blk.common.entity.HealthRecord;
import com.blk.common.util.AlterUtil;
import com.blk.common.util.BitmapFactoryUtil;
import com.blk.common.util.ConfigUtil;
import com.blk.common.util.FileUtil;
import com.blk.common.util.HttpCallbackListener;
import com.blk.common.util.HttpRequestUtil;
import com.blk.common.util.WeiboDialogUtils;
import com.blk.medical_record.AddMemberActivity;

import org.w3c.dom.Text;

import java.io.File;

public class HealthyNewsDetailActivity extends AppCompatActivity {

    private ImageView icon_back;
    private TextView news_title,news_author,news_times,news_content;
    private ImageView news_cover;
    private int nid;
    private Dialog weiboDialogUtils;                         //加载框
    private final int Init_UI = 1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Init_UI:
                    HealthNews healthNews = (HealthNews) msg.obj;
                    //标题
                    news_title.setText(healthNews.getTitle());
                    //作者
                    news_author.setText(healthNews.getAuthor());
                    //时间
                    String[] operTime = healthNews.getOperTime().split(" ");
                    String[] date = operTime[0].split("-");
                    news_times.setText(date[0] + "/" + date[1] + "/" + date[2]);
                    //图片
                    String coverUrl = healthNews.getCoverUrl();
                    File file = new File(getFilesDir().getAbsolutePath() + "/healthNews/",coverUrl.substring(coverUrl.lastIndexOf("/") + 1));
                    if (file.exists()){
                        news_cover.setImageBitmap(BitmapFactoryUtil.getBitmap(file.getAbsolutePath()));
                    }else {
                        BitmapFactoryUtil.setNetworkBitmap(HealthyNewsDetailActivity.this,news_cover,ConfigUtil.getServerAddress() + "/"  + healthNews.getCoverUrl());
                    }
                    //内容
                    news_content.setText(Html.fromHtml(healthNews.getContent()));
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
        setContentView(R.layout.healthy_news_detail);
        //初始化控件
        initView();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            nid = bundle.getInt("nid");
            //加载健康资讯信息
            new  HealthNewsThread().execute();
        }
    }

    class HealthNewsThread extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            weiboDialogUtils = WeiboDialogUtils.createLoadingDialog(HealthyNewsDetailActivity.this,"加载中...");
        }

        @Override
        protected Void doInBackground(Void... integers) {
            String url = ConfigUtil.getServerAddress() + "/healthNews/findById/" + nid ;
            HttpRequestUtil.sendHttpRequest(url, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(response);
                    int code = jsonObject.getIntValue("code");
                    if (code == 0){
                        String data = jsonObject.getString("data");
                        HealthNews healthNews = JSONObject.parseObject(data,HealthNews.class);
                        Message message = Message.obtain();
                        message.what = Init_UI;
                        message.obj = healthNews;
                        //执行更新UI操作
                        handler.sendMessage(message);
                    }else {
                        Looper.prepare();
                        AlterUtil.alterTextShort(HealthyNewsDetailActivity.this,"加载健康资讯信息失败");
                        Looper.loop();
                        WeiboDialogUtils.closeDialog(weiboDialogUtils);
                    }
                }

                @Override
                public void onError(Exception e) {
                    Looper.prepare();
                    AlterUtil.alterTextShort(HealthyNewsDetailActivity.this,"加载健康资讯信息失败");
                    Looper.loop();
                    WeiboDialogUtils.closeDialog(weiboDialogUtils);
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            WeiboDialogUtils.closeDialog(weiboDialogUtils);
        }
    }

    private void initView(){
        icon_back = (ImageView) findViewById(R.id.icon_back);
        icon_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        news_cover = (ImageView) findViewById(R.id.news_cover);
        news_title = (TextView) findViewById(R.id.news_title);
        news_author = (TextView) findViewById(R.id.news_author);
        news_times = (TextView) findViewById(R.id.news_time);
        news_content = (TextView) findViewById(R.id.news_content);
    }

    @Override
    //安卓重写返回键事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return true;
    }

}
