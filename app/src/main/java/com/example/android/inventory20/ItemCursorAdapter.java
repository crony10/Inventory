package com.example.android.inventory20;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import static android.content.ContentValues.TAG;

public class ItemCursorAdapter extends CursorAdapter {

    public ItemCursorAdapter(Context context, Cursor c) {
        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView quantityTextView = (TextView)view.findViewById(R.id.current_quantity);
        TextView priceTextView = (TextView)view.findViewById(R.id.current_price);

        int nameColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_PRICE);

        String itemName = cursor.getString(nameColumnIndex);
        String quantity = "Quantity: " + cursor.getString(quantityColumnIndex);
        String price = "Price: " + cursor.getString(priceColumnIndex);

        Log.i(TAG, "bindView: name of the item is: " + itemName);
        Log.i(TAG, "bindView: quantity of the item is: " + quantity);
        Log.i(TAG, "bindView: price of the item is: " + price);

        nameTextView.setText(itemName);
        quantityTextView.setText(quantity);
        priceTextView.setText(price);
    }
}
