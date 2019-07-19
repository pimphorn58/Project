package com.example.project_pp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private ImageView img_logo;
    private TextView user,password;
    private Button butt_login,butt_can;
   // private Toolbar imformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        View information = findViewById(R.id.toolbar);
        img_logo = findViewById(R.id.imglogoit);
        user = findViewById(R.id.box_user);
        password = findViewById(R.id.box_pass);

        butt_login = findViewById(R.id.buttlogin);
        butt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    login(user.getText().toString(),password.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        butt_can = findViewById(R.id.buttcan);
        butt_can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotomain = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(gotomain);
            }
        });

    }

    public void login(String username,String password) throws IOException {


        String url = "http://"+getString(R.string.url_laravel)+".ngrok.io/login/api/"+username+"/"+password ;

        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                final String mMessage = e.getMessage();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       Toast.makeText(getApplicationContext(),"ลองอีกครั้ง",Toast.LENGTH_LONG).show();

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String mMessage = response.body().string();
                try {
                    JSONArray jsonArr = new JSONArray(mMessage);

                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject jsonObj = jsonArr.getJSONObject(i);
                        Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("username", jsonObj.getString("username"));
                        editor.putInt("status", Integer.parseInt(jsonObj.getString("status")));
                        editor.commit();

                        //startActivity(intent);

                        finish();

                    }

                } catch (final JSONException ex) {
                    ex.printStackTrace();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });


                }

            }
        });
    }
}
