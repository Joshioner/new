package com.blk.health_tool.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.blk.common.MyApplication;
import com.blk.health_tool.Entity.AlarmInfo;
import com.loonggg.lib.alarmmanager.clock.AlarmManagerUtil;

import java.util.Calendar;

public class AlarmUtil {
    public static final String ALARM_ACTION = "com.loonggg.alarm.clock";
    public static void setAlarmClock(AlarmInfo alarmInfo){
        int id = alarmInfo.getId();
        //开启闹钟
        Calendar calendar = Calendar.getInstance();
        String[] datesArray = alarmInfo.getDate().split("-");
        String[] timeArray = alarmInfo.getTime().split(":");
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                (Calendar.DAY_OF_MONTH), Integer.parseInt(timeArray[0]), Integer.parseInt(timeArray[1]), 0);
        for (int index = 0; index < alarmInfo.getDays();index++){
            for (int j = 0; j < alarmInfo.getTimes();j++){
                // soundOrVibrator(0:震动，1:铃声)
                AlarmManagerUtil.setAlarm(MyApplication.getContext() , 0, calendar, id, 0, "主人，请按时吃药哦", 1,alarmInfo.getDrugName(),alarmInfo.getNums());
                //一次性设置多个闹钟，id应该不同，不然后面的会覆盖前面的闹钟
                id++;
           //     Log.i("Main","TestTestSet " + calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "  " +  calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
                if (calendar.get(Calendar.HOUR_OF_DAY) + alarmInfo.getIntervalTime() > 24 || (calendar.get(Calendar.HOUR_OF_DAY) + alarmInfo.getIntervalTime() == 24 && calendar.get(Calendar.MINUTE) >0)){
                    break;
                }
                //间隔时间
                calendar.add(Calendar.HOUR_OF_DAY,alarmInfo.getIntervalTime());
            }
            calendar.set(Integer.parseInt(datesArray[0]),Integer.parseInt(datesArray[1]) - 1,Integer.parseInt(datesArray[2]) ,Integer.parseInt(timeArray[0]),Integer.parseInt(timeArray[1]));
            //天数
            calendar.add(Calendar.DAY_OF_MONTH,1 * (index + 1) );
        }
    }

    public static void cancelAlarmClock(AlarmInfo alarmInfo){
        int id = alarmInfo.getId();
        //开启闹钟
        Calendar calendar = Calendar.getInstance();
        String[] datesArray = alarmInfo.getDate().split("-");
        String[] timeArray = alarmInfo.getTime().split(":");
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                (Calendar.DAY_OF_MONTH), Integer.parseInt(timeArray[0]), Integer.parseInt(timeArray[1]), 0);
        AlarmManager am = (AlarmManager) MyApplication.getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ALARM_ACTION);
        intent.putExtra("intervalMillis", 0);
        intent.putExtra("msg", "主人，请按时吃药哦");
        intent.putExtra("id", id);
        intent.putExtra("soundOrVibrator", 1);
        intent.putExtra("drugName",alarmInfo.getDrugName());
        intent.putExtra("nums",alarmInfo.getNums());
        for (int index = 0; index < alarmInfo.getDays();index++){
            for (int j = 0; j < alarmInfo.getTimes();j++){
                //取消闹钟设置
                //一次性设置多个闹钟，id应该不同，不然后面的会覆盖前面的闹钟
                //Flags为FLAG_CANCEL_CURRENT ：如果AlarmManager管理的PendingIntent已经存在，那么将会取消当前的PendingIntent，从而创建一个新的PendingIntent
                PendingIntent sender = PendingIntent.getBroadcast(MyApplication.getContext(), id, intent, PendingIntent
                        .FLAG_CANCEL_CURRENT);
                am.cancel(sender);
                id++;
              //  Log.i("Main","TestTestCancel " + calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "  " +  calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
                if (calendar.get(Calendar.HOUR_OF_DAY) + alarmInfo.getIntervalTime() > 24 || (calendar.get(Calendar.HOUR_OF_DAY) + alarmInfo.getIntervalTime() == 24 && calendar.get(Calendar.MINUTE) >0)){
                    break;
                }
                //间隔时间
                calendar.add(Calendar.HOUR_OF_DAY,alarmInfo.getIntervalTime());
            }
            calendar.set(Integer.parseInt(datesArray[0]),Integer.parseInt(datesArray[1]) - 1,Integer.parseInt(datesArray[2]) ,Integer.parseInt(timeArray[0]),Integer.parseInt(timeArray[1]));
            //天数
            calendar.add(Calendar.DAY_OF_MONTH,1 * (index + 1) );
        }
    }
}
