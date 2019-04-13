package com.blk.medical_record;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blk.MainActivity;
import com.blk.R;
import com.blk.common.CommomDialog;
import com.blk.common.ToolBarSet;
import com.blk.common.entity.CaseHistory;
import com.blk.common.entity.User;
import com.blk.common.util.AlterUtil;
import com.blk.common.util.ConfigUtil;
import com.blk.common.util.HttpCallbackListener;
import com.blk.common.util.HttpRequestUtil;
import com.blk.common.util.HttpSendUtil;
import com.blk.common.util.WeiboDialogUtils;
import com.blk.fragment.medicalRecord_Fragment;
import com.blk.medical_record.Adapter.CaseHistoryDetailBaseAdapter;
import com.blk.medical_record.Adapter.SearchContentBaseAdapter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class medicalRecordSearchActivity extends Activity  implements View.OnClickListener{
     private ImageView icon_back;         //返回按钮
     private ImageView icon_search;      //指定搜索内容医生
     private View search_content_view;   //搜索内容的popupwindow
//     private ListView search_content_listView;  //搜索内容的ListView
     private TextView search_content_title;        //搜索病历的标题
     private SearchContentBaseAdapter searchContentBaseAdapter;  //适配器，装的是返回给listView的数据
     private PopupWindow search_content_popupWindow;   //点击对应的搜索内容之后显示的对话框
     private EditText searchContent;   //搜索的内容
    private SharedPreferences sharedPreferences;
    private User user;  //用户信息
    private ListView medicalListView;    //显示病历详细信息的ListView
    private List<CaseHistory> caseHistoryList;  //病历信息的列表
    private CaseHistory CaseHistory ;   //病历详细信息
    private CaseHistoryDetailBaseAdapter detail_baseAdapter; //传递给medicalListView的适配器
    public  static Dialog weiboDialogUtils;
    private int fid = 0;
    private final int Init_MedicalHistory_Adapter = 1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Init_MedicalHistory_Adapter:
                    //加载适配器并关闭加载框
                    medicalListView.setAdapter(detail_baseAdapter);//将适配器传递给medicalListView，类似于填充数据
                    if (weiboDialogUtils != null){
                        WeiboDialogUtils.closeDialog(weiboDialogUtils);
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //获取sharedPreferences中的userInfo信息
        sharedPreferences =  getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        String userInfo = sharedPreferences.getString("userInfo",null);
        user = com.alibaba.fastjson.JSONObject.parseObject(userInfo,User.class);
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        ToolBarSet.setBar(window);
        setContentView(R.layout.medical_record_search);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        //注册广播信息
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.refreshCaseHistory");
        registerReceiver(refreshBroadcastReceiver,intentFilter);
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null){
            fid = bundle.getInt("fid",0);
        }
        //初始化控件
        InitView();

    }

    //广播
    private BroadcastReceiver refreshBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //加载病历信息
            new CaseHistoryThread().execute(user.getUid(),fid);
        }
    };
    //初始化控件
    private void InitView()
    {
        icon_back = (ImageView) findViewById(R.id.icon_more);
        icon_back.setOnClickListener(this);
        icon_search = (ImageView) findViewById(R.id.icon_search);
        icon_search.setOnClickListener(this);
     //   search_content_view =  LayoutInflater.from(medicalRecordSearchActivity.this).inflate(R.layout.medical_record_search_content_popupwindow,null);
        medicalListView = (ListView) findViewById(R.id.medical_record_detail_list);
        //病历信息列表的点击
        medicalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView cid = (TextView) view.findViewById(R.id.list_id);
                Intent medicalRecordContentIntent = new Intent(medicalRecordSearchActivity.this,medicalRecordContentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("data",2);
                bundle.putInt("cid",Integer.valueOf(cid.getText().toString()));
                medicalRecordContentIntent.putExtras(bundle);
                startActivity(medicalRecordContentIntent);
            }
        });
        searchContent = (EditText) findViewById(R.id.search_content);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.icon_more:
                //返回按钮
                new CommomDialog(medicalRecordSearchActivity.this, R.style.dialog, "您确定退出，不搜索么？", new CommomDialog.OnCloseListener() {
                            @Override
                            public void onClick(Dialog dialog, boolean confirm) {
                                //不搜索，确认退出
                                if (confirm) {
                                    dialog.dismiss();
                                    Intent confirmIntent = new Intent(medicalRecordSearchActivity.this, MainActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("data", 1);
                                    confirmIntent.putExtras(bundle);
                                    medicalRecordSearchActivity.this.finish();
                                    startActivity(confirmIntent);
                                }
                                //取消退出
                                else {
                                    dialog.dismiss();
                                }

                            }
                        }).setTitle("提示").show();

                break;
            case R.id.icon_search:
                String content = searchContent.getText().toString();
                if (content == null || content == ""){
                    AlterUtil.alterTextLong(medicalRecordSearchActivity.this,"搜素内容为空");
                }else {
                  new CaseHistoryThread().execute(user.getUid(),fid);
                }
                break;
//            case R.id.doctor:
//                AlertDialog.Builder builder = new AlertDialog.Builder(medicalRecordSearchActivity.this);
//                View dialogView = View.inflate(medicalRecordSearchActivity.this,R.layout.medical_record_search_content_popupwindow,null);
//               search_content_listView = (ListView) dialogView.findViewById(R.id.medial_record_search_content_list);
//                List<String> arrayList = new ArrayList<String>();
//                arrayList.add("张蕴芳");
//                arrayList.add("刘孝义");
//                arrayList.add("杨艳红");
//                searchContentBaseAdapter = new SearchContentBaseAdapter(medicalRecordSearchActivity.this,arrayList);
//                //使用适配器
//                search_content_listView.setAdapter(searchContentBaseAdapter);
//                builder.setView(dialogView);
//                final  AlertDialog alterDialog = builder.create();
//                alterDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_round_white);
//                    /*
//                   * 将对话框的大小按屏幕大小的百分比设置
//                       */
//
////                  WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
////                 p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
////                 p.width = (int) (d.getWidth() * 0.65); // 宽度设置为屏幕的0.65
////                 dialogWindow.setAttributes(p);
//                alterDialog.show();
//                //设置大小
//                WindowManager m = getWindowManager();
//                Display display = m.getDefaultDisplay(); // 获取屏幕宽、高用
//                WindowManager.LayoutParams layoutParams = alterDialog.getWindow().getAttributes();
//                layoutParams.width = (int) (display.getWidth()  * 0.8);
//                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
//                layoutParams.y = -(int) (display.getHeight() * 0.1);   //设置位置
//                layoutParams.alpha = 0.8f;    //设置透明度
//                alterDialog.getWindow().setAttributes(layoutParams);
//                search_content_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Intent intent = new Intent(medicalRecordSearchActivity.this,MainActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putInt("data",1);
//                        intent.putExtras(bundle);
//                        medicalRecordSearchActivity.this.finish();
//                        startActivity(intent);
//                    }
//                });
//                break;
        }
    }

    //加载病历信息列表
    class CaseHistoryThread extends AsyncTask<Integer,Void,Void> {
        @Override
        protected void onPreExecute() {
            caseHistoryList = new ArrayList<CaseHistory>();
            if (weiboDialogUtils == null){
                weiboDialogUtils = WeiboDialogUtils.createLoadingDialog( medicalRecordSearchActivity.this,"加载中...");
            }
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            Message message = Message.obtain();
            message.what = Init_MedicalHistory_Adapter;
            String address = null;
            try {
                address = ConfigUtil.getServerAddress() + "/caseHistory/getAllByCondition/" + integers[0] +"/" + integers[1] + "?query=" + URLEncoder.encode( searchContent.getText().toString(),"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            HttpRequestUtil.sendHttpRequest(address,new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(response);
                    int code = jsonObject.getIntValue("code");
                    if (code == 0){
                        String data = jsonObject.getString("data");
                        JSONArray jsonArray = JSONArray.parseArray(data);
                        if (jsonArray.size() > 0){
                            for (int i = 0; i < jsonArray.size();i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                CaseHistory caseHistory = new CaseHistory();
                                caseHistory.setCid(object.getIntValue("cid"));
                                caseHistory.setUid(object.getIntValue("uid"));
                                caseHistory.setVisitDate(object.getString("visitDate"));
                                caseHistory.setHospitalName(object.getString("hospitalName"));
                                caseHistory.setPhoto(object.getString("photo"));
                                caseHistory.setOffice(object.getString("office"));
                                caseHistoryList.add(caseHistory);

                            }
                        }else {
                            AlterUtil.alterTextLong(medicalRecordSearchActivity.this,"查询结果为空");
                        }
                        detail_baseAdapter = new CaseHistoryDetailBaseAdapter(medicalRecordSearchActivity.this,caseHistoryList);//初始化适配器
                        handler.sendMessage(message);  //发送handle消息
                    }else {
                        Looper.prepare();
                        AlterUtil.alterTextLong(medicalRecordSearchActivity.this,"获取病历列表详情失败");
                        Looper.loop();
                        WeiboDialogUtils.closeDialog(weiboDialogUtils);
                    }
                }

                @Override
                public void onError(Exception e) {
                    WeiboDialogUtils.closeDialog(weiboDialogUtils);
                }

            });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }
}
