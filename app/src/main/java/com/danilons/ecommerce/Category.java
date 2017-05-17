package com.danilons.ecommerce;

import io.realm.RealmObject;

/**
 * Created by danilons on 2017-04-20.
 */

public class Category extends RealmObject {
    private String catName;

    public String getCatName(){
        return this.catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

}
