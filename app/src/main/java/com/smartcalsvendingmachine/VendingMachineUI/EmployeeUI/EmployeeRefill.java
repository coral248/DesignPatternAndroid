package com.smartcalsvendingmachine.VendingMachineUI.EmployeeUI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.smartcalsvendingmachine.R;
import com.smartcalsvendingmachine.VendingMachineUI.MainActivity;
import com.smartcalsvendingmachine.SQLiteDatabase.VendingMachineDatabase;

import java.util.ArrayList;

public class EmployeeRefill extends Activity {
    private ArrayList<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_refill);
        TableLayout table = (TableLayout) findViewById(R.id.item_table);
        TextView idText, nameText, capacityText;
        EditText quantityText;
        Button addFullButton;
        items = new ArrayList<>();

        String query = "SELECT * FROM " + VendingMachineDatabase.ITEMS_TABLE;

        Cursor cursor = MainActivity.VBD.rawquery(query);
        int count;

        if (cursor != null) {
            count = cursor.getCount();
            cursor.moveToFirst();
        } else {
            count = 0;
        }

        for (int i = 0; i < count; i++) {
            int id = cursor.getInt(cursor.getColumnIndex(VendingMachineDatabase.ITEM_ID));
            String name = cursor.getString(cursor.getColumnIndex(VendingMachineDatabase.ITEM_NAME));
            double price = cursor.getDouble(cursor.getColumnIndex(VendingMachineDatabase.ITEM_PRICE));
            int capacity = cursor.getInt(cursor.getColumnIndex(VendingMachineDatabase.ITEM_CAPACITY));
            int quantity = cursor.getInt(cursor.getColumnIndex(VendingMachineDatabase.ITEM_QUANTITY));
            items.add(new Item(id, name, price, capacity));

            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT));
            idText = new TextView(this);
            idText.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            idText.setText("" + id);
            nameText = new TextView(this);
            nameText.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            nameText.setText(name);
            capacityText = new TextView(this);
            capacityText.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            capacityText.setText("" + capacity);
            quantityText = new EditText(this);
            quantityText.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            quantityText.setId(i + 1);
            quantityText.setInputType(InputType.TYPE_CLASS_NUMBER);
            quantityText.setText("" + quantity);
            addFullButton = new Button(this);
            addFullButton.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            addFullButton.setText(getResources().getString(R.string.add_full));
            row.addView(idText);
            row.addView(nameText);
            row.addView(capacityText);
            row.addView(quantityText);
            row.addView(addFullButton);
            table.addView(row);
            addFullButton.setOnClickListener(new MyListener(quantityText, capacity));
            cursor.moveToNext();
        }
    }

    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.finish:
                EditText editText;
                int quantity;
                Item item;
                for (int i = 0; i <items.size(); i++) {
                    item = items.get(i);
                    editText = (EditText) findViewById(i + 1);
                    quantity = Integer.parseInt(editText.getText().toString());
                    if(quantity > item.getCapacity()){
                        showErrorDialog("Quantity cannot be larger than capacity. Item: " + item.getID());
                        break;
                    }
                    item.setQuantity(quantity);
                    MainActivity.VBD.query("UPDATE " + VendingMachineDatabase.ITEMS_TABLE
                            + " SET " + VendingMachineDatabase.ITEM_QUANTITY + " = " + quantity
                            + " WHERE " + VendingMachineDatabase.ITEM_ID + " = " + item.getID());
                }
                new RefillTask().execute();
                break;
            case R.id.cancel:
                intent = new Intent(this, EmployeeOptions.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }

    private class MyListener implements View.OnClickListener {
        private EditText quantityView;
        private int number;

        MyListener(EditText et, int n){
            quantityView = et;
            number = n;
        }

        @Override
        public void onClick(View v) {
            quantityView.setText("" + number);
        }
    }

    // show error dialog
    private void showErrorDialog(String error) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Error:");
        alertDialog.setMessage(error);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private class RefillTask extends AsyncTask<Void, Void, String> {

        private Exception ex;

        protected String doInBackground(Void... params) {
            try {
                Item item;
                for (int i = 0; i < items.size(); i++) {
                    item = items.get(i);
                    MainActivity.eProxy.updateMachineItemQuantity(MainActivity.machineID, item.getID(), item.getQuantity());
                }
                return "";
            } catch (Exception e) {
                ex = e;
                return null;
            }
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                toast("Machine successfully refilled.");
                Intent intent = new Intent(EmployeeRefill.this, EmployeeOptions.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                toast("Error.");
                if(ex != null)
                    ex.printStackTrace();
            }
        }
    }

    private void toast(String string) {
        Toast.makeText(getApplicationContext(),
                string, Toast.LENGTH_SHORT).show();
    }
}
