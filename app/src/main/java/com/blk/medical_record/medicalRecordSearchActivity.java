package com.blk.medical_record;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blk.MainActivity;
import com.blk.R;
import com.blk.common.CommomDialog;
import com.blk.common.ToolBarSet;
import com.blk.medical_record.Adapter.SearchContentBaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class medicalRecordSearchActivity extends Activity  implements View.OnClickListener{
     private ImageView icon_back;         //返回按钮
     private ImageView doctor_search;      //指定搜索内容医生
     private View search_content_view;   //搜索内容的popupwindow
     private ListView search_content_listView;  //搜索内容的ListView
     private TextView search_content_title;        //搜索病历的标题
     private SearchContentBaseAdapter searchContentBaseAdapter;  //适配器，装的是返回给listView的数据
     private PopupWindow search_content_popupWindow;   //点击对应的搜索内容之后显示的对话框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        ToolBarSet.setBar(window);
        setContentView(R.layout.medical_record_search);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        //初始化控件
        InitView();
        //事件
        InitEvent();

    }

    //初始化控件
    private void InitView()
    {
        icon_back = (ImageView) findViewById(R.id.icon_more);
        doctor_search = (ImageView) findViewById(R.id.doctor);
     //   search_content_view =  LayoutInflater.from(medicalRecordSearchActivity.this).inflate(R.layout.medical_record_search_content_popupwindow,null);
    //    search_content_listView = (ListView) search_content_view.findViewById(R.id.medial_record_search_content_list);
    }

    //事件
    private void InitEvent()
    {
        icon_back.setOnClickListener(this);
        doctor_search.setOnClickListener(this);



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
            case R.id.doctor:
                AlertDialog.Builder builder = new AlertDialog.Builder(medicalRecordSearchActivity.this);
                View dialogView = View.inflate(medicalRecordSearchActivity.this,R.layout.medical_record_search_content_popupwindow,null);
               search_content_listView = (ListView) dialogView.findViewById(R.id.medial_record_search_content_list);
                List<String> arrayList = new ArrayList<String>();
                arrayList.add("张蕴芳");
                arrayList.add("刘孝义");
                arrayList.add("杨艳红");
                searchContentBaseAdapter = new SearchContentBaseAdapter(medicalRecordSearchActivity.this,arrayList);
                //使用适配器
                search_content_listView.setAdapter(searchContentBaseAdapter);
                builder.setView(dialogView);
                final  AlertDialog alterDialog = builder.create();
                alterDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_round_white);
                    /*
                   * 将对话框的大小按屏幕大小的百分比设置
                       */

//                  WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//                 p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
//                 p.width = (int) (d.getWidth() * 0.65); // 宽度设置为屏幕的0.65
//                 dialogWindow.setAttributes(p);
                alterDialog.show();
                //设置大小
                WindowManager m = getWindowManager();
                Display display = m.getDefaultDisplay(); // 获取屏幕宽、高用
                WindowManager.LayoutParams layoutParams = alterDialog.getWindow().getAttributes();
                layoutParams.width = (int) (display.getWidth()  * 0.8);
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                layoutParams.y = -(int) (display.getHeight() * 0.1);   //设置位置
                layoutParams.alpha = 0.8f;    //设置透明度
                alterDialog.getWindow().setAttributes(layoutParams);
                search_content_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(medicalRecordSearchActivity.this,MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("data",1);
                        intent.putExtras(bundle);
                        medicalRecordSearchActivity.this.finish();
                        startActivity(intent);
                    }
                });
                break;
        }
    }
//    //初始化搜索内容弹出框
//    private void InitSearchContentPopupWindow()
//    {
//        List<String> arrayList = new ArrayList<String>();
//        arrayList.add("张三");
//        arrayList.add("李四");
//        searchContentBaseAdapter = new SearchContentBaseAdapter(medicalRecordSearchActivity.this,arrayList);
//        //使用适配器
//        search_content_listView.setAdapter(searchContentBaseAdapter);
//
////        //初始化
////        search_content_popupWindow.setContentView(search_content_view);
////        search_content_popupWindow.setClippingEnabled(false);
////        search_content_popupWindow.setTouchable(true);
////        search_content_popupWindow.setFocusable(true);
////        search_content_popupWindow.setOutsideTouchable(true);
////        search_content_popupWindow.setBackgroundDrawable(new BitmapDrawable());
//
//    }
//    private void backgroundAlpha(float f) {
//        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
//        lp.alpha = f;
//        this.getWindow().setAttributes(lp);
//    }
}
