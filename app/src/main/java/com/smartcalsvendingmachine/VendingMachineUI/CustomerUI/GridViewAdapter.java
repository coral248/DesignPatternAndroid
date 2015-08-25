package com.smartcalsvendingmachine.VendingMachineUI.CustomerUI;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.smartcalsvendingmachine.R;
import com.smartcalsvendingmachine.VendingMachineUI.MainActivity;
import com.smartcalsvendingmachine.SQLiteDatabase.VendingMachineDatabase;

public class GridViewAdapter extends ResourceCursorAdapter {

    public GridViewAdapter(Context context, int layout, Cursor c, int flags) {

        super(context, layout, c, flags);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        return inflater.inflate(R.layout.grid_item, parent, false);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView nameTextView = (TextView) view.findViewById(R.id.item_name);
        TextView priceTextView = (TextView) view.findViewById(R.id.item_price);
        int id = cursor.getInt(cursor.getColumnIndex(VendingMachineDatabase.ITEM_ID));
        String name = cursor.getString(cursor.getColumnIndex(VendingMachineDatabase.ITEM_NAME));
        double price = cursor.getDouble(cursor.getColumnIndex(VendingMachineDatabase.ITEM_PRICE));
        nameTextView.setText(name);
        priceTextView.setText("$" + price);

        String pic = cursor.getString(cursor.getColumnIndex(VendingMachineDatabase.ITEM_PIC));
        if(!pic.equals("")){
            ImageView imageView = (ImageView) view.findViewById(R.id.item_image);
            int bitmapSize = (int) context.getResources().getDimension(
                    R.dimen.grid_image_height);
            Bitmap thumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(MainActivity.mDirPath + pic), bitmapSize, bitmapSize);
            imageView.setImageBitmap(thumbImage);
        }

        view.setOnClickListener(new MyListener(id));
    }

    private class MyListener implements View.OnClickListener {
        private int code;

        MyListener(int item){
            code = item;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ItemDetail.class);
            intent.putExtra("code", code);
            v.getContext().startActivity(intent);
        }
    }
}
