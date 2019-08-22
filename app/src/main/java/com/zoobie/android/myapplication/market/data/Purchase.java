package com.zoobie.android.myapplication.market.data;


import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;

public class Purchase {

    public enum PurchaseType {
        SHOPPING,
        ENTERTAINMENT,
        BILLS_PAYMENT,
        HEALTHCARE,
        SIGNLE_PURCHASE
    }

    String name;
    String date;
    String time;
    String address;
    int STORE_ID;
    ArrayList<Product> products;
    String description;
    PurchaseType type;

    private Purchase(){

    }
    private Purchase(PurchaseType type, String name, String address, String date, String time, String description, ArrayList<Product> products) {
        this.type = type;
        this.name = name;
        this.description = description;

    }

    public static Purchase newProductsPurchase(){
        return new Purchase();
    }


    /**
     *
     * @return
     */
    public static Purchase newSinglePurchase(String where, Date date, @Nullable String address, @Nullable String description){
        return new Purchase();
    }
}
