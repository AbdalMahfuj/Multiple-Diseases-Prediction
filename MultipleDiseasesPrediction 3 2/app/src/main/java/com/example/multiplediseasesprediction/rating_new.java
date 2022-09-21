package com.example.multiplediseasesprediction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import android.widget.ProgressBar;

import java.util.Formatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class rating_new extends AppCompatActivity {

    Button SendFeed;
    EditText age;
    String rating4="",rating1="",rating2="", rating3="",rating5="";
    RatingBar ratingbar4,ratingbar1,ratingbar2,ratingbar3,ratingbar5;
    RadioGroup genderGroup;
    RadioButton gender;
    ProgressBar androidProgressBar;
    int progressStatusCounter = 0;
    TextView textView;
    Handler progressHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_new);
        Intent intent = getIntent();
        String disease = intent.getExtras().getString("disease");
        androidProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        textView = (TextView) findViewById(R.id.textView);
        age=(EditText)findViewById(R.id.age);
        genderGroup = findViewById(R.id.rating_gender);
        ratingbar1=(RatingBar)findViewById(R.id.ratingBar1);
        ratingbar2=(RatingBar)findViewById(R.id.ratingBar2);
        ratingbar3=(RatingBar)findViewById(R.id.ratingBar3);
        ratingbar4=(RatingBar)findViewById(R.id.ratingBar4);
        ratingbar5=(RatingBar)findViewById(R.id.ratingBar5);

        SendFeed=(Button)findViewById(R.id.sendFeed);

        SendFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int radioId=genderGroup.getCheckedRadioButtonId();
                gender = (RadioButton) findViewById(radioId);
                rating1=String.valueOf(ratingbar1.getRating());
                rating2=String.valueOf(ratingbar2.getRating());
                rating3=String.valueOf(ratingbar3.getRating());
                rating4=String.valueOf(ratingbar4.getRating());
                rating5=String.valueOf(ratingbar5.getRating());

                float str1 = Float.parseFloat(rating1);
                float str2 = Float.parseFloat(rating2);
                float str3 = Float.parseFloat(rating3);
                float str4 = Float.parseFloat(rating4);
                float str5 = Float.parseFloat(rating5);
                float average_rate = (str1+str2+str3+str4+str5)/5;

                Formatter formatter = new Formatter();
                formatter.format("%.2f", average_rate);

                //Start progressing
                //int finalRating = rating*20;
                float finalAverage_rate = average_rate * 20;
                new Thread(new Runnable() {
                    public void run() {
                        while (progressStatusCounter < finalAverage_rate) {
                            progressStatusCounter += 2;
                            progressHandler.post(new Runnable() {
                                public void run() {
                                    androidProgressBar.setProgress(progressStatusCounter);
                                    //Status update in textview
                                    textView.setText("Overall Rating: " + progressStatusCounter + "/" + androidProgressBar.getMax());
                                }
                            });
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();

                rating_request ratingRequest=new rating_request();
                ratingRequest.setRating1(str1);
                ratingRequest.setRating2(str2);
                ratingRequest.setRating3(str3);
                ratingRequest.setRating4(str4);
                ratingRequest.setRating5(str5);
                ratingRequest.setDisease(disease);
                ratingRequest.setAge(age.getText().toString());
                ratingRequest.setGender(gender.getText().toString());
                Toast.makeText(getApplicationContext(), "Overall Rating: "+formatter.toString()+" Out of 5" , Toast.LENGTH_LONG).show();
                rating(ratingRequest);
            }
        });
    }
    public void rating(rating_request ratingRequest) {
        Call<ratingResponse> resultResponseCall = ApiClient.getService().ratingSubmit(ratingRequest);
        resultResponseCall.enqueue(new Callback<ratingResponse>() {
            @Override
            public void onResponse(Call<ratingResponse> call, Response<ratingResponse> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(rating_new.this, "Thank you for share your experience", Toast.LENGTH_SHORT).show();
                    finish();


                } else {
                    String message = "An Error Occurred!";
                    Toast.makeText(rating_new.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ratingResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(rating_new.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}