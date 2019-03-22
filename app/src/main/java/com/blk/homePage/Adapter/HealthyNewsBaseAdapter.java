package com.blk.homePage.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blk.R;
import com.blk.common.MyApplication;
import com.blk.common.util.BitmapFactoryUtil;
import com.blk.health_tool.HealthyNewsDetailActivity;
import com.blk.medical_record.entity.HealthyNews;
import com.wx.goodview.GoodView;

import java.util.List;

/**
 * Created by asus on 2018/2/6.
 */

public class HealthyNewsBaseAdapter extends BaseAdapter {
    private Context context;
    private List<HealthyNews> list;
    private GoodView goodView;
    public HealthyNewsBaseAdapter(Context context, List<HealthyNews> list, GoodView goodView)
    {
        this.context = context;
        this.list = list;
        this.goodView = goodView;
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
        RelativeLayout newsRelativeLayout;
        TextView news_id;
      //  ImageView news_cover;
        TextView news_title;
        TextView news_author_name;
        TextView news_content;
        ImageView collection_icon;
        TextView collection_flag;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         ViewHolder viewHolder = null;
        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.healthy_news,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.newsRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.newsRelativeLayout);
            viewHolder.news_id = (TextView) convertView.findViewById(R.id.news_id);
        //    viewHolder.news_cover = (ImageView) convertView.findViewById(R.id.news_cover);
            viewHolder.news_title = (TextView) convertView.findViewById(R.id.news_title);
            viewHolder.news_author_name = (TextView) convertView.findViewById(R.id.author_name);
            viewHolder.news_content = (TextView) convertView.findViewById(R.id.news_content);
            viewHolder.collection_icon = (ImageView) convertView.findViewById(R.id.collection_icon);
            viewHolder.collection_flag = (TextView) convertView.findViewById(R.id.collection_flag);
           convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.news_id.setText(String.valueOf(list.get(position).getNews_id()));
      //  viewHolder.news_cover.setImageBitmap(BitmapFactoryUtil.getBitmap(list.get(position).getImage_cover()));
        viewHolder.news_title.setText(list.get(position).getTitle());
        viewHolder.news_author_name.setText(list.get(position).getAuthor_name());
        viewHolder.news_content.setText(list.get(position).getContent());
        viewHolder.collection_icon.setImageResource(list.get(position).getCollection_status() == 1 ?R.mipmap.collection_pressed:R.mipmap.collection_normal);
        viewHolder.collection_flag.setText(String.valueOf(list.get(position).getCollection_status()));

        // 设置位置，获取点击的条目按钮
        viewHolder.collection_icon.setTag(position);
//        //收藏的点击事件
        viewHolder.collection_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int flag = list.get(position).getCollection_status();
                View parent = (View) view.getParent();
                //点击后状态更改为收藏
                if (flag == 0){
                    ((ImageView) view).setImageResource(R.mipmap.collection_pressed);
                    goodView.setTextInfo("收藏成功", Color.parseColor("#1CBD82"), 12);
                    goodView.show(view);
                    list.get(position).setCollection_status(1);
                    ((TextView) parent.findViewById(R.id.collection_flag)).setText("1");
                }else{
                    ((ImageView) view).setImageResource(R.mipmap.collection_normal);
                    list.get(position).setCollection_status(0);
                    ((TextView) parent.findViewById(R.id.collection_flag)).setText("0");
                }
            }
        });

        // 设置位置，获取点击的条目按钮
        viewHolder.newsRelativeLayout.setTag(position);
        //查看详情的点击事件
        viewHolder.newsRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,HealthyNewsDetailActivity.class);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

}
