package com.example.multiplediseasesprediction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class covid_result extends AppCompatActivity {
    private TextView result,acc;
    private Button rate_btn,home;
    CovidResponseBody covidResponseBody;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_result);
        result=findViewById(R.id.result);
        acc=findViewById(R.id.acc);
        rate_btn = findViewById(R.id.rate_btn);
        rate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewActivity();
            }
        });
        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHomeActivity();
            }
        });
        Intent intent=getIntent();
        if(intent.getExtras()!=null) {
            covidResponseBody= (CovidResponseBody) intent.getSerializableExtra("data");
            result.setText(covidResponseBody.getLabel().toString());
            acc.setText("Accuracy : "+covidResponseBody.getAccuracy().toString());
        }

    }
    public void openNewActivity(){
        Intent intent = new Intent(this, rating_new.class).putExtra("disease","covid");
        startActivity(intent);
    }
    public void openHomeActivity(){
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }
}