package com.blk.medical_record.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blk.R;
import com.blk.medical_record.entity.healthy_news;

import java.util.List;

/**
 * Created by asus on 2018/2/6.
 */

public class healthyNewsBaseAdapter extends BaseAdapter {
    private Context context;
    private List<healthy_news> list;

    public healthyNewsBaseAdapter(Context context,List<healthy_news> list)
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
    public class ViewHolder
    {
        TextView news_title;
        TextView news_author_name;
        TextView news_content;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.healthy_news,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.news_title = (TextView) convertView.findViewById(R.id.news_title);
            viewHolder.news_author_name = (TextView) convertView.findViewById(R.id.author_name);
            viewHolder.news_content = (TextView) convertView.findViewById(R.id.news_content);
           convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.news_title.setText(list.get(position).getTitle());
        viewHolder.news_author_name.setText(list.get(position).getAuthor_name());
        viewHolder.news_content.setText(list.get(position).getContent());
        return convertView;
    }
}
