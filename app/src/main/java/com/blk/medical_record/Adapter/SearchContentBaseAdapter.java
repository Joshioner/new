package com.blk.medical_record.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blk.R;

import java.util.List;

/**
 * Created by asus on 2018/2/2.
 */

public class SearchContentBaseAdapter extends BaseAdapter {
    private Context context;
    private List<String>  list;

    public SearchContentBaseAdapter(Context context,List<String> list)
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
    class ViewHolder
    {
        TextView search_content;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.medical_record_search_content_list_style,null);
            viewHolder = new ViewHolder();
            viewHolder.search_content = (TextView) convertView.findViewById(R.id.search_content_list);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.search_content.setText(list.get(position).toString());
        return convertView;
    }
}
