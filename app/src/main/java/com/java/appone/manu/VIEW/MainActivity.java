package com.java.appone.manu.VIEW;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.java.appone.manu.CONTROLLER.DatabaseC;
import com.java.appone.manu.CONTROLLER.ProductAdapter;
import com.java.appone.manu.R;

public class MainActivity extends AppCompatActivity {

    boolean initialActivity;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        initialActivity = sharedPreferences.getBoolean("initialActivity", false);
        if (!initialActivity) {
            intent = new Intent(this, InitialActivity.class);
            startActivity(intent);
            finish();
        }else{
            Toolbar toolbar = findViewById(R.id.toolbar_activity_main);
            setSupportActionBar(toolbar);

            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_menu);
            toolbar.setOverflowIcon(drawable);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        findViewById(R.id.btn_addProducts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.see_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, LSProductActivity.class);
                startActivity(intent);
            }
        });

        getUserName();
        setCard();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Toast.makeText(this, "Configurações", Toast.LENGTH_SHORT).show();
                break;
            case R.id.add_products:
                intent = new Intent(this, AddProductActivity.class);
                startActivity(intent);
                break;
            case R.id.list_products:
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getUserName(){
        DatabaseC dc = new DatabaseC(getBaseContext());
        AppCompatTextView userName = findViewById(R.id.txt_user);
        Cursor cursor = dc.returnUserName();

        userName.setText(cursor.getString(cursor.getColumnIndexOrThrow("user_name")));
    }

    private void setCard() {
        DatabaseC dc = new DatabaseC(getBaseContext());
        Cursor cursor = dc.listAllProducts();

        View card = findViewById(R.id.maincardview);
        View recycler = findViewById(R.id.mainrecyclerview);

        if (cursor.getCount() > 0) {
            card.setVisibility(View.INVISIBLE);
            recycler.setVisibility(View.VISIBLE);
            setRecyclerView();
        } else {
            recycler.setVisibility(View.INVISIBLE);
            card.setVisibility(View.VISIBLE);
        }
    }

    private void setRecyclerView() {
        DatabaseC dc = new DatabaseC(getBaseContext());
        Cursor cursor = dc.listAllProducts();

        RecyclerView recyclerView = findViewById(R.id.recycler_view_main_activity);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
        ProductAdapter productAdapter = new ProductAdapter(this, cursor);
        recyclerView.setAdapter(productAdapter);
    }
}