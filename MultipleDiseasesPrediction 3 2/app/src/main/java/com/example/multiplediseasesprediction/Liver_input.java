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

public class Liver_input extends AppCompatActivity {
    RadioGroup genderGroup;
    RadioButton gender;
    Button submit,cancel;
    EditText age, t_bilirubin, d_bilirubin, alkaine_phos, alanine_amino_transferase,aspartate_amino_transferase, t_protein, albumin, albumin_globulinRatio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liver_input);

        genderGroup = findViewById(R.id.gender_liver_GroupId);
        age=findViewById(R.id.age);

        submit = findViewById(R.id.submitbtn);
        t_bilirubin = findViewById(R.id.bilirubin);
        d_bilirubin = findViewById(R.id.direct_bilirubin);
        alkaine_phos = findViewById(R.id.alkaine_phosId);
        alanine_amino_transferase = findViewById(R.id.Alanine_Aminotransferase);
        aspartate_amino_transferase = findViewById(R.id.aspartateAminoTraId);
        t_protein = findViewById(R.id.Total_Proteins);
        albumin = findViewById(R.id.Albumin);
        albumin_globulinRatio = findViewById(R.id.Albumin_and_Globulin_Ratio);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int radioId=genderGroup.getCheckedRadioButtonId();
                gender = (RadioButton) findViewById(radioId);

                LiverRequest liverRequest = new LiverRequest();
                liverRequest.setAge(age.getText().toString());
                if(gender.getText().toString()=="Male"){
                    liverRequest.setGender("1");
                }
                else{
                    liverRequest.setGender("0");
                }

                liverRequest.setTotalBilirubin(t_bilirubin.getText().toString());
                liverRequest.setDirectBilirubin(d_bilirubin.getText().toString());
                liverRequest.setAlkalinePhosphatase(alkaine_phos.getText().toString());
                liverRequest.setAlanineAminotransferase(alanine_amino_transferase.getText().toString());
                liverRequest.setAsparateAminotransferase(aspartate_amino_transferase.getText().toString());
                liverRequest.setTotalProtein(t_protein.getText().toString());
                liverRequest.setAlbumin(albumin.getText().toString());
                liverRequest.setAlbuminGlobulinRatio(albumin_globulinRatio.getText().toString());
                LiverPredict(liverRequest);
            }
        });
    }

    public void LiverPredict(LiverRequest liverRequest) {
        loading_dialog loadingDialog=new loading_dialog(Liver_input.this);
        loadingDialog.start_loading_dialog();
        Call<ResultResponse> resultResponseCall = ApiClient.getService().liverPred(liverRequest);
        resultResponseCall.enqueue(new Callback<ResultResponse>() {
            @Override
            public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                if(response.isSuccessful()) {
                    loadingDialog.dismiss_dialog();
                    ResultResponse resultResponse = response.body();
                    startActivity(new Intent(Liver_input.this, Result.class).putExtra("data",resultResponse));
                    finish();


                } else {
                    loadingDialog.dismiss_dialog();
                    String message = "An Error Occurred!";
                    Toast.makeText(Liver_input.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResultResponse> call, Throwable t) {
                loadingDialog.dismiss_dialog();
                View parentLayout=findViewById(R.id.parentLiver);
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
//                Toast.makeText(Liver_input.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}