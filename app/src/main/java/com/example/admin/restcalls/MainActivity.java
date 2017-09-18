package com.example.admin.restcalls;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.admin.restcalls.model.MyResponse;
import com.example.admin.restcalls.model.github.GitHubRepo;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://www.mocky.io/v2/59bbf4780f0000c703ff87ee";
    TextView tvResult;
    ImageView imageView;
    String image_url = "https://static1.squarespace.com/static/54e8ba93e4b07c3f655b452e/t/56c2a04520c64707756f4267/1493764650017/";
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = (TextView) findViewById(R.id.tvResult);
        imageView = (ImageView) findViewById(R.id.ivTest);

        Glide.with(this).load(image_url).into(imageView);

        client = new OkHttpClient();

        Log.d("TAGAsyncResponse", "run: " + Thread.currentThread() + "Main thread");


    }

    public void makingRestCalls(View view) {
        final Request request = new Request.Builder().url(BASE_URL).build();
        switch (view.getId()) {

            //create okhttp client

            case R.id.btnNativeHttp:
                HttpNativeThread httpNativeThread = new HttpNativeThread(BASE_URL);
                httpNativeThread.start();
                break;

            case R.id.btnOkHttpSync:


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String response = client
                                    .newCall(request)
                                    .execute()
                                    .body()
                                    .string();
                            Log.d("MainActOkSyncTag", "run: " + response);
                            Gson gson = new Gson();
                            MyResponse myResponse = gson.fromJson(response, MyResponse.class);
                            //Log.d("TAG", "run: " + myResponse.getTitle());
                            Log.d("TAGAsyncResponse", "run: " + Thread.currentThread() + myResponse.getProperties().getAge().getDescription());

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                Toast.makeText(this, "Check your logs", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnOkHttpAsync:

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("TAGAsync", "onFailure: " + e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        String responseAsync = response.body().string();
                        Gson gson = new Gson();
                        MyResponse myResponse = gson.fromJson(responseAsync, MyResponse.class);
                        Log.d("TAGAsyncResponse", "run: " + Thread.currentThread() + myResponse.getProperties().getAge().getDescription());
                    }
                });

                break;

            case R.id.btnRetrofitSync:
                final retrofit2.Call<List<GitHubRepo>> callRepos =
                        RetrofitHelper.createCall("manroopsingh");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final String repoName = callRepos.execute().body().get(1).getName();
                            Log.d("TAG", "makingRestCalls: " + repoName);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvResult.setText(repoName);
                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();


                break;

            case R.id.btnRetrofitAsync:

                break;
        }
    }
}
