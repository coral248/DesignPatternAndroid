package com.smartcalsvendingmachine.VendingMachineUI.Customer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.smartcalsvendingmachine.R;
import com.smartcalsvendingmachine.VendingMachineUI.MainActivity;

public class CustomerCard extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_card);
    }

    public void onClick(View view){
        Intent intent;
        switch(view.getId()){
            case R.id.buy_card:
                intent = new Intent(this, CustomerBuyCard.class);
                startActivity(intent);
                break;
            case R.id.check_balance:
                intent = new Intent(this, CustomerCheckBalance.class);
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
