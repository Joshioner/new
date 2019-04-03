package com.blk.medical_record;


import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.blk.R;
import com.blk.common.CommomDialog;
import com.blk.common.ToolBarSet;
import com.blk.entity.User;
import com.blk.medical_record.Adapter.MemberManageBaseAdapter;

import java.io.File;
import java.util.ArrayList;

public class MemberManageActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backIcon;  //返回按钮
    private TextView addMember;  //添加成员
    private ListView memberManageListview;   //成员信息listview
    private ArrayList<User> userArrayList;
    private MemberManageBaseAdapter memberManageBaseAdapter;  //成员管理适配器
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        ToolBarSet.setBar(window);
        setContentView(R.layout.member_manage);

        //初始化控件
        initView();

        //初始化点击事件
        initEvent();
    }

    /**
     * 初始化控件
     */
    public void initView(){
       backIcon = (ImageView) findViewById(R.id.memberManageBack);
       addMember = (TextView) findViewById(R.id.addMember);
       memberManageListview = (ListView) findViewById(R.id.memberListListView);
       //加载家庭成员数据
       new UserInfoThread().execute();
    }

    /**
     * 重写点击事件
     */
    public void initEvent(){
        backIcon.setOnClickListener(this);   //返回事件
        addMember.setOnClickListener(this);  //添加成员
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.memberManageBack:
                this.finish();
                break;
            case R.id.addMember:
                String title = "添加家属提示";
                String content = "1.由于涉及到个人隐私问题，绑定者必须征得被绑定成员的同意，否则一切由绑定者负责。<br>" +
                        "2.因绑定而产生的疑议，一经证实，将取消绑定者家属绑定功能";
                new CommomDialog(MemberManageActivity.this, R.style.dialog, Html.fromHtml(title).toString(), Html.fromHtml(content).toString(),"同意","不同意", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        //不保存，确认退出
                        if (confirm) {
                            //病历保存成功，对话框消失，跳转到病历详情页面
                            dialog.dismiss();
                            Intent confirmIntent = new Intent(MemberManageActivity.this, AddMemberActivity.class);
                            startActivity(confirmIntent);
                        }
                        //取消退出
                        else {
                            dialog.dismiss();
                        }

                    }
                }).show();
                break;
             default:
                 break;
        }
    }

    class UserInfoThread extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            userArrayList = new ArrayList<>();

        }

        //执行加载数据
        @Override
        protected Void doInBackground(Void... voids) {
            String imagePath = getApplicationContext().getFilesDir().getAbsolutePath() + "/image/1.jpg";
            userArrayList.add(new User(imagePath,"张三","关系 : " +"父子",1,"1975-11-15"));
            userArrayList.add(new User(imagePath,"李四","关系 : " +"兄弟",1,"1996-12-16"));
            userArrayList.add(new User(imagePath,"刘艺","关系 : " +"母子",0,"1974-10-22"));
            //初始化适配器
            memberManageBaseAdapter = new MemberManageBaseAdapter(getApplicationContext(),userArrayList);
            memberManageBaseAdapter.notifyDataSetChanged();
            return null;
        }

        //更新ui
        @Override
        protected void onPostExecute(Void aVoid) {
            //填充数据，更新ui
           memberManageListview.setAdapter(memberManageBaseAdapter);
        }
    }
}
