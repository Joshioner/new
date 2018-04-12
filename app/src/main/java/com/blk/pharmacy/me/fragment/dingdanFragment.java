package com.blk.pharmacy.me.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blk.R;

public class dingdanFragment extends Fragment {

    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.pharmacy_dingdan,container,false);
//        //初始化控件
//        initView();
//        //事件
//        initEvent();
        return view;
    }
}
