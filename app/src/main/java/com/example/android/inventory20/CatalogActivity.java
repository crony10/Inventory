package com.example.android.inventory20;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventory20.ItemContract.ItemEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "catalog:";
    private ItemDbHelper mDbHelper;

    private static final int URI_LOADER = 0;

    ItemCursorAdapter mCursorAdapter;

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


        ListView itemListView = (ListView) findViewById(R.id.list);

        View emptyView = findViewById(R.id.empty_view);
        itemListView.setEmptyView(emptyView);

        mCursorAdapter = new ItemCursorAdapter(this,null);
        itemListView.setAdapter(mCursorAdapter);

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v(TAG,"In here but cannot start the editor");
                Intent intent = new Intent(CatalogActivity.this,EditorActivity.class);

                Uri currentItemUri = ContentUris.withAppendedId(ItemEntry.CONTENT_URI,id);

                intent.setData(currentItemUri);

                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(URI_LOADER,null, this);
        mDbHelper = new ItemDbHelper(this);
    }

//    @Override
//    protected void onStart(){
//        super.onStart();
//    }

    private void insertDummyData(){
//        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ItemEntry.COLUMN_ITEM_NAME,"burette");
        values.put(ItemEntry.COLUMN_ITEM_QUANTITY,10);
        values.put(ItemEntry.COLUMN_ITEM_PRICE,100);

//        long newRowId = db.insert(ItemEntry.TABLE_NAME,null,values);
        Uri newUri = getContentResolver().insert(ItemEntry.CONTENT_URI,values);
    }

//    private void displayDbInfo(){
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//
//        String[] projection = {
//                ItemEntry._ID,
//                ItemEntry.COLUMN_ITEM_NAME,
//                ItemEntry.COLUMN_ITEM_QUANTITY,
//                ItemEntry.COLUMN_ITEM_PRICE};
//
//        Cursor cursor = getContentResolver().query(
//                ItemEntry.CONTENT_URI,
//                projection,
//                null,
//                null,
//                null);
//
////        TextView displayView = (TextView)findViewById(R.id.textView);
//        try{
////            displayView.setText("The table contains " + cursor.getCount() + " entries\n");
//            Toast.makeText(this, "total entries are: "+ cursor.getCount(), Toast.LENGTH_SHORT).show();
////            displayView.append(ItemEntry._ID + " - " +
////                    ItemEntry.COLUMN_ITEM_NAME + " - " +
////                    ItemEntry.COLUMN_ITEM_QUANTITY + " - " +
////                    ItemEntry.COLUMN_ITEM_PRICE  + "\n");
//
////            int idColumnIndex = cursor.getColumnIndex(ItemEntry._ID);
////            int nameColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_NAME);
////            int quantityColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_QUANTITY);
////            int priceColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_PRICE);
////
////
////            while(cursor.moveToNext()) {
////                int currentId = cursor.getInt(idColumnIndex);
////                String currentName = cursor.getString(nameColumnIndex);
////                String currentQuantity = cursor.getString(quantityColumnIndex);
////                String currentPrice = cursor.getString(priceColumnIndex);
////
////                Toast.makeText(this, "id is: " + currentId + " name: " + currentName + " quantity:"
////                        + currentQuantity + " price: "+ currentPrice, Toast.LENGTH_SHORT).show();
////            }
//        }
//        finally {
//            cursor.close();
//        }
//
////        ListView itemListView = (ListView) findViewById(R.id.list);
////
////        ItemCursorAdapter adapter = new ItemCursorAdapter(this,cursor);
////
////        itemListView.setAdapter(adapter);
//    }

    private void deleteAllItem(){
        int rowsDeleted = getContentResolver().delete(ItemEntry.CONTENT_URI,null,null);
        Toast.makeText(this, "All the rows are successfully deleted", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "deleteAllItem: all rows deleted");
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
                return true;
            case R.id.delete_all_entries:
                deleteAllItem();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle bundle) {

                String[] projection = {
                        ItemEntry._ID,
                        ItemEntry.COLUMN_ITEM_NAME,
                        ItemEntry.COLUMN_ITEM_QUANTITY,
                        ItemEntry.COLUMN_ITEM_PRICE};
                return new CursorLoader(
                        this,
                        ItemEntry.CONTENT_URI,
                        projection,
                        null,
                        null,
                        null
                );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}