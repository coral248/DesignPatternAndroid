package com.smartcalsvendingmachine.VendingMachineUI.EmployeeUI;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.smartcalsvendingmachine.R;
import com.smartcalsvendingmachine.VendingMachineUI.MainActivity;

public class EmployeeOptions extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_options);
    }

    public void onClick(View view){
        Intent intent;
        switch(view.getId()){
            case R.id.register:
                new RegisterCheckTask().execute();
                break;
            case R.id.refill:
                new RefillCheckTask().execute();
                break;
            case R.id.manage_item:
                new ManageCheckTask().execute();
                break;
            case R.id.logout:
                try{
                    MainActivity.eProxy.disconnect();
                } catch(Exception e){
                    Toast.makeText(getApplicationContext(),
                            "Error.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }

    private class RegisterCheckTask extends AsyncTask<Void, Void, Integer> {

        private Exception ex;

        protected Integer doInBackground(Void... params) {
            try {
                return MainActivity.eProxy.checkMachine(MainActivity.machineID);
            } catch (Exception e) {
                ex = e;
                return -1;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == 0) {
                Intent intent = new Intent(EmployeeOptions.this, RegisterAddress.class);
                startActivity(intent);
            } else if (result == 1) {
                toast("This machine has been registered!");
            } else if (ex != null) {
                ex.printStackTrace();
            }
        }
    }

    private class RefillCheckTask extends AsyncTask<Void, Void, Integer> {

        private Exception ex;

        protected Integer doInBackground(Void... params) {
            try {
                return MainActivity.eProxy.checkMachine(MainActivity.machineID);
            } catch (Exception e) {
                ex = e;
                return -1;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == 1) {
                Intent intent = new Intent(EmployeeOptions.this, EmployeeRefill.class);
                startActivity(intent);
            } else if (result == 0) {
                toast("Register first!");
            } else if (ex != null) {
                ex.printStackTrace();
            }
        }
    }

    private class ManageCheckTask extends AsyncTask<Void, Void, Integer> {

        private Exception ex;

        protected Integer doInBackground(Void... params) {
            try {
                return MainActivity.eProxy.checkMachine(MainActivity.machineID);
            } catch (Exception e) {
                ex = e;
                return -1;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == 1) {
                Intent intent = new Intent(EmployeeOptions.this, ManageItems.class);
                startActivity(intent);
            } else if (result == 0) {
                toast("Register first!");
            } else if (ex != null) {
                ex.printStackTrace();
            }
        }
    }

    private void toast(String string) {
        Toast.makeText(getApplicationContext(),
                string, Toast.LENGTH_SHORT).show();
    }
}
