package com.zoobie.android.myapplication.market.shops;

public class ShopsData {
    public static final int NEPTUN_ID = 0;
    public static final int BRAVO_ID = 1;
    public static final int ARAZ_ID = 2;

    //add markets here
    public static int[] avialableMarkets = {
            NEPTUN_ID, BRAVO_ID, ARAZ_ID
    };

    public static String getShopName(final int id) {
        switch (id) {
            case NEPTUN_ID:
                return "Neptun market";
            case BRAVO_ID:
                return "Bravo";
            case ARAZ_ID:
                return "Araz Supermarket";
            default:
                return "Unknown market";
        }
    }

}
