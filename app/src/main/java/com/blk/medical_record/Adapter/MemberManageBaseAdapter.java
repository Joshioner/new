package com.blk.medical_record.Adapter;

import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blk.R;
import com.blk.common.entity.FamilyMember;
import com.blk.common.util.BitmapFactoryUtil;
import com.blk.common.util.FileUtil;

import java.io.File;
import java.util.List;

public class MemberManageBaseAdapter extends BaseAdapter {
    private Context context;
    private List<FamilyMember> list;
    public MemberManageBaseAdapter(Context context, List<FamilyMember> list)
    {
        this.context = context;
        this.list = list;
    }

    private class ViewHolder
    {
         ImageView memberPhoto;   //用户头像
         TextView memberName;      //用户名
         TextView relation;       //关系
         ImageView sex;           //性别
         TextView birthday;      //生日
         TextView memberId;      //成员编号
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MemberManageBaseAdapter.ViewHolder viewHolder = null;
        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.member_manage_list,parent,false);
            viewHolder = new MemberManageBaseAdapter.ViewHolder();
            viewHolder.memberPhoto = (ImageView) convertView.findViewById(R.id.member_photo);
            viewHolder.memberName = (TextView) convertView.findViewById(R.id.member_name);
            viewHolder.relation = (TextView) convertView.findViewById(R.id.relation);
            viewHolder.sex = (ImageView) convertView.findViewById(R.id.sex);
            viewHolder.birthday = (TextView) convertView.findViewById(R.id.birthday);
            viewHolder.memberId = (TextView) convertView.findViewById(R.id.member_id);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (MemberManageBaseAdapter.ViewHolder) convertView.getTag();
        }
        String fileName = list.get(position).getProfile();
        if(fileName != null){
            fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
            File file = FileUtil.getMemberPhoto(context,fileName);
            viewHolder.memberPhoto.setImageBitmap(BitmapFactoryUtil.getBitmap(file.getAbsolutePath()));
        }else {
            viewHolder.memberPhoto.setImageResource(R.mipmap.member_photo);
        }
        viewHolder.memberName.setText(list.get(position).getName());
        viewHolder.relation.setText(list.get(position).getRelation());
        viewHolder.memberId.setText(String.valueOf(list.get(position).getFid()));
        int gender = list.get(position).getGender();
        int resId = R.mipmap.male_normal;
        if (gender == 0){
            resId = R.mipmap.female_normal;
        }

        viewHolder.sex.setImageBitmap(BitmapFactoryUtil.getBitmapByRes(context,resId));
        viewHolder.birthday.setText(list.get(position).getBirthday());
        return convertView;
    }
}
