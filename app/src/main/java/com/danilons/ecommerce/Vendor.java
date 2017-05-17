package com.danilons.ecommerce;

import com.google.android.gms.maps.model.LatLng;

import io.realm.RealmObject;

/**
 * Created by danilons on 2017-04-22.
 */

public class Vendor extends RealmObject{
    private int id;
    private String Name;
    private String address;
    private double x;
    private double y;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    public void setLocation(double x, double y){
        this.x = x;
        this.y = y;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public LatLng getLocation(){
        LatLng pos = new LatLng(this.x,this.y);
        return pos;
    }
}
