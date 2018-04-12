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
import com.blk.health_tool.alarm_assistant;
import com.blk.health_tool.auscultation_detail;


/**
 * Created by asus on 2017/8/9.
 */

public class healthyTool_Fragment extends Fragment {

    private View view ;
    private RelativeLayout drugCheck_right;
    private RelativeLayout auscultation_right;
    private RelativeLayout alarm_assistant_right;

    public healthyTool_Fragment(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.healthy_tool,container,false);

        initView();
        initEvent();

        return view;
    }
    private void initView() {
        drugCheck_right = (RelativeLayout) view.findViewById(R.id.drugCheck_right);
        auscultation_right = (RelativeLayout) view.findViewById(R.id.auscultation_right);
        alarm_assistant_right = (RelativeLayout) view.findViewById(R.id.alarm_assistant_right);

    }
    private void initEvent() {
        drugCheck_right.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DrugCheckActivity.class);
                startActivity(intent);
            }
        });
        auscultation_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), auscultation_detail.class);
                startActivity(intent);
            }
        });
        alarm_assistant_right.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), alarm_assistant.class);
                startActivity(intent);
            }
        });


    }



}
