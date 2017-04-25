package ru.serv_techno.sandwichclub03;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import retrofit2.Retrofit;

public class SplashActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private static APIv1 myApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


    }
}
