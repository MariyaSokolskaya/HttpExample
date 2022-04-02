package com.example.httpexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public final String apiKey = "MY_KEY";
    public final String address =
            "https://api.openweathermap.org/data/2.5/weather";
    OkHttpClient okHttpClient;

    TextView responseText;
    Button requestButton;
    EditText cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        responseText = findViewById(R.id.response);
        requestButton = findViewById(R.id.request);
        cityName = findViewById(R.id.city);

        okHttpClient = new OkHttpClient();
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsynsWeather asynsWeather = new AsynsWeather();
                asynsWeather.execute(address);
            }
        });
    }

    class AsynsWeather extends AsyncTask<String, Void, Response>{
        String city="";
        @Override
        protected void onPreExecute() {
            city = cityName.getText().toString();
            super.onPreExecute();
        }

        @Override
        protected Response doInBackground(String... strings) {
            //TODO создать строку запроса
            HttpUrl.Builder urlBuilder = HttpUrl.parse(strings[0]).newBuilder();
            //TODO добавление параметров
            urlBuilder.addQueryParameter("appid", apiKey);
            urlBuilder.addQueryParameter("q", city);
            urlBuilder.addQueryParameter("units", "metric");

            //TODO создаем строку запроса
            String addr = urlBuilder.build().toString();
            //TODO создание запроса
            Request request = new Request.Builder().url(addr).build();
            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            if(response != null){
                try {
                    responseText.setText(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                responseText.setText("Ответ не получен");
            }
        }
    }
}