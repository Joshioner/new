package com.blk.common.identify;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.blk.R;

/**
 * Created by lzx on 2017/11/21.
 */
/*在manifests静态注册*/
public class AlarmbroadcasReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle myBundle = intent.getExtras();
        String alarm_event = myBundle.getString("alarm_content");
        NotificationManager mNotificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle(alarm_event)//设置通知栏标题
                    .setContentText("时间到了，记得噢！") //<span style="font-family: Arial;">/设置通知栏显示内容</span>
                    .setContentIntent(getDefalutIntent(context,Notification.FLAG_INSISTENT)) //设置通知栏点击意图
//                  .setNumber(number) //设置通知集合的数量
                    .setTicker("闹钟助手提醒您！") //通知首次出现在通知栏，带上升动画效果的
                    .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                    .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
//                  .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                    .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                    //.setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                    //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                    .setSmallIcon(R.mipmap.alarm_assistant)//设置通知小ICON
                    .setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.music));
            //.setLights(0xff0000ff, 300, 0)
            mNotificationManager.notify(1, mBuilder.build());
    }
        public PendingIntent getDefalutIntent(Context context,int flags){
        PendingIntent pendingIntent= PendingIntent.getActivity(context, 1, new Intent(), flags);
        return pendingIntent;
        }
}
