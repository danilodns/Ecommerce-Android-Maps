package com.danilons.ecommerce;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by danilons on 2017-04-22.
 */

public class CustomAdapterVendor extends ArrayAdapter<String> {
    private String[] itemname;
    Context mContext;
    public CustomAdapterVendor(Activity context, String[] itemname){
        super(context,R.layout.vendors_row, itemname);
        this.itemname = itemname;
        this.mContext = context;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView == null) { // for the first time, inflate the view
            LayoutInflater inflater =  LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.vendors_row, parent, false);
        }
        TextView nameVendor = (TextView) convertView.findViewById(R.id.Itemname);
        nameVendor.setText(itemname[position]);
        return convertView;
    }
}
