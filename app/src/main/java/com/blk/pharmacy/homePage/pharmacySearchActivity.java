package com.blk.pharmacy.homePage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.blk.R;
import com.blk.common.ToolBarSet;

public class pharmacySearchActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView drug_search;  //药物搜索按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        ToolBarSet.setBar(window);
        setContentView(R.layout.pharmacy_search);

        //初始化控件
        InitView();
        //事件
       InitEvent();
    }

    private void InitEvent() {
        drug_search.setOnClickListener(this);
    }

    private void InitView() {
        drug_search = (ImageView) findViewById(R.id.drug_search);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent ;
        switch (id)
        {
            case R.id.drug_search:
                intent = new Intent(pharmacySearchActivity.this,DrugListSearchActivity.class);
                startActivity(intent);
                break;
        }
    }
}
