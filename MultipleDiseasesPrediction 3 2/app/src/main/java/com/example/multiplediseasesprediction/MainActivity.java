package com.example.multiplediseasesprediction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    Button submit,cancel,homepage;
    EditText pregnancies,glucose,bp,skinThickness,insulin,bmi,diabetesPeDFunc,age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        submit = findViewById(R.id.submitbtn);
        cancel = findViewById(R.id.cancelbtn);
        pregnancies = findViewById(R.id.pregnancies);
        glucose = findViewById(R.id.glucose);
        bp = findViewById(R.id.blood_pressure);
        skinThickness = findViewById(R.id.skin_thickness);
        insulin = findViewById(R.id.insulin);
        bmi = findViewById(R.id.bmi);
        diabetesPeDFunc = findViewById(R.id.diabetes_pedigreeFunction);
        age = findViewById(R.id.age);




        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            DiabetesPredRequest diabetesPredRequest= new DiabetesPredRequest();
                diabetesPredRequest.setAge(age.getText().toString());
                diabetesPredRequest.setBMI(bmi.getText().toString());
                diabetesPredRequest.setInsulin(insulin.getText().toString());
                diabetesPredRequest.setBloodPressure(bp.getText().toString());
                diabetesPredRequest.setSkinThickness(skinThickness.getText().toString());
                diabetesPredRequest.setGlucose(glucose.getText().toString());
                diabetesPredRequest.setPregnancies(pregnancies.getText().toString());
                diabetesPredRequest.setDiabetesPedigreeFunction(diabetesPeDFunc.getText().toString());
                diabetespredt(diabetesPredRequest);

            }
        });


    }

    public void diabetespredt(DiabetesPredRequest diabetesPredRequest){
        loading_dialog loadingDialog=new loading_dialog(MainActivity.this);
        loadingDialog.start_loading_dialog();
        Call<ResultResponse> resultResponseCall = ApiClient.getService().diabetespred(diabetesPredRequest);
        resultResponseCall.enqueue(new Callback<ResultResponse>() {
            @Override
            public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                if(response.isSuccessful()) {
                    loadingDialog.dismiss_dialog();
                    ResultResponse resultResponse = response.body();
                    startActivity(new Intent(MainActivity.this, Result.class).putExtra("data",resultResponse));
                    finish();


                } else {
                    loadingDialog.dismiss_dialog();
                    String message = "An Error Occurred!";
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResultResponse> call, Throwable t) {
                loadingDialog.dismiss_dialog();
                String message = t.getLocalizedMessage();
                View parentLayout=findViewById(R.id.parentMain);
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


//                Toast.makeText(MainActivity.this, "Server busy!!! Please try again later", Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void resultActivity() {
       Intent intent = new Intent(this, Result.class);
        startActivity(intent);
    }
}