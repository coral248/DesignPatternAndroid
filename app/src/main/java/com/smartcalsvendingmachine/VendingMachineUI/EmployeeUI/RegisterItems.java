package com.smartcalsvendingmachine.VendingMachineUI.EmployeeUI;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.smartcalsvendingmachine.R;
import com.smartcalsvendingmachine.VendingMachineUI.MainActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegisterItems extends Activity {

    public static List<Item> items;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_items);

        listView = (ListView) findViewById(R.id.item_list_view);
        items = new ArrayList<>();

        new LoadItemsTask().execute();
    }

    public void onClick(View view){
        Intent intent;
        switch(view.getId()){
            case R.id.next:
                intent = new Intent(this, RegisterItemNumbers.class);
                startActivity(intent);
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

    private class LoadItemsTask extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... params) {
            try {
                String result = MainActivity.eProxy.getItemIDs();
                String[] ids = result.split(" ");
                for(String idString: ids){
                    String itemJson = MainActivity.eProxy.getItem(Integer.parseInt(idString));
                    JSONObject jObject  = new JSONObject(itemJson);
                    int id = jObject.getInt("id");
                    String name = jObject.getString("name");
                    double price = jObject.getDouble("price");
                    String type = jObject.getString("type");
                    int calories = jObject.getInt("calories");
                    int sugar = jObject.getInt("sugar");
                    String info = jObject.getString("info");
                    String pic = jObject.getString("pic");
                    items.add(new Item(id, name, price, type, calories, sugar, info, pic));
                }
                return "";
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if(result != null){
                setAdapter();
            }
        }
    }

    private void setAdapter(){
        ArrayAdapter<Item> adapter = new ListViewAdapter(this, R.layout.list_item, items);
        listView.setAdapter(adapter);
    }
}
