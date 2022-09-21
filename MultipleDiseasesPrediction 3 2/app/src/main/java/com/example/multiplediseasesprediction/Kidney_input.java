package com.example.multiplediseasesprediction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Kidney_input extends AppCompatActivity {
    EditText age,bp,rbc,wbc,hemoglobin,bloodGlucoserandom;
    RadioGroup appetiteGroup,puscellGroup,hypertension, diabetesMellitusGroup,anemiaGroup;
    RadioButton appetite,puscell,diabetesMellitus,anemia,hyper;
    Button submit,cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kidney_input);
        submit = findViewById(R.id.submitbtn);

        age = findViewById(R.id.age);
        bp = findViewById(R.id.blood_pressure);
        rbc = findViewById(R.id.rbc);
        wbc = findViewById(R.id.wbc);
        hypertension = (RadioGroup) findViewById(R.id.hyperGroupId);
        hemoglobin = findViewById(R.id.hemoglobinId);
        bloodGlucoserandom = findViewById(R.id.blood_glucose_random);
        appetiteGroup = (RadioGroup) findViewById(R.id.appetiteGroupId);
        puscellGroup = (RadioGroup) findViewById(R.id.puscellGroupId);
        diabetesMellitusGroup = (RadioGroup) findViewById(R.id.diabetesMellitusGroupId);
        anemiaGroup = (RadioGroup) findViewById(R.id.anemiaGroupId);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int radioId=appetiteGroup.getCheckedRadioButtonId();
                appetite = (RadioButton) findViewById(radioId);
                int radioId1=puscellGroup.getCheckedRadioButtonId();
                puscell = (RadioButton) findViewById(radioId1);
                int radioId2=diabetesMellitusGroup.getCheckedRadioButtonId();
                diabetesMellitus = (RadioButton) findViewById(radioId2);
                int radioId3=anemiaGroup.getCheckedRadioButtonId();
                anemia = (RadioButton) findViewById(radioId3);
                int radioId4=hypertension.getCheckedRadioButtonId();
                hyper = (RadioButton) findViewById(radioId4);
                KidneyRequest kidneyRequest = new KidneyRequest();
                kidneyRequest.setAge(age.getText().toString());
                kidneyRequest.setBp(bp.getText().toString());
                kidneyRequest.setRbc(rbc.getText().toString());
                kidneyRequest.setWbc(wbc.getText().toString());
                kidneyRequest.setHemoglobin(hemoglobin.getText().toString());
                kidneyRequest.setBlood_glucoseRandom(bloodGlucoserandom.getText().toString());

                if(appetite.getText().toString()=="Yes"){ // appetite
                    kidneyRequest.setAppetite("1");
                }
                else{
                    kidneyRequest.setAppetite("0");
                }

                if(puscell.getText().toString()=="Normal"){ // Puscell
                    kidneyRequest.setPusCell("1");
                }
                else{
                    kidneyRequest.setPusCell("0");
                }

                if(diabetesMellitus.getText().toString()=="Yes"){ // diabetesMellitus
                    kidneyRequest.setDiabetesMellitus("1");
                }
                else{
                    kidneyRequest.setDiabetesMellitus("0");
                }
                if(hyper.getText().toString()=="Yes"){ // diabetesMellitus
                    kidneyRequest.setHypertension("1");
                }
                else{
                    kidneyRequest.setHypertension("0");
                }
                if(anemia.getText().toString()=="Yes"){ // anemia
                    kidneyRequest.setAnemia("1");
                }
                else{
                    kidneyRequest.setAnemia("0");
                }

                KidneyPredict(kidneyRequest);

            }
        });
    }
    public void KidneyPredict(KidneyRequest kidneyRequest) {
        loading_dialog loadingDialog=new loading_dialog(Kidney_input.this);
        loadingDialog.start_loading_dialog();
        Call<ResultResponse> resultResponseCall = ApiClient.getService().kidneyPred(kidneyRequest);
        resultResponseCall.enqueue(new Callback<ResultResponse>() {
            @Override
            public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                if(response.isSuccessful()) {
                    loadingDialog.dismiss_dialog();
                    ResultResponse resultResponse = response.body();
                    startActivity(new Intent(Kidney_input.this, Result.class).putExtra("data",resultResponse));
                    finish();


                } else {
                    loadingDialog.dismiss_dialog();
                    String message = "An Error Occurred!";
                    Toast.makeText(Kidney_input.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResultResponse> call, Throwable t) {
                loadingDialog.dismiss_dialog();
                View parentLayout=findViewById(R.id.parentKidney);
                ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()==true){
                    Snackbar snackbar=Snackbar.make(parentLayout,"Server busy!!! Please try again later",Snackbar.LENGTH_SHORT);
                    snackbar.setDuration(5000);
                    snackbar.show();

                }
                else{
                    Snackbar snackbar=Snackbar.make(parentLayout,"You are offline, please connect to the internet",Snackbar.LENGTH_SHORT);
                    snackbar.setDuration(5000);
                    snackbar.show();
                }
//                String message = t.getLocalizedMessage();
//                Toast.makeText(Kidney_input.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}