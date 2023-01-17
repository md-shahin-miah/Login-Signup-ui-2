package com.shahin.loginsignupui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.widget.Toast;

import java.util.Calendar;

public class Utility {

    public static boolean isConnected(Context context) {
        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null) {
                return info.isConnected() || info.isConnectedOrConnecting();
            }
        }
        return false;
    }

    public static void showNetworkAlert(final Context context, int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppTheme_Dialog);
        builder.setTitle(R.string.app_name);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Check", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent NetworkAction = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                context.startActivity(NetworkAction);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (context instanceof Activity) {
                    ((Activity) context).finish();
                }
            }
        });
        builder.show();
    }

    public static void showToast(Context context, String message) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    public static void showToast(Context context, int message) {
        showToast(context, context.getResources().getString(message));
    }

    public static String getUniqueImageFileName() {
        Calendar cal = Calendar.getInstance();
        return "IMG_" + cal.getTimeInMillis();
    }
}
