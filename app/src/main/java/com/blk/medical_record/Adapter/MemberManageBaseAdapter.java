package com.blk.medical_record.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blk.R;
import com.blk.common.util.BitmapFactoryUtil;
import com.blk.entity.User;
import com.blk.health_tool.Entity.auscultation_detail_list;

import org.w3c.dom.Text;

import java.util.List;

public class MemberManageBaseAdapter extends BaseAdapter {
    private Context context;
    private List<User> list;
    public MemberManageBaseAdapter(Context context, List<User> list)
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
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (MemberManageBaseAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.memberPhoto.setImageBitmap(BitmapFactoryUtil.getBitmap(list.get(position).getPhotoImage()));
        viewHolder.memberName.setText(list.get(position).getName());
        viewHolder.relation.setText(list.get(position).getRelation());
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
