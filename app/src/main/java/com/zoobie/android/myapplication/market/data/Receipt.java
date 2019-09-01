package com.zoobie.android.myapplication.market.data;


import com.zoobie.android.myapplication.market.shops.Currency;
import com.zoobie.android.myapplication.market.shops.ShopsData;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Receipt {



    private Timestamp date;
    private int ID;
    private String address;
    private int STORE_ID;
    private ArrayList<Product> products;
    private String description;
    private ReceiptType type;
    private float total;
    private Currency currency;
    private int uniqueStoreId;
    private int rowid;

    public int getRowid() {
        return rowid;
    }

    public void setRowid(int rowid) {
        this.rowid = rowid;
    }

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

    public Receipt(int ID, int STORE_ID, Timestamp date, String address, String description, ArrayList<Product> products) {
        this.type = ReceiptType.SHOPPING_RECEIPT;
        this.description = description;
        this.ID = ID;
        this.STORE_ID = STORE_ID;
        this.address = address;
        this.date = date;
        this.products = products;
        this.total = 0;
    }

    public int getUniqueStoreId(){
        return uniqueStoreId;
    }
    public Receipt(int rowid, int uniqueStoreId, int storeId, Timestamp date, String address, String comment, float total){
        this.rowid = rowid;
        this.uniqueStoreId = uniqueStoreId;
        this.type = ReceiptType.SHOPPING_RECEIPT;
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


    public enum ReceiptType {
        SHOPPING_RECEIPT
    }
}
