package com.blk.medical_record;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blk.LoginActivity;
import com.blk.R;
import com.blk.common.CommomDialog;
import com.blk.common.ToolBarSet;
import com.blk.common.entity.FamilyMember;
import com.blk.common.entity.HealthRecord;
import com.blk.common.util.AlterUtil;
import com.blk.common.util.ConfigUtil;
import com.blk.common.util.HttpCallbackListener;
import com.blk.common.util.HttpRequestUtil;
import com.blk.common.util.WeiboDialogUtils;
import com.blk.entity.User;
import com.blk.health_tool.Adapter.HealthRecordAdapter;
import com.blk.health_tool.HealthRecordActivity;
import com.blk.medical_record.Adapter.MemberManageBaseAdapter;

import java.io.File;
import java.util.ArrayList;

public class MemberManageActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backIcon;  //返回按钮
    private TextView addMember;  //添加成员
    private ListView memberManageListview;   //成员信息listview
    private ArrayList<FamilyMember> familyMemberArrayList;
    private MemberManageBaseAdapter memberManageBaseAdapter;  //成员管理适配器
    private Dialog weiboDialogUtils;                         //加载框
    private SharedPreferences sharedPreferences;
    private com.blk.common.entity.User user;  //用户信息
    private final int Init_Adapter = 1;
    private final int DELETE_MEMBER = 2;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Init_Adapter:
                    //加载适配器并关闭加载框
                    memberManageListview.setAdapter(memberManageBaseAdapter);
                    WeiboDialogUtils.closeDialog(weiboDialogUtils);
                    break;
                case DELETE_MEMBER:
                    int position = (int) msg.obj;
                    familyMemberArrayList.remove(position);
                    memberManageBaseAdapter.notifyDataSetChanged();
                    break;

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        ToolBarSet.setBar(window);
        //获取sharedPreferences中的userInfo信息
        sharedPreferences = getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        String userInfo = sharedPreferences.getString("userInfo",null);
        user = com.alibaba.fastjson.JSONObject.parseObject(userInfo, com.blk.common.entity.User.class);
        setContentView(R.layout.member_manage);

        //初始化控件
        initView();

        //加载家庭成员数据
        new MemberInfoThread().execute();
        //注册广播信息
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.refreshMemberList");
        registerReceiver(refreshBroadcastReceiver,intentFilter);
    }

    //广播
    private BroadcastReceiver refreshBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //加载家庭成员数据
            new MemberInfoThread().execute();
        }
    };
    /**
     * 初始化控件
     */
    public void initView(){
       backIcon = (ImageView) findViewById(R.id.memberManageBack);
       addMember = (TextView) findViewById(R.id.addMember);
       memberManageListview = (ListView) findViewById(R.id.memberListListView);
       //家庭成员列表信息的点击
       memberManageListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              TextView fidText = (TextView) view.findViewById(R.id.member_id);
              Intent intent = new Intent(MemberManageActivity.this,AddMemberActivity.class);
               Bundle bundle = new Bundle();
               bundle.putInt("fid",Integer.valueOf(fidText.getText().toString()));
               intent.putExtras(bundle);
               startActivity(intent);
           }
       });
       //家庭成员列表信息长按删除操作
        memberManageListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                TextView member_id = (TextView) view.findViewById(R.id.member_id);
                //弹出提示框
                new CommomDialog(MemberManageActivity.this, R.style.dialog, "您确定删除该家庭成员信息？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                       dialog.setCanceledOnTouchOutside(true);
                        if(confirm){
                            //删除成员信息
                            deleteMemberInfo(Integer.valueOf(member_id.getText().toString()),dialog,position);
                        }
                        else
                        {
                            dialog.dismiss();
                        }

                    }
                }).setTitle("提示").show();


                return true;
            }
        });
       backIcon.setOnClickListener(this);   //返回事件
       addMember.setOnClickListener(this);  //添加成员
    }

    //删除成员信息
    private void deleteMemberInfo(int fid,Dialog dialog,int position){
        if (user.getUid() <= 0){
            dialog.dismiss();
            AlterUtil.alterTextShort(this,"请先登陆");
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
        if (fid < 0){
            dialog.dismiss();
            AlterUtil.alterTextShort(this,"删除成员信息失败");
            return;
        }
        String address = ConfigUtil.getServerAddress() + "/member/deleteMember/" + user.getUid() + "/" + fid;
        HttpRequestUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(response);
                int code = jsonObject.getIntValue("code");
                if (code == 0){
                     Message message = Message.obtain();
                     message.what = DELETE_MEMBER;
                     message.obj = position;
                     handler.sendMessage(message);
                     dialog.dismiss();
                    Looper.prepare();
                    AlterUtil.alterTextShort(MemberManageActivity.this,"删除成员信息成功");
                    Looper.loop();
                }else {
                    dialog.dismiss();
                    Looper.prepare();
                    AlterUtil.alterTextShort(MemberManageActivity.this,"删除成员信息失败");
                    Looper.loop();
                }
            }

            @Override
            public void onError(Exception e) {
                dialog.dismiss();
                Looper.prepare();
                AlterUtil.alterTextShort(MemberManageActivity.this,"删除成员信息失败");
                Looper.loop();
            }
        });
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

    class MemberInfoThread extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            weiboDialogUtils = WeiboDialogUtils.createLoadingDialog(MemberManageActivity.this,"加载中");
            familyMemberArrayList = new ArrayList<>();
        }

        //执行加载数据
        @Override
        protected Void doInBackground(Void... voids) {
            Message message = Message.obtain();
            message.what = Init_Adapter;
            String address = ConfigUtil.getServerAddress() + "/member/getAll/" + user.getUid();
            HttpRequestUtil.sendHttpRequest(address, new HttpCallbackListener() {
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
                                //成员信息
                                FamilyMember member = new FamilyMember();
                                member.setFid(object.getInteger("fid"));
                                member.setName(object.getString("name"));
                                member.setProfile(object.getString("profile"));
                                member.setGender(object.getInteger("gender"));
                                member.setBirthday(object.getString("birthday"));
                                member.setRelation(object.getString("relation"));
                                member.setUid(object.getInteger("uid"));
                                familyMemberArrayList.add(member);
                            }
                        }
                        memberManageBaseAdapter = new MemberManageBaseAdapter(MemberManageActivity.this,familyMemberArrayList);
                        handler.sendMessage(message);
                    }else {
                        Looper.prepare();
                        AlterUtil.alterTextLong(MemberManageActivity.this,"获取家庭成员列表失败");
                        Looper.loop();
                        WeiboDialogUtils.closeDialog(weiboDialogUtils);
                    }
                }

                @Override
                public void onError(Exception e) {
                    Looper.prepare();
                    AlterUtil.alterTextLong(MemberManageActivity.this,"获取家庭成员列表失败");
                    Looper.loop();
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
