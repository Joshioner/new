package com.blk;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.blk.common.ToolBarSet;
import com.blk.fragment.CustomViewPager;
import com.blk.fragment.MyFragmentPagerAdapter;
import com.blk.fragment.healthyTool_Fragment;
import com.blk.fragment.homePage_Fragment;
import com.blk.fragment.me_Fragment;
import com.blk.fragment.medicalRecord_Fragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,View.OnTouchListener{


    private RadioGroup botton_bar;
    private RadioButton homePage;
    private RadioButton medicalRecord;
    private RadioButton healthyTool;
    private RadioButton me;
    private CustomViewPager  viewPager;

    private List<Fragment> fragmentList;

    private MyFragmentPagerAdapter myFragmentPagerAdapter;



    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        ToolBarSet.setBar(window);


        setContentView(R.layout.activity_main);
        //初始化Radio按钮
        initRadio();

        InitView();

        myFragmentPagerAdapter=new MyFragmentPagerAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(myFragmentPagerAdapter);
        viewPager.setCurrentItem(0);
        homePage.setChecked(true);
        InitEvent();
    }





    @Override
    protected void onResume() {
        int id = getIntent().getIntExtra("data",0);
        switch(id)
        {
            case 1:
                  Fragment medical_record = new medicalRecord_Fragment();
                  FragmentManager fragmentManager1 = getSupportFragmentManager();
                  FragmentTransaction transaction1 = fragmentManager1.beginTransaction();
                  transaction1.replace(R.id.vpager,medical_record);
                  transaction1.commit();
                  viewPager.setCurrentItem(PAGE_TWO);
                  medicalRecord.setChecked(true);
                 break;
            case 2:
                Fragment healthy_tool = new healthyTool_Fragment();
                FragmentManager fragmentManager2 = getSupportFragmentManager();
                FragmentTransaction transaction2 = fragmentManager2.beginTransaction();
                transaction2.replace(R.id.vpager,healthy_tool);
                transaction2.commit();
                viewPager.setCurrentItem(PAGE_THREE);
                healthyTool.setChecked(true);
                break;
        }
        super.onResume();
    }

    private void InitEvent() {
        viewPager.setOnTouchListener(this);
        botton_bar.setOnCheckedChangeListener(this);
    }

    private void InitView() {

        botton_bar = (RadioGroup) findViewById(R.id.bottom_bar);
        homePage = (RadioButton) findViewById(R.id.homePage);
        medicalRecord = (RadioButton) findViewById(R.id.medicalRecord);
        healthyTool = (RadioButton) findViewById(R.id.healthyTool);
        me = (RadioButton) findViewById(R.id.me);
        viewPager= (CustomViewPager) findViewById(R.id.vpager);

        fragmentList = new ArrayList<>();
        fragmentList.add(new homePage_Fragment());
        fragmentList.add(new medicalRecord_Fragment());
        fragmentList.add(new healthyTool_Fragment());
        fragmentList.add(new me_Fragment());


    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId)
        {
            case R.id.homePage:
                viewPager.setCurrentItem(PAGE_ONE,false);
                break;
            case R.id.medicalRecord:
                viewPager.setCurrentItem(PAGE_TWO,false);
                break;
            case R.id.healthyTool:
                viewPager.setCurrentItem(PAGE_THREE,false);
                break;
            case R.id.me:
                viewPager.setCurrentItem(PAGE_FOUR,false);
                break;
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    private void initRadio() {
        RadioButton homePage = (RadioButton) findViewById(R.id.homePage);
        RadioButton medicalRecord = (RadioButton) findViewById(R.id.medicalRecord);
        RadioButton healthyTool = (RadioButton) findViewById(R.id.healthyTool);
        RadioButton mePage = (RadioButton) findViewById(R.id.me);

        //定义底部标签图片大小和位置
        Drawable home_page = getResources().getDrawable(R.drawable.home_page);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        home_page.setBounds(0, 0, 30, 30);
        //设置图片在文字的哪个方向
        homePage.setCompoundDrawables(null, home_page, null, null);

        //定义底部标签图片大小和位置
        Drawable medical_record = getResources().getDrawable(R.drawable.medical_record);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        medical_record.setBounds(0, 0, 30, 30);
        //设置图片在文字的哪个方向
        medicalRecord.setCompoundDrawables(null, medical_record, null, null);

        //定义底部标签图片大小和位置
        Drawable healthy_tool = getResources().getDrawable(R.drawable.healthy_tool);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        healthy_tool.setBounds(0, 0, 30, 30);
        //设置图片在文字的哪个方向
        healthyTool.setCompoundDrawables(null, healthy_tool, null, null);

        //定义底部标签图片大小和位置
        Drawable me = getResources().getDrawable(R.drawable.me);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        me.setBounds(0, 0, 30, 30);
        //设置图片在文字的哪个方向
        mePage.setCompoundDrawables(null, me, null, null);
    }


}
