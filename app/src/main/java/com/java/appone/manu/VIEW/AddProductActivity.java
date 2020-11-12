package com.java.appone.manu.VIEW;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.java.appone.manu.CONTROLLER.DatabaseC;
import com.java.appone.manu.CONTROLLER.MoneyTextWatcher;
import com.java.appone.manu.MODEL.ProductM;
import com.java.appone.manu.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class AddProductActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    byte[] photoByte;
    String category = "Alimentos", unit = "Unidades";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        Toolbar toolbar = findViewById(R.id.toolbar_activity_add_product);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        categorySpinner();

        unitSpinner();
    }

    @Override
    protected void onStart() {
        super.onStart();

        patternPhoto();

        findViewById(R.id.take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(AddProductActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddProductActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
                } else {
                    takePhoto();
                }
            }
        });

        findViewById(R.id.input_productExpiration).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        TextInputEditText value = findViewById(R.id.input_productValue);
        value.addTextChangedListener(new MoneyTextWatcher(value));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_product_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.save_product:
                onButtonRegisterClick();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void categorySpinner() {
        String[] categoryContentPattern = {"Alimentos", "Brinquedos", "Casa", "Cosméticos", "Eletrônicos", "Vestuário"};
        ProductM pm = new ProductM();
        pm.setCategoryArray(categoryContentPattern);

        AppCompatSpinner spinner = findViewById(R.id.category_spinner);
        List<String> categories = new ArrayList<>();
        String[] categoryContent = pm.getCategoryArray();
        Collections.addAll(categories, categoryContent);


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.activity_add_product_form_spinner_layout, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                category = "Alimentos";
            }
        });
    }

    private void unitSpinner() {
        String[] unitContent = {"Unidades", "Gramas", "Kilogramas", "Mililitros", "Litros", "Metros", "Centímetros"};

        AppCompatSpinner spinner = findViewById(R.id.unit_spinner);
        List<String> units = new ArrayList<>();
        Collections.addAll(units, unitContent);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.activity_add_product_form_spinner_layout, units);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                unit = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                unit = "Unidades";
            }
        });
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle extras = data.getExtras();
                assert extras != null;
                Bitmap photo = (Bitmap) extras.get("data");
                AppCompatImageView imageView = findViewById(R.id.photo_product);
                imageView.setImageBitmap(photo);

                assert photo != null;
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                photoByte = byteArrayOutputStream.toByteArray();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void patternPhoto() {
        ProductM pm = new ProductM();
        Bitmap bitmapPhoto = drawableToBitmap();

        assert bitmapPhoto != null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapPhoto.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        photoByte = byteArrayOutputStream.toByteArray();
        pm.setPhoto(photoByte);
    }

    private Bitmap drawableToBitmap() {
        Drawable photo = getDrawable(R.drawable.ic_camera);

        assert photo != null;
        try {
            Bitmap bitmapPhoto = Bitmap.createBitmap(photo.getIntrinsicWidth(), photo.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmapPhoto);
            photo.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            photo.draw(canvas);
            return bitmapPhoto;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    private void clearFields(TextInputEditText name, TextInputEditText brand, TextInputEditText size, TextInputEditText amount, TextInputEditText expiration, TextInputEditText value, TextInputEditText comments) {
        ProductM pm = new ProductM();
        AppCompatImageView photo = findViewById(R.id.photo_product);
        photo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_camera));

        name.setText("");
        brand.setText("");
        size.setText("");
        amount.setText("");
        expiration.setText("");
        value.setText("");
        comments.setText("");

        pm.setAmountFloat(0);
        pm.setAmount(0);

        patternPhoto();
    }

    private void onButtonRegisterClick() {
        ProductM pm = new ProductM();
        DatabaseC dc = new DatabaseC(getBaseContext());
        final View myView = findViewById(R.id.activity_add_id);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final TextInputEditText name = findViewById(R.id.input_productName);
        final TextInputEditText brand = findViewById(R.id.input_productBrand);
        final TextInputEditText size = findViewById(R.id.input_productSize);
        final TextInputEditText amount = findViewById(R.id.input_productAmount);
        final TextInputEditText expiration = findViewById(R.id.input_productExpiration);
        final TextInputEditText value = findViewById(R.id.input_productValue);
        final TextInputEditText comments = findViewById(R.id.input_productComments);

        if (name.getText().toString().equals("") || amount.getText().toString().equals("") || value.getText().toString().equals("")) {
            Snackbar.make(myView, "Por favor, preencha os campos obrigatórios!", Snackbar.LENGTH_SHORT).show();
        } else if (category.equals("Vestuário") && size.getText().toString().equals("")) {
            Snackbar.make(myView, "Quando a categoria do produto é Vestuário o campo Tamanho é obrigatório!", Snackbar.LENGTH_LONG).show();
        } else if (category.equals("Alimentos") && expiration.getText().toString().equals("")) {
            Snackbar.make(myView, "Quando a categoria do produto é Alimentos ou Cosméticos o campo Validade é obrigatório!", Snackbar.LENGTH_LONG).show();
        } else if (amount.getText().toString().equals("0") || amount.getText().toString().equals(".")) {
            Snackbar.make(myView, "A quantidade do produto não pode ser 0(ZERO) ou nulo.", Snackbar.LENGTH_LONG).show();
        } else {
            final Cursor cursor = dc.searchProductByName(name.getText().toString());
            if (cursor.getCount() > 0) {
                builder.setTitle("Produto já cadastrado!");
                builder.setMessage("O produto " + name.getText().toString() + " já está cadastrado no sistema!");
                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        clearFields(name, brand, size, amount, expiration, value, comments);
                    }
                });
                builder.setCancelable(false);
                builder.create().show();
            } else {
                String price;
                pm.setPhoto(photoByte);
                pm.setName(name.getText().toString());
                pm.setBrand(brand.getText().toString());
                pm.setSize(size.getText().toString());
                pm.setCategory(category);
                if (!amount.getText().toString().contains(".")) {
                    pm.setAmount(Integer.parseInt(amount.getText().toString()));
                    pm.setAmountFloat(0);
                } else {
                    pm.setAmountFloat(Float.parseFloat(amount.getText().toString()));
                    pm.setAmount(0);
                }
                pm.setUnit(unit);
                pm.setExpiration(expiration.getText().toString());
                price = MoneyTextWatcher.formatPriceSave(value.getText().toString());
                pm.setValue(Float.parseFloat(price));
                pm.setComments(comments.getText().toString());
                boolean result = dc.registerProduct(pm);
                if (result) {
                    clearFields(name, brand, size, amount, expiration, value, comments);
                    builder.setTitle("Produto cadastrado com sucesso!");
                    builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            builder.setTitle("Cadastrar mais um produto?");
                            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    findViewById(R.id.input_productName).requestFocus();
                                }
                            });
                            builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    onBackPressed();
                                }
                            });
                            builder.create().show();
                        }
                    });
                    builder.setCancelable(false);
                    builder.create().show();
                } else {
                    Snackbar.make(myView, "Erro ao tentar cadastrar o produto.", Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH) + 1,
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        String date = i2 + "/" + i1 + "/" + i;
        TextInputEditText expiration = findViewById(R.id.input_productExpiration);
        expiration.setText(date);
    }
}
