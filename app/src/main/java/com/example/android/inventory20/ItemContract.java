package com.example.android.inventory20;

import android.net.Uri;
import android.provider.BaseColumns;

public final class ItemContract {

//    public static final String CONTENT_AUTHORITY = "com.example.android.inventory20";
//
//    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
//
//    public static final String PATH_ITEM = "Inventory";

    private ItemContract(){}

    public static abstract class ItemEntry implements BaseColumns{

//        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_ITEM);

        public static final String TABLE_NAME = "Inventory";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_ITEM_NAME =  "name";
        public static final String COLUMN_ITEM_PRICE =  "price";
        public static final String COLUMN_ITEM_QUANTITY =  "quantity";
    }
}

