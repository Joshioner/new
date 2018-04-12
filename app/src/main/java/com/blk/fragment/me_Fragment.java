package com.blk.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blk.R;

/**
 * Created by asus on 2017/8/9.
 */

public class me_Fragment extends Fragment {
    private View view ;

    public me_Fragment(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.me,container,false);
        return view;
   //     return inflater.inflate(R.layout.view5, container, false);
    }


}
