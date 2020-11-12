package com.java.appone.manu.CONTROLLER;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.java.appone.manu.MODEL.DatabaseM;
import com.java.appone.manu.MODEL.ProductM;
import com.java.appone.manu.MODEL.UserM;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatabaseC {

    private SQLiteDatabase database;
    private DatabaseM manageDatabase;

    public DatabaseC(Context context) {
        manageDatabase = new DatabaseM(context);
    }

    public boolean registerUser(UserM um){
        database = manageDatabase.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("user_name", um.getUserName());

        long result = database.insert("tbl_user",null,values);
        database.close();

        return result > 0;
    }

    public Cursor returnUserName(){
        Cursor cursor;

        String[] value = {"user_name"};
        database = manageDatabase.getReadableDatabase();
        cursor = database.query("tbl_user",value,null,null,null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
        }
        database.close();
        return cursor;
    }

    public boolean registerProduct(ProductM pm) {
        database = manageDatabase.getWritableDatabase();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss",Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        ContentValues values = new ContentValues();
        values.put("product_image", pm.getPhoto());
        values.put("product_name", pm.getName());
        values.put("product_brand", pm.getBrand());
        values.put("product_size", pm.getSize());
        values.put("product_category", pm.getCategory());
        if (pm.getAmount() > 0) {
            values.put("product_amount", pm.getAmount());
        } else {
            values.put("product_amountf", pm.getAmountFloat());
        }
        values.put("product_unit", pm.getUnit());
        values.put("product_expiration", pm.getExpiration());
        values.put("product_value", pm.getValue());
        values.put("product_comments", pm.getComments());
        values.put("product_creationdate",simpleDateFormat.format(date));

        long result = database.insert("tbl_product", null, values);
        database.close();

        return result > 0;
    }

    public void updateProductAmount(int productId, int productAmount) {
        database = manageDatabase.getReadableDatabase();
        String where = "_id = " + productId;

        ContentValues values = new ContentValues();
        values.put("product_amount", productAmount);

        database.update("tbl_product", values, where, null);
        database.close();
    }

    public void updateProduct(ProductM pm) {
        database = manageDatabase.getReadableDatabase();
        String where = "_id = " + pm.getCod();

        ContentValues values = new ContentValues();
        values.put("product_name", pm.getName());
        values.put("product_brand", pm.getBrand());
        values.put("product_amount", pm.getAmount());
        values.put("product_value", pm.getValue());

        database.update("tbl_product", values, where, null);
        database.close();
    }

    public Cursor listAllProducts() {
        Cursor cursor;

        String[] values = {"product_image", "product_name", "product_category", "product_amount", "product_amountf", "product_unit", "product_value", "product_creationdate"};
        database = manageDatabase.getReadableDatabase();
        cursor = database.query("tbl_product", values, null, null, null, null, "product_creationdate DESC");

        if (cursor != null) {
            cursor.moveToFirst();
        }
        database.close();
        return cursor;
    }

    public Cursor listAllProductsOrderByName() {
        Cursor cursor;

        String[] values = {"product_image", "product_name", "product_category", "product_amount", "product_amountf", "product_unit", "product_value", "product_creationdate"};
        database = manageDatabase.getReadableDatabase();
        cursor = database.query("tbl_product", values, null, null, null, null, "product_name ASC");

        if (cursor != null) {
            cursor.moveToFirst();
        }
        database.close();
        return cursor;
    }

    public Cursor searchProduct(int productId) {
        Cursor cursor;

        String[] values = {"_id", "product_name", "product_brand", "product_amount", "product_value", "product_description"};
        String where = "_id = " + productId;
        database = manageDatabase.getReadableDatabase();
        cursor = database.query("tbl_product", values, where, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        database.close();
        return cursor;
    }

    public Cursor searchProductByName(String productName) {
        Cursor cursor;

        String[] values = {"product_name"};
        String where = "product_name = '" + productName + "'";
        database = manageDatabase.getReadableDatabase();
        cursor = database.query("tbl_product", values, where, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        database.close();
        return cursor;
    }

    public void deleteProduct(int productId) {
        database = manageDatabase.getReadableDatabase();
        String where = "_id = " + productId;

        database.delete("tbl_product", where, null);
        database.close();
    }
}
