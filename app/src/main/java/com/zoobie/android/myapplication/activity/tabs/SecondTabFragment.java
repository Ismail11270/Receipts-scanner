package com.zoobie.android.myapplication.activity.tabs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.zoobie.android.myapplication.R;
import com.zoobie.android.myapplication.adapters.ReceiptsListAdapter;
import com.zoobie.android.myapplication.market.data.Product;
import com.zoobie.android.myapplication.market.data.Receipt;
import com.zoobie.android.myapplication.activity.ReceiptScanner;
import com.zoobie.android.myapplication.storage.ProductsDB;

import java.util.ArrayList;


public class SecondTabFragment extends Fragment {


    @Override
    public void onPause() {
        super.onPause();
    }

    private Resources res;
    private ArrayList<Product> productsList;
    private FloatingActionButton addFab, galleryFab, cameraFab;
    private View galleryView, cameraView;
    private ReceiptsListAdapter adapter;
    private RecyclerView recyclerView;
    private RelativeLayout noItemLayout;
    private View fabActiveView;
    private FrameLayout bottomSheetDialog;

    final int REQUEST_CODE_ADD_NEW_RECEIPT = 101;
    private boolean fabActive = false;

    private ViewPager viewPager;

    public SecondTabFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second_tab, container, false);
        viewPager = container.getRootView().findViewById(R.id.pager);
        initializeViews(view);
        initializeFabs();
        initializeRecyclerView();

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (position != 1) {
                        initHiddenViews(cameraView);
                        initHiddenViews(galleryView);
                        addFab.setRotation(0);
                        fabActive = false;
                        addFab.animate().alpha(0).setDuration(300).start();
                    } else {
                        addFab.animate().alpha(1).setDuration(1000).start();
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        return view;
    }


    private void initializeFabs() {
        initHiddenViews(galleryView);
        initHiddenViews(cameraView);
        addFab.setAlpha(0f);
        addFab.animate().alpha(1).setDuration(1000).start();
        addFab.setOnClickListener((v) -> {
            handleFab();
        });

        galleryFab.setOnClickListener((v) -> {
            Intent intent = new Intent(getContext(), ReceiptScanner.class);
            intent.putExtra("source", ReceiptScanner.PICK_GALLERY);
            startActivityForResult(intent, REQUEST_CODE_ADD_NEW_RECEIPT);
        });
        cameraFab.setOnClickListener((v) -> {
            Intent intent = new Intent(getContext(), ReceiptScanner.class);
            intent.putExtra("source", ReceiptScanner.PICK_CAMERA);
            startActivityForResult(intent, REQUEST_CODE_ADD_NEW_RECEIPT);

        });

        fabActiveView.setOnClickListener(view -> {
            handleFab();
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        System.out.println("RESUME SECOND TAB");
        if (adapter.getItemCount() > 0) noItemLayout.setVisibility(View.GONE);
        else noItemLayout.setVisibility(View.VISIBLE);
        if (fabActive) {
            addFab.animate().rotation(45.f + 180.f).setDuration(0);
            fabActive = true;
            initHiddenViews(cameraView);
            initHiddenViews(galleryView);
            fabActive = false;
            fabActiveView.setVisibility(View.GONE);
        }

    }

    private void handleFab() {
        if (!fabActive) {
            addFab.animate().rotation(45.f + 180.f).setDuration(400);
            fabActive = true;
            showView(galleryView);
            showView(cameraView);
            fabActiveView.setVisibility(View.VISIBLE);
        } else {
            addFab.animate().rotation(0).setDuration(200);
            fabActive = false;
            hideViews(galleryView);
            hideViews(cameraView);
            fabActiveView.setVisibility(View.GONE);
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

    private void hideViews(View v) {

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
        recyclerView = view.findViewById(R.id.receiptsRecyclerView);
        noItemLayout = view.findViewById(R.id.no_data_layout);
        fabActiveView = view.findViewById(R.id.fabActiveView);
        fabActiveView.setVisibility(View.GONE);
        bottomSheetDialog = view.findViewById(R.id.bottomDialog);
        res = getResources();
    }

    private void initializeRecyclerView() {
        ProductsDB db = new ProductsDB(getContext());
        ArrayList<Receipt> listOfReceipts = db.getEveryReceipt();
        if (listOfReceipts.size() == 0) noItemLayout.setVisibility(View.VISIBLE);
        else noItemLayout.setVisibility(View.GONE);
        adapter = new ReceiptsListAdapter(getContext(), listOfReceipts, noItemLayout, bottomSheetDialog);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}
