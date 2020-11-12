package com.java.appone.manu.VIEW;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.java.appone.manu.CONTROLLER.DatabaseC;
import com.java.appone.manu.MODEL.UserM;
import com.java.appone.manu.R;

public class InitialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        final TextInputEditText userName = findViewById(R.id.input_userName);

        findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userName.getText().toString().equals("")) {
                    Snackbar.make(findViewById(R.id.initial_activity_id), "Preencha o campo com o seu nome!", Snackbar.LENGTH_LONG).show();
                } else {
                    UserM um = new UserM();
                    DatabaseC dc = new DatabaseC(getBaseContext());

                    um.setUserName(userName.getText().toString());
                    boolean result = dc.registerUser(um);
                    if (result) {
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("initialActivity", true);
                        editor.apply();

                        Intent intent = new Intent(InitialActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }
}
