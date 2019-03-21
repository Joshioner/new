package com.blk.health_tool.Adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.blk.R;
import com.blk.common.MyApplication;
import com.blk.common.identify.MyListener;
import com.blk.health_tool.AlarmAddActivity;
import com.blk.health_tool.Entity.AlarmInfo;
import com.blk.health_tool.dbHelper.AlarmDbHelper;
import com.blk.health_tool.util.AlarmUtil;
import com.loonggg.lib.alarmmanager.clock.AlarmManagerUtil;

import java.util.Calendar;
import java.util.List;

/**
 * Created by lzx on 2018/3/6.
 */

public class AlarmAdapter extends BaseAdapter {
    private Context context;
    private List<AlarmInfo> lists;
    private AlarmDbHelper dbHelper;
    public static final String ALARM_ADD_ACTION = "android.intent.action.ADD_ALARM";

    public AlarmAdapter(List<AlarmInfo> lists, Context context) {
        this.context = context;
        this.lists = lists;
        dbHelper = new AlarmDbHelper(context);
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.alarm_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.alarm_id = (TextView) convertView.findViewById(R.id.alarm_id);
            viewHolder.alarm_date = (TextView) convertView.findViewById(R.id.alarm_date);
            viewHolder.alarm_time = (TextView) convertView.findViewById(R.id.alarm_time);
            viewHolder.drugName = (TextView) convertView.findViewById(R.id.drug_name);
            viewHolder.nums = (TextView) convertView.findViewById(R.id.nums);
            viewHolder.alarm_switch = (Switch) convertView.findViewById(R.id.alarm_switch);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.alarm_id.setText(String.valueOf(lists.get(position).getId()));
        viewHolder.alarm_date.setText(lists.get(position).getDate());
        viewHolder.alarm_time.setText(lists.get(position).getTime());
        viewHolder.drugName.setText("药品：" + lists.get(position).getDrugName());
        viewHolder.nums.setText("用量：每天" + lists.get(position).getTimes() + "次，每次" + lists.get(position).getNums() + "粒/瓶/ml");

        viewHolder.alarm_switch.setChecked(lists.get(position).getStatus() == 1);
        viewHolder.alarm_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean flag) {
                // 解决滑动打开关闭switch时起作用的问题--setOnCheckedChangeListener重复加载的问题（onCheckedChanged：滑动则触发次事件，而不是点击）
                if (!compoundButton.isPressed()) {
                    return;
                }
                AlarmInfo alarmInfo = lists.get(position);
                int id = alarmInfo.getId();
                //开启闹钟
                Calendar calendar = Calendar.getInstance();
                String[] datesArray = alarmInfo.getDate().split("-");
                String[] timeArray = alarmInfo.getTime().split(":");
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                        (Calendar.DAY_OF_MONTH), Integer.parseInt(timeArray[0]), Integer.parseInt(timeArray[1]), 0);
                //开启闹钟(更新闹钟状态，并且设置闹钟)
                if (flag) {
                    try {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        //更新状态
                        db.execSQL("update alarm set status = 1 where alarm_id = " + lists.get(position).getId());
                        //设置闹钟
                        AlarmUtil.setAlarmClock(alarmInfo);

                    } catch (Exception e) {
                        Log.e("TestTest", e.getMessage());
                    }
                    Log.i("TestTest", lists.get(position).getId() + " ");
                }
                //关闭闹钟（更新闹钟状态，并且设置闹钟）
                else {
                    try {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        //更新状态
                        db.execSQL("update alarm set status = 0 where alarm_id = " + lists.get(position).getId());
                        //关闭闹钟
                        AlarmUtil.cancelAlarmClock(alarmInfo);
                    } catch (Exception e) {
                        Log.e("TestTest", e.getMessage());
                    }
                    Log.i("TestTest", "关闭状态");
                }
            }
        });


        return convertView;
    }

    private class ViewHolder {
        TextView alarm_id;
        TextView alarm_date;
        TextView alarm_time;
        TextView drugName;
        TextView nums;
        Switch alarm_switch;
    }
}
