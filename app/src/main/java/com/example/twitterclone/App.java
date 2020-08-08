package com.example.twitterclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("KVOxL53n3jBYM5jGDTw9o2Jau2YouuLrIPNsP7J0")
                // if defined
                .clientKey("DE3sjyqGSVplTQXrNgkZWnRaOOQXUgI3YSKIHi4C")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
