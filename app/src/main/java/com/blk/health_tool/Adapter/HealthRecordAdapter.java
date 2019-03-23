package com.blk.health_tool.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blk.R;
import com.blk.common.MyApplication;
import com.blk.health_tool.Entity.HealthRecord;
import com.blk.health_tool.HealthRecordDetailActivity;
import com.blk.health_tool.util.GetDate;

import java.util.List;

public class HealthRecordAdapter extends RecyclerView.Adapter<HealthRecordAdapter.HealthRecordViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<HealthRecord> list;
    private int mEditPosition = -1;

    public HealthRecordAdapter(Context context, List<HealthRecord> list){
        mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.list = list;
    }
    @Override
    public HealthRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HealthRecordViewHolder(mLayoutInflater.inflate(R.layout.health_record_list, parent, false));
    }

    @Override
    public void onBindViewHolder(final HealthRecordViewHolder viewHolder, final int position) {

        String dateSystem = GetDate.getDate().toString();
        if(list.get(position).getDate().equals(dateSystem)){
            viewHolder.circleImage.setImageResource(R.mipmap.circle_green);
        }
        viewHolder.id.setText(String.valueOf(list.get(position).getId()));
        viewHolder.date.setText(list.get(position).getDate() + "    " + list.get(position).getTime());
        viewHolder.address.setText(list.get(position).getAddress());
        if (null != list.get(position).getFood() && !"".equals(list.get(position).getFood())){
            viewHolder.foodRelativeLayout.setVisibility(View.VISIBLE);
            viewHolder.food.setText(list.get(position).getFood());
        }
        if (null != list.get(position).getSport() && !"".equals(list.get(position).getSport())){
            viewHolder.sportRelativeLayout.setVisibility(View.VISIBLE);
            viewHolder.sport.setText(list.get(position).getSport());
        }
        viewHolder.symptom.setText(list.get(position).getSymptom());
        viewHolder.editImage.setVisibility(View.INVISIBLE);
//        if(mEditPosition == position){
//            viewHolder.editImage.setVisibility(View.VISIBLE);
//        }else {
//            viewHolder.editImage.setVisibility(View.GONE);
//        }
        viewHolder.editRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.editImage.getVisibility() == View.VISIBLE){
                    viewHolder.editImage.setVisibility(View.GONE);
                }else {
                    viewHolder.editImage.setVisibility(View.VISIBLE);
                }
                if(mEditPosition != position){
                    notifyItemChanged(mEditPosition);
                }
                mEditPosition = position;
            }
        });

        viewHolder.editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,HealthRecordDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id",list.get(position).getId());
                bundle.putString("symptom",list.get(position).getSymptom());
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class HealthRecordViewHolder extends RecyclerView.ViewHolder{

        TextView date;
        TextView address;
        TextView food;
        TextView sport;
        TextView symptom;
        RelativeLayout foodRelativeLayout;
        RelativeLayout sportRelativeLayout;
        TextView id;
        LinearLayout editRelativeLayout;
        ImageView editImage;
        ImageView circleImage;

        HealthRecordViewHolder(View convertView){
            super(convertView);
            date = (TextView) convertView.findViewById(R.id.health_record_date);
            food = (TextView) convertView.findViewById(R.id.health_record_food);
            address = (TextView) convertView.findViewById(R.id.health_record_address);
            sport = (TextView) convertView.findViewById(R.id.health_record_sport);
            symptom = (TextView) convertView.findViewById(R.id.health_record_symptom);
            foodRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.health_record_food_relativeLayout);
            sportRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.health_record_sport_relativeLayout);
            id = (TextView) convertView.findViewById(R.id.health_record_id);
            editImage = (ImageView) convertView.findViewById(R.id.edit_image);
            editRelativeLayout = (LinearLayout) convertView.findViewById(R.id.health_record_edit_relativeLayout);
            circleImage = (ImageView) convertView.findViewById(R.id.circle_image);
        }
    }
}