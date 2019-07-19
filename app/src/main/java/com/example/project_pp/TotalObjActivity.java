package com.example.project_pp;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

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

public class TotalObjActivity extends AppCompatActivity {
    private CardView card1, card2, card3, card4, card5, card6;

    SharedPreferences p;
    RecyclerView recyclerView;
    private List<Model> movieList = new ArrayList<>();
    private RecyclerViewSearchAdapter mAdapter;
    Button refresh_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.total_obj);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        p = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        int status = p.getInt("status", 0);

        recyclerView = findViewById(R.id.recycler_view_total);
        mAdapter = new RecyclerViewSearchAdapter(movieList, status);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        refresh_btn = findViewById(R.id.refresh_btn);
        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerview();
            }
        });


        recyclerview();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }


    public void recyclerview() {

        String url = "http://" + getString(R.string.url_laravel) + ".ngrok.io/getall";

        Log.d("aaaa", url);

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
                        refresh_btn.setVisibility(View.VISIBLE);
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


                        String usernamea = jsonObj.get("username").toString();

                        if (p.getString("username", "").equals(usernamea)) {

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


                    refresh_btn.setVisibility(View.GONE);


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();

                        }
                    });


                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

            }
        });
    }
}
