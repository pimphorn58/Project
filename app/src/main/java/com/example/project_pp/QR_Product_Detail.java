package com.example.project_pp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QR_Product_Detail extends AppCompatActivity {
    TextView code, room, detail, status_test, type_id, name, email, tel, type_txt, email_txt;
    SharedPreferences p;
    ImageView imga;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr__product__detail);


        p = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int statusa = p.getInt("status", 0);
        String url = "http://"+getString(R.string.url_image)+".ngrok.io/Project/img/";
        ArrayList<String> list = getIntent().getStringArrayListExtra("list");



        imga = findViewById(R.id.imga);
        code = findViewById(R.id.code);
        room = findViewById(R.id.room);
        detail = findViewById(R.id.detail);
        status_test = findViewById(R.id.status_test);
        type_id = findViewById(R.id.type_id);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        tel = findViewById(R.id.tel);

        type_txt = findViewById(R.id.type_txt);
        email_txt = findViewById(R.id.email_txt);

        int s = Integer.valueOf(list.get(3));

        Toast.makeText(getApplicationContext(),String.valueOf(s),Toast.LENGTH_LONG).show();


        if (list.size() != 0) {
            if (statusa != 0) {
                code.setText(list.get(0));
                room.setText(list.get(1));
                detail.setText(list.get(2));

                int type = Integer.valueOf(list.get(5));

                if (s == 1) status_test.setText("ว่าง");
                else if (s == 2) status_test.setText("ไม่ว่าง");
                else status_test.setText("ชำรุด");

                if (type == 1) type_id.setText("computer");
                else if (type == 2) type_id.setText("tablet");
                else if (type == 3) type_id.setText("vr");
                else if (type == 4) type_id.setText("iot");
                else if (type == 5) type_id.setText("pinter");
                else type_id.setText("supple");

                String urlimg = url + list.get(4);
                Glide.with(this).load(urlimg).into(imga);

                //status.setText(list.get(3));
                // img.setText(list.get(4));
                // type_id.setText(list.get(5));


                name.setText(list.get(6));
                email.setText(list.get(7));
                tel.setText(list.get(8));

            } else {

                if (s == 1) status_test.setText("ว่าง");
                else if (s == 2) status_test.setText("ไม่ว่าง");
                else status_test.setText("ชำรุด");


                code.setText(list.get(0));
                room.setText(list.get(1));
                detail.setText(list.get(2));
                name.setText(list.get(6));
                tel.setText(list.get(8));

                String urlimg1 = url + list.get(4);
                Toast.makeText(getApplicationContext(),url+list.get(4),Toast.LENGTH_LONG).show();
                Glide.with(this).load(urlimg1).into(imga);


                email_txt.setVisibility(View.GONE);
                type_txt.setVisibility(View.GONE);
                email.setVisibility(View.GONE);
                type_id.setVisibility(View.GONE);

            }
        } else {

        }


    }

}
