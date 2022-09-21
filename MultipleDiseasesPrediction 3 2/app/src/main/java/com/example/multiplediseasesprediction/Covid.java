package com.example.multiplediseasesprediction;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;


import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


//import org.apache.commons.io.FileUtils;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Covid extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_READ_FOLDERS =3 ;
    private Button btnselect,btnsubmit;
    private ImageView pic;
    private TextView pth;
    private Bitmap bitmap;
    private int IMG_REQUEST=21;
    String img;
    Uri fileUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid);
        String[] PERMISSIONS = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        ActivityCompat.requestPermissions(this,
                PERMISSIONS,
                PERMISSION_REQUEST_READ_FOLDERS);
        btnselect=findViewById(R.id.btn_choose_file);
        btnsubmit=findViewById(R.id.btn_submit_file);
        pic=findViewById(R.id.pic);

        btnselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//              startActivityForResult(intent,IMG_REQUEST);
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                someActivityResultLauncher.launch(photoPickerIntent);


            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

    }
    private void uploadImage(){
        // create upload service client


        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri

        loading_dialog loadingDialog=new loading_dialog(Covid.this);
        loadingDialog.start_loading_dialog();
        File file = new File(img);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

// MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        // create RequestBody instance from file




        // finally, execute the request
        Call<CovidResponseBody> call = ApiClient.getService().upload(body);
        call.enqueue(new Callback<CovidResponseBody>() {
            @Override
            public void onResponse(Call<CovidResponseBody> call,
                                   Response<CovidResponseBody> response) {

                if(response.isSuccessful()) {
                    loadingDialog.dismiss_dialog();
                    CovidResponseBody resultResponse = response.body();
                    startActivity(new Intent(Covid.this, covid_result.class).putExtra("data",resultResponse));
                    finish();


                } else {
                    loadingDialog.dismiss_dialog();
                    String message = "An Error Occurred!";
                    Toast.makeText(Covid.this, message, Toast.LENGTH_SHORT).show();
                }
                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<CovidResponseBody> call, Throwable t) {
                loadingDialog.dismiss_dialog();
                Log.e("Upload error:", t.getMessage());
                View parentLayout=findViewById(R.id.parentCovid);
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
            }
        });
    }
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    // doSomeOperations();
                    Intent data = result.getData();
                    Uri selectedImage = Objects.requireNonNull(data).getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    BitmapFactory.decodeStream(imageStream);
                    pic.setImageURI(selectedImage);// To display selected image in image view

                    img=getRealPathFromURI(selectedImage);

                }
            });
    public String getRealPathFromURI(Uri contentUri) {

        // can post image
        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery( contentUri,
                proj, // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }
}