package com.blk.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.blk.LoginActivity;
import com.blk.R;

/**
 * Created by asus on 2017/8/9.
 */

public class me_Fragment extends Fragment implements View.OnClickListener {
    private View view ;
    private RelativeLayout logoutRelativeLayout;
    public me_Fragment(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       if (view == null){
           view = inflater.inflate(R.layout.me,container,false);
       }else {
           ViewGroup parent = (ViewGroup) view.getParent();
             if(parent != null) {
               parent.removeView(view);
               }
       }
       //初始化控件
       initView();
        return view;
   //     return inflater.inflate(R.layout.view5, container, false);
    }

    private void initView(){
        logoutRelativeLayout = (RelativeLayout) view.findViewById(R.id.logoutRelativeLayout);
        logoutRelativeLayout.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.logoutRelativeLayout:
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
