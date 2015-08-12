package com.smartcalsvendingmachine.VendingMachineUI;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.content.Intent;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.smartcalsvendingmachine.Database.VendingMachineDatabase;
import com.smartcalsvendingmachine.R;
import com.smartcalsvendingmachine.SocketProxy.CustomerServerProxy;
import com.smartcalsvendingmachine.SocketProxy.EmployeeServerProxy;
import com.smartcalsvendingmachine.SocketProxy.MachineServerProxy;
import com.smartcalsvendingmachine.VendingMachineUI.Customer.CustomerOptions;
import com.smartcalsvendingmachine.VendingMachineUI.Employee.EmployeeOptions;
import com.smartcalsvendingmachine.VendingMachineUI.Machine.AlarmNotificationReceiver;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final long PEORIOD = 5 * 60 * 1000L;
    // Database
    public static VendingMachineDatabase VBD;
    public static String mDirPath;
    public static int machineID;
    public static CustomerServerProxy cProxy;
    public static EmployeeServerProxy eProxy;
    public static MachineServerProxy mProxy;

    private AlarmManager mAlarmManager;
    private PendingIntent mNotificationReceiverPendingIntent;
    private MenuItem mMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        machineID = prefs.getInt("machineID", (int) (Math.random() * 99.) + 1);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("machineID", machineID);
        editor.apply();

        VBD = new VendingMachineDatabase(getApplicationContext());
        cProxy = new CustomerServerProxy();
        eProxy = new EmployeeServerProxy();
        mProxy = new MachineServerProxy();

        File parentDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS);
        mDirPath = parentDir.getAbsoluteFile() + "/SmartCalsVendingMachine/";
        File storageDir = new File(mDirPath);

        if (!storageDir.exists() || !storageDir.isDirectory()) {
            storageDir.mkdirs();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mMenuItem = menu.findItem(R.id.action_sync);
        String alarm = getDefaults("alarm", this);
        if (alarm != null && alarm.equals("on")){
            mMenuItem.setIcon(R.drawable.action_sync_on);
            turnOnSync();
        } else {
            mMenuItem.setIcon(R.drawable.action_sync_off);
            shutOffSync();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_ip) {
            final Dialog ipDialog = new Dialog(this);
            ipDialog.setContentView(R.layout.ip_dialog);
            ipDialog.setTitle(getResources().getString(R.string.enter_ip));
            final EditText ipText = (EditText) ipDialog.findViewById(R.id.ip_text);
            String ip = MainActivity.getDefaults("serverIP", MainActivity.this);
            if(ip != null){
                ipText.setText(ip);
            }
            Button dialogButton = (Button) ipDialog.findViewById(R.id.enter);
            dialogButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    String ip = ipText.getText().toString();
                    setDefaults("serverIP", ip, MainActivity.this);
                    ipDialog.dismiss();
                    toast("IP successfully recorded.");
                }
            });
            ipDialog.show();
            return true;
        } else if (id == R.id.action_tool){
            final Dialog loginDialog = new Dialog(this);
            loginDialog.setContentView(R.layout.employee_login_dialog);
            loginDialog.setTitle(getResources().getString(R.string.employee_login));
            final EditText ipEdit = (EditText)loginDialog.findViewById(R.id.login_server_ip);
            final EditText idEdit = (EditText)loginDialog.findViewById(R.id.login_employee_id);
            final EditText pwdEdit = (EditText)loginDialog.findViewById(R.id.login_employee_pwd);
            String ip = MainActivity.getDefaults("serverIP", MainActivity.this);
            if(ip != null){
                ipEdit.setText(ip);
            }
            Button dialogButton = (Button) loginDialog.findViewById(R.id.enter);
            dialogButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    String ip = ipEdit.getText().toString();
                    setDefaults("serverIP", ip, MainActivity.this);
                    String userId = idEdit.getText().toString();
                    String userPwd = pwdEdit.getText().toString();
                    new ConnectEmployeeServerTask().execute(ip, userId, userPwd);
                    loginDialog.dismiss();
                }
            });
            loginDialog.show();
            return true;
        } else if (id == R.id.action_sync) {
            String alarm = getDefaults("alarm", this);
            if (alarm != null && alarm.equals("on")){
                mMenuItem.setIcon(R.drawable.action_sync_off);
                shutOffSync();
                setDefaults("alarm", "off", this);
                Toast.makeText(this, "Sync Off", Toast.LENGTH_LONG).show();
            } else {
                mMenuItem.setIcon(R.drawable.action_sync_on);
                turnOnSync();
                setDefaults("alarm", "on", this);
                Toast.makeText(this, "Sync On", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    public void onClick(View view){
        switch(view.getId()){
            case R.id.start:
                Intent intent = new Intent(MainActivity.this, CustomerOptions.class);
                startActivity(intent);
                break;
        }
    }

    private void toast(String string) {
        Toast.makeText(getApplicationContext(),
                string, Toast.LENGTH_SHORT).show();
    }

    private void turnOnSync() {
        // Get the AlarmManager Service
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Create an Intent to broadcast to the AlarmNotificationReceiver

        Intent notificationReceiverIntent = new Intent(MainActivity.this,
                AlarmNotificationReceiver.class);

        // Create an PendingIntent that holds the NotificationReceiverIntent
        mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(
                MainActivity.this, 0, notificationReceiverIntent, 0);

        // Set repeating alarm
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(), PEORIOD,
                mNotificationReceiverPendingIntent);
    }

    private void shutOffSync(){
        if(mAlarmManager != null){
            mAlarmManager.cancel(mNotificationReceiverPendingIntent);
        }
    }

    private class ConnectEmployeeServerTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {
            try {
                return MainActivity.eProxy.connect(params[0], Integer.parseInt(params[1]), params[2]);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(String name) {
            if (name != null){
                Intent intent = new Intent(MainActivity.this, EmployeeOptions.class);
                startActivity(intent);
            } else {
                toast("Could not connect to the server.");
            }
        }
    }
}
