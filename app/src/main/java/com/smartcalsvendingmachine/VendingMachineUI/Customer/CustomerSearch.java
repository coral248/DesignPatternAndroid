package com.smartcalsvendingmachine.VendingMachineUI.Customer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.smartcalsvendingmachine.R;
import com.smartcalsvendingmachine.VendingMachineUI.MainActivity;

public class CustomerSearch extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_search);
    }

    public void onClick(View view){
        Intent intent;
        switch(view.getId()){
            case R.id.search:
                EditText calFromField = (EditText)findViewById(R.id.calories_from);
                EditText calToField = (EditText)findViewById(R.id.calories_to);
                EditText sugarFromField = (EditText)findViewById(R.id.sugar_from);
                EditText sugarToField = (EditText)findViewById(R.id.sugar_to);
                String calFrom = calFromField.getText().toString();
                String calTo = calToField.getText().toString();
                String sugarFrom = sugarFromField.getText().toString();
                String sugarTo = sugarToField.getText().toString();

                intent = new Intent(this, CustomerSearchResult.class);
                if(!calFrom.equals("")){
                    intent.putExtra("cmin", Integer.parseInt(calFrom));
                }
                if(!calTo.equals("")){
                    intent.putExtra("cmax", Integer.parseInt(calTo));
                }
                if(!sugarFrom.equals("")){
                    intent.putExtra("smin", Integer.parseInt(sugarFrom));
                }
                if(!sugarTo.equals("")){
                    intent.putExtra("smax", Integer.parseInt(sugarTo));
                }
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
