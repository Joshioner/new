package com.blk.common.identify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.blk.R;

import java.util.List;

/**
 * Created by lzx on 2018/3/6.
 */

public class alarmAdapter extends BaseAdapter {
    private Context context;
    private List<alarmList> lists;

    public alarmAdapter(List<alarmList> lists,Context context){
        this.context = context;
        this.lists = lists;
    }
    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        MyListener myListener=null;
        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.alarm_list,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.alarm_date = (TextView) convertView.findViewById(R.id.alarm_date);
            viewHolder.alarm_time = (TextView) convertView.findViewById(R.id.alarm_time);
            viewHolder.alarm_content = (TextView) convertView.findViewById(R.id.alarm_content);
            viewHolder.alarm_switch = (Switch) convertView.findViewById(R.id.alarm_switch);

            String[] alarmdata = {lists.get(position).getAlarm_date(),lists.get(position).getAlarm_time(),lists.get(position).getAlarm_content()};
            myListener=new MyListener(position,context,alarmdata);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.alarm_date.setText(lists.get(position).getAlarm_date());
        viewHolder.alarm_time.setText(lists.get(position).getAlarm_time());
        viewHolder.alarm_content.setText(lists.get(position).getAlarm_content());

        viewHolder.alarm_switch.setTag(position);
        viewHolder.alarm_switch.setOnCheckedChangeListener(myListener);

        return convertView;
    }
    private class ViewHolder
    {
        TextView alarm_date;
        TextView alarm_time;
        TextView alarm_content;
        Switch alarm_switch;
    }
}
