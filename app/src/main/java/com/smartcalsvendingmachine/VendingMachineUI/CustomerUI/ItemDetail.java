package com.smartcalsvendingmachine.VendingMachineUI.CustomerUI;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartcalsvendingmachine.R;
import com.smartcalsvendingmachine.VendingMachineUI.MainActivity;
import com.smartcalsvendingmachine.SQLiteDatabase.VendingMachineContentProvider;
import com.smartcalsvendingmachine.SQLiteDatabase.VendingMachineDatabase;


public class ItemDetail extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
    private int code;
    private double price;
    private int quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        Intent intent = getIntent();
        code = intent.getIntExtra("code", 0);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader( int id, Bundle args){
        return new CursorLoader(this, VendingMachineContentProvider.CONTENT_URI,
                null, VendingMachineDatabase.ITEM_ID + "=?", new String[] {String.valueOf(code)}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor){
        cursor.moveToFirst();

        String name = cursor.getString(cursor.getColumnIndex(VendingMachineDatabase.ITEM_NAME));
        price = cursor.getDouble(cursor.getColumnIndex(VendingMachineDatabase.ITEM_PRICE));
        int calories = cursor.getInt(cursor.getColumnIndex(VendingMachineDatabase.ITEM_CAL));
        int sugar = cursor.getInt(cursor.getColumnIndex(VendingMachineDatabase.ITEM_SUGAR));
        String info = cursor.getString(cursor.getColumnIndex(VendingMachineDatabase.ITEM_INFO));
        String picture = cursor.getString(cursor.getColumnIndex(VendingMachineDatabase.ITEM_PIC));
        quantity = cursor.getInt(cursor.getColumnIndex(VendingMachineDatabase.ITEM_QUANTITY));

        if(!picture.equals("")){
            ImageView imageView = (ImageView) findViewById(R.id.item_detail_image);
            int target = imageView.getHeight();
            Bitmap thumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(MainActivity.mDirPath + picture), target, target);
            imageView.setImageBitmap(thumbImage);
        }

        TextView nameTextView = (TextView) findViewById(R.id.item_detail_text);
        nameTextView.setText(name + "\nprice: $" + price + "\nCalories: " + calories + "Ca\nSugars: " + sugar + "g");

        WebView webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///" + MainActivity.mDirPath + info);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){
    }

    public void onClick(View view){
        Intent intent;
        switch(view.getId()){
            case R.id.buy_coins:
                intent = new Intent(this, BuyWithCoins.class);
                intent.putExtra("code", code);
                intent.putExtra("price", price);
                intent.putExtra("quantity", quantity);
                startActivity(intent);
                break;
            case R.id.buy_card:
                intent = new Intent(this, BuyWithCard.class);
                intent.putExtra("code", code);
                intent.putExtra("price", price);
                intent.putExtra("quantity", quantity);
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
