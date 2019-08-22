package com.zoobie.android.myapplication.market.data;

public class Product {
    private String name;
    private float amount;
    private float price;
    private float cost;

    public Product(String name, float amount, float price){
        this.amount = amount;
        this.name = name;
        this.price = price;
        this.cost = price * amount;
        System.out.println("product created");
    }

    public String getName() {
        return name;
    }

    public float getAmount() {
        return amount;
    }

    public float getPrice() {
        return price;
    }

    public float getCost() {
        return cost;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return name + " " + amount + " pcs " + price + "$";
    }
}
