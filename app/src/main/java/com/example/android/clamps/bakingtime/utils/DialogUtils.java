package com.example.android.clamps.bakingtime.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.android.clamps.bakingtime.R;
import com.squareup.picasso.Picasso;

public class DialogUtils {
    private static AlertDialog alertDialog;
    private static AlertDialog alertDialogWithButton;

    public static void closeDialog()
    {
        alertDialog.dismiss();
    }
    public static void showDialogWithButtons(final Context context,int resId,String message){

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.bakingapp_dialog_layout,null);
        builder.setView(view);
        CardView cardView = view.findViewById(R.id.cv_dialog_card);
        ImageView dialogImage = view.findViewById(R.id.iv_dialog_image);
        ImageView dialogClose = view.findViewById(R.id.iv_dialog_close);
        TextView dialogText = view.findViewById(R.id.tv_dialog_text);
        Button dialogButtonRetry = view.findViewById(R.id.btn_retry);
        Button dialogButtonExit = view.findViewById(R.id.btn_exit);
        Picasso.get().load(resId).into(dialogImage);
        dialogText.setText(message);
        cardView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));

        alertDialogWithButton = builder.create();

        dialogButtonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogWithButton.dismiss();
                ((Activity)context).finish();
            }
        });

        dialogButtonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogWithButton.dismiss();
                Intent intent = ((Activity) context).getIntent();
                ((Activity)context).finish();
                context.startActivity(intent);
            }
        });

        dialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogWithButton.dismiss();
            }
        });

        alertDialogWithButton.show();
    }
    }

