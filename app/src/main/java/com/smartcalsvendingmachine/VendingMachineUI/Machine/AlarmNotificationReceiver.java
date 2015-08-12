package com.smartcalsvendingmachine.VendingMachineUI.Machine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.smartcalsvendingmachine.VendingMachineUI.MainActivity;

public class AlarmNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String isRegistered = MainActivity.getDefaults("isRegistered", context);
        String ip = MainActivity.getDefaults("serverIP", context);

        if (isRegistered.equals("true")) {
            Intent dailyUpdater = new Intent(context, MyService.class);
            dailyUpdater.putExtra("serverIP", ip);
            context.startService(dailyUpdater);
        }
    }
}
