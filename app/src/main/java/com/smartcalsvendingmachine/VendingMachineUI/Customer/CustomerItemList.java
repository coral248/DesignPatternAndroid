package com.smartcalsvendingmachine.VendingMachineUI.Customer;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.RadioGroup;

import com.smartcalsvendingmachine.R;
import com.smartcalsvendingmachine.VendingMachineUI.MainActivity;
import com.smartcalsvendingmachine.Database.VendingMachineContentProvider;
import com.smartcalsvendingmachine.Database.VendingMachineDatabase;

public class CustomerItemList extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int SORT_BY_CAL = 0;
    private static final int SORT_BY_SUGAR = 1;

    private String type;
    private int sort;
    private GridViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_grid);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.sort_radio);
        radioGroup.check(R.id.sort_by_calories);
        sort = SORT_BY_CAL;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.sort_by_calories:
                        sort = SORT_BY_CAL;
                        getLoaderManager().restartLoader(0, null, CustomerItemList.this);
                        break;
                    case R.id.sort_by_sugar:
                        sort = SORT_BY_SUGAR;
                        getLoaderManager().restartLoader(0, null, CustomerItemList.this);
                        break;
                }
            }
        });

        mAdapter = new GridViewAdapter(this, R.layout.grid_item, null, 0);
        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(mAdapter);

        getLoaderManager().initLoader(0, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader( int id, Bundle args){
        if(sort == SORT_BY_CAL){
            return new CursorLoader(this, VendingMachineContentProvider.CONTENT_URI,
                    null, VendingMachineDatabase.ITEM_TYPE + "=? AND " + VendingMachineDatabase.ITEM_QUANTITY
                    + ">0", new String[] {type}, VendingMachineDatabase.ITEM_CAL + " ASC");
        } else if (sort == SORT_BY_SUGAR) {
            return new CursorLoader(this, VendingMachineContentProvider.CONTENT_URI,
                    null, VendingMachineDatabase.ITEM_TYPE + "=? AND " + VendingMachineDatabase.ITEM_QUANTITY
                    + ">0", new String[] {type}, VendingMachineDatabase.ITEM_SUGAR + " ASC");
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data){
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        mAdapter.swapCursor(null);
    }

    public void onClick(View view){
        switch(view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.cancel:
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }
}
