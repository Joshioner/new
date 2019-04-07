package com.blk.medical_record.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blk.R;
import com.blk.medical_record.entity.PersonMemberInfo;

import java.util.List;

/**
 * Created by asus on 2018/1/28.
 */

public class personMemberInfoBaseAdapter extends BaseAdapter {
    private Context context;
    private List<PersonMemberInfo> list;

    public personMemberInfoBaseAdapter(Context context, List<PersonMemberInfo> list)
    {
        this.context = context;
        this.list = list;
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

    private class ViewHolder
    {
        TextView member_name;
        TextView member_uid;
        TextView member_fid;
        TextView member_profile;
        TextView member_flag;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.person_member,parent,false);
            viewHolder.member_uid = (TextView) convertView.findViewById(R.id.member_uid);
            viewHolder.member_fid = (TextView) convertView.findViewById(R.id.member_fid);
            viewHolder.member_name = (TextView) convertView.findViewById(R.id.member_name);
            viewHolder.member_profile = (TextView) convertView.findViewById(R.id.member_profile);
            viewHolder.member_flag = (TextView) convertView.findViewById(R.id.member_flag);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.member_uid.setText(String.valueOf(list.get(position).getUid()));
        viewHolder.member_fid.setText(String.valueOf(list.get(position).getFid()));
        viewHolder.member_name.setText(list.get(position).getName());
        viewHolder.member_profile.setText(list.get(position).getProfile());
        viewHolder.member_flag.setText(String.valueOf(list.get(position).isFlag()));
        return convertView;
    }
}
