package com.example.kr;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.example.kr.R;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_manufacturers) {
                openManufacturersActivity();
                return true;
            } else if (itemId == R.id.action_logout) {
                logoutUser();
                return true;
            } else {
                Toast.makeText(HomeActivity.this, "Выбран пункт меню с id: " + itemId, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void openManufacturersActivity() {
        Intent intent = new Intent(HomeActivity.this, ManufacturersActivity.class);
        startActivity(intent);
    }

    private void logoutUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Вы уверены, что хотите выйти?")
                .setPositiveButton("Да", (dialog, which) -> {
                    mAuth.signOut();
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Закрываем HomeActivity, чтобы пользователь не мог вернуться назад
                })
                .setNegativeButton("Отмена", null)
                .show();
    }
}