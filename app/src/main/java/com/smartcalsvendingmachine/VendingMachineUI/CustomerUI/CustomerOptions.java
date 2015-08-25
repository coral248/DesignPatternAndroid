package com.smartcalsvendingmachine.VendingMachineUI.CustomerUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.smartcalsvendingmachine.R;
import com.smartcalsvendingmachine.VendingMachineUI.MainActivity;


public class CustomerOptions extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_options);
    }

    public void onClick(View view){
        Intent intent;
        switch(view.getId()){
            case R.id.view_items:
                intent = new Intent(this, CustomerItemType.class);
                startActivity(intent);
                break;
            case R.id.search_suggestion:
                intent = new Intent(this, CustomerSearch.class);
                startActivity(intent);
                break;
            case R.id.smartCalCard:
                intent = new Intent(this, CustomerCard.class);
                startActivity(intent);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.cancel:
                intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }
}
