package com.qbent.enfinsapp.global;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogue
{
    Context context = null;

    public AlertDialogue(Context context)
    {
        this.context = context;
    }

    public void showAlertMessage(String error)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("ENFIN's Admin");
        builder.setMessage(error);
        builder.setCancelable(true);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
