package com.blk.pharmacy.homePage.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blk.R;
import com.blk.pharmacy.homePage.entity.pharmacy_list_detail;

import java.util.List;

/**
 * Created by asus on 2017/11/4.
 */

public class pharmacyListDetailBaseAdapter extends BaseAdapter{
    private Context context;
    private List<pharmacy_list_detail> list;

    public pharmacyListDetailBaseAdapter(Context context, List<pharmacy_list_detail> list)
    {
        this.context = context;
        this.list = list;
    }
    private class ViewHolder
    {
        TextView pharmacy_name;
        TextView time;
        TextView distance;
        TextView send;
        TextView distribution;
        TextView average;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.pharmacy_list_detail,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.pharmacy_name = (TextView) convertView.findViewById(R.id.pharmacy_name);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.distance = (TextView) convertView.findViewById(R.id.distance);
            viewHolder.send = (TextView) convertView.findViewById(R.id.send);
            viewHolder.distribution = (TextView) convertView.findViewById(R.id.distribution);
            viewHolder.average = (TextView) convertView.findViewById(R.id.average);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.pharmacy_name.setText(list.get(position).getPharmacyName());
        viewHolder.time.setText(list.get(position).getTime());
        viewHolder.distance.setText(list.get(position).getDistance());
        viewHolder.send.setText(list.get(position).getSend());
        viewHolder.distance.setText(list.get(position).getDistance());
        viewHolder.average.setText(list.get(position).getAverage());
        return convertView;
    }
}
