package com.smartcalsvendingmachine.SQLiteDatabase;
//VendingMachine database layer, creating database and handling database requests from everywhere.
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VendingMachineDatabase extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 4;
    // Database
    public static final String DATABASE_NAME = "VendingMachine";
    // Tables
    public static final String ITEMS_TABLE = "items";
    public static final String SALES_TABLE = "sales";

    // items Table Column names
    public static final String ITEM_ID = "_id";
    public static final String ITEM_NAME = "name";
    public static final String ITEM_PRICE = "price";
    public static final String ITEM_TYPE = "type";
    public static final String ITEM_CAL = "calories";
    public static final String ITEM_SUGAR = "sugar";
    public static final String ITEM_INFO = "info";
    public static final String ITEM_PIC = "picture";
    public static final String ITEM_CAPACITY = "capacity";
    public static final String ITEM_QUANTITY = "quantity";

    // sales Table Column names
    public static final String SALE_ID = "ID";
    public static final String SALE_ITEM = "item";
    public static final String SALE_PROFIT = "profit";
    public static final String SALE_PDATE = "purchaseDate";
    public static final String SALE_UDATE = "uploadDate";

    SQLiteDatabase db;

    public VendingMachineDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // table create query
        String CREATE_ITEMS_TABLE = "CREATE TABLE IF NOT EXISTS " +
                ITEMS_TABLE + " ("
                + ITEM_ID + " INTEGER PRIMARY KEY UNIQUE, "
                + ITEM_NAME + " TEXT, "
                + ITEM_PRICE + " REAL, "
                + ITEM_TYPE + " TEXT, "
                + ITEM_CAL + " INTEGER, "
                + ITEM_SUGAR + " INTEGER, "
                + ITEM_INFO + " TEXT, "
                + ITEM_PIC + " TEXT, "
                + ITEM_CAPACITY + " INTEGER, "
                + ITEM_QUANTITY + " INTEGER)";
        String CREATE_SALES_TABLE = "CREATE TABLE IF NOT EXISTS " +
                SALES_TABLE + " ("
                + SALE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SALE_ITEM + " INTEGER, "
                + SALE_PROFIT + " REAL, "
                + SALE_PDATE + " TEXT, "
                + SALE_UDATE + " TEXT)";

        db.execSQL(CREATE_ITEMS_TABLE);
        db.execSQL(CREATE_SALES_TABLE);
    }

    public long insert(String table, ContentValues cv) {
        return db.insert(table, null, cv);
    }

    public Cursor rawquery(String query) {
        return db.rawQuery(query, null);
    }

    public void query(String query) {
        db.execSQL(query);
    }

    // delete all tables
    public void deleteAll() {
        db.delete(ITEMS_TABLE, null, null);
        db.delete(SALES_TABLE, null, null);
    }

    // recreated the database when the version changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + ITEMS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SALES_TABLE);
        // Create tables again
        onCreate(db);
    }
}