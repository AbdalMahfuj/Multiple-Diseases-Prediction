package com.example.multiplediseasesprediction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;

import java.util.Timer;
import java.util.TimerTask;

public class HomePage extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_page);
        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                View parentLayout=findViewById(R.id.parentHome);
                ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()==true){

                }
                else{
                    Snackbar snackbar=Snackbar.make(parentLayout,"You are offline, please connect to the internet",Snackbar.LENGTH_SHORT);
                    snackbar.setDuration(2000);
                    snackbar.show();
                }
            }
        },1,5000);

        CardView diabetes = findViewById(R.id.diabetes_btn) ;
        CardView liver = findViewById(R.id.liver_btn) ;
        CardView kidney = findViewById(R.id.kidney_btn) ;
        CardView heart = findViewById(R.id.heart_btn) ;
        CardView covid = findViewById(R.id.covid_btn) ;

        diabetes.setOnClickListener(this);
        liver.setOnClickListener(this);
        kidney.setOnClickListener(this);
        heart.setOnClickListener(this);
        covid.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.diabetes_btn:
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                v.getContext().startActivity(intent);
                break;
            case R.id.liver_btn:
                Intent intent2 = new Intent(v.getContext(), Liver_input.class);
                v.getContext().startActivity(intent2);
                break;
            case R.id.kidney_btn:
                Intent intent3 = new Intent(v.getContext(), Kidney_input.class);
                v.getContext().startActivity(intent3);
                break;
            case R.id.heart_btn:
                Intent intent4 = new Intent(v.getContext(), Heart_input.class);
                v.getContext().startActivity(intent4);
                break;
            case R.id.covid_btn:
                Intent intent5 = new Intent(v.getContext(), Covid.class);
                v.getContext().startActivity(intent5);
                break;
        }

    }
}