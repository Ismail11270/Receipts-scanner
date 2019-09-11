package com.zoobie.android.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.zoobie.android.myapplication.R;
import com.zoobie.android.myapplication.market.data.Product;
import com.zoobie.android.myapplication.storage.ProductsDB;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductsExpandableListAdapter extends BaseExpandableListAdapter {


    private HashMap<String, ArrayList<Product>> productsTable;
    private ArrayList<Product> products;
    private Context context;
    private ProductsDB db;

    public ProductsExpandableListAdapter(Context context, ProductsDB db) {
        this.context = context;
        this.db = db;
        productsTable = new HashMap<>();
        getChildren();
    }

    private void getChildren() {
        products = db.getEveryProduct();
        for (Product p : products) {
            if (!productsTable.containsKey(p.getName())) {
                productsTable.put(p.getName(), new ArrayList<>());
                productsTable.get(p.getName()).add(p);
            } else {
                productsTable.get(p.getName()).add(p);
            }
        }
    }

    @Override
    public int getGroupCount() {
        return products == null ? 0 : products.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return productsTable != null && productsTable.get(products.get(i).getName()) != null ?
                productsTable.get(products.get(i).getName()).size() : 0;
    }

    @Override
    public Object getGroup(int i) {
        return products.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return productsTable != null && productsTable.get(products.get(i).getName()) != null ?
                productsTable.get(products.get(i).getName()).get(i1) : null;
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(android.R.layout.simple_expandable_list_item_1, null, false);

        ((TextView) view).setText(products.get(groupPosition).getName());




        return view;
    }

    @Override
    public View getChildView(int parentPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, null, false);

        try {
            ((TextView) view).setText(productsTable.get(products.get(parentPosition).getName()).get(childPosition).getName());
        } catch (NullPointerException e) {

        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
