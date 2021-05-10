package com.example.android.inventory20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.inventory20.ItemContract.ItemEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CatalogActivity extends AppCompatActivity {

    private ItemDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CatalogActivity.this,EditorActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new ItemDbHelper(this);
    }

    @Override
    protected void onStart(){
        super.onStart();
        displayDbInfo();
    }

    private void insertDummyData(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ItemEntry.COLUMN_ITEM_NAME,"burette");
        values.put(ItemEntry.COLUMN_ITEM_QUANTITY,10);
        values.put(ItemEntry.COLUMN_ITEM_PRICE,100);

        long newRowId = db.insert(ItemEntry.TABLE_NAME,null,values);
//        Uri newUri = getContentResolver().insert(ItemEntry.CONTENT_URI,values);
    }

    private void displayDbInfo(){
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                ItemEntry._ID,
                ItemEntry.COLUMN_ITEM_NAME,
                ItemEntry.COLUMN_ITEM_QUANTITY,
                ItemEntry.COLUMN_ITEM_PRICE};

        Cursor cursor = getContentResolver().query(
                ItemEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);

//        TextView displayView = (TextView)findViewById(R.id.textView);
//        try{
//            displayView.setText("The table contains " + cursor.getCount() + " entries\n");
//            displayView.append(ItemEntry._ID + " - " +
//                    ItemEntry.COLUMN_ITEM_NAME + " - " +
//                    ItemEntry.COLUMN_ITEM_QUANTITY + " - " +
//                    ItemEntry.COLUMN_ITEM_PRICE  + "\n");
//
//            int idColumnIndex = cursor.getColumnIndex(ItemEntry._ID);
//            int nameColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_NAME);
//            int quantityColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_QUANTITY);
//            int priceColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_PRICE);
//
//
//            while(cursor.moveToNext()) {
//                int currentId = cursor.getInt(idColumnIndex);
//                String currentName = cursor.getString(nameColumnIndex);
//                String currentQuantity = cursor.getString(quantityColumnIndex);
//                String currentPrice = cursor.getString(priceColumnIndex);
//
//                displayView.append("\n" + currentId + " - "+
//                        currentName + " - " +
//                        currentQuantity  + " - " +
//                        currentPrice);
//            }
//        }
//        finally {
//            cursor.close();
//        }

        ListView itemListView = (ListView) findViewById(R.id.list);

        ItemCursorAdapter adapter = new ItemCursorAdapter(this,cursor);

        itemListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_catalog,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.insert_dummy_entry:
                insertDummyData();
                displayDbInfo();
                return true;
            case R.id.delete_all_entries:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}