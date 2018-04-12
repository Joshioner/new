package com.blk.pharmacy.homePage.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blk.R;
import com.blk.pharmacy.homePage.entity.drug_list_detail;

import java.util.List;

/**
 * Created by asus on 2017/11/4.
 */

public class drugListDetailBaseAdapter extends BaseAdapter{
    private Context context;
    private List<drug_list_detail> list;

    public drugListDetailBaseAdapter(Context context, List<drug_list_detail> list)
    {
        this.context = context;
        this.list = list;
    }
    private class ViewHolder
    {
        TextView pharmacy_name;
        TextView drug_name;
        TextView drug_function;
        TextView price_int;
        TextView price_double;
        TextView payNumber;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.drug_list_detail,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.pharmacy_name = (TextView) convertView.findViewById(R.id.pharmacyName);
            viewHolder.drug_name = (TextView) convertView.findViewById(R.id.drug_name);
            viewHolder.drug_function = (TextView) convertView.findViewById(R.id.drug_function);
            viewHolder.price_int = (TextView) convertView.findViewById(R.id.price_int);
            viewHolder.price_double = (TextView) convertView.findViewById(R.id.price_double);
            viewHolder.payNumber = (TextView) convertView.findViewById(R.id.payNumber);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.pharmacy_name.setText(list.get(position).getPharmacyName());
        viewHolder.drug_name.setText(list.get(position).getDrugName());
        viewHolder.drug_function.setText(list.get(position).getDrugFunction());
        viewHolder.price_int.setText(list.get(position).getPriceInt());
        viewHolder.price_double.setText(list.get(position).getPriceDouble());
        viewHolder.payNumber.setText(list.get(position).getPayNumber());
        return convertView;
    }
}
