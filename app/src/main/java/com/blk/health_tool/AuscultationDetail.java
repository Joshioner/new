package com.blk.health_tool;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.blk.R;
import com.blk.common.ToolBarSet;
import com.blk.health_tool.Adapter.AuscultationDetailListBaseAdapter;
import com.blk.health_tool.Entity.auscultation_detail_list;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzx on 2018/3/28.
 */

public class AuscultationDetail extends AppCompatActivity implements View.OnClickListener {
    private ListView auscultation_detail_listview;
    private List<auscultation_detail_list>  auscultation_detail_list;
    private AuscultationDetailListBaseAdapter auscultationListAdapter;
    private TextView goto_auscultation;
    private ImageView auscultation_detail_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auscultation_detail);
        Window window = getWindow();
        ToolBarSet.setBar(window);
        initview();
        initEvent();
    }

    private void initview() {
        auscultation_detail_listview = (ListView)findViewById(R.id.auscultation_detail_listview);
        auscultation_detail_list = new ArrayList<auscultation_detail_list>();
        AddAuscultation();
        auscultationListAdapter = new AuscultationDetailListBaseAdapter(AuscultationDetail.this,auscultation_detail_list);
        auscultation_detail_listview.setAdapter(auscultationListAdapter);
        goto_auscultation = (TextView)findViewById(R.id.goto_auscultation);
        auscultation_detail_back = (ImageView) findViewById(R.id.auscultation_detail_back);

    }


    private void initEvent() {
        goto_auscultation.setOnClickListener(this);
        auscultation_detail_back.setOnClickListener(this);
    }

    private void AddAuscultation() {
        auscultation_detail_list auscultation_list = new auscultation_detail_list();
        auscultation_list.setAuscultation_message("早睡早起，身体棒！");
        auscultation_list.setAuscultation_date("2018-03-28");
        auscultation_list.setAuscultation_time("14:13");
        auscultation_detail_list.add(auscultation_list);
        auscultation_detail_list.add(auscultation_list);
}

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent ;
        switch (id)
        {
            case R.id.goto_auscultation:
                intent = new Intent(this,MedicalAuscultationActivity.class);
                startActivity(intent);
                break;
            case R.id.auscultation_detail_back:
                this.finish();
                break;
        }
    }
}
