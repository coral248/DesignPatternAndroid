package com.smartcalsvendingmachine.VendingMachineUI.Customer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.smartcalsvendingmachine.R;
import com.smartcalsvendingmachine.VendingMachineUI.MainActivity;

public class CustomerBuyCard extends Activity {

    private double balance = 0;
    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_buy_card);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.card_radio);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.ten:
                        balance = 10;
                        break;
                    case R.id.twenty:
                        balance = 20;
                        break;
                    case R.id.fifty:
                        balance = 50;
                        break;
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAlertDialog != null){
            mAlertDialog.dismiss();
        }
    }

    public void onClick(View view){
        Intent intent;
        switch(view.getId()){
            case R.id.buy_it:
                new BuyCardTask().execute(balance);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.cancel:
                intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }

    // show error dialog
    private void showErrorDialog(String error) {
        mAlertDialog = new AlertDialog.Builder(CustomerBuyCard.this).create();
        mAlertDialog.setTitle("Error:");
        mAlertDialog.setMessage(error);
        mAlertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        mAlertDialog.show();
    }

    // show success dialog
    private void showSuccessDialog(String message) {
        mAlertDialog = new AlertDialog.Builder(CustomerBuyCard.this).create();
        mAlertDialog.setTitle("Success:");
        mAlertDialog.setMessage(message);
        mAlertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(CustomerBuyCard.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
        mAlertDialog.show();
    }

    private class BuyCardTask extends AsyncTask<Double, Void, String> {

        protected String doInBackground(Double... params) {
            try {
                MainActivity.cProxy.connect(MainActivity.getDefaults("serverIP", CustomerBuyCard.this));
                int result = MainActivity.cProxy.buyCard(params[0]);
                MainActivity.cProxy.disconnect();
                return "Card successfully bought.\nCard number: " + result + "\nBalance: $" + params[0];
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(String result) {
            if (result != null){
                Log.i("result", result);
                showSuccessDialog(result);
            } else {
                showErrorDialog("Error.");
            }
        }
    }
}
