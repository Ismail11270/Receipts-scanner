package com.zoobie.android.myapplication.market.data;


import androidx.annotation.Nullable;

import com.zoobie.android.myapplication.market.shops.ShopsData;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class Receipt {
    public String getAddress() {
        return address;
    }

    public Timestamp getDate() {
        return date;
    }

    public int getSTORE_ID() {
        return STORE_ID;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public String getDescription() {
        return description;
    }

    public float getTotal() { return total; }
    public PurchaseType getType() {
        return type;
    }

    //ToDo when adding different purchase types, only adding receipts possible rn
    public enum PurchaseType {
        SHOPPING,
        ENTERTAINMENT,
        BILLS_PAYMENT,
        HEALTHCARE,
        SIGNLE_PURCHASE
    }

    private Timestamp date;
    private int ID;
    private String address;
    private int STORE_ID;
    private ArrayList<Product> products;
    private String description;
    private PurchaseType type;
    private float total;
    private int uniqueStoreId;
    private Receipt(){

    }
    public Receipt(int ID, int STORE_ID, Timestamp date, String address, String description, ArrayList<Product> products) {
        this.type = PurchaseType.SHOPPING;
        this.description = description;
        this.ID = ID;
        this.STORE_ID = STORE_ID;
        this.address = address;
        this.date = date;
        this.products = products;
        this.total = 0;
    }

    public Receipt(int uniqueStoreId, int storeId, Timestamp date, String address, String comment, float total){
        this.uniqueStoreId = uniqueStoreId;
        this.type = PurchaseType.SHOPPING;
        this.STORE_ID=storeId;
        this.date = date;
        this.address = address;
        this.description = comment;
        this.total = total;
    }

    @Override
    public String toString() {
        return ShopsData.getShopName(STORE_ID) + " " +
                address + " " +
                date.toString() + " " +
                description + " " +
                total + "$";
    }
}
