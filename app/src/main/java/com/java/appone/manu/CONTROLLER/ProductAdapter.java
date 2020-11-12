package com.java.appone.manu.CONTROLLER;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.java.appone.manu.R;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private Cursor mCursor;
    private LayoutInflater mLayoutInflater;

    public ProductAdapter(Context context, Cursor cursor) {
        mCursor = cursor;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.activity_main_recyclerview_card_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        byte[] image = mCursor.getBlob(mCursor.getColumnIndexOrThrow("product_image"));
        String name = mCursor.getString(mCursor.getColumnIndexOrThrow("product_name"));
        String category = mCursor.getString(mCursor.getColumnIndexOrThrow("product_category"));
        int amount = mCursor.getInt(mCursor.getColumnIndexOrThrow("product_amount"));
        float amountf = mCursor.getFloat(mCursor.getColumnIndexOrThrow("product_amountf"));
        String unit = mCursor.getString(mCursor.getColumnIndexOrThrow("product_unit"));
        float value = mCursor.getFloat(mCursor.getColumnIndexOrThrow("product_value"));

        Bitmap bitmapImage = BitmapFactory.decodeByteArray(image, 0, image.length);
        NumberFormat convertValue = NumberFormat.getCurrencyInstance(Locale.getDefault());
        String categorY = "Categoria: " + category;
        String amountUnit;
        if (amountf == 0) {
            amountUnit = "Quantidade: " + amount + " " + unit.toLowerCase();
        } else {
            amountUnit = "Quantidade: " + amountf + " " + unit.toLowerCase();
        }
        String valuE = "Valor: " + convertValue.format(value);

        holder.productImage.setImageBitmap(bitmapImage);
        holder.productName.setText(name);
        holder.productCategory.setText(categorY);
        holder.productAmountUnit.setText(amountUnit);
        holder.productValue.setText(valuE);
    }

    @Override
    public int getItemCount() {
        int i = 0;
        do{
            i++;
        }while(mCursor.moveToPosition(i) && i < 5);
        return i;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView productImage;
        AppCompatTextView productName;
        AppCompatTextView productCategory;
        AppCompatTextView productAmountUnit;
        AppCompatTextView productValue;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.txt_productName);
            productCategory = itemView.findViewById(R.id.txt_category);
            productAmountUnit = itemView.findViewById(R.id.txt_amount_unit);
            productValue = itemView.findViewById(R.id.txt_value);
        }
    }
}