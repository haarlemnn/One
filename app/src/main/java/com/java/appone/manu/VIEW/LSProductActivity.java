package com.java.appone.manu.VIEW;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.java.appone.manu.CONTROLLER.DatabaseC;
import com.java.appone.manu.CONTROLLER.ProductAdapterAll;
import com.java.appone.manu.R;
import com.oshi.libsearchtoolbar.SearchAnimationToolbar;

public class LSProductActivity extends AppCompatActivity implements SearchAnimationToolbar.OnSearchQueryChangedListener {

    private SearchAnimationToolbar searchAnimationToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lsproduct);

        searchAnimationToolbar = findViewById(R.id.toolbar_search);
        searchAnimationToolbar.setSupportActionBar(this);
        searchAnimationToolbar.setOnSearchQueryChangedListener(this);
        if(getSupportActionBar() != null){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        setRecyclerView();
    }

    @Override
    public void onBackPressed() {
        boolean handledByToolbar = searchAnimationToolbar.onBackPressed();

        if(!handledByToolbar){
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_lsproduct_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                searchAnimationToolbar.onSearchIconClick();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSearchCollapsed() {

    }

    @Override
    public void onSearchQueryChanged(String query) {

    }

    @Override
    public void onSearchExpanded() {

    }

    @Override
    public void onSearchSubmitted(String query) {

    }

    public void setRecyclerView(){
        DatabaseC dc = new DatabaseC(getBaseContext());
        Cursor cursor = dc.listAllProductsOrderByName();

        RecyclerView recyclerView = findViewById(R.id.recycler_view_lsproduct_adctivity);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(),LinearLayoutManager.VERTICAL,false));
        ProductAdapterAll productAdapter = new ProductAdapterAll(this,cursor);
        recyclerView.setAdapter(productAdapter);
    }
}
