package com.blk.medical_record.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blk.R;
import com.blk.common.MyApplication;
import com.blk.common.entity.CaseHistory;

import java.util.List;

/**
 * Created by asus on 2017/11/4.
 */
public class CaseHistoryDetailBaseAdapter extends BaseAdapter{
    private Context context;
    private List<CaseHistory> list;

    public CaseHistoryDetailBaseAdapter(Context context, List<CaseHistory> list)
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
        ImageView medical_record_image;
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
            viewHolder.medical_record_image = (ImageView) convertView.findViewById(R.id.medical_record_image);
            convertView.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.list_id.setText(String.valueOf(list.get(position).getCid()));
        viewHolder.date.setText(list.get(position).getVisitDate().substring(0,10));
        viewHolder.hospital_name.setText(list.get(position).getHospitalName());
        viewHolder.department.setText(list.get(position).getOffice());
        String fileName = list.get(position).getPhoto();
        Bitmap bitmap = BitmapFactory.decodeFile(MyApplication.getContext().getFilesDir().getAbsolutePath() + "/image/" + fileName.substring(fileName.lastIndexOf("\\") + 1));
        viewHolder.medical_record_image.setImageBitmap(bitmap);
        return convertView;
    }
}
