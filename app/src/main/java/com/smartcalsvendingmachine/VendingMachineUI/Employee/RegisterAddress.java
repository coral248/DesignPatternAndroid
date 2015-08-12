package com.smartcalsvendingmachine.VendingMachineUI.Employee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.smartcalsvendingmachine.R;
import com.smartcalsvendingmachine.VendingMachineUI.MainActivity;

public class RegisterAddress extends Activity {

    private EditText addressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_address);

        addressText = (EditText) findViewById(R.id.address);
        addressText.setText(MainActivity.getDefaults("machineAddress", RegisterAddress.this));
    }

    public void onClick(View view){
        Intent intent;
        switch(view.getId()){
            case R.id.address_next:
                MainActivity.setDefaults("machineAddress", addressText.getText().toString(), RegisterAddress.this);
                intent = new Intent(this, RegisterItems.class);
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
}
