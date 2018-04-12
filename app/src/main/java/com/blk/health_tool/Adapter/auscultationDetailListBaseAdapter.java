package com.blk.health_tool.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blk.R;
import com.blk.health_tool.Entity.auscultation_detail_list;

import java.util.List;

/**
 * Created by lzx on 2018/3/28.
 */

public class auscultationDetailListBaseAdapter extends BaseAdapter {
    private Context context;
    private List<auscultation_detail_list> list;
    public auscultationDetailListBaseAdapter(Context context, List<auscultation_detail_list> list)
    {
        this.context = context;
        this.list = list;
    }

    private class ViewHolder
    {
        TextView auscultation_message;
        TextView auscultation_date;
        TextView auscultation_time;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.auscultation_detail_list,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.auscultation_message = (TextView) convertView.findViewById(R.id.auscultation_message);
            viewHolder.auscultation_date = (TextView) convertView.findViewById(R.id.auscultation_date);
            viewHolder.auscultation_time = (TextView) convertView.findViewById(R.id.auscultation_time);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.auscultation_message.setText(list.get(position).getAuscultation_message());
        viewHolder.auscultation_date.setText(list.get(position).getAuscultation_date());
        viewHolder.auscultation_time.setText(list.get(position).getAuscultation_time());
        return convertView;
    }
}
