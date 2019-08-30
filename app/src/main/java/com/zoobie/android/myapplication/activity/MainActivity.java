package com.zoobie.android.myapplication.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.tabs.TabLayout;
import com.zoobie.android.myapplication.R;
import com.zoobie.android.myapplication.adapters.ViewPagerAdapter;
import com.zoobie.android.myapplication.market.shops.Currency;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    TabLayout tabLayout;
    private SharedPreferences settingsDataSp;
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 400;
    private String cameraPermission[];
    private String storagePermission[];
    public static Currency currency = Currency.DEFAULT_CURRENCY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.orange_400));
        toolbar = findViewById(R.id.toolBar);
        toolbar.setNavigationIcon(R.drawable.navigation_icon);
        setSupportActionBar(toolbar);
        setTitle("");
        cameraPermission = new String[]{
                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        storagePermission = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        viewPager = findViewById(R.id.pager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        requestCameraPermission();
        requestStoragePermission();


//        settingsDataSp = this.getSharedPreferences("com.zoobie.android.myapplication", MODE_PRIVATE);
//        currency = Currency.getCurrencyFromId(settingsDataSp.getInt("currency", Currency.DEFAULT_CURRENCY.id));
//        Log.i("Currency 1",currency.getDisplayName());
//        System.out.println("Hate it");
    }


    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("RESTART");
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit Application?")
                .setMessage("Click yes to exit!")
                .setCancelable(true)
                .setPositiveButton("Yes",
                        (dialog, id) -> {
                            moveTaskToBack(true);
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                        })
                .setNegativeButton("No", (dialog, id) -> dialog.dismiss())
                .create().show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("RESUME");
        Intent intent = getIntent();
        int a = intent.getIntExtra("page", 0);
        TabLayout.Tab tab = tabLayout.getTabAt(a);
        tab.select();
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.cancel:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.searchBtnMenu:

                break;
            default:
                break;
        }
        return true;
    }


}