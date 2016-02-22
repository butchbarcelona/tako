package proj.tako.services;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import proj.tako.MainActivity;

/**
 * Created by tonnyquintos on 10/24/15.
 */
public class Util {

    private static Util instance;

    private Util(){

    }

    public static Util getInstance(){
        if(instance == null){
            instance = new Util();
        }
        return instance;
    }


    public void showDialog(Context ctx, String message, String okButton, String cancelButton
      , DialogInterface.OnClickListener positiveListener
      , DialogInterface.OnClickListener negativeListener){
        new AlertDialog.Builder(ctx)
          .setTitle(MainActivity.TAG)
          .setMessage(message)
          .setPositiveButton(okButton, positiveListener)
          .setNegativeButton(cancelButton, negativeListener)
          .setIcon(android.R.drawable.ic_dialog_alert)
          .show();
    }

    public void showDialog(Context ctx,String title, String message, String okButton
      , DialogInterface.OnClickListener positiveListener ){
        new AlertDialog.Builder(ctx)
          .setTitle(title)
          .setMessage(message)
          .setPositiveButton(okButton, positiveListener)
          .setIcon(android.R.drawable.ic_dialog_alert)
          .show();
    }

    public void showDialog(Context ctx, String message, String okButton
      , DialogInterface.OnClickListener positiveListener ){
        new AlertDialog.Builder(ctx)
          .setTitle(MainActivity.TAG)
          .setMessage(message)
          .setPositiveButton(okButton, positiveListener)
          .setIcon(android.R.drawable.ic_dialog_alert)
          .show();
    }




}
