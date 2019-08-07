package com.example.android.clamps.bakingtime.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonInstance {
    private static Gson sGson;

    private GsonInstance() {

    }

    public static Gson getGsonInstance() {
        if(sGson == null) {
            sGson = new GsonBuilder().create();
        }
        return sGson;
    }


}
