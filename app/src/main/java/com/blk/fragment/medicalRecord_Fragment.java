package com.blk.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.ocr.sdk.OCR;
import com.blk.R;
import com.blk.RegisterActivity;
import com.blk.common.CommomDialog;
import com.blk.common.entity.CaseHistory;
import com.blk.common.entity.FamilyMember;
import com.blk.common.entity.User;
import com.blk.common.ShowAllListView;
import com.blk.common.identify.XCRoundImageView;
import com.blk.common.util.AlterUtil;
import com.blk.common.util.BitmapFactoryUtil;
import com.blk.common.util.ConfigUtil;
import com.blk.common.util.FileUtil;
import com.blk.common.util.HttpCallbackListener;
import com.blk.common.util.HttpRequestUtil;
import com.blk.common.util.HttpSendUtil;
import com.blk.common.util.WeiboDialogUtils;
import com.blk.health_tool.HealthRecordActivity;
import com.blk.health_tool.HealthRecordDetailActivity;
import com.blk.medical_record.Adapter.CaseHistoryDetailBaseAdapter;
import com.blk.medical_record.Adapter.personMemberInfoBaseAdapter;
import com.blk.medical_record.MemberManageActivity;
import com.blk.medical_record.dataAnalyseActivity;
import com.blk.medical_record.entity.PersonMemberInfo;
import com.blk.medical_record.medicalRecordContentActivity;
import com.blk.medical_record.medicalRecordSearchActivity;
import com.blk.medical_record.testActivity;
import com.blk.medical_record.util.GetAccessTokenUtil;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/8/9.
 */

public class medicalRecord_Fragment extends Fragment implements View.OnClickListener{
    private final int SHOW_MEDICAL_RECORD = 1;
    private View view,popupView ;
    private DrawerLayout drawerLayout;
    private ImageView icon_more,icon_add;   //点击更多的图片,选择拍照或者选择照片
    private ListView memberListView ;    //显示家庭成员的memberList
    private List<PersonMemberInfo> memberlist;   //家庭成员具体信息的列表
    private personMemberInfoBaseAdapter member_info_baseAdapter;  //传递给memberListView的适配器
  //  private PersonMemberInfo member_info ;   //家庭成员具体信息
    private AlertDialog.Builder alertDialog;
    private ListView medicalListView;    //显示病历详细信息的ListView
    private List<CaseHistory> caseHistoryList;  //病历信息的列表
    private CaseHistory CaseHistory ;   //病历详细信息
    private CaseHistoryDetailBaseAdapter detail_baseAdapter; //传递给medicalListView的适配器

    private PopupWindow chooseTakeSelectPop;   //选择图片或者拍照的弹出框
    private ImageView closeSelect,takePhoto,choosePhoto;  //关闭弹出框，选择图片，拍照

    private ImageView medicalRecordSearchBox;   //病历搜索框

    private XCRoundImageView personImage;     //用户头像

    private final int medicalRecordSearchRequestCode = 101;   //病历搜索框请求码

    private TextView data_analyse;       //成员管理、数据对比

    private TextView personName,personUid,personFid,personProfile,personFlag;   //当前用户名

    private TextView memberManage;  //成员管理
    private SharedPreferences sharedPreferences;
    private User user;  //用户信息

    public  static Dialog weiboDialogUtils;

    private final int Init_MedicalHistory_Adapter = 1;
    private final int DELETE_MEDICAL = 2;
    private final int Init_Member_Adapter = 3;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Init_MedicalHistory_Adapter:
                    //加载适配器并关闭加载框
                    medicalListView.setAdapter(detail_baseAdapter);//将适配器传递给medicalListView，类似于填充数据
                    WeiboDialogUtils.closeDialog(weiboDialogUtils);
                    break;
                case DELETE_MEDICAL:
                    int position = (int) msg.obj;
                    caseHistoryList.remove(position);
                    detail_baseAdapter.notifyDataSetChanged();
                    break;
                case Init_Member_Adapter:
                    //加载适配器并关闭加载框
                    memberListView.setAdapter(member_info_baseAdapter);//将适配器传递给memberListView，类似于填充数据
                    break;

            }
        }
    };
    public medicalRecord_Fragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //获取sharedPreferences中的userInfo信息
        sharedPreferences = getActivity().getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        String userInfo = sharedPreferences.getString("userInfo",null);
        user = com.alibaba.fastjson.JSONObject.parseObject(userInfo,User.class);
       if (view == null){
           view=inflater.inflate(R.layout.medical_record,container,false);
       }else {
           ViewGroup parent = (ViewGroup) view.getParent();
           if(parent != null) {
               parent.removeView(view);
           }
       }
        //注册广播信息
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.refreshCaseHistory");
        getActivity().registerReceiver(refreshBroadcastReceiver,intentFilter);
        //初始化控件
        initView();
        //事件
       initEvent();
        //加载病历信息
        new CaseHistoryThread().execute(user.getUid(),0);
        //加载家庭成员信息
       new MemberListInfo().execute();
        return view;
    }
    //广播
    private BroadcastReceiver refreshBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //加载病历信息
            new CaseHistoryThread().execute(user.getUid(),Integer.valueOf(personFid.getText().toString()));
        }
    };
    //初始化控件
    private void initView()
    {
        drawerLayout = (DrawerLayout) view.findViewById(R.id.drawerLayout);
        // 禁止手势滑动
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        icon_more = (ImageView) view.findViewById(R.id.icon_more);
        icon_add = (ImageView) view.findViewById(R.id.icon_add);
        medicalListView = (ListView) view.findViewById(R.id.medical_record_detail_list);
        memberManage = (TextView) view.findViewById(R.id.member_manage);
        data_analyse = (TextView) view.findViewById(R.id.data_analyse);
        //用户头像
        personImage = (XCRoundImageView) view.findViewById(R.id.person_photo);
        File file = FileUtil.getPersonPhoto(getActivity());
        if (file.exists()){
            personImage.setImageBitmap(BitmapFactoryUtil.getBitmap(file.getAbsolutePath()));
        }else {
            personImage.setImageResource(R.mipmap.person_profile);
        }
        //用户名
        personName = (TextView) view.findViewById(R.id.person_name);
        personName.setText(user.getAccountName());
        //uid
        personUid = (TextView) view.findViewById(R.id.person_uid);
        personUid.setText(String.valueOf(user.getUid()));
        //fid
        personFid = (TextView) view.findViewById(R.id.person_fid);
        personFid.setText(String.valueOf(0));
        //flag
        personFlag = (TextView) view.findViewById(R.id.person_flag);
        personFlag.setText(String.valueOf("true"));
        //头像
        personProfile = (TextView) view.findViewById(R.id.person_profile);
        personProfile.setText(user.getPhone());
        medicalRecordSearchBox = (ImageView) view.findViewById(R.id.medicalRecordSearchBox);
        alertDialog = new AlertDialog.Builder(getActivity());
        //解决scrollview中嵌套listview只显示一个item的问题
        //ShowAllListView.setListViewHeightBasedOnChildren(medicalListView);
        memberListView = (ListView) view.findViewById(R.id.person_member_list);
    }


    //加载家庭成员信息
    class MemberListInfo extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            memberlist = new ArrayList<PersonMemberInfo>();
        }

        @Override
        protected Void doInBackground(Void... integers) {
            Message message = Message.obtain();
            message.what = Init_Member_Adapter;
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
                                PersonMemberInfo memberInfo = new PersonMemberInfo();
                                //uid
                                memberInfo.setUid(object.getInteger("uid"));
                                //fid
                                memberInfo.setFid(object.getInteger("fid"));
                                //头像路径
                                memberInfo.setProfile(object.getString("profile"));
                                //名字
                                memberInfo.setName(object.getString("name"));
                                memberInfo.setFlag(false);
                                memberlist.add(memberInfo);
                            }
                        }
                        member_info_baseAdapter = new personMemberInfoBaseAdapter(getActivity(),memberlist);//初始化适配器
                        handler.sendMessage(message);  //发送handle消息
                    }else {
                        Looper.prepare();
                        AlterUtil.alterTextLong(getActivity(),"获取家庭成员列表详情失败");
                        Looper.loop();
                        WeiboDialogUtils.closeDialog(weiboDialogUtils);
                    }
                }

                @Override
                public void onError(Exception e) {
                    Looper.prepare();
                    AlterUtil.alterTextLong(getActivity(),"获取家庭成员列表详情失败");
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


    //加载病历信息列表
    class CaseHistoryThread extends AsyncTask<Integer,Void,Void>{
        @Override
        protected void onPreExecute() {
            caseHistoryList = new ArrayList<CaseHistory>();
            weiboDialogUtils = WeiboDialogUtils.createLoadingDialog(getActivity(),"加载中...");
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            Message message = Message.obtain();
            message.what = Init_MedicalHistory_Adapter;
            String address = ConfigUtil.getServerAddress() + "/caseHistory/getAll/" + integers[0] +"/" + integers[1];
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
                                CaseHistory caseHistory = new CaseHistory();
                                caseHistory.setCid(object.getIntValue("cid"));
                                caseHistory.setUid(object.getIntValue("uid"));
                                caseHistory.setVisitDate(object.getString("visitDate"));
                                caseHistory.setHospitalName(object.getString("hospitalName"));
                                caseHistory.setPhoto(object.getString("photo"));
                                caseHistory.setOffice(object.getString("office"));
                                caseHistoryList.add(caseHistory);

                            }
                        }
                        detail_baseAdapter = new CaseHistoryDetailBaseAdapter(getActivity(),caseHistoryList);//初始化适配器
                        handler.sendMessage(message);  //发送handle消息
                    }else {
                        Looper.prepare();
                        AlterUtil.alterTextLong(getActivity(),"获取病历列表详情失败");
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

    //事件
    private void initEvent()
    {
        //左边的更多图片，点击触发事件--弹出左边的抽屉
        icon_more.setOnClickListener(this);
        //选择按钮，选择图片或者拍照
        icon_add.setOnClickListener(this);
        //病历信息搜索框
        medicalRecordSearchBox.setOnClickListener(this);
        //成员管理事件
        memberManage.setOnClickListener(this);
        //数据对比
        data_analyse.setOnClickListener(this);
        //病历信息列表的点击
        medicalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView cid = (TextView) view.findViewById(R.id.list_id);
                Intent medicalRecordContentIntent = new Intent(getActivity(),medicalRecordContentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("data",2);
                bundle.putInt("cid",Integer.valueOf(cid.getText().toString()));
                medicalRecordContentIntent.putExtras(bundle);
                startActivity(medicalRecordContentIntent);
            }
        });
        //病历信息列表长按--删除病历信息
        medicalListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                TextView text_list_id = (TextView) view.findViewById(R.id.list_id);
                //弹出提示框
                new CommomDialog(getActivity(), R.style.dialog, "您确定删除该病历信息？", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if(confirm){
                            //删除病历信息
                            deleteCaseHistory(Integer.valueOf(text_list_id.getText().toString()),dialog,position);
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

        //用户家庭组病历信息管理
        memberListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //移除的成员信息
                 TextView member_uid = (TextView) view.findViewById(R.id.member_uid);
                 TextView member_fid = (TextView) view.findViewById(R.id.member_fid);
                 TextView member_name = (TextView) view.findViewById(R.id.member_name);
                 TextView member_profile = (TextView) view.findViewById(R.id.member_profile);
                 TextView member_flag = (TextView) view.findViewById(R.id.member_flag);
                 //当前成员信息
                 PersonMemberInfo memberInfo =  new PersonMemberInfo();
                 memberInfo.setUid(Integer.valueOf(personUid.getText().toString()));
                 memberInfo.setFid(Integer.valueOf(personFid.getText().toString()));
                 memberInfo.setName(personName.getText().toString());
                 memberInfo.setProfile(personProfile.getText().toString());
                 memberInfo.setFlag(personFlag.getText().toString().equals("true"));
                 //设置当前成员信息
                personUid.setText(member_uid.getText());
                personFid.setText(member_fid.getText());
                personName.setText(member_name.getText());
                personProfile.setText(member_profile.getText());
                personFlag.setText(member_flag.getText());
                //移除点击的成员信息
                memberlist.remove(position);
                //添加当前成员信息
                memberlist.add(memberInfo);
                //刷新adapter
                member_info_baseAdapter.notifyDataSetChanged();
                //设置头像
                File file = null;
                if (personFlag.getText().toString().equals("true")){
                     file = FileUtil.getPersonPhoto(getActivity());

                }else {
                    String fileName = personProfile.getText().toString();
                    if (fileName != null && fileName != ""){
                        fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
                        file = FileUtil.getMemberPhoto(getActivity(),fileName);
                    }
                }
                if (file != null && file.exists()){
                    personImage.setImageBitmap(BitmapFactoryUtil.getBitmap(file.getAbsolutePath()));
                }else {
                    personImage.setImageResource(R.mipmap.person_profile);
                }
                //刷新病历列表信息
                if (Integer.valueOf(personFid.getText().toString()) > 0){
                    //获取某个家庭成员病历列表
                    new CaseHistoryThread().execute(user.getUid(),Integer.valueOf(personFid.getText().toString()));
                }else {
                    //获取用户病历列表
                    new CaseHistoryThread().execute(user.getUid(),0);
                }
            }
        });

        //accessToken初始化
        GetAccessTokenUtil.getInstance(getActivity()).initAccessToken();

    }
//
    private void icon_addEvent()
    {
        initAddPopupWindow();
        chooseTakeSelectPop.showAsDropDown(popupView,0,0);
        backgroundAlpha(0.8f);
    }
  //初始化弹出框
    private void initAddPopupWindow()
    {
        //初始化
          popupView = LayoutInflater.from(getActivity()).inflate(R.layout.choose_take_select_popupwindow,null);
        // 创建PopupWindow对象，其中：
         // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
        // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
          chooseTakeSelectPop = new PopupWindow(popupView,getActivity().getWindow().getAttributes().width,getActivity().getWindow().getAttributes().height,true);
          chooseTakeSelectPop.setClippingEnabled(false);
        // 设置PopupWindow是否能响应外部点击事件
          chooseTakeSelectPop.setTouchable(true);
        // 设置PopupWindow是否能响应外部点击事件
          chooseTakeSelectPop.setOutsideTouchable(true);
        // 设置PopupWindow的背景
          chooseTakeSelectPop.setBackgroundDrawable(new BitmapDrawable());

          closeSelect = (ImageView) popupView.findViewById(R.id.closeChoose);
          takePhoto = (ImageView) popupView.findViewById(R.id.takePhoto);
          choosePhoto = (ImageView) popupView.findViewById(R.id.choosePhoto);
          //对病历进行拍照，并识别病历信息
          takePhoto.setOnClickListener(this);
          //选择病历图片，并识别病历信息
          choosePhoto.setOnClickListener(this);
          //关闭弹出选择框
          closeSelect.setOnClickListener(this);

    }
    //删除病历信息
    private void deleteCaseHistory(int cid,Dialog dialog,int position){
        if (cid <= 0){
            dialog.dismiss();
            AlterUtil.alterTextShort(getActivity(),"删除病历信息失败");
            return;
        }
        String address = ConfigUtil.getServerAddress() + "/caseHistory/deleteCaseHistory/" + cid;
        HttpRequestUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(response);
                int code = jsonObject.getIntValue("code");
                if (code == 0){
                    //刷新adapter
                    Message message = Message.obtain();
                    message.what = DELETE_MEDICAL;
                    message.obj = position;
                    handler.sendMessage(message);
                    dialog.dismiss();
                    dialog.dismiss();
                    Looper.prepare();
                    AlterUtil.alterTextShort(getActivity(),"删除病历信息成功");
                    Looper.loop();
                }else {
                    dialog.dismiss();
                    Looper.prepare();
                    AlterUtil.alterTextShort(getActivity(),"删除病历信息失败");
                    Looper.loop();
                }
            }

            @Override
            public void onError(Exception e) {
                dialog.dismiss();
                Looper.prepare();
                AlterUtil.alterTextShort(getActivity(),"删除病历信息失败");
                Looper.loop();
            }
        });
    }
    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams lp =getActivity().getWindow().getAttributes();
        lp.alpha = f;
        getActivity().getWindow().setAttributes(lp);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            //病历信息搜索框
            case R.id.medicalRecordSearchBox:
                 Intent searchIntent = new Intent(getActivity(),medicalRecordSearchActivity.class);
                 startActivity(searchIntent);
                break;
            //选择按钮，选择图片或者拍照
            case R.id.icon_add:
                icon_addEvent();
                break;
            //左边的更多图片，点击触发事件--弹出左边的抽屉
            case R.id.icon_more:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
            //对病历进行拍照，并识别病历信息
            case R.id.takePhoto:
                Intent takePhotoIntent = new Intent(getActivity(),medicalRecordContentActivity.class);
                backgroundAlpha(1.0f);
                chooseTakeSelectPop.dismiss();
                chooseTakeSelectPop = null;
                   /* 通过Bundle对象存储需要传递的数据 */
                Bundle bundleTake=new Bundle();
                bundleTake.putInt("data",1);
                bundleTake.putInt("fid",Integer.valueOf(personFid.getText().toString()));
                /*把bundle对象assign给Intent*/
                takePhotoIntent.putExtras(bundleTake);
                startActivity(takePhotoIntent);
                break;
            //选择病历图片，并识别病历信息
            case R.id.choosePhoto:
                Intent choosePhotoIntent = new Intent(getActivity(),medicalRecordContentActivity.class);
                backgroundAlpha(1.0f);
                chooseTakeSelectPop.dismiss();
                chooseTakeSelectPop = null;
                   /* 通过Bundle对象存储需要传递的数据 */
                Bundle bundleChoose=new Bundle();
                bundleChoose.putInt("data",0);
                bundleChoose.putInt("fid",Integer.valueOf(personFid.getText().toString()));
                /*把bundle对象assign给Intent*/
                choosePhotoIntent.putExtras(bundleChoose);
                startActivity(choosePhotoIntent);
                break;
            //关闭弹出选择框
            case R.id.closeChoose:
                backgroundAlpha(1.0f);
                chooseTakeSelectPop.dismiss();
                chooseTakeSelectPop = null;
                break;
            //家庭成员管理
            case R.id.member_manage:
                Intent intent = new Intent(getActivity(),MemberManageActivity.class);
                startActivity(intent);
                break;
            //数据对比
            case R.id.data_analyse:
                Intent dataAnalyseIntent = new Intent(getActivity(),dataAnalyseActivity.class);
                startActivity(dataAnalyseIntent);
                break;

        }
    }




    private void alertText(String title, String message) {
        boolean isNeedLoop = false;
        if (Looper.myLooper() == null) {
            Looper.prepare();
            isNeedLoop = true;
        }
        alertDialog.setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定", null)
                .show();
        if (isNeedLoop) {
            Looper.loop();
        }
    }

    //动态处理权限问题
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]  permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            GetAccessTokenUtil.getInstance(getActivity()).initAccessToken();
        } else {
            Toast.makeText(getActivity(), "需要android.permission.READ_PHONE_STATE", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 释放内存资源
        OCR.getInstance().release();
    }
}


