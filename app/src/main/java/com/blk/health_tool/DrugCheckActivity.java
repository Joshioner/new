package com.blk.health_tool;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blk.MainActivity;
import com.blk.R;
import com.blk.common.util.HttpCallbackListener;
import com.blk.common.util.HttpSendUtil;
import com.blk.common.ToolBarSet;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lzx on 2018/2/28.
 */

public class DrugCheckActivity extends AppCompatActivity implements View.OnClickListener {
    private final int SHOW_MEDICAL_RECORD = 1;
    private ImageView medicine_search_back;
    private ImageView medicine_search_camera;
    private LinearLayout medicine_search;
    private EditText drug_edittext;
    private String drug_id;
    private RelativeLayout relativeLayout2;
    private LinearLayout linearLayout3;
    private TextView drug_name;
    private TextView drug_cnumber;
    private TextView drug_specification;
    private TextView drug_storageInfo;
    private TextView drug_usageInfo;
    private TextView drug_composition;
    private TextView drug_indication;
    private TextView drug_adverse;
    private TextView drug_contraindication;
    private TextView drug_notice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drug_check);
        Window window = getWindow();
        ToolBarSet.setBar(window);

        initView();
        initEvent();
    }

    private void initView() {
        medicine_search_back = (ImageView)findViewById(R.id.medicine_search_back);
        medicine_search_camera = (ImageView)findViewById(R.id.medicine_search_camera);
        medicine_search = (LinearLayout)findViewById(R.id.medicine_search);
        drug_edittext = (EditText)findViewById(R.id.drug_edittext);
        relativeLayout2 = (RelativeLayout)findViewById(R.id.relative2);
        linearLayout3 = (LinearLayout)findViewById(R.id.linear2);
        relativeLayout2.setVisibility(View.VISIBLE);
        linearLayout3.setVisibility(View.GONE);

        drug_name = (TextView)findViewById(R.id.drug_name);
        drug_cnumber = (TextView)findViewById(R.id.drug_cnumber);
        drug_specification = (TextView)findViewById(R.id.drug_specification);
        drug_storageInfo = (TextView)findViewById(R.id.drug_storageInfo);
        drug_usageInfo = (TextView)findViewById(R.id.drug_usageInfo);
        drug_composition = (TextView)findViewById(R.id.drug_composition);
        drug_indication = (TextView)findViewById(R.id.drug_indication);
        drug_adverse = (TextView)findViewById(R.id.drug_adverse);
        drug_contraindication = (TextView)findViewById(R.id.drug_contraindication);
        drug_notice = (TextView)findViewById(R.id.drug_notice);
    }

    private void initEvent() {
        medicine_search_back.setOnClickListener(this);
        medicine_search_camera.setOnClickListener(this);
        medicine_search.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.medicine_search:
                drug_id = drug_edittext.getText().toString();
                if (drug_id.equals("")){
                    Toast.makeText(this,"国药准字不能为空，请重新输入",Toast.LENGTH_SHORT).show();
                }else {
                    AddDrugDetailList(drug_id);
                }
                break;
            case R.id.medicine_search_back:
                Intent intent = new Intent(DrugCheckActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("data", 2);
                intent.putExtras(bundle);
                DrugCheckActivity.this.finish();
                startActivity(intent);
                break;
            case R.id.medicine_search_camera:
                break;
        }
    }
    private void AddDrugDetailList(String drug_id) {
        String address = "http://47.95.246.177:8080/gdufs_blk_ssh/drug_findByCnumber";
        String data = "number="+drug_id;
        HttpSendUtil.sendHttpRequest(address,data,new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                // Toast.makeText(medical_record_detail.this, "6666", Toast.LENGTH_SHORT).show();
                Message message = new Message();
                message.what = SHOW_MEDICAL_RECORD;
                message.obj = response;
                handler.sendMessage(message);
            }
            @Override
            public void onError(Exception e) {
            }
        });
    }
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_MEDICAL_RECORD:
                    String result = (String) msg.obj;
                    onPostExecute(result);
                    break;
            }
            super.handleMessage(msg);
        }
    };
    protected void onPostExecute(String result) {
        if (result != null) {
            // Log.i("MainActivity","111"+result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                //药品名称
                String dname = jsonObject.getString("drug.dname");
                //国药准字
                String cnumber  = jsonObject.getString("drug.cnumber");
                //规格
                String specification = jsonObject.getString("specification");
                //贮藏
                String storageInfo = jsonObject.getString("storageInfo");
                //用法用量
                String usageInfo = jsonObject.getString("usageInfo");
                //成分
                String composition = jsonObject.getString("composition");
                //适应症
                String indication = jsonObject.getString("indication");
                //不良反应
                String adverse = jsonObject.getString("adverse");
                //禁忌
                String contraindication = jsonObject.getString("contraindication");
                //注意事项
                String notice = jsonObject.getString("notice");
                relativeLayout2.setVisibility(View.GONE);
                linearLayout3.setVisibility(View.VISIBLE);
                drug_name.setText(dname);
                drug_cnumber.setText(cnumber);
                drug_specification.setText(specification);
                drug_storageInfo.setText(storageInfo);
                drug_usageInfo.setText(usageInfo);
                drug_composition.setText(composition);
                drug_indication.setText(indication);
                drug_adverse.setText(adverse);
                drug_contraindication.setText(contraindication);
                drug_notice.setText(notice);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {

        }

    }
}
