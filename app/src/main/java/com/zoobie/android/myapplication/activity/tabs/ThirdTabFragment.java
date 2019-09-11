package com.zoobie.android.myapplication.activity.tabs;


import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zoobie.android.myapplication.R;
import com.zoobie.android.myapplication.adapters.ProductsExpandableListAdapter;
import com.zoobie.android.myapplication.market.data.Receipt;
import com.zoobie.android.myapplication.storage.ProductsDB;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThirdTabFragment extends Fragment {

    ExpandableListView productsParentList;
    TextView textView;
    Button showPurchasesBtn;
    FloatingActionButton addProductFab;
    ProductsDB db;
    public ThirdTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_third_tab, container, false);
        textView = view.findViewById(R.id.fragment_textview);
        textView.setVisibility(View.GONE);
        productsParentList = view.findViewById(R.id.productsParentList);
        addProductFab = view.findViewById(R.id.addProductFab);
        db = new ProductsDB(getContext());

        ProductsExpandableListAdapter adapter = new ProductsExpandableListAdapter(getContext(),db);

        productsParentList.setAdapter(adapter);

        productsParentList.setOnGroupExpandListener(position -> {
            Toast.makeText(getContext(), "List expanded", Toast.LENGTH_SHORT).show();
        });

        productsParentList.setOnGroupCollapseListener(position -> {
            Toast.makeText(getContext(), "List Collapsed", Toast.LENGTH_SHORT).show();
        });





        if(adapter.getGroupCount() == 0) textView.setVisibility(View.VISIBLE);


        addProductFab.setOnClickListener(v -> {


        });
        return view;
    }

}
