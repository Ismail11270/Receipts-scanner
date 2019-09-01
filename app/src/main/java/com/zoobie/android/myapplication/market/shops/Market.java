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

        private final String PRODUCT_NAME_KEYWORDS[] = {"mehsutun","mehsuiun","mehsulun","mehsuun", "mehsulunadi", "mehsulun adi"};
        private final String PRODUCT_AMOUNT_KEYWORDS[] = {"miq", "mlq", "miq.", "mq", "mig", "mig.","mia"};
        private final String PRODUCT_PRICE_KEYWORDS[] = {"qiy", "qly", "qiy.", "qly.", "qy", "aiy.", "aiy", "aly.", "aly"};
        //ToDO: Make the data extractor recognize table end
        private final String LOWER_LIMIT_KEYWORDS[] = {"topkdv","toplam","topkddv"};
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

        private final String[] PRODUCT_NAME_KEYWORDS = {"melhsuiun","mehsulun", "mehsulunadi", "mehsulun adi"};
        private final String[] PRODUCT_AMOUNT_KEYWORDS = {"miq", "mlq", "miq.", "mq", "mig", "mig.","mia"};
        private final String[] PRODUCT_PRICE_KEYWORDS = {"qiy", "qly", "qiy.", "qly.", "qy", "aiy.", "aiy", "aly.", "aly"};
        private final String[] LOWER_LIMIT_KEYWORDS = { "yekun","yekun edv","yekunedv"};

        public Bravo(String address) {
            this.address = address;
        }

        public String getAddress() {
            return address;
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

        @Override
        public int getId() {
            return ID;
        }
    }

    //Todo Setup araz
    class Araz implements Market {
        public final static int ID = ShopsData.ARAZ_ID;
        private final String[] PRODUCT_NAME_KEYWORDS = {"mehsutun","mehsuiun","melhsuiun","mehsulun","mehsuun", "mehsulunadi", "mehsulun adi"};
        private final String[] PRODUCT_AMOUNT_KEYWORDS = {"mia","miq", "mlq", "miq.", "mq", "mig", "mig.","mia"};
        private final String[] PRODUCT_PRICE_KEYWORDS = {"qiy", "qly", "qiy.", "qly.", "qy", "aiy.", "aiy", "aly.", "aly"};
        private final String[] LOWER_LIMIT_KEYWORDS = { "yekun","yekun edv","yekunedv"};

        private String address;

        public Araz(String address) {
            this.address = address;
        }

        public String getAddress() {
            return address;
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
        @Override
        public int getId() {
            return ID;
        }
    }


}


