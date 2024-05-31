package com.example.kr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private Button btnManufacturers;
    private Button btnLogout;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("com.example.kr", MODE_PRIVATE);

        btnManufacturers = findViewById(R.id.btnManufacturers);
        btnLogout = findViewById(R.id.btnLogout);

        btnManufacturers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openManufacturersActivity();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });
    }

    private void openManufacturersActivity() {
        Intent intent = new Intent(HomeActivity.this, ManufacturersActivity.class);
        startActivity(intent);
    }

    private void logoutUser() {
        mAuth.signOut();
        // Clear "Remember Me" preference
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("rememberMe", false);
        editor.apply();

        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Закрываем HomeActivity, чтобы пользователь не мог вернуться назад
    }
}