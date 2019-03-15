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
        TextView person_member_name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.person_member,parent,false);
            viewHolder.person_member_name = (TextView) convertView.findViewById(R.id.person_member_name);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.person_member_name.setText(list.get(position).getPerson_name());
        return convertView;
    }
}
