package com.blk.health_tool;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blk.R;
import com.blk.common.CommomDialog;
import com.blk.common.ToolBarSet;

import java.util.Calendar;

import cc.trity.floatingactionbutton.FloatingActionButton;
import cn.qqtheme.framework.picker.TimePicker;
import cn.qqtheme.framework.util.ConvertUtils;

public class HealthRecordDetailActivity extends Activity implements View.OnClickListener {

    private FloatingActionButton add, back, delete;
    private TextView health_record_title;
    private EditText time;
    private ImageView back_imageView;
    private EditText symptom;
    private int health_record_id = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        ToolBarSet.setBar(window);
        setContentView(R.layout.healthy_record_detail);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            health_record_id = bundle.getInt("id");
        }
        //初始化控件
        initView();
    }

    public void initView() {
        //标题
        health_record_title = (TextView) findViewById(R.id.health_record_title);
        health_record_title.setText("添加康复记录");
        //时间
        time = (EditText) findViewById(R.id.health_record_time);
        time.setOnClickListener(this);
        //添加按钮
        add = (FloatingActionButton) findViewById(R.id.health_record_add);
        add.setOnClickListener(this);
        //删除按钮
        delete = (FloatingActionButton) findViewById(R.id.health_record_delete);
        delete.setOnClickListener(this);
//        //返回按钮
        back = (FloatingActionButton) findViewById(R.id.health_record_back_icon);
        back.setOnClickListener(this);
        //标题栏的返回按钮
        back_imageView = (ImageView) findViewById(R.id.health_record_back);
        back_imageView.setVisibility(View.GONE);
        //症状
        symptom = (EditText) findViewById(R.id.health_record_symptom);
        if (health_record_id >= 0){
            symptom.setText(getIntent().getExtras().getString("symptom"));
            delete.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //选择时间
            case R.id.health_record_time:
                TimePicker timePicker = getTimePicker();
                timePicker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
                    @Override
                    public void onTimePicked(String hour, String minute) {
                        time.setFocusable(true);
                        time.setText(hour + ":" + minute);
                        time.setFocusable(false);
                    }
                });
                timePicker.show();
                break;
            //返回按钮
            case R.id.health_record_back_icon:
                //弹出提示框
                new CommomDialog(HealthRecordDetailActivity.this, R.style.dialog, "您确定不保存此康复记录？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if(confirm){
                             HealthRecordDetailActivity.this.finish();
                        }
                        else
                        {
                            dialog.dismiss();
                        }

                    }
                }).setTitle("提示").show();
                break;
            //添加按钮
            case R.id.health_record_add:
                HealthRecordDetailActivity.this.finish();
                break;
                //删除按钮
            case R.id.health_record_delete:
                //弹出提示框
                new CommomDialog(HealthRecordDetailActivity.this, R.style.dialog, "您确定删除此康复记录？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if(confirm){
                            HealthRecordDetailActivity.this.finish();
                        }
                        else
                        {
                            dialog.dismiss();
                        }

                    }
                }).setTitle("提示").show();
                break;
        }
    }

    public TimePicker getTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        TimePicker picker = new TimePicker(this, TimePicker.HOUR_24);
        picker.setUseWeight(false);
        picker.setCycleDisable(false);
        picker.setRangeStart(0, 0);//00:00
        picker.setRangeEnd(23, 59);//23:59
        picker.setSelectedItem(currentHour, currentMinute);
        picker.setTopLineVisible(false);
        picker.setTextPadding(ConvertUtils.toPx(this, 15));
        return picker;
    }
}
