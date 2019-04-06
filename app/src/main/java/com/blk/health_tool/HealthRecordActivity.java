package com.blk.health_tool;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blk.MainActivity;
import com.blk.R;
import com.blk.common.ToolBarSet;
import com.blk.common.entity.CaseHistory;
import com.blk.common.entity.HealthRecord;
import com.blk.common.entity.User;
import com.blk.common.util.AlterUtil;
import com.blk.common.util.ConfigUtil;
import com.blk.common.util.HttpCallbackListener;
import com.blk.common.util.HttpRequestUtil;
import com.blk.common.util.WeiboDialogUtils;
import com.blk.health_tool.Adapter.HealthRecordAdapter;
import com.blk.medical_record.Adapter.CaseHistoryDetailBaseAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class HealthRecordActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton enter_icon;
    private RecyclerView health_record_recyclerView;
    private List<HealthRecord> healthRecordList;
    private HealthRecordAdapter healthRecordAdapter;
    private SharedPreferences sharedPreferences;
    private TextView main_tv_date;
    private User user;  //用户信息
    private LinearLayout itemFirstLinearLayout;  //第一个item
    private Dialog weiboDialogUtils;                         //加载框
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        ToolBarSet.setBar(window);
        //获取sharedPreferences中的userInfo信息
        sharedPreferences = getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        String userInfo = sharedPreferences.getString("userInfo",null);
        user = com.alibaba.fastjson.JSONObject.parseObject(userInfo,User.class);
        setContentView(R.layout.health_record);

        //注册广播信息
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.refreshHealthRecord");
        registerReceiver(refreshBroadcastReceiver,intentFilter);
        //初始化控件
        initView();
        //加载康复记录
        new InitHealthRecordThread().execute();
    }

    //广播
    private BroadcastReceiver refreshBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //加载康复记录
            new InitHealthRecordThread().execute();
        }
    };
    public void initView(){
        enter_icon = (FloatingActionButton) findViewById(R.id.enter_icon);
        enter_icon.setOnClickListener(this);
        health_record_recyclerView = (RecyclerView) findViewById(R.id.health_record_list);
        //当前时间
        main_tv_date = (TextView) findViewById(R.id.main_tv_date);
        //操作时间
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        main_tv_date.setText(simpleDateFormat.format(date));
        itemFirstLinearLayout = (LinearLayout) findViewById(R.id.item_first);
    }

    class InitHealthRecordThread extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            healthRecordList = new ArrayList<HealthRecord>();
            weiboDialogUtils = WeiboDialogUtils.createLoadingDialog(HealthRecordActivity.this,"加载中...");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String address = ConfigUtil.getServerAddress() + "/healthRecord/getAll/" + user.getUid();
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
                                HealthRecord healthRecord = new HealthRecord();
                                healthRecord.setHid(object.getInteger("hid"));
                                healthRecord.setDate(object.getString("date"));
                                healthRecord.setTime(object.getString("time"));
                                healthRecord.setSport(object.getString("sport"));
                                healthRecord.setFood(object.getString("food"));
                                healthRecord.setSymptom(object.getString("symptom"));
                                healthRecord.setAddress(object.getString("address"));
                                if (healthRecord.getDate().equals(main_tv_date.getText().toString())){
                                    itemFirstLinearLayout.setVisibility(View.GONE);
                                }
                                healthRecordList.add(healthRecord);
                            }
                        }
                        healthRecordAdapter = new HealthRecordAdapter(HealthRecordActivity.this,healthRecordList);
                        health_record_recyclerView.setLayoutManager(new LinearLayoutManager(HealthRecordActivity.this));
                        health_record_recyclerView.setAdapter(healthRecordAdapter);
                        WeiboDialogUtils.closeDialog(weiboDialogUtils);
                    }else {
                        Looper.prepare();
                        AlterUtil.alterTextLong(HealthRecordActivity.this,"获取康复记录列表详情失败");
                        Looper.loop();
                        WeiboDialogUtils.closeDialog(weiboDialogUtils);
                    }
                }

                @Override
                public void onError(Exception e) {
                    WeiboDialogUtils.closeDialog(weiboDialogUtils);
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.enter_icon:
                Intent intent = new Intent(HealthRecordActivity.this,HealthRecordDetailActivity.class);
                startActivity(intent);
                break;

        }
    }

}
