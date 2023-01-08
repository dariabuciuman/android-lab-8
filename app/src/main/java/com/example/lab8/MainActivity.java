package com.example.lab8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    TextView resultView;
    EditText editText;
    RadioButton usd, eur, gbp, mdl;
    String jsonURL = "http://www.floatrates.com/daily/ron.json";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultView = findViewById(R.id.resultView);
        editText = findViewById(R.id.editTextNumber);
        usd = findViewById(R.id.usdButton);
        eur = findViewById(R.id.eurButton);
        gbp = findViewById(R.id.gbpButton);
        mdl = findViewById(R.id.mdlButton);
    }

    public void onClickExchange(View view) {

        if(editText.getText().toString().isEmpty()) {
            Toast.makeText(MainActivity.this, "Please enter a value to exchange", Toast.LENGTH_SHORT).show();
        } else {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(jsonURL).build();


            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String recData = response.body().string();
                    Log.i(TAG, "onResponse: " + recData);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject(recData);
                                Double value;
                                if (usd.isChecked()) {
                                    value = Double.parseDouble(json.getJSONObject("usd").getString("rate")) * Double.parseDouble(editText.getText().toString());
                                    int temp = (int) (value * 100.0);
                                    value = ((double) temp) / 100.0;
                                    resultView.setText(value.toString() + " USD");
                                }
                                if (eur.isChecked()) {
                                    value = Double.parseDouble(json.getJSONObject("eur").getString("rate")) * Double.parseDouble(editText.getText().toString());
                                    int temp = (int) (value * 100.0);
                                    value = ((double) temp) / 100.0;
                                    resultView.setText(value.toString() + " EUR");
                                }
                                if (gbp.isChecked()) {
                                    value = Double.parseDouble(json.getJSONObject("gbp").getString("rate")) * Double.parseDouble(editText.getText().toString());
                                    int temp = (int) (value * 100.0);
                                    value = ((double) temp) / 100.0;
                                    resultView.setText(value.toString() + " GBP");
                                }
                                if (mdl.isChecked()) {
                                    value = Double.parseDouble(json.getJSONObject("mdl").getString("rate")) * Double.parseDouble(editText.getText().toString());
                                    int temp = (int) (value * 100.0);
                                    value = ((double) temp) / 100.0;
                                    resultView.setText(value.toString() + " MDL");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        }
    }
}