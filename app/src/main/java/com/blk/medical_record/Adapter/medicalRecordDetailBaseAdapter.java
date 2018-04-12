package com.blk.medical_record.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blk.R;
import com.blk.medical_record.entity.medicalRecordDetail;

import java.util.List;

/**
 * Created by asus on 2017/11/4.
 */

public class medicalRecordDetailBaseAdapter extends BaseAdapter{
    private Context context;
    private List<medicalRecordDetail> list;

    public medicalRecordDetailBaseAdapter(Context context,List<medicalRecordDetail> list)
    {
        this.context = context;
        this.list = list;
    }
    private class ViewHolder
    {
        TextView list_id;
        TextView date;
        TextView hospital_name;
        TextView department;
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
        ViewHolder viewHolder = null;
        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.medical_record_detail,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.list_id = (TextView) convertView.findViewById(R.id.list_id);
            viewHolder.date = (TextView) convertView.findViewById(R.id.times);
            viewHolder.hospital_name = (TextView) convertView.findViewById(R.id.hospital_name);
            viewHolder.department = (TextView) convertView.findViewById(R.id.department);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.list_id.setText(list.get(position).getList_id());
        viewHolder.date.setText(list.get(position).getDate());
        viewHolder.hospital_name.setText(list.get(position).getHospital_name());
        viewHolder.department.setText(list.get(position).getDepartment());
        return convertView;
    }
}
