package com.enconiya.app.DialogHelpers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.enconiya.app.R;

public class AlertDialogHelper {
    Context context;
    TextView title, body;
    Button okaybtn;
    String main, des;
    Activity activity;

    public AlertDialogHelper(Context context, String main, String des) {
        this.context = context;
        this.main = main;
        this.des = des;
    }

    public void showenconiyaDialog(){
        Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View root = LayoutInflater.from(context).inflate(R.layout.successdialog,(LinearLayout) activity.findViewById(R.id.alertdialog_container));
        title = root.findViewById(R.id.alert_title);
        body = root.findViewById(R.id.body_text_alerr_dialog);

        dialog.show();
    }
}
