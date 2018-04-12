package com.blk.common.identify;

import android.content.Context;
import android.content.Intent;
import android.widget.CompoundButton;


/**
 * Created by lzx on 2017/11/16.
 */
public class MyListener implements CompoundButton.OnCheckedChangeListener{
    int mPosition;
    Context mContext;
    String[] alarminfo;
    public MyListener(int inPosition, Context context, String[] alarm_info){
        mPosition= inPosition;
        mContext = context;
        alarminfo=alarm_info;
    }
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            String alarmtime=alarminfo[0]+" "+alarminfo[1];
            String alarmcontent=alarminfo[2];

            Intent i =new Intent(mContext,AlarmService.class);
            i.putExtra("alarm_time",alarmtime);
            i.putExtra("alarm_content",alarmcontent);
            mContext.startService(i);

        } else {
            Intent i =new Intent(mContext,AlarmService.class);
            mContext.stopService(i);
        }
    }




}