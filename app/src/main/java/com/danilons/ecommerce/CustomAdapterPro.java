package com.danilons.ecommerce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by danilons on 2017-04-20.
 */

public class CustomAdapterPro extends ArrayAdapter<Product> {
    private ArrayList<Product> dataSet;
    private ArrayList<Integer> quantity;
    Context mContext;


    public CustomAdapterPro(ArrayList<Product> data, Context context) {

        super(context, R.layout.row_product, data);
        this.dataSet = data;
        this.mContext=context;

    }
    public CustomAdapterPro(ArrayList<Product> data, ArrayList<Integer> qnts, Context context) {

        super(context, R.layout.row_product, data);
        this.dataSet = data;
        this.quantity = qnts;
        this.mContext=context;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) { // for the first time, inflate the view
            LayoutInflater inflater =  LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.row_product, parent, false);
        }

        TextView model = (TextView)convertView.findViewById(R.id.modelNameRow);
        TextView price = (TextView)convertView.findViewById(R.id.priceRow);
        TextView desc = (TextView)convertView.findViewById(R.id.descRow);
        TextView qnt = (TextView)convertView.findViewById(R.id.qntRow);
        ImageView proImg = (ImageView)convertView.findViewById(R.id.imageView);

        Product temp = dataSet.get(position);


        model.setText(temp.getProModel());
        price.setText("CAD "+temp.getPrice());
        desc.setText(temp.getProDesc());
        if(this.quantity == null){
            qnt.setText(""+temp.getQuantity());
        }else{

            qnt.setText(""+this.quantity.get(position));
        }
        //);
        proImg.setImageResource(temp.getImgID()[0]);

        return convertView;
    }
}
