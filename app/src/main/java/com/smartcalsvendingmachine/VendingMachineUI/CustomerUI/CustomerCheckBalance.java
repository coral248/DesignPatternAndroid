package com.smartcalsvendingmachine.VendingMachineUI.CustomerUI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.smartcalsvendingmachine.R;
import com.smartcalsvendingmachine.VendingMachineUI.MainActivity;

public class CustomerCheckBalance extends Activity {

    private EditText cardNumberText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_swipe_card);

        cardNumberText = (EditText) findViewById(R.id.card_number);
    }

    public void onClick(View view){
        switch(view.getId()){
            case R.id.swipe:
                int cardNumber = Integer.parseInt(cardNumberText.getText().toString());
                new CheckBalanceTask().execute(cardNumber);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.cancel:
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }

    // show success dialog
    private void showSuccessDialog(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Success");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(CustomerCheckBalance.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
        alertDialog.show();
    }

    // show error dialog
    private void showErrorDialog(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private class CheckBalanceTask extends AsyncTask<Integer, Void, String> {

        protected String doInBackground(Integer... params) {
            try {
                MainActivity.cProxy.connect(MainActivity.getDefaults("serverIP", CustomerCheckBalance.this), 0, null);
                double result = MainActivity.cProxy.checkBalance(params[0]);
                if(result < 0){
                    return null;
                }
                MainActivity.cProxy.disconnect();

                return "Remaining balance: $" + result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(String result) {
            if (result != null){
                showSuccessDialog(result);
            } else {
                showErrorDialog("Socket error.");
            }
        }
    }
}
