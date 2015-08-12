package com.smartcalsvendingmachine.Database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.SQLException;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

public class VendingMachineContentProvider extends ContentProvider {
    public static final String PROVIDER_NAME = "com.smartcalsvendingmachine.provider";
    public static final String URL = "content://" + PROVIDER_NAME + "/items";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    private static HashMap<String, String> ITEMS_PROJECTION_MAP;

    static final int ITEMS = 1;
    static final int ITEM_ID = 2;
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "items", ITEMS);
        uriMatcher.addURI(PROVIDER_NAME, "items/#", ITEM_ID);
    }

    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        VendingMachineDatabase dbHelper = new VendingMachineDatabase(getContext());
        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insert(VendingMachineDatabase.ITEMS_TABLE, "", values);

        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(VendingMachineDatabase.ITEMS_TABLE);
        switch (uriMatcher.match(uri)) {
            case ITEMS:
                qb.setProjectionMap(ITEMS_PROJECTION_MAP);
                break;
            case ITEM_ID:
                qb.appendWhere(VendingMachineDatabase.ITEM_ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        Cursor c = qb.query(db, projection, selection, selectionArgs,
                null, null, sortOrder);

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        count = db.delete(VendingMachineDatabase.ITEMS_TABLE, null, null);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count;
        switch (uriMatcher.match(uri)){
            case ITEMS:
                count = db.update(VendingMachineDatabase.ITEMS_TABLE, values,
                        selection, selectionArgs);
                break;
            case ITEM_ID:
                count = db.update(VendingMachineDatabase.ITEMS_TABLE, values,
                        VendingMachineDatabase.ITEM_ID +
                        " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case ITEMS:
                return "vnd.android.cursor.dir/vnd.example.students";
            case ITEM_ID:
                return "vnd.android.cursor.item/vnd.example.students";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}
