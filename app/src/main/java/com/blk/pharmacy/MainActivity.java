package com.blk.pharmacy;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.blk.R;
import com.blk.common.ToolBarSet;
import com.blk.fragment.CustomViewPager;
import com.blk.fragment.MyFragmentPagerAdapter;
import com.blk.pharmacy.fragment.pharmacy_homePageFragment;
import com.blk.pharmacy.fragment.pharmacy_meFragment;
import com.blk.pharmacy.fragment.pharmacy_shoppingFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,View.OnTouchListener{


    private RadioGroup bottom_bar_line;
    private RadioButton homePage;
    private RadioButton shopping;
    private RadioButton me;
    private CustomViewPager  viewPager;

    private List<Fragment> fragmentList;

    private MyFragmentPagerAdapter myFragmentPagerAdapter;



    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        Window window = getWindow();
        ToolBarSet.setBar(window);


        setContentView(R.layout.pharmacy_main);
        InitView();
        //初始化Radio按钮
        initRadio();

        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(myFragmentPagerAdapter);
        viewPager.setCurrentItem(0);
        homePage.setChecked(true);
       InitEvent();
    }





    @Override
    protected void onResume() {
//        int id = getIntent().getIntExtra("data",0);
//        switch(id)
//        {
//            case 1:
//                Fragment medical_record = new medicalRecord_Fragment();
//                FragmentManager fragmentManager1 = getSupportFragmentManager();
//                FragmentTransaction transaction1 = fragmentManager1.beginTransaction();
//                transaction1.replace(R.id.vpager,medical_record);
//                transaction1.commit();
//                viewPager.setCurrentItem(PAGE_TWO);
//                medicalRecord.setChecked(true);
//                break;
//            case 2:
//                Fragment healthy_tool = new healthyTool_Fragment();
//                FragmentManager fragmentManager2 = getSupportFragmentManager();
//                FragmentTransaction transaction2 = fragmentManager2.beginTransaction();
//                transaction2.replace(R.id.vpager,healthy_tool);
//                transaction2.commit();
//                viewPager.setCurrentItem(PAGE_THREE);
//                healthyTool.setChecked(true);
//                break;
//        }
        super.onResume();
    }

    private void InitEvent() {
        viewPager.setOnTouchListener(this);
        bottom_bar_line.setOnCheckedChangeListener(this);
    }

    private void InitView() {

        bottom_bar_line = (RadioGroup) findViewById(R.id.bottom_bar_line);
        homePage = (RadioButton) findViewById(R.id.pharmacy_homePage);
        shopping = (RadioButton) findViewById(R.id.pharmacy_shopping);
        me = (RadioButton) findViewById(R.id.pharmacy_me);
        viewPager= (CustomViewPager) findViewById(R.id.viewpager);

        fragmentList = new ArrayList<>();
        fragmentList.add(new pharmacy_homePageFragment());
        fragmentList.add(new pharmacy_shoppingFragment());
        fragmentList.add(new pharmacy_meFragment());


    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId)
        {
            case R.id.pharmacy_homePage:
                viewPager.setCurrentItem(PAGE_ONE,false);
                break;
            case R.id.pharmacy_shopping:
                viewPager.setCurrentItem(PAGE_TWO,false);
                break;
            case R.id.pharmacy_me:
                viewPager.setCurrentItem(PAGE_THREE,false);
                break;
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    private void initRadio() {

        //定义底部标签图片大小和位置
        Drawable home_page = getResources().getDrawable(R.drawable.home_page);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        home_page.setBounds(0, 0, 30, 30);
        //设置图片在文字的哪个方向
        homePage.setCompoundDrawables(null, home_page, null, null);

        //定义底部标签图片大小和位置
        Drawable pharmacy_shopping = getResources().getDrawable(R.drawable.medical_record);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        pharmacy_shopping.setBounds(0, 0, 30, 30);
        //设置图片在文字的哪个方向
        shopping.setCompoundDrawables(null, pharmacy_shopping, null, null);

        //定义底部标签图片大小和位置
        Drawable pharmacy_me = getResources().getDrawable(R.drawable.me);
        //当这个图片被绘制时，给他绑定一个矩形 ltrb规定这个矩形
        pharmacy_me.setBounds(0, 0, 30, 30);
        //设置图片在文字的哪个方向
        me.setCompoundDrawables(null, pharmacy_me, null, null);

    }

    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }


    }

}
