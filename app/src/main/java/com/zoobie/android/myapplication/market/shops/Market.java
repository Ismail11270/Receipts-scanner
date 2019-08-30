package com.zoobie.android.myapplication.market.shops;

import java.util.ArrayList;
import java.util.Arrays;

public interface Market {
    ArrayList<String> getProductNameKeywords();

    ArrayList<String> getProductAmountKeywords();

    ArrayList<String> getProductPriceKeywords();

    ArrayList<String> getLowerLimitKeywords();

    String getAddress();
    int getId();

    static Market getInstance(int id, String address) {
        switch (id) {
            case Neptun.ID:
                return new Neptun(address);
            case Bravo.ID:
                return new Bravo(address);
            case Araz.ID:
                return new Araz(address);
            default:
                return null;
        }
    }

    class Neptun implements Market {
        public final static int ID = ShopsData.NEPTUN_ID;

        private final String PRODUCT_NAME_KEYWORDS[] = {"mehsulun", "mehsulunadi", "mehsulun adi"};
        private final String PRODUCT_AMOUNT_KEYWORDS[] = {"miq", "mlq", "miq.", "mq", "mig", "mig."};
        private final String PRODUCT_PRICE_KEYWORDS[] = {"qiy", "qly", "qiy.", "qly.", "qy", "aiy.", "aiy", "aly.", "aly"};
        //ToDO: Make the data extractor recognize table end
        private final String LOWER_LIMIT_KEYWORDS[] = {"TOPKDV"};
        private String address;

        public Neptun(String address) {
            this.address = address;
        }

        public String getAddress() {
            return address;
        }

        public int getId() {
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

    //todo setup bravo
    class Bravo implements Market {

        public static final int ID = ShopsData.BRAVO_ID;
        private String address;

        public Bravo(String address) {
            this.address = address;
        }

        public String getAddress() {
            return address;
        }

        @Override
        public ArrayList<String> getProductNameKeywords() {
            return new ArrayList<String>(Arrays.asList("1", "2"));
        }

        @Override
        public ArrayList<String> getProductAmountKeywords() {
            return new ArrayList<String>(Arrays.asList("1", "2"));
        }

        @Override
        public ArrayList<String> getProductPriceKeywords() {
            return new ArrayList<String>(Arrays.asList("1", "2"));
        }

        @Override
        public ArrayList<String> getLowerLimitKeywords() {
            return new ArrayList<String>(Arrays.asList("1", "2"));
        }

        @Override
        public int getId() {
            return ID;
        }
    }

    //Todo Setup araz
    class Araz implements Market {
        public final static int ID = ShopsData.ARAZ_ID;
        private String address;

        public Araz(String address) {
            this.address = address;
        }

        public String getAddress() {
            return address;
        }

        @Override
        public ArrayList<String> getProductNameKeywords() {
            return null;
        }

        @Override
        public ArrayList<String> getProductAmountKeywords() {
            return null;
        }

        @Override
        public ArrayList<String> getProductPriceKeywords() {
            return null;
        }

        @Override
        public ArrayList<String> getLowerLimitKeywords() {
            return null;
        }

        @Override
        public int getId() {
            return 0;
        }
    }


}


