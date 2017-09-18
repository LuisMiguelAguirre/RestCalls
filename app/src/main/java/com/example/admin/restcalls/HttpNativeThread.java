package com.example.admin.restcalls;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Luis Aguirre on 9/6/2017.
 */

public class HttpNativeThread extends Thread {

    String BASE_URL = null ;
    HttpURLConnection urlConnection;

    public HttpNativeThread(String BASE_URL) {
        this.BASE_URL = BASE_URL;
    }

    @Override
    public void run() {
        super.run();


        try {
            URL url = new URL(BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

            Scanner scanner = new Scanner(inputStream);
            while   (scanner.hasNext()){
                //Log.d("HttpNativeTag", "run: " + scanner.nextLine());
                Log.d("TAGAsyncResponse", "run: " + Thread.currentThread() + scanner.nextLine());

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
