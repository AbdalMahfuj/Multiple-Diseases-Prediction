package com.example.multiplediseasesprediction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Result extends AppCompatActivity {
    ResultResponse resultResponse;
    TextView res,result_show;
    TextView title;
    Button rate_btn,home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        res=findViewById(R.id.resultId);
        title=findViewById(R.id.title_dis);
        result_show=findViewById(R.id.result_show);
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
            resultResponse = (ResultResponse) intent.getSerializableExtra("data");
            Log.e("TAG", "---->" + resultResponse.getResult());
            title.setText(resultResponse.getDisease().toString()+" prediction result");
            if(resultResponse.getResult().toString().equalsIgnoreCase("1")){
                result_show.setText("Sorry!");
                res.setText("You are affected by "+resultResponse.getDisease().toString());


            }
            else{
                result_show.setTextColor(ContextCompat.getColor(this, R.color.greenColor));
                result_show.setText("Congrats!");
                res.setText("You are healthy");
            }

        }
    }
    public void openNewActivity(){
        Intent intent = new Intent(this, rating_new.class).putExtra("disease",resultResponse.getDisease().toString());
        startActivity(intent);
    }
    public void openHomeActivity(){
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }
}