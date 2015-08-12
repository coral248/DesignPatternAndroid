package com.smartcalsvendingmachine.VendingMachineUI.Customer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.smartcalsvendingmachine.R;
import com.smartcalsvendingmachine.VendingMachineUI.MainActivity;
import com.smartcalsvendingmachine.Database.VendingMachineDatabase;

public class BuyWithCoins extends Activity {
    private int code;
    private double price;
    private int quantity;
    private EditText paidText;
    private double paidAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_with_coins);

        Intent intent = getIntent();
        code = intent.getIntExtra("code", 0);
        price = intent.getDoubleExtra("price", 0);
        quantity = intent.getIntExtra("quantity", 0);

        paidText = (EditText) findViewById(R.id.paid_price);
        paidAmount = 0;
    }

    public void onClick(View view){
        switch(view.getId()){
            case R.id.buy_it:
                paidAmount += Double.parseDouble(paidText.getText().toString());
                if(paidAmount < price){
                    showMoreDialog();
                    break;
                }

                MainActivity.VBD.query("UPDATE " + VendingMachineDatabase.ITEMS_TABLE
                        + " SET " + VendingMachineDatabase.ITEM_QUANTITY + " = " + (quantity - 1)
                        + " WHERE " + VendingMachineDatabase.ITEM_ID + " = " + code);

                ContentValues ctx = new ContentValues();
                ctx.put(VendingMachineDatabase.SALE_ITEM, code);
                ctx.put(VendingMachineDatabase.SALE_PROFIT, price);
                ctx.put(VendingMachineDatabase.SALE_PDATE,
                        android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()).toString());
                MainActivity.VBD.insert(VendingMachineDatabase.SALES_TABLE, ctx);

                showSuccessDialog("Item successfully bought!\nYour change is: $" + (paidAmount - price));
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
    private void showMoreDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BuyWithCoins.this);
        builder.setTitle("Please insert more coins!");
        builder.setMessage("Current balance: $" + paidAmount);
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                paidText.setText("");
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                showSuccessDialog("Your change is: $" + paidAmount);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // show success dialog
    private void showSuccessDialog(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Thank you for using SmartCal Vending Machine.");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(BuyWithCoins.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
        alertDialog.show();
    }
}
