package com.example.multiplediseasesprediction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Heart_input extends AppCompatActivity {
    RadioGroup genderGroup,cholesterolGroup,glucoseGroup;
    RadioButton gender,cholesterol,glucose;
    Button submit,cancel;
    CheckBox smoking,alcohol,physical_act;
    EditText age,height,weight,sbp,dbp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_input);

        submit = findViewById(R.id.submitbtn);
        age = findViewById(R.id.age);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        sbp = findViewById(R.id.systol);
        dbp = findViewById(R.id.diastolic);

        genderGroup = (RadioGroup) findViewById(R.id.genderGroupId) ;
        cholesterolGroup = (RadioGroup) findViewById(R.id.cholesterolGroupId);
        glucoseGroup = (RadioGroup) findViewById(R.id.glcoseGroupId);
        smoking = (CheckBox)findViewById(R.id.smoking);
        alcohol = (CheckBox)findViewById(R.id.alcohol_intake) ;
        physical_act = (CheckBox)findViewById(R.id.physical_activity) ;



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int radioId=genderGroup.getCheckedRadioButtonId();
                gender = (RadioButton) findViewById(radioId);
                int radioId1=cholesterolGroup.getCheckedRadioButtonId();
                cholesterol = (RadioButton) findViewById(radioId1);
                int radioId2=glucoseGroup.getCheckedRadioButtonId();
                glucose = (RadioButton) findViewById(radioId2);

                HeartRequest heartRequest = new HeartRequest();

                heartRequest.setAge(age.getText().toString());
                heartRequest.setHeight(height.getText().toString());
                heartRequest.setWeight(weight.getText().toString());
                heartRequest.setSbp(weight.getText().toString());
                heartRequest.setSbp(sbp.getText().toString());
                heartRequest.setDbp(dbp.getText().toString());
                if(gender.getText().toString()=="Male"){ // gender
                    heartRequest.setGender("1");
                }
                else{
                    heartRequest.setGender("0");
                }
                if(cholesterol.getText().toString()=="Normal"){ //cholesterol
                    heartRequest.setCholesterol("1");
                }
                else{
                    heartRequest.setCholesterol("0");
                }
                if(glucose.getText().toString()=="Normal"){ // glucose
                    heartRequest.setGlucose("1");
                }
                else{
                    heartRequest.setGlucose("0");
                }

                if(smoking.isChecked())  // smoking
                {
                    heartRequest.setSmoking("1");
                } else {
                    heartRequest.setSmoking("0");
                }

                if(alcohol.isChecked())  // alcohol
                {
                    heartRequest.setAlcohol("1");
                } else {
                    heartRequest.setAlcohol("0");
                }

                if(physical_act.isChecked())  // physical activity
                {
                    heartRequest.setPhys_activity("1");
                } else {
                    heartRequest.setPhys_activity("0");
                }

                HeartPredict(heartRequest);
            }
        });


    }

    public void HeartPredict(HeartRequest heartRequest){
        loading_dialog loadingDialog=new loading_dialog(Heart_input.this);
        loadingDialog.start_loading_dialog();
        Call<ResultResponse> resultResponseCall = ApiClient.getService().heartPredi(heartRequest);
        resultResponseCall.enqueue(new Callback<ResultResponse>() {
            @Override
            public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                if(response.isSuccessful()) {
                    loadingDialog.dismiss_dialog();
                    ResultResponse resultResponse = response.body();
                    startActivity(new Intent(Heart_input.this, Result.class).putExtra("data",resultResponse));
                    finish();


                } else {
                    loadingDialog.dismiss_dialog();
                    String message = "An Error Occurred!";
                    Toast.makeText(Heart_input.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResultResponse> call, Throwable t) {
                loadingDialog.dismiss_dialog();
//                String message = t.getLocalizedMessage();
                View parentLayout=findViewById(R.id.parentHeart);
                ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()==true){
                    Snackbar snackbar=Snackbar.make(parentLayout,"Server busy!!! Please try again later",Snackbar.LENGTH_SHORT);
                    snackbar.setDuration(5000);
                    snackbar.show();
                    HeartPredict(heartRequest);
                }
                else{
                    Snackbar snackbar=Snackbar.make(parentLayout,"You are offline, please connect to the internet",Snackbar.LENGTH_SHORT);
                    snackbar.setDuration(5000);
                    snackbar.show();
                }
//                Toast.makeText(Heart_input.this, message, Toast.LENGTH_SHORT).show();


            }
        });

    }
}