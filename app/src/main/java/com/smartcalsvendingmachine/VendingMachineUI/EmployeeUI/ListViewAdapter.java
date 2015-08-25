package com.smartcalsvendingmachine.VendingMachineUI.EmployeeUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.smartcalsvendingmachine.R;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter<Item> {

    private final List<Item> items;

    public ListViewAdapter(Context context, int resource, List<Item> items) {
        super(context, resource, items);
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_item, null);
        Item item = items.get(position);

        TextView name = (TextView) row.findViewById(R.id.item_name);
        TextView code = (TextView) row.findViewById(R.id.item_code);
        TextView price = (TextView) row.findViewById(R.id.item_price);
        CheckBox cb = (CheckBox) row.findViewById(R.id.item_checkbox);
        cb.setChecked(item.isChosen());

        name.setText(item.getName());
        code.setText("" + item.getID());
        price.setText("$" + item.getPrice());
        cb.setOnCheckedChangeListener(new MyListener(position));

        return row;
    }

    private class MyListener implements CompoundButton.OnCheckedChangeListener {
        private int position;

        MyListener(int p){
            position = p;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                items.get(position).setChosen(true);
            } else {
                items.get(position).setChosen(false);
            }
        }
    }
}
