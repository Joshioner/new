package com.blk;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.blk.common.TimePickerView;
import com.blk.common.ToolBarSet;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private RadioButton maleRadio,femaleRadio;  //男、女单选框
    private EditText personPhoto,birthday;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        ToolBarSet.setBar(window);
        setContentView(R.layout.register);

        //初始化控件
        initView();
        //初始化点击事件
        initEvent();
        //初始化单选按钮控件
        initRadio();
    }

    private void initRadio() {
        RadioButton male = (RadioButton) findViewById(R.id.male);
        RadioButton female = (RadioButton) findViewById(R.id.female);

        //定义底部标签图片大小和位置
        Drawable male_image = getResources().getDrawable(R.drawable.register_sex_male);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        male_image.setBounds(0, 0, 50, 50);
        //设置图片在文字的哪个方向
        male.setCompoundDrawables(male_image, null, null, null);

        //定义底部标签图片大小和位置
        Drawable female_image = getResources().getDrawable(R.drawable.register_sex_female);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        female_image.setBounds(0, 0, 50, 50);
        //设置图片在文字的哪个方向
        female.setCompoundDrawables(female_image, null, null, null);
    }

    /**
     * 初始化控件
     */
    public void initView(){
        maleRadio = (RadioButton) findViewById(R.id.male);
        femaleRadio = (RadioButton) findViewById(R.id.female);
        personPhoto = (EditText) findViewById(R.id.person_photo_hint);
        birthday = (EditText) findViewById(R.id.birthday_text_hint);

    }

    /**
     * 初始化点击事件
     */
    public void initEvent(){
       birthday.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.birthday_text_hint:
                TimePickerView pvTime = new TimePickerView.Builder(RegisterActivity.this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        String time = getTime(date);
                        Toast.makeText(RegisterActivity.this,time,Toast.LENGTH_LONG).show();
                        birthday.setFocusable(true);
                        birthday.setText(time);
                        birthday.setFocusable(false);
                    }
                })
                        .setType(TimePickerView.Type.YEAR_MONTH_DAY)//默认全部显示
                        .setCancelText("取消")//取消按钮文字
                        .setSubmitText("确定")//确认按钮文字
                        .setContentSize(20)//滚轮文字大小
                        .setTitleSize(20)//标题文字大小
                        .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                        .isCyclic(true)//是否循环滚动
                        .setTextColorCenter(Color.BLACK)//设置选中项的颜色
                        .setTitleColor(Color.BLACK)//标题文字颜色
                        .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                        .setCancelColor(Color.BLUE)//取消按钮文字颜色
                        .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                        .isDialog(true)//是否显示为对话框样式
                        .build();
                pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
                pvTime.show();
                break;
             default:
                 break;
        }
    }

    public String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
}
