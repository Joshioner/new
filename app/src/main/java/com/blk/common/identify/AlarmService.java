package com.blk.common.identify;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class AlarmService extends Service {
    final String DEFAULT_TIME="2018-03-28 00:00";
    MyThread myThread;

    TimeUtil timeUtil = new TimeUtil();
    public AlarmService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        myThread.flag = false;
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Bundle myBundle = intent.getExtras();
        String alarmtime = myBundle.getString("alarm_time");
        String alarmcontent = myBundle.getString("alarm_content");

        //注意、int存储的范围-2147483648～2147483647
        int response_time_difference=Integer.valueOf(timeUtil.getTimeDifference(DEFAULT_TIME,alarmtime));
        Log.i("MainActivity",response_time_difference+"");

        myThread = new MyThread(response_time_difference,alarmcontent);
        myThread.start();
        super.onStart(intent, startId);
    }


    class MyThread extends Thread {
        boolean flag = true;
        int response_time_difference;
        String alarm_content;
        int eatting_num;
        MyThread(int a,String b){
            this.response_time_difference =a;
            this.alarm_content=b;
        }
        @Override
        public void run() {
            while (flag){
                int now_time_difference = Integer.valueOf(timeUtil.getTimeDifference(DEFAULT_TIME,timeUtil.getNowTime()));
                if (now_time_difference == response_time_difference) {
                    Intent i = new Intent("com.baidu.ocr.user.identify.ServBroad.myThread");
                    i.putExtra("alarm_content",alarm_content);
                    sendBroadcast(i);
                }
                try {
                    Thread.sleep(60*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}


