package com.blk.medical_record;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.blk.R;
import com.blk.common.ToolBarSet;

public class testActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        ToolBarSet.setBar(window);
        setContentView(R.layout.test);

    }





}
