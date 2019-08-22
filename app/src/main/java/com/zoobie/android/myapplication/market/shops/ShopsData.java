package com.zoobie.android.myapplication.market.shops;

public class ShopsData {
    public static final int NEPTUN_ID = 201;
    public static final int BARAZ_STORE_ID = 202;
    public static String getShopName(final int id) {
        switch (id) {
            case NEPTUN_ID:
                return "Neptun market";
            case BARAZ_STORE_ID:
                return "Bazar Store";
            default:
                return "Unknown market";
        }
    }
}
