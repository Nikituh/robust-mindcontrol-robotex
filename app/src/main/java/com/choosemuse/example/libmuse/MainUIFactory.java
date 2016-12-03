package com.choosemuse.example.libmuse;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.ActivityCompat;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Macbook on 8/18/16.
 */
public class MainUIFactory {

    /**
     * In the UI, the list of Muses you can connect to is displayed in a Spinner object for this example.
     * This spinner adapter contains the MAC addresses of all of the headbands we have discovered.
     */
    public ArrayAdapter<String> spinnerAdapter;

    MainActivity context;

    public void init(MainActivity context) {

        this.context = context;

        context.setContentView(R.layout.activity_main);
        Button refreshButton = (Button) context.findViewById(R.id.refresh);
        refreshButton.setOnClickListener(context);

        Button connectButton = (Button) context.findViewById(R.id.connect);
        connectButton.setOnClickListener(context);
        Button disconnectButton = (Button) context.findViewById(R.id.disconnect);
        disconnectButton.setOnClickListener(context);
        Button pauseButton = (Button) context.findViewById(R.id.pause);
        pauseButton.setOnClickListener(context);

        spinnerAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item);
        Spinner musesSpinner = (Spinner) context.findViewById(R.id.muses_spinner);
        musesSpinner.setAdapter(spinnerAdapter);
    }


    /**
     * The following methods update the TextViews in the UI with the data
     * from the buffers.
     */
    public void updateAccel(double[] accelBuffer) {
        TextView acc_x = (TextView)context.findViewById(R.id.acc_x);
        TextView acc_y = (TextView)context.findViewById(R.id.acc_y);
        TextView acc_z = (TextView)context.findViewById(R.id.acc_z);
        acc_x.setText(String.format("%6.2f", accelBuffer[0]));
        acc_y.setText(String.format("%6.2f", accelBuffer[1]));
        acc_z.setText(String.format("%6.2f", accelBuffer[2]));
    }

    public void updateEeg(double[] eegBuffer) {
        TextView tp9 = (TextView)context.findViewById(R.id.eeg_tp9);
        TextView fp1 = (TextView)context.findViewById(R.id.eeg_af7);
        TextView fp2 = (TextView)context.findViewById(R.id.eeg_af8);
        TextView tp10 = (TextView)context.findViewById(R.id.eeg_tp10);
        tp9.setText(String.format("%6.2f", eegBuffer[0]));
        fp1.setText(String.format("%6.2f", eegBuffer[1]));
        fp2.setText(String.format("%6.2f", eegBuffer[2]));
        tp10.setText(String.format("%6.2f", eegBuffer[3]));
    }

    public void updateAlpha(double[] alphaBuffer) {
        TextView elem1 = (TextView)context.findViewById(R.id.elem1);
        elem1.setText(String.format("%6.2f", alphaBuffer[0]));
        TextView elem2 = (TextView)context.findViewById(R.id.elem2);
        elem2.setText(String.format("%6.2f", alphaBuffer[1]));
        TextView elem3 = (TextView)context.findViewById(R.id.elem3);
        elem3.setText(String.format("%6.2f", alphaBuffer[2]));
        TextView elem4 = (TextView)context.findViewById(R.id.elem4);
        elem4.setText(String.format("%6.2f", alphaBuffer[3]));
    }

    public void showIntroDialog()
    {
        DialogInterface.OnClickListener buttonListener =
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();

                        String[] permissions = new String[] { Manifest.permission.ACCESS_COARSE_LOCATION };
                        ActivityCompat.requestPermissions(context, permissions, 0);
                    }
                };

        AlertDialog introDialog = new AlertDialog.Builder(context)
                .setTitle(R.string.permission_dialog_title)
                .setMessage(R.string.permission_dialog_description)
                .setPositiveButton(R.string.permission_dialog_understand, buttonListener)
                .create();
        introDialog.show();
    }

}
