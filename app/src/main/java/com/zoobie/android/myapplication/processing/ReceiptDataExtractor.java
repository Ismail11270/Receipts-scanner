package com.zoobie.android.myapplication.processing;

import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.zoobie.android.myapplication.market.data.Product;
import com.zoobie.android.myapplication.market.shops.Market;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

public class ReceiptDataExtractor {

    private SparseArray<TextBlock> items;
    private ArrayList<Product> productsList;

    private StringBuilder sb;
    private Text productName;
    private Text productAmount;
    private Text productPrice;
    private TreeMap<Integer, Text> dataEntries;
    private List<Text> wordsList;

    private Market market;
    private TreeMap<Integer, String> pricesMap, amountsMap;
    private TreeMap<Integer, String> namesMap;

    public ReceiptDataExtractor(SparseArray<TextBlock> items, Market market) {
        this.items = items;
        this.market = market;
        productsList = new ArrayList<>();
        wordsList = new ArrayList<>();
        dataEntries = new TreeMap<>();
        dataEntries = new TreeMap<>();
        boolean first = true, seconds = true, third = true;
        for (int i = 0; i < items.size(); i++) {
            TextBlock myItem = items.valueAt(i);
            for (Text line : myItem.getComponents()) {
                dataEntries.put(line.getCornerPoints()[0].y, line);
                for (Text word : line.getComponents()) {
                    Log.i("constructor", word.getValue());
                    if (market.getProductNameKeywords().contains(word.getValue().toLowerCase()) && first) {
                        this.productName = word;
                        System.out.println(word.getValue() + " found ");
                        first = false;
                    } else if (market.getProductAmountKeywords().contains(word.getValue().toLowerCase()) && seconds) {
                        this.productAmount = word;
                        System.out.println(word.getValue() + " found ");
                        seconds = false;
                    } else if (market.getProductPriceKeywords().contains(word.getValue().toLowerCase()) && third) {
                        this.productPrice = word;
                        System.out.println(word.getValue() + " found ");
                        third = false;
                    } else {
                        wordsList.add(word);
                    }
                }
            }
        }
    }

        //todo add bottom table borders check
    public ArrayList<Product> extractProducts() throws NullPointerException {
        if (productName == null || productAmount == null || productPrice == null)
            throw new NullPointerException("Failed to recognize data. Columns undetected");
        sb = new StringBuilder();
        //get text from sb until there is no text left
        String name = "";

        pricesMap = new TreeMap<>();
        amountsMap = new TreeMap<>();
        namesMap = new TreeMap<>();
        TreeSet<Integer> keys = new TreeSet<>(dataEntries.keySet());
        boolean foundName = false;
        for (Integer key : keys) {
//        for(int i = 0; i < dataEntries.size(); i++){
            Text line = dataEntries.get(key);
            Text tempName = null;

            for (Text word : line.getComponents()) {
                if (word.getCornerPoints()[1].x < productAmount.getCornerPoints()[0].x && word.getCornerPoints()[0].y > productName.getCornerPoints()[2].y) {
                    name += word.getValue() + " ";
                    foundName = true;
                    tempName = word;
                } else if (word.getCornerPoints()[0].x > productPrice.getCornerPoints()[0].x - 50
                        && word.getCornerPoints()[0].x < productPrice.getCornerPoints()[1].x
                        && word.getCornerPoints()[0].y > productPrice.getCornerPoints()[2].y) {
                    pricesMap.put(word.getCornerPoints()[0].y, word.getValue());
//                    System.out.println(word.getValue() + " price");
                } else if (word.getCornerPoints()[0].x > productAmount.getCornerPoints()[0].x - 50
                        && word.getCornerPoints()[0].x < productPrice.getCornerPoints()[0].x
                        && word.getCornerPoints()[0].y > productAmount.getCornerPoints()[2].y) {
                    amountsMap.put(word.getCornerPoints()[0].y, word.getValue());
//                    System.out.println(word.getValue() + " amount");
                }
            }
            if (foundName) {
//                System.out.println(lines);
                namesMap.put(tempName.getCornerPoints()[0].y, name);
                name = "";
                foundName = false;
            }
        }
        System.out.println(namesMap.size() + " " + amountsMap.size() + " " + pricesMap.size());
        productsList = getProductsList();
        TreeSet<Integer> nameKeys = new TreeSet<>(namesMap.keySet());
        TreeSet<Integer> amountKeys = new TreeSet<>(amountsMap.keySet());
        TreeSet<Integer> priceKeys = new TreeSet<>(pricesMap.keySet());
//        for(int i = 0; i < namesMap.size(); i++){
//            System.out.println(namesMap.get(nameKeys.pollFirst()) + " " + amountsMap.get(amountKeys.pollFirst()) + " " + pricesMap.get(priceKeys.pollFirst()));
//        }
        return productsList;
    }

    private ArrayList<Product> getProductsList() {
        ArrayList<Product> products = new ArrayList<>();
        TreeSet<Integer> nameKeys = new TreeSet<>(namesMap.keySet());
        TreeSet<Integer> amountKeys = new TreeSet<>(amountsMap.keySet());
        TreeSet<Integer> priceKeys = new TreeSet<>(pricesMap.keySet());
        int size = max(nameKeys.size(), amountKeys.size(), priceKeys.size());
        Integer[] nameKey = new Integer[nameKeys.size()], amountKey = new Integer[amountKeys.size()], priceKey = new Integer[priceKeys.size()];
        nameKeys.toArray(nameKey);
        amountKeys.toArray(amountKey);
        priceKeys.toArray(priceKey);
        for (int i = 0, j = 0, k = 0; i < nameKey.length && j < amountKey.length && k < priceKey.length; ) {
            System.out.println(i + " " + j + " " + k);
            if (Math.abs(nameKey[i] - amountKey[j]) < 70 && Math.abs(amountKey[j] - priceKey[k]) < 70) {
                String name = namesMap.get(nameKey[i]);
                float amount, price;
                try {
                    amount = Float.parseFloat(amountsMap.get(amountKey[j]).replace(',', '.'));
                    price = Float.parseFloat(pricesMap.get(priceKey[k]).replace(',', '.'));
                } catch (NumberFormatException e) {
                    amount = 0;
                    price = 0;
                }
                products.add(new Product(name, amount, price));
                i++;
                j++;
                k++;
            } else {
                String name = namesMap.get(nameKey[i]);
                float amount, price;
                try {
                    amount = Float.parseFloat(amountsMap.get(amountKey[j]).replace(',', '.'));
                    price = Float.parseFloat(pricesMap.get(priceKey[k]).replace(',', '.'));
                } catch (NumberFormatException e) {
                    amount = 0;
                    price = 0;
                }
                int pos = min(nameKey[i], amountKey[j], priceKey[k]);

                boolean a = false, b = false, c = false;
                if (Math.abs(nameKey[i] - pos) > 70) {
                    namesMap.put(pos, "null");
                    a = true;
                    name = " ";
                }
                if (Math.abs(amountKey[j] - pos) > 70) {
                    amountsMap.put(pos, "null");
                    b = true;
                    amount = 0f;
                }
                if (Math.abs(priceKey[k] - pos) > 70) {
                    pricesMap.put(pos, "null");
                    c = true;
                    price = 0f;
                }
                if (!a) i++;
                if (!b) j++;
                if (!c) k++;
                products.add(new Product(name, amount, price));
            }
        }
        return products;
    }

    Integer min(Integer a, Integer b, Integer c) {
        int temp = a <= b ? a : b;
        return temp <= c ? temp : c;
    }

    int max(int a, int b, int c) {
        int temp = a >= b ? a : b;
        return temp >= c ? temp : c;
    }

    public List<Product> getProducts() {
        return productsList;
    }
}