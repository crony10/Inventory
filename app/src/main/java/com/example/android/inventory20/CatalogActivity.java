package com.example.android.inventory20;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.inventory20.ItemContract.ItemEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;




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

        Uri newUri = getContentResolver().insert(ItemEntry.CONTENT_URI,values);
    }

    private void deleteAllItem(){
        int rowsDeleted = getContentResolver().delete(ItemEntry.CONTENT_URI,null,null);
        Toast.makeText(this, "All the rows are successfully deleted", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "deleteAllItem: all rows deleted");
    }

    private void orderMore(){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT,"Item order for: ");

        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
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
            case R.id.order_more:
                orderMore();
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
                        ItemEntry.COLUMN_ITEM_QUANTITY};
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