package com.smartcalsvendingmachine.VendingMachineUI.Employee;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.smartcalsvendingmachine.R;
import com.smartcalsvendingmachine.VendingMachineUI.MainActivity;
import com.smartcalsvendingmachine.Database.VendingMachineDatabase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class RegisterItemNumbers extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_item_numbers);
        TableLayout table = (TableLayout) findViewById(R.id.item_table);
        TextView id, name, price;
        EditText capacity, quantity;
        Item item;

        for (int i = 0; i < RegisterItems.items.size(); i++) {
            item = RegisterItems.items.get(i);
            if (!item.isChosen()) {
                continue;
            }
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT));
            id = new TextView(this);
            id.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            id.setText("" + item.getID());
            name = new TextView(this);
            name.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            name.setText(item.getName());
            price = new TextView(this);
            price.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            price.setText("$" + item.getPrice());
            capacity = new EditText(this);
            capacity.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            capacity.setId(2 * i + 1);
            capacity.setInputType(InputType.TYPE_CLASS_NUMBER);
            capacity.setText("" + 10);
            quantity = new EditText(this);
            quantity.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            quantity.setId(2 * i + 2);
            quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
            quantity.setText("" + 10);
            row.addView(id);
            row.addView(name);
            row.addView(price);
            row.addView(capacity);
            row.addView(quantity);
            table.addView(row);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.finish:
                Item item;
                EditText editText;
                for (int i = 0; i < RegisterItems.items.size(); i++) {
                    item = RegisterItems.items.get(i);
                    if (!item.isChosen()) {
                        continue;
                    }
                    editText = (EditText) findViewById(2 * i + 1);
                    item.setCapacity(Integer.parseInt(editText.getText().toString()));
                    editText = (EditText) findViewById(2 * i + 2);
                    item.setQuantity(Integer.parseInt(editText.getText().toString()));
                }
                MainActivity.VBD.query("DELETE FROM " + VendingMachineDatabase.ITEMS_TABLE);
                MainActivity.VBD.query("DELETE FROM " + VendingMachineDatabase.SALES_TABLE);
                new RegisterTask().execute();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.cancel:
                Intent intent = new Intent(this, EmployeeOptions.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }

    private class RegisterTask extends AsyncTask<Void, Void, String> {

        private Exception ex;

        protected String doInBackground(Void... params) {
            try {
                int result = MainActivity.eProxy.addMachine(MainActivity.machineID, MainActivity.getDefaults("machineAddress", RegisterItemNumbers.this));
                if (result < 0) {
                    return null;
                }
                Item item;
                for (int i = 0; i < RegisterItems.items.size(); i++) {
                    item = RegisterItems.items.get(i);

                    if (!item.isChosen()) {
                        continue;
                    }

                    MainActivity.eProxy.addItemToMachine(MainActivity.machineID, item.getID(), item.getCapacity(), item.getQuantity());

                    String info = MainActivity.eProxy.getFile(item.getInfo());

                    File outDir = new File(MainActivity.mDirPath + "items/");
                    String info_path = "items/item_" + item.getID() + ".html";

                    if (!outDir.exists() || !outDir.isDirectory()) {
                        outDir.mkdir();
                    }
                    try {
                        File outputFile = new File(outDir, "item_" + item.getID() + ".html");
                        Writer writer = new BufferedWriter(new FileWriter(outputFile, false));
                        writer.write(info);
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    item.setInfo(info_path);

                    //TODO: get pic from server
                    item.setPic("");

                    //insert into SQLite database
                    ContentValues ctx = new ContentValues();
                    ctx.put(VendingMachineDatabase.ITEM_ID, item.getID());
                    ctx.put(VendingMachineDatabase.ITEM_NAME, item.getName());
                    ctx.put(VendingMachineDatabase.ITEM_PRICE, item.getPrice());
                    ctx.put(VendingMachineDatabase.ITEM_TYPE, item.getType());
                    ctx.put(VendingMachineDatabase.ITEM_CAL, item.getCalories());
                    ctx.put(VendingMachineDatabase.ITEM_SUGAR, item.getSugar());
                    ctx.put(VendingMachineDatabase.ITEM_INFO, item.getInfo());
                    ctx.put(VendingMachineDatabase.ITEM_PIC, item.getPic());
                    ctx.put(VendingMachineDatabase.ITEM_CAPACITY, item.getCapacity());
                    ctx.put(VendingMachineDatabase.ITEM_QUANTITY, item.getQuantity());
                    MainActivity.VBD.insert(VendingMachineDatabase.ITEMS_TABLE, ctx);
                }

                return "";
            } catch (Exception e) {
                ex = e;
                return null;
            }
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                MainActivity.setDefaults("isRegistered", "true", RegisterItemNumbers.this);
                toast("Machine successfully registered.");
                Intent intent = new Intent(RegisterItemNumbers.this, EmployeeOptions.class);
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
