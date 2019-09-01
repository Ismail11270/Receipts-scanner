package com.zoobie.android.myapplication.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zoobie.android.myapplication.market.data.Product;
import com.zoobie.android.myapplication.market.data.Receipt;
import com.zoobie.android.myapplication.market.shops.Market;

import java.sql.RowId;
import java.sql.RowIdLifetime;
import java.sql.Timestamp;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.zoobie.android.myapplication.market.shops.ShopsData.getShopName;


//ToDo fix db
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
                    "DROP TABLE IF EXISTS shops");
            db.execSQL("" +
                    "DROP TABLE IF EXISTS shoppings");
            db.execSQL("" +
                    "DROP TABLE IF EXISTS purchases");
            db.execSQL("" +
                    "CREATE TABLE IF NOT EXISTS shops (" +
                    "id INT," +
                    "name VARCHAR(30)," +
                    "address VARCHAR(30)" +
                    ")");
            db.execSQL("" +
                    "CREATE TABLE shoppings (" +
                    //rowid int
                    "shop_id INT," +
                    "date TIMESTAMP," +
                    "comment TEXT(200)," +
                    "total INT," +
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
            sharedPreferences.edit().putBoolean("first_start",false).apply();
//            addNewShop(NEPTUN_ID, "null");
        }

    }


    public void updateReceiptData(Receipt receipt) {
        int rowid = receipt.getRowid();
        int shop_id = receipt.getUniqueStoreId();
        long date = receipt.getDate().getTime();
        float total = 0;
        String comment = receipt.getDescription();
        ArrayList<Product> products = receipt.getProducts();

        Log.i("rowid",rowid+" updateReceiptData");


        //program removing all the old products, and puts all the new ones

        //TODO add a solution to detect if a product is deleted or updated and modify the db according to that
        deleteProductsForReceipt(rowid);
        for(Product p : products){
            total += p.getPrice() * p.getAmount();
            insertPurchase(p,rowid);
        }
        int temp = (int)(total*100);
        total = (float)temp / 100;

        db.execSQL("UPDATE shoppings SET date="+date+", comment='"+comment+"', total="+total+" WHERE rowid="+rowid);
    }

    private void deleteProductsForReceipt(int receiptId) {
        db.execSQL("DELETE FROM purchases WHERE shopping_id="+receiptId);
    }

    public ArrayList<Receipt> getEveryReceipt(){
        ArrayList<Receipt> receipts = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT s.rowid as rowid_shop,id,date,comment,address,total,shop_id FROM shoppings s JOIN shops sh on s.shop_id=sh.rowid ORDER BY date",null);
        int shopIdCl = c.getColumnIndex("id");
        int dateCl = c.getColumnIndex("date");
        int commentCl = c.getColumnIndex("comment");
        int addressCl = c.getColumnIndex("address");
        int totalCl = c.getColumnIndex("total");
        int shopUniqueId= c.getColumnIndex("shop_id");
        int rowidCl = c.getColumnIndex("rowid_shop");

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Log.i("shopid",c.getInt(shopIdCl)+" getEveryReceipt");
            receipts.add(new Receipt(c.getInt(rowidCl),c.getInt(shopUniqueId), c.getInt(shopIdCl), new Timestamp(c.getLong(dateCl)), c.getString(addressCl), c.getString(commentCl), c.getFloat(totalCl)));
        }
        c.close();
        return receipts;
    }

    public Receipt getSingleReceipt(int id){
        Cursor c = db.rawQuery("SELECT s.rowid as rowid_shop,id,date,comment,address,total,shop_id FROM shoppings s JOIN shops sh on s.shop_id=sh.rowid WHERE s.rowid="+id+" ORDER BY date",null);
        int shopIdCl = c.getColumnIndex("id");
        int dateCl = c.getColumnIndex("date");
        int commentCl = c.getColumnIndex("comment");
        int addressCl = c.getColumnIndex("address");
        int totalCl = c.getColumnIndex("total");
        int shopUniqueId= c.getColumnIndex("shop_id");
        int rowidCl = c.getColumnIndex("rowid_shop");
        c.moveToFirst();
        Log.i("rowid",c.getInt(rowidCl)+" getSingleReceipt");


        return new Receipt(c.getInt(rowidCl),c.getInt(shopUniqueId), c.getInt(shopIdCl),
                new Timestamp(c.getLong(dateCl)), c.getString(addressCl),
                c.getString(commentCl), c.getFloat(totalCl));
    }

    public ArrayList<Product> getProducts(int receiptId) {
        ArrayList<Product> productsList = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT * FROM purchases WHERE shopping_id="+receiptId,null);
        Log.i("rowid",receiptId+" getProducts");

        int productNameCl = c.getColumnIndex("product_name");
        int productPriceCl = c.getColumnIndex("product_price");
        int productAmountCl = c.getColumnIndex("product_amount");

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            productsList.add(new Product(c.getString(productNameCl),c.getFloat(productAmountCl),c.getFloat(productPriceCl)));
        }
        return productsList;
    }

    public Market queryStore(int id){
        Cursor c = db.rawQuery("SELECT id,address FROM shops WHERE rowid="+id,null);
        int idCl = c.getColumnIndex("id");
        int addressCl = c.getColumnIndex("address");

        c.moveToFirst();

        return Market.getInstance(c.getInt(idCl),c.getString(addressCl));
    }
    public boolean addNewShop(final int SHOP_ID, String address){
        db.execSQL("INSERT INTO shops VALUES( " + SHOP_ID + ",'" + getShopName(SHOP_ID) + "','" + address + "')");
        return true;
    }




    public void addNewShopping(Receipt receipt){
        Cursor c = db.rawQuery("SELECT rowid FROM shops WHERE id="+receipt.getSTORE_ID()+" AND address='"+ receipt.getAddress()+"'",null);
        if(c.getCount() == 0)
        {
            addNewShop(receipt.getSTORE_ID(), receipt.getAddress());
            c = db.rawQuery("SELECT rowid FROM shops WHERE id="+receipt.getSTORE_ID()+" AND address='"+ receipt.getAddress()+"'",null);
        }
        float cost = 0;
        for(Product product : receipt.getProducts()){
            cost+=product.getCost();
        }
        int temp = (int)(cost*100);
        cost = (float)temp/100;
        c.moveToFirst();
        db.execSQL("INSERT INTO shoppings VALUES(" + c.getInt(0) + ","+ receipt.getDate().getTime()+", '"+ receipt.getDescription()+"', "+cost+")");
        c = db.rawQuery("SELECT MAX(rowid) FROM shoppings",null);
        c.moveToFirst();
        Log.i("rowid",c.getInt(0)+" addNewShopping");

        for(Product product : receipt.getProducts()){
            insertPurchase(product,c.getInt(0));
            System.out.println(product.getName() + " was saved in the db");
        }
    }

    private void insertPurchase(Product product,int shopping_id) {
        db.execSQL("INSERT INTO purchases (shopping_id,product_name,product_price,product_amount) " +
                                        "VALUES("+shopping_id+",'"+product.getName()+"',"+product.getPrice()+","+product.getAmount()+")");
    }


    public void removeReceipt(Receipt receipt) {
        int receiptId = receipt.getRowid();
        deleteProductsForReceipt(receiptId);
        deleteReceipt(receiptId);
    }

    private void deleteReceipt(int receiptId) {
        db.execSQL("DELETE FROM shoppings WHERE rowid="+receiptId);
    }
}
