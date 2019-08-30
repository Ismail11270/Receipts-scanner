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
public class FirstTabFragment extends Fragment {

    TextView textView;
    Button showPurchasesBtn;

    public FirstTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first_tab, container, false);
        textView = view.findViewById(R.id.fragment_textview);
        textView.setText(getArguments().getString("message") + " " + getArguments().getInt("id"));
        showPurchasesBtn = view.findViewById(R.id.showpurchasesbtn);
        showPurchasesBtn.setOnClickListener(v -> {
            ProductsDB db = new ProductsDB(getContext());
            ArrayList<Receipt> receipts = db.getEveryReceipt();
            if (receipts.size() > 0)
                textView.setText(receipts.toString());
            else Toast.makeText(getContext(), "Not purchases yet", Toast.LENGTH_SHORT).show();


        });
        if (getArguments().getInt("id") == 2) {
            textView.setBackgroundColor(Color.GREEN);
        }
        return view;
    }

}
