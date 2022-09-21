package com.example.multiplediseasesprediction;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class loading_dialog {
    private Activity activity;
    private AlertDialog dialog;
    loading_dialog(Activity myactivity){
        activity=myactivity;
    }
    void start_loading_dialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        LayoutInflater inflater=activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog,null));
        builder.setCancelable(false);
        dialog=builder.create();
        dialog.show();
    }
    void dismiss_dialog(){
        dialog.dismiss();
    }
}
