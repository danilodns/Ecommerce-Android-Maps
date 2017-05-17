package com.danilons.ecommerce;

import io.realm.RealmObject;

/**
 * Created by danilons on 2017-04-24.
 */

public class Cart extends RealmObject{
    private int userId;
    private int productId;
    private int quant;


    public int getProductId() {
        return productId;
    }

    public int getQuant() {
        return quant;
    }

    public int getUserId() {
        return userId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setQuant(int quant) {
        this.quant = quant;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
