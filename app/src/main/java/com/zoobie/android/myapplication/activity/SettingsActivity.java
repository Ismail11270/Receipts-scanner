package com.zoobie.android.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.zoobie.android.myapplication.R;
import com.zoobie.android.myapplication.fragment.SettingsFragment;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        Fragment fragment = new SettingsFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.settingsFragmentContainer,fragment).commit();
        findViewById(R.id.backBtn).setOnClickListener(v ->{
            onBackPressed();
        });
    }
}
