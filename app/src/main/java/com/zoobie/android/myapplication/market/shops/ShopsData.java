package com.zoobie.android.myapplication.market.shops;

public class ShopsData {
    public static final int NEPTUN_ID = 1101;
    public static final int BRAVO_ID = 1102;

    //add markets here
    public static int[] avialableMarkets = {
            NEPTUN_ID
    };

    public static String getShopName(final int id) {
        switch (id) {
            case NEPTUN_ID:
                return "Neptun market";
            default:
                return "Unknown market";
        }
    }
}
