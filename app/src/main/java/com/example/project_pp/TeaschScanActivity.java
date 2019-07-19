package com.example.project_pp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class TeaschScanActivity extends AppCompatActivity
        implements ZXingScannerView.ResultHandler {

    public static final int REQUEST_QR_SCAN = 4;
    private Button btnScan;
    private TextView txtResult;

    private ZXingScannerView mScannerView;
    private static final int REQUEST_CAMERA = 1;
    List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teasch_scan);


        mScannerView = new ZXingScannerView(getApplicationContext());
        setContentView(mScannerView);
        mScannerView.setResultHandler(TeaschScanActivity.this);
        mScannerView.startCamera();


    }

    @Override
    public void handleResult(Result result) {
        mScannerView.stopCamera();

        try {
            getHttpResponse(result.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Toast.makeText(this, result.getText(), Toast.LENGTH_SHORT).show();

        //String a = result.getText();
      //  finish();
    }

    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }


    public void getHttpResponse(String code) throws IOException {

        String revers = new StringBuilder(code).reverse().toString();
        String sub = revers.substring(0,6);
        String backrevers = new StringBuilder(sub).reverse().toString();



        String url = "http://"+getString(R.string.url_laravel)+".ngrok.io/getallproduct/"+backrevers;

        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

//        Response response = client.newCall(request).execute();
//        Log.e(TAG, response.body().string());

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
                //call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String mMessage = response.body().string();
                try {
                    JSONArray jsonArr = new JSONArray(mMessage);

                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject jsonObj = jsonArr.getJSONObject(i);
                        String k = jsonObj.keys().next();
                       // Log.e("Info", "Key: " + k + ", value: " + jsonObj.getString("code"));
                        list = new ArrayList<>();
                        list.add(jsonObj.getString("code"));
                        list.add(jsonObj.getString("room"));
                        list.add(jsonObj.getString("detail"));
                        list.add(jsonObj.getString("status"));
                        list.add(jsonObj.getString("img"));
                        list.add(jsonObj.getString("type_id"));
                        list.add(jsonObj.getString("name"));
                        list.add(jsonObj.getString("email"));
                        list.add(jsonObj.getString("tel"));


                        Intent intent = new Intent(getApplicationContext(),QR_Product_Detail.class);
                        intent.putStringArrayListExtra("list", (ArrayList<String>) list);
                        startActivity(intent);

                        finish();


                    }

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
              //  Log.e("aaaaaaaaa", mMessage);
               // Toast.makeText(getApplicationContext(), mMessag"" Toast.LENGTH_SHORT).show();
            }
        });
    }


}
