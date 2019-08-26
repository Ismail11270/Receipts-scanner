package com.zoobie.android.myapplication.ui.tabs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zoobie.android.myapplication.R;
import com.zoobie.android.myapplication.market.data.Product;
import com.zoobie.android.myapplication.processing.ReceiptScanner;

import java.util.ArrayList;


public class SecondTabFragment extends Fragment {


    Resources res;
    ArrayList<Product> productsList;
    FloatingActionButton addFab, galleryFab, cameraFab;
    View galleryView, cameraView;
    final int REQUEST_CODE_ADD_NEW_RECEIPT = 101;
    private boolean fabActive = false;

    public SecondTabFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second_tab, container, false);
        initializeViews(view);

        initHiddenViews(galleryView);
        initHiddenViews(cameraView);

        addFab.setOnClickListener((v) -> {
            handleFab();
        });
        galleryFab.setOnClickListener((v)->{
            Intent intent = new Intent(getContext(), ReceiptScanner.class);
            intent.putExtra("source", ReceiptScanner.PICK_GALLERY);
            startActivityForResult(intent, REQUEST_CODE_ADD_NEW_RECEIPT);
        });
        cameraFab.setOnClickListener((v)->{
            Intent intent = new Intent(getContext(), ReceiptScanner.class);
            intent.putExtra("source", ReceiptScanner.PICK_CAMERA);
            startActivityForResult(intent, REQUEST_CODE_ADD_NEW_RECEIPT);
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (fabActive) {
            addFab.animate().rotation(45.f + 180.f).setDuration(0);
            fabActive = true;
            initHiddenViews(cameraView);
            initHiddenViews(galleryView);
            fabActive = false;
        }

    }

    private void handleFab() {
        if (!fabActive) {
            addFab.animate().rotation(45.f + 180.f).setDuration(400);
            fabActive = true;
            showView(galleryView);
            showView(cameraView);
        } else {
            addFab.animate().rotation(0).setDuration(200);
            fabActive = false;
            hideViews(galleryView);
            hideViews(cameraView);
        }
    }

    private void showView(View v) {
        v.setVisibility(View.VISIBLE);
        v.setAlpha(0f);
        v.setTranslationY(v.getHeight());
        v.animate()
                .setDuration(300)
                .translationY(0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                })
                .alpha(1f)
                .start();
    }

    private void initHiddenViews(View v) {
        v.setTranslationY(v.getHeight());
        v.setVisibility(View.GONE);
        v.setAlpha(0.f);
    }

    private void hideViews(View v){

        v.setAlpha(1f);
        v.setTranslationY(0);
        v.animate()
                .setDuration(300)
                .translationY(v.getHeight())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        v.setVisibility(View.GONE);
                        super.onAnimationEnd(animation);
                    }
                })
                .alpha(0f)
                .start();

    }

    private void initializeViews(View view) {
//        listView = view.findViewById(R.id.receiptsList);
        addFab = view.findViewById(R.id.add_fab);
        galleryFab = view.findViewById(R.id.add_from_gallery);
        cameraFab = view.findViewById(R.id.add_from_camera);
        galleryView = view.findViewById(R.id.add_from_gallery_view);
        cameraView = view.findViewById(R.id.add_from_camera_view);
        productsList = new ArrayList<>();
        res = getResources();
    }

    private void initializeListView() {

    }

}
