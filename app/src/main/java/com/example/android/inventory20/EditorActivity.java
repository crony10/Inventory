package com.example.android.inventory20;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.example.android.inventory20.ItemContract.ItemEntry;
import android.content.CursorLoader;
import android.content.Loader;
import android.app.LoaderManager;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ItemDbHelper mDbHelper;

    private static final int EXISTING_ITEM_LOADER = 0;

    private EditText mName;

    private EditText mQuantity;

    private int quantity = 100;

    private int price = 100;

    private Uri mCurrentItemUri;
    
    private boolean mItemHasChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mName = (EditText) findViewById(R.id.edit_item_name);

        Intent intent = getIntent();
        mCurrentItemUri = intent.getData();

        if (mCurrentItemUri == null) {
            setTitle("Add an item");

            invalidateOptionsMenu();
        } else {
            setTitle("Edit an item");

            getLoaderManager().initLoader(EXISTING_ITEM_LOADER,null,this);
        }
        mName = (EditText) findViewById(R.id.edit_item_name);
        Button plusQuantity = findViewById(R.id.plus_button);
        Button minusQuantity = findViewById(R.id.minus_button);

        mName.setOnTouchListener(mTouchListener);
//        mPrice.setOnTouchListener(mTouchListener);
//        mQuantity.setOnTouchListener(mTouchListener);\
        plusQuantity.setOnTouchListener(mTouchListener);
        minusQuantity.setOnTouchListener(mTouchListener);

    }

    public void quantityPlus(View v) {
        int increment;
        mQuantity = (EditText) findViewById(R.id.increase_by_quantity);
        String incrementString = "";
        incrementString = mQuantity.getText().toString();

        if (!incrementString.equals("")) {
            increment = Integer.parseInt(incrementString);
        } else {
            increment = 10;
        }
        quantity += increment;
        displayQuantity(quantity);
    }

    public void quantityMinus(View v) {
        int decrement;
        mQuantity = (EditText) findViewById(R.id.increase_by_quantity);
        String decrementString = "";
        decrementString = mQuantity.getText().toString();

        if (!decrementString.equals("")) {
            decrement = Integer.parseInt(decrementString);
        } else {
            decrement = 10;
        }

        if (decrement <= quantity) {
            quantity -= decrement;
        }
        displayQuantity(quantity);
    }

    public void displayQuantity(int quantity) {
        TextView quantityView = findViewById(R.id.number_display);
        quantityView.setText(String.valueOf(quantity));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    private void saveItem() {

        if(mCurrentItemUri == null) {
            String nameString = mName.getText().toString().trim();
            if(TextUtils.isEmpty(nameString)){
                return;
            }
            int mQuantity = quantity;

            ContentValues values = new ContentValues();
            values.put(ItemContract.ItemEntry.COLUMN_ITEM_NAME, nameString);
            values.put(ItemContract.ItemEntry.COLUMN_ITEM_QUANTITY, mQuantity);

            Uri newUri = getContentResolver().insert(ItemContract.ItemEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, "Insertion failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Insertion successful", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            String nameString = mName.getText().toString().trim();
            if(TextUtils.isEmpty(nameString)){
                return;
            }

            int mQuantity = quantity;

            ContentValues values = new ContentValues();
            values.put(ItemContract.ItemEntry.COLUMN_ITEM_NAME, nameString);
            values.put(ItemContract.ItemEntry.COLUMN_ITEM_QUANTITY, mQuantity);

            int rowsAffected = getContentResolver().update(mCurrentItemUri,values,null,null);

            if (rowsAffected == 0) {
                Toast.makeText(this, "insertion failed",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "insertion successful and rows affected : "+ rowsAffected,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void deleteItem(){
        if(mCurrentItemUri != null){
            int rowsDeleted = getContentResolver().delete(mCurrentItemUri,null,null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, "Deletion failed",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Deletion successful and rows affected : "+ rowsDeleted,
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveItem();
                finish();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!mItemHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return true;
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mItemHasChanged = true;
            return false;
        }
    };

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit the item editor?");
        builder.setPositiveButton("Discard",discardButtonClickListener);
        builder.setNegativeButton("Keep", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if(dialog != null){
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you really want to delete this pet?");
        builder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteItem();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dialog != null){
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed(){
        if(!mItemHasChanged){
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ItemEntry.COLUMN_ITEM_NAME,
                ItemEntry.COLUMN_ITEM_QUANTITY
        };
        return new CursorLoader (this,
                mCurrentItemUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if(cursor.moveToFirst()){
            int nameColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_NAME);
            int quantityColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_QUANTITY);

            String name = cursor.getString(nameColumnIndex);
            String quantity = cursor.getString(quantityColumnIndex);

            mName.setText(name);

            TextView quantityView = findViewById(R.id.number_display);
            quantityView.setText(String.valueOf(quantity));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mName.setText("");

        TextView quantityView = findViewById(R.id.number_display);
        quantityView.setText(String.valueOf(0));
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);

        if(mCurrentItemUri == null){
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }
}
