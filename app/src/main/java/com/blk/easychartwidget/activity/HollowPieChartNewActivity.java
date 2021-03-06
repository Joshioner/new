package com.blk.easychartwidget.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.blk.R;
import com.blk.easychartwidget.entity.PieDataEntity;
import com.blk.easychartwidget.widget.HollowPieNewChart;

import java.util.ArrayList;
import java.util.List;

public class HollowPieChartNewActivity extends AppCompatActivity {
    private int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hollow_pie_chart_new);
        HollowPieNewChart pieChart = (HollowPieNewChart) findViewById(R.id.chart);
        List<PieDataEntity> dataEntities = new ArrayList<>();
        for(int i = 0;i<9;i++){
            PieDataEntity entity = new PieDataEntity("name"+i,i+1,mColors[i]);
            dataEntities.add(entity);
        }
        pieChart.setDataList(dataEntities);
        pieChart.setOnItemPieClickListener(new HollowPieNewChart.OnItemPieClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(HollowPieChartNewActivity.this,"点击了"+position,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
