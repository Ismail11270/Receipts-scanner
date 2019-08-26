package com.zoobie.android.myapplication.market.shops;


import java.util.ArrayList;
import java.util.Arrays;

public class Neptun extends Market {
    private final String PRODUCT_NAME_KEYWORDS[] = {"mehsulun","mehsulunadi","mehsulun adi"};
    private final String PRODUCT_AMOUNT_KEYWORDS[] = {"miq","mlq","miq.","mq","mig","mig."};
    private final String PRODUCT_PRICE_KEYWORDS[] = {"qiy","qly","qiy.","qly.","qy","aiy.","aiy","aly."};
    private final String LOWER_LIMIT_KEYWORDS[] = {"TOPKDV"};
    private final int ID = ShopsData.NEPTUN_ID;
    public int getId(){
        return ID;
    }
    @Override
    public ArrayList<String> getProductNameKeywords() {
        return new ArrayList<>(Arrays.asList(PRODUCT_NAME_KEYWORDS));
    }

    @Override
    public ArrayList<String> getProductAmountKeywords() {
        return new ArrayList<>(Arrays.asList(PRODUCT_AMOUNT_KEYWORDS));
    }

    @Override
    public ArrayList<String> getProductPriceKeywords() {
        return new ArrayList<>(Arrays.asList(PRODUCT_PRICE_KEYWORDS));
    }

    @Override
    public ArrayList<String> getLowerLimitKeywords() {
        return new ArrayList<>(Arrays.asList(LOWER_LIMIT_KEYWORDS));
    }
}


