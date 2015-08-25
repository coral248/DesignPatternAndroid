package com.smartcalsvendingmachine.VendingMachineUI.EmployeeUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.smartcalsvendingmachine.R;

public class ManageItems extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_items);
    }

    public void onClick(View view){
        Intent intent;
        switch(view.getId()){
            case R.id.add_item:
                intent = new Intent(this, AddItems.class);
                startActivity(intent);
                break;
            case R.id.delete_item:
                intent = new Intent(this, DeleteItem.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                startActivity(intent);
                finish();
                break;
            case R.id.cancel:
                intent = new Intent(this, EmployeeOptions.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }
}
