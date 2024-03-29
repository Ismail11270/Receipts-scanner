package com.zoobie.android.myapplication.market.shops;

public enum Currency {
    USD("USD", 1, 1, "$") {

    },
    AZN("AZN", 0.6f, 2, "AZN") {

    },
    EUR("EUR", 1.1f, 2, "€") {

    };

    public static final Currency DEFAULT_CURRENCY = USD;

    private String displayName;
    private float value;
    public int id;
    public String symbol;
    Currency(String currency, float value, int id,String symbol) {
        this.id = id;
        this.symbol = symbol;
        this.displayName = currency;
        this.value = value;
    }

    public String getSymbol(){
        return this.symbol;
    }
    public static Currency getCurrencyFromId(int id) {
        switch (id) {
            case 1:
                return USD;
            case 2:
                return AZN;
            case 3:
                return EUR;
            default:
                return USD;
        }
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float convert(Currency to, float amount) {
        return convert(this, to, amount);
    }

    public float convert(Currency from, Currency to, float amount) {
        return (amount * to.value) / from.value;
    }


}
