package com.blk.test;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.blk.R;

/**
 * 作者：chs on 2016/9/6 16:07
 * 邮箱：657083984@qq.com
 */
public class test extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        LineChart lineChart = (LineChart) findViewById(R.id.chart);
//        List<ChartEntity> data = new ArrayList<>();
//        for(int i =0;i<10;i++){
//            data.add(new ChartEntity(String.valueOf(i), (float) (Math.random()*1000)));
//        }
//        lineChart.setData(data);
//        lineChart.startAnimation(2000);
    }
}
