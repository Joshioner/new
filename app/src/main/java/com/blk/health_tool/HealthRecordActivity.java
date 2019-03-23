package com.blk.health_tool;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;

import com.blk.R;
import com.blk.common.ToolBarSet;
import com.blk.health_tool.Adapter.HealthRecordAdapter;
import com.blk.health_tool.Entity.HealthRecord;

import java.util.ArrayList;
import java.util.List;


public class HealthRecordActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton enter_icon;
    private RecyclerView health_record_recyclerView;
    private List<HealthRecord> healthRecordList;
    private HealthRecordAdapter healthRecordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        ToolBarSet.setBar(window);
        setContentView(R.layout.health_record);

        //初始化控件
        initView();
        //加载康复记录
        new InitHealthRecordThread().execute();
    }

    public void initView(){
        enter_icon = (FloatingActionButton) findViewById(R.id.enter_icon);
        enter_icon.setOnClickListener(this);
        health_record_recyclerView = (RecyclerView) findViewById(R.id.health_record_list);


    }

    class InitHealthRecordThread extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            healthRecordList = new ArrayList<HealthRecord>();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HealthRecord healthRecord = new HealthRecord();
            for (int index = 0; index < 4;index++){
                healthRecord.setId(index);
                healthRecord.setDate("2019年03月" + (20 + index ) + "日");
                healthRecord.setTime("12:15");
                healthRecord.setSport("1111长跑30分钟，散步一个小时，打篮球半个小时");
                healthRecord.setFood("11111苹果、梨、香蕉，喝酒");
                healthRecord.setSymptom("胸口骨头疼痛，按压会痛，早上起床或走路时不会痛，久坐后活动肩膀或扩胸有时会有咯吱的轻微响声111");
                healthRecord.setAddress("家里沙发");
                healthRecordList.add(healthRecord);
            }
            healthRecordAdapter = new HealthRecordAdapter(HealthRecordActivity.this,healthRecordList);
            health_record_recyclerView.setLayoutManager(new LinearLayoutManager(HealthRecordActivity.this));
            health_record_recyclerView.setAdapter(healthRecordAdapter);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.enter_icon:
                Intent intent = new Intent(this,HealthRecordDetailActivity.class);
                startActivity(intent);
                break;

        }
    }


}
