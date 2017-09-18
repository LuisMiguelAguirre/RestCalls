package com.example.admin.restcalls;

import com.example.admin.restcalls.model.github.GitHubRepo;
import com.example.admin.restcalls.model.github.Owner;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Luis Aguirre on 9/6/2017.
 */

public class RetrofitHelper {

    public static final String BASE_URL = "https://api.github.com/";

    //Login interceptor works
    //OkHttpClient mOkHttpClient = new OkHttpClient.Builder().addInterceptor();

    public static Retrofit create() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;

    }

    //create a static method to use the interface verbs
    public static Call<List<GitHubRepo>> createCall(String user) {

        Retrofit retrofit = create();
        ApiService apiService = retrofit.create(ApiService.class);
        return apiService.getGithubRepos(user);
    }

    //create an interface to have all the paths and verbs for the rest api
    interface ApiService {
        @GET("users/{user}/repos")
        //@Headers()
        Call<List<GitHubRepo>> getGithubRepos(@Path("user") String user); //Query()

        /*@GET("users/{user}")
        Call<Owner> getGithubProfile(@Path("user") String user);*/

    }
}

