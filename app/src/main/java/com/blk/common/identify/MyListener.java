package com.blk.common.identify;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.blk.health_tool.Entity.AlarmInfo;


/**
 * Created by lzx on 2017/11/16.
 */
public class MyListener implements CompoundButton.OnCheckedChangeListener{
    int mPosition;
    Context mContext;
    AlarmInfo alarminfo;
    public MyListener(int inPosition, Context context, AlarmInfo alarmInfo){
        mPosition= inPosition;
        mContext = context;
        alarminfo=alarmInfo;
    }
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean flag) {
        if (flag) {     //开启闹钟
            Log.i("TestTest",alarminfo.getId() + " ");
        } else {     //关闭闹钟
            Log.i("TestTest","关闭状态");
        }
    }




}