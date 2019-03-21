package com.blk.health_tool;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blk.MainActivity;
import com.blk.R;
import com.blk.common.MyApplication;
import com.blk.common.ToolBarSet;
import com.blk.health_tool.Adapter.AlarmAdapter;
import com.blk.health_tool.Entity.AlarmInfo;
import com.blk.health_tool.dbHelper.AlarmDbHelper;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzx on 2018/3/1.
 */

public class AlarmAssistantActivity extends Activity implements View.OnClickListener {
    private  List<AlarmInfo> alarmInfoList = null;
    private  AlarmInfo alarmInfo = null;
    private AlarmAdapter alarmadapter= null;
    private ListView listView = null;
    private ImageView addAlarm;
    private ImageView alarm_Back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_assistant);
        Window window = getWindow();
        ToolBarSet.setBar(window);

        //初始化控件
        initView();
        //异步加载闹钟列表
        new InitAlarmListThread().execute();
    }

    /**
     * 初始化闹钟列表
     */
    class InitAlarmListThread extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            //闹钟列表
            alarmInfoList = new ArrayList<AlarmInfo>();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            AlarmDbHelper dbHelper = new AlarmDbHelper(AlarmAssistantActivity.this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query("alarm",null,null,null,null,null,"alarm_id desc",null);
            while (cursor.moveToNext()){
                alarmInfo = new AlarmInfo();
                int id = cursor.getInt(cursor.getColumnIndex("alarm_id"));
                String drugName = cursor.getString(cursor.getColumnIndex("drug_name"));
                int times = cursor.getInt(cursor.getColumnIndex("times"));
                int nums = cursor.getInt(cursor.getColumnIndex("nums"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                int intervalTime = cursor.getInt(cursor.getColumnIndex("interval_time"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                int status = cursor.getInt(cursor.getColumnIndex("status"));
                int days = cursor.getInt(cursor.getColumnIndex("days"));
                alarmInfo.setId(id);
                alarmInfo.setDays(days);
                alarmInfo.setDate(date);
                alarmInfo.setIntervalTime(intervalTime);
                alarmInfo.setTime(time);
                alarmInfo.setTimes(times);
                alarmInfo.setDrugName(drugName);
                alarmInfo.setStatus(status);
                alarmInfo.setNums(nums);
                alarmInfoList.add(alarmInfo);
            }
            alarmadapter = new AlarmAdapter(alarmInfoList,AlarmAssistantActivity.this);
            alarmadapter.notifyDataSetChanged();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            listView.setAdapter(alarmadapter);
        }
    }
    /**
     * 初始化控件
     */
    private void initView() {
        listView = (ListView)findViewById(R.id.alarm_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.i("TestTest","1111" + "  " + position );
                int alarm_id = Integer.parseInt(((TextView)view.findViewById(R.id.alarm_id)).getText().toString());
                Intent intent = new Intent(AlarmAssistantActivity.this,AlarmAddActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", alarm_id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        addAlarm = (ImageView) findViewById(R.id.add_alarm);
        addAlarm.setOnClickListener(this);
        alarm_Back = (ImageView) findViewById(R.id.alarm_back);
        alarm_Back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.alarm_back:
               this.finish();
           case R.id.add_alarm:
               Intent intent = new Intent(AlarmAssistantActivity.this,AlarmAddActivity.class);
               startActivity(intent);
               break;
           default:
               break;
       }
    }
    @Override
    public void onBackPressed() {
        this.finish();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        new InitAlarmListThread().execute();
    }
}
