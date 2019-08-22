package com.zoobie.android.myapplication.ui.tabs;


import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zoobie.android.myapplication.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FirstTabFragment extends Fragment {

    TextView textView;
    public FirstTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first_tab,container,false);
        textView = view.findViewById(R.id.fragment_textview);
        textView.setText(getArguments().getString("message") + " " + getArguments().getInt("id"));
        if (getArguments().getInt("id") == 2){
            textView.setBackgroundColor(Color.GREEN);
        }
        return view;
    }

}
