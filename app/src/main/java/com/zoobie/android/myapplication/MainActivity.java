package com.zoobie.android.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
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
    private MenuItem currencyItem;

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


        settingsDataSp = this.getSharedPreferences("settings", MODE_PRIVATE);
        currency = Currency.getCurrencyFromId(settingsDataSp.getInt("currency", Currency.DEFAULT_CURRENCY.id));

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
            case R.id.currency:
                currencyItem = item;
                break;
            case R.id.settings:

                break;
            case R.id.searchBtnMenu:

                break;
            default:
                break;
        }
        return true;
    }

    public void onCurrencyChanged(MenuItem item) {
        int id = item.getItemId();
        SubMenu currencyMenu = currencyItem.getSubMenu();
        String confirmationMessage = "The currency has been changed to ";
        if (R.id.currency_azn == id) {
            currencyItem.setTitle(Currency.AZN.getSymbol());
            item.setChecked(true);
            settingsDataSp.edit().putInt("currency", Currency.AZN.id).apply();
            Toast.makeText(this, confirmationMessage + Currency.AZN.getDisplayName(), Toast.LENGTH_SHORT).show();
        } else {
            MenuItem notClicked = currencyMenu.findItem(R.id.currency_azn);
            notClicked.setChecked(false);
        }

        if (R.id.currency_eur == id) {
            currencyItem.setTitle(Currency.EUR.getSymbol());
            item.setChecked(true);
            settingsDataSp.edit().putInt("currency", Currency.EUR.id).apply();
            Toast.makeText(this, confirmationMessage + Currency.EUR.getDisplayName(), Toast.LENGTH_SHORT).show();
        } else {
            MenuItem notClicked = currencyMenu.findItem(R.id.currency_eur);
            notClicked.setChecked(false);
        }
        if (R.id.currency_usd == id) {
            currencyItem.setTitle(Currency.USD.getSymbol());
            item.setChecked(true);
            settingsDataSp.edit().putInt("currency", Currency.USD.id).apply();
            Toast.makeText(this, confirmationMessage + Currency.USD.getDisplayName(), Toast.LENGTH_SHORT).show();

        } else {
            MenuItem notClicked = currencyMenu.findItem(R.id.currency_usd);
            notClicked.setChecked(false);
        }
    }
}