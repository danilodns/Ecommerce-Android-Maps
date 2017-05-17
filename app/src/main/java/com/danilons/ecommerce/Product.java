package com.danilons.ecommerce;

import java.io.Serializable;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by danilons on 2017-04-20.
 */

public class Product extends RealmObject {
    private int Id;
    private double price;
    private String proDesc;
    private String proModel;
    private Category proCat;
    private int quantity;
    private RealmList<RealmInt> imgID;
    private RealmList<Vendor> vendors;

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setProDesc(String proDesc) {
        this.proDesc = proDesc;
    }

    public String getProDesc() {
        return proDesc;
    }

    public void setProModel(String proModel) {
        this.proModel = proModel;
    }

    public String getProModel() {
        return proModel;
    }

    public void setProCat(Category proCat) {
        this.proCat = proCat;
    }

    public Category getProCat() {
        return proCat;
    }

    public void setImgID(Realm realm, int[] imgID) {
        RealmList<RealmInt> list = new RealmList<RealmInt>();

        for (int img: imgID) {
            RealmInt rInt = realm.createObject(RealmInt.class);
            rInt.RealmInt(img);
            list.add(rInt);
        }
        this.imgID = list;
    }

    public int[] getImgID() {
        int[] imagesId = new int[this.imgID.size()];
        for (int i=0; i<this.imgID.size(); i++) {
            if (imgID.get(i) != null) {
                imagesId[i] = imgID.get(i).getVal();
            }
        }

        return imagesId;
    }

    public void setImgIDs(RealmList<RealmInt> imgID) {
        this.imgID = imgID;
    }
    public RealmList<RealmInt> getImagId(){
        return this.imgID;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public RealmList<Vendor> getVendors() {
        return vendors;
    }

    public void setVendors(RealmList<Vendor> vendors) {
        this.vendors = vendors;
    }

}
