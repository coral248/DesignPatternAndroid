package com.smartcalsvendingmachine.VendingMachineUI.MachineUI;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.smartcalsvendingmachine.SQLiteDatabase.VendingMachineContentProvider;
import com.smartcalsvendingmachine.SQLiteDatabase.VendingMachineDatabase;
import com.smartcalsvendingmachine.R;
import com.smartcalsvendingmachine.VendingMachineUI.MainActivity;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class MyService extends IntentService {
    private static final int MY_NOTIFICATION_ID = 1;
    private String ip;

    public MyService() {
        super("VendingMachineDailyUpdate");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("MyService", "About to execute MyTask");
        ip = (String) intent.getExtras().get("serverIP");
        new MyTask().execute();
        this.sendNotification(this);
    }
    private class MyTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                int machineID = MainActivity.machineID;
                MainActivity.mProxy.connect(ip, machineID, null);

                //upload item quantities
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
                    int quantity = cursor.getInt(cursor.getColumnIndex(VendingMachineDatabase.ITEM_QUANTITY));
                    MainActivity.mProxy.updateMachineItemQuantity(machineID, id, quantity);
                    cursor.moveToNext();
                }

                //upload sales
                query = "SELECT * FROM " + VendingMachineDatabase.SALES_TABLE + " WHERE "
                        + VendingMachineDatabase.SALE_UDATE + " IS NULL";

                cursor = MainActivity.VBD.rawquery(query);

                if (cursor != null) {
                    count = cursor.getCount();
                    cursor.moveToFirst();
                } else {
                    count = 0;
                }

                for (int i = 0; i < count; i++) {
                    int itemId = cursor.getInt(cursor.getColumnIndex(VendingMachineDatabase.SALE_ITEM));
                    double profit = cursor.getDouble(cursor.getColumnIndex(VendingMachineDatabase.SALE_PROFIT));
                    String purchaseDate = cursor.getString(cursor.getColumnIndex(VendingMachineDatabase.SALE_PDATE));
                    MainActivity.mProxy.addSale(machineID, itemId, profit, purchaseDate);
                    MainActivity.VBD.query("UPDATE " + VendingMachineDatabase.SALES_TABLE
                            + " SET " + VendingMachineDatabase.SALE_UDATE + " = '"
                            + android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()).toString() + "'");
                    cursor.moveToNext();
                }

                //download updated items
                String result = MainActivity.mProxy.getUpdatedIDs(machineID);
                String[] ids = result.split(" ");
                for(String idString: ids){
                    String itemJson = MainActivity.mProxy.getItem(Integer.parseInt(idString));
                    JSONObject jObject  = new JSONObject(itemJson);
                    int id = jObject.getInt("id");
                    String name = jObject.getString("name");
                    double price = jObject.getDouble("price");
                    String type = jObject.getString("type");
                    int calories = jObject.getInt("calories");
                    int sugar = jObject.getInt("sugar");
                    String info = jObject.getString("info");
                    String pic = jObject.getString("pic");

                    String infoFile = MainActivity.mProxy.getFile(info);

                    File outputFile = new File(MainActivity.mDirPath + "items/item_" + id + ".html");

                    try {
                        Writer writer = new BufferedWriter(new FileWriter(outputFile, false));
                        writer.write(infoFile);
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //TODO: pic

                    ContentValues ctx = new ContentValues();
                    ctx.put(VendingMachineDatabase.ITEM_NAME, name);
                    ctx.put(VendingMachineDatabase.ITEM_PRICE, price);
                    ctx.put(VendingMachineDatabase.ITEM_TYPE, type);
                    ctx.put(VendingMachineDatabase.ITEM_CAL, calories);
                    ctx.put(VendingMachineDatabase.ITEM_SUGAR, sugar);
                    getContentResolver().update(VendingMachineContentProvider.CONTENT_URI,
                            ctx, VendingMachineDatabase.ITEM_ID+"=?",new String[] {String.valueOf(id)});
                }
                MainActivity.mProxy.updateSyncDate(machineID);
                MainActivity.mProxy.disconnect();

                return "";
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    private void sendNotification(Context context) {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        long[] vibratePattern = { 0, 200, 200, 300 };

        // Build the Notification
        Notification.Builder notificationBuilder = new Notification.Builder(
                context).setTicker("SmartCalsVendingMachine")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true).setContentTitle("SmartCalsVendingMachine")
                .setContentText("Items updated!").setContentIntent(contentIntent)
                .setSound(alarmSound).setVibrate(vibratePattern);

        // Get the NotificationManager
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        // Pass the Notification to the NotificationManager:
        mNotificationManager.notify(MY_NOTIFICATION_ID,
                notificationBuilder.build());

    }
}