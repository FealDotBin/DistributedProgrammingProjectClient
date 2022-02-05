/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.common.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This class adapts a Java interface to a REST API.
 * @author aferr
 */
public class RetrofitBuilder {
    /**
     * Base URL of the server to which HTTP requests are sent
     */
    public static final String BASE_URL = "http://localhost:8080/api/";
    private Retrofit retrofit;
    private Gson gson;
    
    /**
     * Create an http client also setting read and write timeouts
     * HttpClient can be used to send requests and retrieve their responses.
     * It also sets a converter that transforms an object into json format 
     * in requests while in a response it does the opposite
     */
    public RetrofitBuilder(){
        
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10,TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .build();
        
        gson = new GsonBuilder().setLenient().create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }
    
    public Retrofit getRetrofit() {
        return retrofit;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }
}
