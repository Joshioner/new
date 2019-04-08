package com.blk.health_tool.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blk.R;
import com.blk.common.entity.MedicalAuscultation;

import java.util.List;


/**
 * Created by lzx on 2018/3/28.
 */

public class AuscultationDetailListBaseAdapter extends BaseAdapter {
    private Context context;
    private List<MedicalAuscultation> list;
    public AuscultationDetailListBaseAdapter(Context context, List<MedicalAuscultation> list)
    {
        this.context = context;
        this.list = list;
    }

    private class ViewHolder
    {
        TextView mid;
        TextView content;
        TextView operTime;
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
            viewHolder.mid = (TextView) convertView.findViewById(R.id.mid);
            viewHolder.content = (TextView) convertView.findViewById(R.id.content);
            viewHolder.operTime = (TextView) convertView.findViewById(R.id.operTime);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mid.setText(String.valueOf(list.get(position).getMid()));
        viewHolder.content.setText(list.get(position).getContent());
        viewHolder.operTime.setText(list.get(position).getOperTime());
        return convertView;
    }
}
