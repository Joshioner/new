package com.blk.common.identify;

/**
 * Created by lzx on 2018/3/6.
 */

public class alarmList {
    private String alarm_date;
    private String alarm_time;
    private String alarm_content;
    public alarmList(){
    }
    public alarmList(String alarm_date,String alarm_time,String alarm_content){
        this.alarm_date=alarm_date;
        this.alarm_time=alarm_time;
        this.alarm_content = alarm_content;
    }
    public String getAlarm_date() {
        return alarm_date;
    }
    public String getAlarm_time() {
        return alarm_time;
    }
    public String getAlarm_content() {
        return alarm_content;
    }
    public void setAlarm_date(String alarm_date) {
        this.alarm_date = alarm_date;
    }
    public void setAlarm_time(String alarm_time) {
        this.alarm_time = alarm_time;
    }
    public void setAlarm_content(String alarm_content) {
        this.alarm_content = alarm_content;
    }

}
