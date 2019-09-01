package com.zoobie.android.myapplication.activity.tabs;


import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zoobie.android.myapplication.R;
import com.zoobie.android.myapplication.market.data.Receipt;
import com.zoobie.android.myapplication.storage.ProductsDB;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThirdTabFragment extends Fragment {

    TextView textView;
    Button showPurchasesBtn;

    public ThirdTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_third_tab, container, false);
        textView = view.findViewById(R.id.fragment_textview);


        return view;
    }

}
