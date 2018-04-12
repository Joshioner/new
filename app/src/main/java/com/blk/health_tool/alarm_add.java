package com.blk.health_tool;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.blk.R;
import com.blk.common.ToolBarSet;
import com.blk.common.identify.TimeUtil;
import com.blk.common.identify.alarmDb;


/**
 * Created by lzx on 2018/3/6.
 */

public class alarm_add extends Activity implements View.OnClickListener {
    //alarm_add页面控件
    private TextView firm_add_alarm;
    private EditText add_alarm_content;
    private ImageView add_alarm_back;
    private TextView select_date_1,select_time_1,select_date_2,select_time_2,select_date_3,select_time_3;
    //date_picker页面控件
    private TextView date_picker_confirm,date_picker_cancel,time_picker_confirm,time_picker_cancel;

    private int year,month,day,hour,munite,second;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_add);
        Window window = getWindow();
        ToolBarSet.setBar(window);
        initView();
        initEvent();
    }

    private void initView() {
        firm_add_alarm = (TextView) findViewById(R.id.firm_add_alarm);
        add_alarm_content = (EditText)findViewById(R.id.add_alarm_content);
        add_alarm_back = (ImageView)findViewById(R.id.add_alarm_back);
        select_date_1 = (TextView)findViewById(R.id.select_date_1);
        select_time_1 = (TextView)findViewById(R.id.select_time_1);
        select_date_2 = (TextView)findViewById(R.id.select_date_2);
        select_time_2 = (TextView)findViewById(R.id.select_time_2);
        select_date_3 = (TextView)findViewById(R.id.select_date_3);
        select_time_3 = (TextView)findViewById(R.id.select_time_3);
        add_alarm_back = (ImageView)findViewById(R.id.add_alarm_back);
    }

    private void initEvent() {
        TimeUtil timeUtil = new TimeUtil();
        select_date_1.setText(timeUtil.getNowDate());
        select_date_2.setText(timeUtil.getNowDate());
        select_date_3.setText(timeUtil.getNowDate());
        select_time_1.setText(timeUtil.getNowHour());
        select_time_2.setText(timeUtil.getNowHour());
        select_time_3.setText(timeUtil.getNowHour());

        select_date_1.setOnClickListener(this);
        select_date_2.setOnClickListener(this);
        select_date_3.setOnClickListener(this);
        select_time_1.setOnClickListener(this);
        select_time_2.setOnClickListener(this);
        select_time_3.setOnClickListener(this);
        add_alarm_back.setOnClickListener(this);
        firm_add_alarm.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.select_date_1:
                creatDateAlertDialog1();
                break;
            case R.id.select_date_2:
                creatDateAlertDialog2();
                break;
            case R.id.select_date_3:
                creatDateAlertDialog3();
                break;
            case R.id.select_time_1:
                creatTimeAlertDialog1();
                break;
            case R.id.select_time_2:
                creatTimeAlertDialog2();
                break;
            case R.id.select_time_3:
                creatTimeAlertDialog3();
                break;
            case R.id.add_alarm_back:
                Intent intent = new Intent(alarm_add.this,alarm_assistant.class);
                finish();
                startActivity(intent);
                break;
            case R.id.firm_add_alarm:
                String alarmdate1 = String.valueOf(select_date_1.getText());
                String alarmdate2 = String.valueOf(select_date_2.getText());
                String alarmdate3 = String.valueOf(select_date_3.getText());
                String alarmtime1 = String.valueOf(select_time_1.getText());
                String alarmtime2 = String.valueOf(select_time_2.getText());
                String alarmtime3 = String.valueOf(select_time_3.getText());
                String alarmcontent = String.valueOf(add_alarm_content.getText());

                alarmDb db = new alarmDb(alarm_add.this);
                SQLiteDatabase dbWrite = db.getWritableDatabase();
                ContentValues cv1 = new ContentValues();
                cv1.put("begin_date",alarmdate1);
                cv1.put("begin_time",alarmtime1);
                cv1.put("alarm_content",alarmcontent);
                dbWrite.insert("alarm",null,cv1);
                ContentValues cv2 = new ContentValues();
                cv2.put("begin_date",alarmdate2);
                cv2.put("begin_time",alarmtime2);
                cv2.put("alarm_content",alarmcontent);
                dbWrite.insert("alarm",null,cv2);
                ContentValues cv3 = new ContentValues();
                cv3.put("begin_date",alarmdate3);
                cv3.put("begin_time",alarmtime3);
                cv3.put("alarm_content",alarmcontent);
                dbWrite.insert("alarm",null,cv3);
                dbWrite.close();
                Intent intent2 = new Intent(alarm_add.this,alarm_assistant.class);
                finish();
                startActivity(intent2);
                break;

        }
    }

    public void creatDateAlertDialog1(){
        AlertDialog.Builder builder = new AlertDialog.Builder(alarm_add.this);
        View dialogView = View.inflate(alarm_add.this, R.layout.date_picker,null);
        date_picker_confirm = (TextView) dialogView.findViewById(R.id.date_picker_confirm);
        date_picker_cancel = (TextView) dialogView.findViewById(R.id.date_picker_cancel);
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.dataPicker);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        date_picker_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        date_picker_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = datePicker.getYear();
                month = datePicker.getMonth();
                day = datePicker.getDayOfMonth();
                //开始时间
                if (month<10&&day<10){
                    select_date_1.setText(year + "-0"+(month+1) + "-0" + day);
                }else if (month<10&&day>9){
                    select_date_1.setText(year + "-0"+(month+1) + "-" + day);
                }else if (month>9&&day<10){
                    select_date_1.setText(year + "-" + (month+1) + "-0" + day);
                }else {
                    select_date_1.setText(year + "-" + (month+1) + "-" + day);
                }
                alertDialog.dismiss();
            }
        });
    }
    public void creatDateAlertDialog2(){
        AlertDialog.Builder builder = new AlertDialog.Builder(alarm_add.this);
        View dialogView = View.inflate(alarm_add.this, R.layout.date_picker,null);
        date_picker_confirm = (TextView) dialogView.findViewById(R.id.date_picker_confirm);
        date_picker_cancel = (TextView) dialogView.findViewById(R.id.date_picker_cancel);
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.dataPicker);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        date_picker_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        date_picker_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = datePicker.getYear();
                month = datePicker.getMonth();
                day = datePicker.getDayOfMonth();
                //开始时间
                if (month<10&&day<10){
                    select_date_2.setText(year + "-0"+(month+1) + "-0" + day);
                }else if (month<10&&day>9){
                    select_date_2.setText(year + "-0"+(month+1) + "-" + day);
                }else if (month>9&&day<10){
                    select_date_2.setText(year + "-" + (month+1) + "-0" + day);
                }else {
                    select_date_2.setText(year + "-" + (month+1) + "-" + day);
                }
                alertDialog.dismiss();
            }
        });
    }
    public void creatDateAlertDialog3(){
        AlertDialog.Builder builder = new AlertDialog.Builder(alarm_add.this);
        View dialogView = View.inflate(alarm_add.this, R.layout.date_picker,null);
        date_picker_confirm = (TextView) dialogView.findViewById(R.id.date_picker_confirm);
        date_picker_cancel = (TextView) dialogView.findViewById(R.id.date_picker_cancel);
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.dataPicker);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        date_picker_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        date_picker_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                year = datePicker.getYear();
                month = datePicker.getMonth();
                day = datePicker.getDayOfMonth();
                //开始时间
                if (month<10&&day<10){
                    select_date_3.setText(year + "-0"+(month+1) + "-0" + day);
                }else if (month<10&&day>9){
                    select_date_3.setText(year + "-0"+(month+1) + "-" + day);
                }else if (month>9&&day<10){
                    select_date_3.setText(year + "-" + (month+1) + "-0" + day);
                }else {
                    select_date_3.setText(year + "-" + (month+1) + "-" + day);
                }
                alertDialog.dismiss();
            }
        });
    }
    public void creatTimeAlertDialog1(){
        AlertDialog.Builder builder = new AlertDialog.Builder(alarm_add.this);
        View dialogView = View.inflate(alarm_add.this, R.layout.time_picker,null);
        time_picker_confirm = (TextView) dialogView.findViewById(R.id.time_picker_confirm);
        time_picker_cancel = (TextView) dialogView.findViewById(R.id.time_picker_cancel);
        final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.timePicker);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        time_picker_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        time_picker_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hour = timePicker.getCurrentHour();
                munite = timePicker.getCurrentMinute();
                if(hour>9&&munite>9){
                    select_time_1.setText(hour+":"+munite);
                }else if(hour>9&&munite<10){
                    select_time_1.setText(hour+":0"+munite);
                }else if (hour<10&&munite>9){
                    select_time_1.setText("0"+hour+":"+munite);
                }else {
                    select_time_1.setText("0"+hour+":0"+munite);
                }
                alertDialog.dismiss();
            }
        });


    }
    public void creatTimeAlertDialog2(){
        AlertDialog.Builder builder = new AlertDialog.Builder(alarm_add.this);
        View dialogView = View.inflate(alarm_add.this, R.layout.time_picker,null);
        time_picker_confirm = (TextView) dialogView.findViewById(R.id.time_picker_confirm);
        time_picker_cancel = (TextView) dialogView.findViewById(R.id.time_picker_cancel);
        final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.timePicker);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        time_picker_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        time_picker_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hour = timePicker.getCurrentHour();
                munite = timePicker.getCurrentMinute();
                if(hour>9&&munite>9){
                    select_time_2.setText(hour+":"+munite);
                }else if(hour>9&&munite<10){
                    select_time_2.setText(hour+":0"+munite);
                }else if (hour<10&&munite>9){
                    select_time_2.setText("0"+hour+":"+munite);
                }else {
                    select_time_2.setText("0"+hour+":0"+munite);
                }
                alertDialog.dismiss();
            }
        });


    }
    public void creatTimeAlertDialog3(){
        AlertDialog.Builder builder = new AlertDialog.Builder(alarm_add.this);
        View dialogView = View.inflate(alarm_add.this, R.layout.time_picker,null);
        time_picker_confirm = (TextView) dialogView.findViewById(R.id.time_picker_confirm);
        time_picker_cancel = (TextView) dialogView.findViewById(R.id.time_picker_cancel);
        final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.timePicker);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        time_picker_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        time_picker_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hour = timePicker.getCurrentHour();
                munite = timePicker.getCurrentMinute();
                if(hour>9&&munite>9){
                    select_time_3.setText(hour+":"+munite);
                }else if(hour>9&&munite<10){
                    select_time_3.setText(hour+":0"+munite);
                }else if (hour<10&&munite>9){
                    select_time_3.setText("0"+hour+":"+munite);
                }else {
                    select_time_3.setText("0"+hour+":0"+munite);
                }
                alertDialog.dismiss();
            }
        });


    }
}
