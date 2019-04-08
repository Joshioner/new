package com.blk.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.blk.R;
import com.blk.health_tool.DrugCheckActivity;
import com.blk.health_tool.AlarmAssistantActivity;
import com.blk.health_tool.AuscultationListActivity;
import com.blk.health_tool.HealthRecordActivity;
import com.blk.health_tool.HealthyNewsActivity;


/**
 * Created by asus on 2017/8/9.
 */

public class healthyTool_Fragment extends Fragment {

    private View view;
    private RelativeLayout drugCheck_right;
    private RelativeLayout auscultation_right;
    private RelativeLayout alarm_assistant_right;
    private RelativeLayout healthy_news;
    private RelativeLayout health_record;

    public healthyTool_Fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.healthy_tool, container, false);
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        initView();
        initEvent();

        return view;
    }

    private void initView() {
        drugCheck_right = (RelativeLayout) view.findViewById(R.id.drugCheck_right);
        auscultation_right = (RelativeLayout) view.findViewById(R.id.auscultation_right);
        alarm_assistant_right = (RelativeLayout) view.findViewById(R.id.alarm_assistant_right);
        healthy_news = (RelativeLayout) view.findViewById(R.id.healthy_news);
        health_record = (RelativeLayout) view.findViewById(R.id.health_record);
    }


    private void initEvent() {
        //药物查询
        drugCheck_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DrugCheckActivity.class);
                startActivity(intent);
            }
        });
        //就医听诊
        auscultation_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AuscultationListActivity.class);
                startActivity(intent);
            }
        });
        //闹钟助手
        alarm_assistant_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AlarmAssistantActivity.class);
                startActivity(intent);
            }
        });
        //健康资讯
        healthy_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HealthyNewsActivity.class);
                startActivity(intent);
            }
        });
        //康复记录表
        health_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HealthRecordActivity.class);
                startActivity(intent);
            }
        });

    }


}
