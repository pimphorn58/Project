package com.example.project_pp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.midi.MidiOutputPort;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.CardView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    private List<Model> movieList = new ArrayList<>();
    private RecyclerViewSearchAdapter mAdapter;
    private CardView card1, card2, card3, card4, card5, card6;
    SharedPreferences p;
    Spinner spinner;
    List<String> list;
    DrawerLayout drawer;
    Button refresh_btn_search;

    int type = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter;

        list = new ArrayList<String>();
        list.add("คอมพิวเตอร์");
        list.add("แท็บเล็ต");
        list.add("VR");
        list.add("iot");
        list.add("printer");
        list.add("อุปกรณ์เสริม");
        list.add("ทั้งหมด");
        adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setSelection(6);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = position;
                recyclerview();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        refresh_btn_search= findViewById(R.id.refresh_btn_search);
        refresh_btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerview();
            }
        });



        p = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int status = p.getInt("status", 0);

        NavigationView bottNav = findViewById(R.id.nav_view);
        recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new RecyclerViewSearchAdapter(movieList, status);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        if (p.getInt("status", 0) == 0 || p.getInt("status", 0) == 3) {
            bottNav.getMenu().findItem(R.id.nav_obj).setVisible(false);
            bottNav.getMenu().findItem(R.id.nav_exit).setVisible(false);
        }

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        bottNav.setNavigationItemSelectedListener(this);

        findViewById(R.id.menuham).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.nav_home) {
            finish();
        } else if (id == R.id.nav_obj) {
            Intent gotoowner = new Intent(SearchActivity.this, TotalObjActivity.class);
            startActivity(gotoowner);
        } else if (id == R.id.nav_exit) {
            p.edit().putString("username", null).commit();
            p.edit().putInt("status", 0).commit();
            Intent gotoexit = new Intent(SearchActivity.this, MainActivity.class);
            startActivity(gotoexit);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }


    public void recyclerview() {

        movieList.clear();

        String url = "http://" + getString(R.string.url_laravel) + ".ngrok.io/getall";

        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refresh_btn_search.setVisibility(View.VISIBLE);

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String mMessage = response.body().string();
                try {
                    JSONArray jsonArr = new JSONArray(mMessage);
                    for (int i = 0; i < jsonArr.length(); i++) {

                        JSONObject jsonObj = jsonArr.getJSONObject(i);

                        if (type == 6) {
                            String code = jsonObj.get("code").toString();
                            String durable_name = jsonObj.get("durable_name").toString();
                            String name = jsonObj.get("name").toString();
                            String room = jsonObj.get("room").toString();
                            String tel = jsonObj.get("tel").toString();
                            String email = jsonObj.get("email").toString();
                            String type_name = jsonObj.get("type_name").toString();
                            String img = jsonObj.get("img").toString();
                            String status = jsonObj.get("status").toString();
                            movieList.add(new Model(code, durable_name, name, room, tel, email, type_name, img, status));
                        }
                        else if (type==0){
                            if (jsonObj.get("type_name").toString().equals(list.get(0))){
                                String code = jsonObj.get("code").toString();
                                String durable_name = jsonObj.get("durable_name").toString();
                                String name = jsonObj.get("name").toString();
                                String room = jsonObj.get("room").toString();
                                String tel = jsonObj.get("tel").toString();
                                String email = jsonObj.get("email").toString();
                                String type_name = jsonObj.get("type_name").toString();
                                String img = jsonObj.get("img").toString();
                                String status = jsonObj.get("status").toString();
                                movieList.add(new Model(code, durable_name, name, room, tel, email, type_name, img, status));

                            }


                        }

                        else if (type==1){

                            if (jsonObj.get("type_name").toString().equals(list.get(1))){
                                String code = jsonObj.get("code").toString();
                                String durable_name = jsonObj.get("durable_name").toString();
                                String name = jsonObj.get("name").toString();
                                String room = jsonObj.get("room").toString();
                                String tel = jsonObj.get("tel").toString();
                                String email = jsonObj.get("email").toString();
                                String type_name = jsonObj.get("type_name").toString();
                                String img = jsonObj.get("img").toString();
                                String status = jsonObj.get("status").toString();
                                movieList.add(new Model(code, durable_name, name, room, tel, email, type_name, img, status));

                            }

                        }

                        else if (type==2){

                            if (jsonObj.get("type_name").toString().equals(list.get(2))){
                                String code = jsonObj.get("code").toString();
                                String durable_name = jsonObj.get("durable_name").toString();
                                String name = jsonObj.get("name").toString();
                                String room = jsonObj.get("room").toString();
                                String tel = jsonObj.get("tel").toString();
                                String email = jsonObj.get("email").toString();
                                String type_name = jsonObj.get("type_name").toString();
                                String img = jsonObj.get("img").toString();
                                String status = jsonObj.get("status").toString();
                                movieList.add(new Model(code, durable_name, name, room, tel, email, type_name, img, status));

                            }

                        }

                        else if (type==3){

                            if (jsonObj.get("type_name").toString().equals(list.get(3))){
                                String code = jsonObj.get("code").toString();
                                String durable_name = jsonObj.get("durable_name").toString();
                                String name = jsonObj.get("name").toString();
                                String room = jsonObj.get("room").toString();
                                String tel = jsonObj.get("tel").toString();
                                String email = jsonObj.get("email").toString();
                                String type_name = jsonObj.get("type_name").toString();
                                String img = jsonObj.get("img").toString();
                                String status = jsonObj.get("status").toString();
                                movieList.add(new Model(code, durable_name, name, room, tel, email, type_name, img, status));

                            }

                        }
                        else if (type==4){

                            if (jsonObj.get("type_name").toString().equals(list.get(4))){
                                String code = jsonObj.get("code").toString();
                                String durable_name = jsonObj.get("durable_name").toString();
                                String name = jsonObj.get("name").toString();
                                String room = jsonObj.get("room").toString();
                                String tel = jsonObj.get("tel").toString();
                                String email = jsonObj.get("email").toString();
                                String type_name = jsonObj.get("type_name").toString();
                                String img = jsonObj.get("img").toString();
                                String status = jsonObj.get("status").toString();
                                movieList.add(new Model(code, durable_name, name, room, tel, email, type_name, img, status));

                            }

                        }

                        else if (type==5){

                            if (jsonObj.get("type_name").toString().equals(list.get(5))){
                                String code = jsonObj.get("code").toString();
                                String durable_name = jsonObj.get("durable_name").toString();
                                String name = jsonObj.get("name").toString();
                                String room = jsonObj.get("room").toString();
                                String tel = jsonObj.get("tel").toString();
                                String email = jsonObj.get("email").toString();
                                String type_name = jsonObj.get("type_name").toString();
                                String img = jsonObj.get("img").toString();
                                String status = jsonObj.get("status").toString();
                                movieList.add(new Model(code, durable_name, name, room, tel, email, type_name, img, status));

                            }

                        }





                    }




                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                            refresh_btn_search.setVisibility(View.GONE);
                        }
                    });


                } catch (JSONException ex) {
                    ex.printStackTrace();

                }

            }
        });
    }

}
