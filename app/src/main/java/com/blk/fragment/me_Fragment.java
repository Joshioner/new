package com.blk.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.blk.LoginActivity;
import com.blk.R;
import com.blk.common.CommomDialog;
import com.blk.medical_record.MemberManageActivity;

/**
 * Created by asus on 2017/8/9.
 */

public class me_Fragment extends Fragment implements View.OnClickListener {
    private View view ;
    private RelativeLayout logoutRelativeLayout,memberRelativeLayout;
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
        memberRelativeLayout = (RelativeLayout) view.findViewById(R.id.member_relativeLayout);
        memberRelativeLayout.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //退出登录
            case R.id.logoutRelativeLayout:
                //弹出提示框
                new CommomDialog(getActivity(), R.style.dialog, "您确定退出登录么？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if(confirm){
                            //清除登录信息
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.commit();
                            Intent intent = new Intent(getActivity(),LoginActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            dialog.dismiss();
                        }
                    }
                }).setTitle("提示").show();
                break;
             //家庭组管理
            case R.id.member_relativeLayout:
                Intent intent = new Intent(getActivity(),MemberManageActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
