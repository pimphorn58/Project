package com.example.project_pp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {
    private ImageView img_logo, img_saan, img_find;
    private Button butt_scan, butt_find, butt_staff, logout_btn;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img_logo = findViewById(R.id.imglogoit);
        img_saan = findViewById(R.id.imgscan);
        img_find = findViewById(R.id.imgfind);
        logout_btn = findViewById(R.id.logout_btn);


        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sp.edit();
        editor.putString("username", null);
        editor.putInt("status", 0);
        editor.commit();


        butt_scan = findViewById(R.id.buttscan);
        butt_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoscan = new Intent(MainActivity.this, TeaschScanActivity.class);
                startActivity(gotoscan);

            }
        });

        butt_find = findViewById(R.id.buttcan);
        butt_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotologin = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(gotologin);
            }
        });

        butt_staff = findViewById(R.id.buttstaff);
        butt_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotologin = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(gotologin);

            }
        });


        if (sp.getInt("status", 0) == 0) {
            logout_btn.setVisibility(View.INVISIBLE);
        }
        else{
            butt_staff.setVisibility(View.INVISIBLE);
            logout_btn.setVisibility(View.VISIBLE);
        }

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor = sp.edit();
                editor.putString("username", null);
                editor.putInt("status", 0);
                editor.commit();
                recreate();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (sp.getInt("status", 0) != 0) {
            logout_btn.setVisibility(View.VISIBLE);
            butt_staff.setVisibility(View.INVISIBLE);
        }


    }
}
