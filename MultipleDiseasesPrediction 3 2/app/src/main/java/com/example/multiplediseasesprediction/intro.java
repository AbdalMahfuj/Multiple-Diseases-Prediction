package com.example.multiplediseasesprediction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class intro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        new Handler().postDelayed(new Runnable() {
            /* class com.example.basiccalculator.MainActivity2.AnonymousClass1 */

            public void run() {
                finish();
                Intent myIntent= new Intent(intro.this,HomePage.class);
                startActivity(myIntent);
            }
        }, 1500);
    }
}