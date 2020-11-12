package com.java.appone.manu.MODEL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseM extends SQLiteOpenHelper {

    public static final int version = 1;
    private static final String databaseName = "database.db";

    public DatabaseM(Context context) {
        super(context, databaseName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE tbl_product (_id integer primary key autoincrement, product_image blob, product_name text, product_brand text, product_size text, product_category text, product_amount integer, product_amountf float, product_unit text, product_expiration text, product_value float, product_comments text, product_creationdate text)";
        String sql2 = "CREATE TABLE tbl_sale (_id integer primary key autoincrement, sale_qd integer, sale_value float, product_cod int, foreign key (product_cod) references tbl_product(_id))";
        String sql3 = "CREATE TABLE tbl_user (_id integer primary key autoincrement, user_name text)";
        db.execSQL(sql);
        db.execSQL(sql2);
        db.execSQL(sql3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tbl_product");
        db.execSQL("DROP TABLE IF EXISTS tbl_sale");
        db.execSQL("DROP TABLE IF EXISTS tbl_user");
        onCreate(db);
    }
}
