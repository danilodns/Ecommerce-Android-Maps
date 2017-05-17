package com.danilons.ecommerce;

import io.realm.RealmObject;

/**
 * Created by danilons on 2017-04-21.
 */

public class RealmInt extends RealmObject {
    private int val;

    public RealmInt() {
    }

    public void RealmInt(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
