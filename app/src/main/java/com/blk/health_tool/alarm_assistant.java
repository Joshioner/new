package com.blk.health_tool;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;

import com.blk.MainActivity;
import com.blk.R;
import com.blk.common.ToolBarSet;
import com.blk.common.identify.alarmAdapter;
import com.blk.common.identify.alarmDb;
import com.blk.common.identify.alarmList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzx on 2018/3/1.
 */

public class alarm_assistant extends Activity implements View.OnClickListener{
    private List<com.blk.common.identify.alarmList> lists = null;
    private com.blk.common.identify.alarmList alarmList = null;
    private alarmAdapter alarmadapter= null;
    private ListView listView = null;
    private ImageView addAlarm;
    private ImageView alarm_Back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_assistant);
        Window window = getWindow();
        ToolBarSet.setBar(window);

        initView();
        lists = new ArrayList<>();
        AddListEvent();
        alarmadapter = new alarmAdapter(lists,this);
        listView.setAdapter(alarmadapter);
        initEvent();
    }

    private void initView() {
        listView = (ListView)findViewById(R.id.alarm_list);
        addAlarm = (ImageView) findViewById(R.id.add_alarm);
        alarm_Back = (ImageView) findViewById(R.id.alarm_back);
    }
    private void initEvent() {
        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(alarm_assistant.this,alarm_add.class);
                startActivity(intent);
            }
        });
        alarm_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(alarm_assistant.this,MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("data",2);
                intent.putExtras(bundle);
                alarm_assistant.this.finish();
                startActivity(intent);
            }
        });
    }
    private void AddListEvent() {
        alarmList = new alarmList("2018-03-06","13:42","吃药");
        lists.add(alarmList);
        alarmDb db = new alarmDb(alarm_assistant.this);
        SQLiteDatabase dbRead = db.getReadableDatabase();
        Cursor b = dbRead.query("alarm",null,null,null,null,null,null);
        while (b.moveToNext()){
            String begin_time =b.getString(b.getColumnIndex("begin_time"));
            String begin_date =b.getString(b.getColumnIndex("begin_date"));
            String alarm_content=b.getString(b.getColumnIndex("alarm_content"));
            lists.add(new alarmList(begin_date,begin_time,alarm_content));
        }
        dbRead.close();
    }
    @Override
    public void onClick(View v) {

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(alarm_assistant.this,MainActivity.class);
        Bundle bundle =  new Bundle();
        bundle.putInt("data",2);
        intent.putExtras(bundle);
        this.finish();
        startActivity(intent);
        super.onBackPressed();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        lists = new ArrayList<>();
        AddListEvent();
        alarmadapter = new alarmAdapter(lists,this);
        listView.setAdapter(alarmadapter);
    }
}
