package com.blk.health_tool;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blk.R;
import com.blk.common.MyApplication;
import com.blk.common.ToolBarSet;
import com.blk.health_tool.dbHelper.AlarmDbHelper;
import com.blk.health_tool.Entity.AlarmInfo;
import com.loonggg.lib.alarmmanager.clock.AlarmManagerUtil;


import org.w3c.dom.Text;

import java.util.Calendar;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.NumberPicker;
import cn.qqtheme.framework.picker.TimePicker;
import cn.qqtheme.framework.util.ConvertUtils;


/**
 * Created by lzx on 2018/3/6.
 */

public class AlarmAddActivity extends AppCompatActivity implements View.OnClickListener {
    public int alarmId = -1;
    //alarm_add页面控件
    private EditText drugName;      //药名
    private EditText times;         //次数
    private EditText nums;        //粒数
    private EditText date;          //开始日期
    private EditText intervalTime;  //间隔时间
    private EditText time;         //开始时间
    private EditText days;        //持续日期
    private Button saveBtn;       //设置提醒
    private TextView delete;        //删除按钮
    private int currentYear;
    private int currentMonth;
    private int currentDay;
    private int currentHour;
    private int currentMinute;
    private AlarmInfo alarmInfo;
    private AlarmDbHelper alarmDbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_add);
        Window window = getWindow();
        ToolBarSet.setBar(window);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            alarmId = bundle.getInt("id");
            Log.i("TestTest","--- " + alarmId);
        }
        //初始化控件
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH) + 1;
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMinute = calendar.get(Calendar.MINUTE);
        drugName = (EditText) findViewById(R.id.drug_name_hint);
        //次数
        times = (EditText) findViewById(R.id.times);
        times.setText(String.valueOf(3));
        times.setOnClickListener(this);
        //粒数
        nums = (EditText) findViewById(R.id.nums);
        nums.setOnClickListener(this);
        //日期
        date = (EditText) findViewById(R.id.date);
        date.setText(currentYear + "-" + currentMonth + "-" + currentDay);
        date.setOnClickListener(this);
        //间隔时间
        intervalTime = (EditText) findViewById(R.id.interval_time);
        //默认4个小时
        intervalTime.setText(String.valueOf(4));
        intervalTime.setOnClickListener(this);
        //时间
        time = (EditText) findViewById(R.id.time);
        time.setText(currentHour + ":" + currentMinute);
        time.setOnClickListener(this);
        //持续天数
        days = (EditText) findViewById(R.id.days);
        days.setOnClickListener(this);
        //设置提醒按钮
        saveBtn = (Button) findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);
        alarmDbHelper = new AlarmDbHelper(this, "my.db", null, 1);
        //删除按钮
        delete = (TextView) findViewById(R.id.delete);
        if (alarmId >= 0){
            delete.setVisibility(View.VISIBLE);
            saveBtn.setText("更新闹钟信息");
            SQLiteDatabase db = alarmDbHelper.getReadableDatabase();
            alarmInfo = new AlarmInfo();
            Cursor cursor = db.query("alarm",null, "alarm_id = ?",new String[]{String.valueOf(alarmId)},null,null,null,null);
            while (cursor.moveToNext()){
                drugName.setText(cursor.getString(cursor.getColumnIndex("drug_name")));
                times.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("times"))));
                nums.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("nums"))));
                time.setText(cursor.getString(cursor.getColumnIndex("time")));
                intervalTime.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("interval_time"))));
                date.setText(cursor.getString(cursor.getColumnIndex("date")));
                days.setText(String.valueOf(cursor.getInt(cursor.getColumnIndex("days"))));
                break;
            }
        }
        delete.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //次数
            case R.id.times:
                NumberPicker timesPicker = getNumberPicker(1, 7, 1, "次数");
                timesPicker.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
                    @Override
                    public void onNumberPicked(int index, Number number) {
                        times.setFocusable(true);
                        times.setText(String.valueOf(number.intValue()));
                        times.setFocusable(false);
                    }
                });
                timesPicker.show();
                break;
            //用量
            case R.id.nums:
                NumberPicker numsPicker = getNumberPicker(1, 7, 1, "粒/瓶/ml");
                numsPicker.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
                    @Override
                    public void onNumberPicked(int i, Number number) {
                        nums.setFocusable(true);
                        nums.setText(String.valueOf(number.intValue()));
                        nums.setFocusable(false);
                    }
                });
                numsPicker.show();
                break;
            //日期
            case R.id.date:
                DatePicker datePicker = getDatePicker();
                datePicker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        date.setFocusable(true);
                        date.setText(year + "-" + month + "-" + day);
                        date.setFocusable(false);
                    }
                });
                datePicker.show();
                break;
            //间隔时间
            case R.id.interval_time:
                NumberPicker intervalPicker = getNumberPicker(1, 24, 1, "次数");
                intervalPicker.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
                    @Override
                    public void onNumberPicked(int index, Number number) {
                        intervalTime.setFocusable(true);
                        intervalTime.setText(String.valueOf(number.intValue()));
                        intervalTime.setFocusable(false);
                    }
                });
                intervalPicker.show();
                break;
            //时间
            case R.id.time:
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
            //天数
            case R.id.days:
                NumberPicker dayPicker = getNumberPicker(1, 31, 1, "天数");
                dayPicker.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
                    @Override
                    public void onNumberPicked(int i, Number number) {
                        days.setFocusable(true);
                        days.setText(String.valueOf(number.intValue()));
                        days.setFocusable(false);
                    }
                });
                dayPicker.show();
                break;
            //设置闹钟提醒
            case R.id.save_btn:
                setClock();
                break;
                //删除闹钟
            case R.id.delete:
                SQLiteDatabase db = alarmDbHelper.getWritableDatabase();
                db.delete("alarm","alarm_id = ?",new String[]{String.valueOf(alarmId)});
                showToast("删除闹钟成功");
                this.finish();
                break;
            default:
                break;
        }

    }

    /**
     * 设置闹钟提醒
     */
    private void setClock() {
        if (drugName.getText().toString() == null || "".equals(drugName.getText().toString())) {
            showToast("请输入药品名称");
            return;
        }
        if (nums.getText().toString() == null || "".equals(nums.getText().toString())) {
            showToast("请输入药品用量");
            return;
        }
        if (days.getText().toString() == null || "".equals(days.getText().toString())) {
            showToast("请输入周期天数");
            return;
        }

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                //闹钟信息
                alarmInfo = new AlarmInfo();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                //先删除操作
                if (alarmId >= 0){
                    SQLiteDatabase db = alarmDbHelper.getWritableDatabase();
                    db.delete("alarm","alarm_id = ?",new String[]{String.valueOf(alarmId)});
                    showToast("删除闹钟成功");
                }
                //药品名称
                alarmInfo.setDrugName(drugName.getText().toString());
                //次数
                alarmInfo.setTimes(Integer.parseInt(times.getText().toString()));
                //用量
                alarmInfo.setNums(Integer.parseInt(nums.getText().toString()));
                //时间
                alarmInfo.setTime(time.getText().toString());
                //间隔时间
                alarmInfo.setIntervalTime(Integer.parseInt(intervalTime.getText().toString()));
                //日期
                alarmInfo.setDate(date.getText().toString());
                //天数
                alarmInfo.setDays(Integer.valueOf(days.getText().toString()));
                Calendar calendar = Calendar.getInstance();
                String[] datesArray = date.getText().toString().split("-");
                String[] timeArray = time.getText().toString().split(":");
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                        (Calendar.DAY_OF_MONTH), Integer.parseInt(timeArray[0]), Integer.parseInt(timeArray[1]), 0);
                calendar.set(Integer.parseInt(datesArray[0]), Integer.parseInt(datesArray[1]) - 1, Integer.parseInt(datesArray[2]), Integer.parseInt(timeArray[0]), Integer.parseInt(timeArray[1]), 0);
                //利用SharedPreferences来存取id数据，保证id的唯一性，不存在id覆盖的问题
                SharedPreferences preferences = getSharedPreferences("clockData", MODE_PRIVATE);
                int id = preferences.getInt("id", 0);
                //获取数据库SQLiteDatabase
                SQLiteDatabase db = alarmDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("drug_name", alarmInfo.getDrugName());
                values.put("times", alarmInfo.getTimes());
                values.put("nums", alarmInfo.getNums());
                values.put("time", alarmInfo.getTime());
                values.put("interval_time", alarmInfo.getIntervalTime());
                values.put("date", alarmInfo.getDate());
                values.put("days", alarmInfo.getDays());
                values.put("alarm_id", id);
                //插入数据（保存当前设置的闹钟的第一个id,以便关闭操作）
                db.insert("alarm", null, values);
                for (int index = 0; index < alarmInfo.getDays(); index++) {
                    for (int j = 0; j < alarmInfo.getTimes(); j++) {
                        // soundOrVibrator(0:震动，1:铃声)
                        AlarmManagerUtil.setAlarm(MyApplication.getContext(), 0, calendar, id, 0, "主人，请按时吃药哦", 1, alarmInfo.getDrugName(), alarmInfo.getNums());
                        //一次性设置多个闹钟，id应该不同，不然后面的会覆盖前面的闹钟
                        id++;
                        Log.i("Main", "TestTest " + calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "  " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
                        if (calendar.get(Calendar.HOUR_OF_DAY) + alarmInfo.getIntervalTime() > 24 || (calendar.get(Calendar.HOUR_OF_DAY) + alarmInfo.getIntervalTime() == 24 && calendar.get(Calendar.MINUTE) > 0)) {
                            break;
                        }
                        //间隔时间
                        calendar.add(Calendar.HOUR_OF_DAY, alarmInfo.getIntervalTime());
                    }
                    calendar.set(Integer.parseInt(datesArray[0]), Integer.parseInt(datesArray[1]) - 1, Integer.parseInt(datesArray[2]), Integer.parseInt(timeArray[0]), Integer.parseInt(timeArray[1]));
                    //天数
                    calendar.add(Calendar.DAY_OF_MONTH, 1 * (index + 1));
                }
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("id", id);
                editor.commit();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Toast.makeText(AlarmAddActivity.this, "设置提醒成功", Toast.LENGTH_SHORT).show();
                AlarmAddActivity.this.finish();
            }
        }.execute();


    }

    private NumberPicker getNumberPicker(int startNumber, int endNumber, int step, String label) {
        NumberPicker picker = new NumberPicker(this);
        picker.setWidth(picker.getScreenWidthPixels());
        picker.setCycleDisable(false);
        picker.setDividerVisible(false);
        picker.setOffset(2);//偏移量
        picker.setRange(startNumber, endNumber, step);//数字范围
        picker.setSelectedItem((endNumber + startNumber) / 2);
        picker.setLabel(label);
        return picker;
    }

    private DatePicker getDatePicker() {
        DatePicker picker = new DatePicker(this);
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(ConvertUtils.toPx(this, 10));
        picker.setRangeStart(2018, 1, 1);
        picker.setRangeEnd(2022, 12, 31);
        picker.setSelectedItem(currentYear, currentMonth, currentDay);
        picker.setResetWhileWheel(false);
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        return picker;
    }

    public TimePicker getTimePicker() {
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

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
