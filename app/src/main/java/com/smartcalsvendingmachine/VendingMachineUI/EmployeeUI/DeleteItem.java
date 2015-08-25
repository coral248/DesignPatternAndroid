package com.smartcalsvendingmachine.VendingMachineUI.EmployeeUI;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.smartcalsvendingmachine.R;
import com.smartcalsvendingmachine.VendingMachineUI.MainActivity;
import com.smartcalsvendingmachine.SQLiteDatabase.VendingMachineDatabase;

import java.util.ArrayList;
import java.util.List;

public class DeleteItem extends Activity {

    private List<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_item);

        ListView listView = (ListView) findViewById(R.id.item_list_view);
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
            items.add(new Item(id, name, price));
            cursor.moveToNext();
        }

        ListViewAdapter adapter = new ListViewAdapter(this, R.layout.list_item, items);
        listView.setAdapter(adapter);
    }

    public void onClick(View view){
        Intent intent;
        switch(view.getId()){
            case R.id.finish:
                new DeleteItemTask().execute();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.cancel:
                intent = new Intent(this, EmployeeOptions.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }

    private class DeleteItemTask extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... params) {
            try {
                Item item;
                for(int i = 0; i < items.size(); i++){
                    item = items.get(i);
                    if(!item.isChosen()){
                        continue;
                    }
                    MainActivity.eProxy.deleteItemFromMachine(MainActivity.machineID, item.getID());
                    MainActivity.VBD.query("DELETE FROM " + VendingMachineDatabase.ITEMS_TABLE
                            + " WHERE " + VendingMachineDatabase.ITEM_ID + "=" + item.getID());
                }
                return "";
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(String result) {
            if (result != null){
                toast("Item successfully deleted.");
                Intent intent = new Intent(DeleteItem.this, EmployeeOptions.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(),
                        "Socket exception.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void toast(String string) {
        Toast.makeText(getApplicationContext(),
                string, Toast.LENGTH_SHORT).show();
    }
}
