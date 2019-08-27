package com.zoobie.android.myapplication.market.shops;
import java.util.ArrayList;

public abstract class Market {
    public abstract ArrayList<String> getProductNameKeywords();
    public abstract ArrayList<String> getProductAmountKeywords();
    public abstract ArrayList<String> getProductPriceKeywords();
    public abstract ArrayList<String> getLowerLimitKeywords();
    public abstract int getId();
}
