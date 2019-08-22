package com.zoobie.android.myapplication.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.zoobie.android.myapplication.market.data.Product;
import com.zoobie.android.myapplication.market.shops.Market;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.zoobie.android.myapplication.market.shops.ShopsData.NEPTUN_ID;
import static com.zoobie.android.myapplication.market.shops.ShopsData.getShopName;

public class ProductsDB {
    SQLiteDatabase db;
    Context context;
    SharedPreferences sharedPreferences;
    public ProductsDB(Context context){
        this.context = context;
        db = context.openOrCreateDatabase("shopping",MODE_PRIVATE,null);
        sharedPreferences = context.getSharedPreferences("com.zoobie.android.myapplication",MODE_PRIVATE);
        boolean isNew = sharedPreferences.getBoolean("first_start",true);
        if (isNew) {
            db.execSQL("" +
                    "CREATE TABLE IF NOT EXISTS shops (" +
                    "id INT," +
                    "name VARCHAR(30)," +
                    "address VARCHAR(30)" +
                    ")");
            db.execSQL("" +
                    "CREATE TABLE IF NOT EXISTS shoppings (" +
                    //rowid int
                    "shop_id INT," +
                    "date TIMESTAMP," +
                    "comment TEXT(200)," +
                    "FOREIGN KEY (shop_id) REFERENCES shops(row_id)" +
                    " )");
            db.execSQL("" +
                    "CREATE TABLE IF NOT EXISTS purchases (" +
                    //rowid int
                    "shopping_id INT," +
                    "product_name VARCHAR(30)," +
                    "product_price FLOAT," +
                    "product_amount FLOAT," +
                    "FOREIGN KEY (shopping_id) REFERENCES shoppings(rowid)" +
                    ")");
            sharedPreferences.edit().putBoolean("is_new",false).apply();
            addNewShop(NEPTUN_ID, "null");
        }

    }
    public boolean addNewShop(final int SHOP_ID, String address){
        db.execSQL("INSERT INTO shops (id,name,address) VALUES( " + NEPTUN_ID + ",'" + getShopName(NEPTUN_ID) + "'," + address + ")");
        return true;
    }



    public void addNewShopping(List<Product> products, Market market) {
        db.execSQL("INSERT INTO shoppings (shop_id,date,comment) VALUES("+market.getId()+",null,null)");
        Cursor c = db.rawQuery("SELECT MAX(rowid) FROM shoppings",null);
        c.moveToFirst();
        for(Product product : products){
            insertPurchase(product,c.getInt(0));
            System.out.println(product.getName() + " was saved in the db");
        }
        Toast.makeText(context, "Data Save Successfully", Toast.LENGTH_SHORT).show();

    }

    private void insertPurchase(Product product,int shopping_id) {
        db.execSQL("INSERT INTO purchases (shopping_id,product_name,product_price,product_amount) " +
                                        "VALUES("+shopping_id+",'"+product.getName()+"',"+product.getPrice()+","+product.getAmount()+")");
    }
}