package com.example.kr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class HomeActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;

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
            } else if (itemId == R.id.action_language) {
                showLanguageDialog();
                return true;
            } else if (itemId == R.id.action_theme) {
                showThemeDialog();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertTheme);
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

    private void changeLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

        // Перезагрузить активность
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


    private void showLanguageDialog() {
        String[] languages = {getString(R.string.russian), getString(R.string.english)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выберите язык")
                .setItems(languages, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            changeLanguage("ru");
                            Toast.makeText(HomeActivity.this, "Русский язык выбран", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            changeLanguage("en");
                            Toast.makeText(HomeActivity.this, "Английский язык выбран", Toast.LENGTH_SHORT).show();
                            break;
                    }
                });
        builder.create().show();
    }

    private void showThemeDialog() {
        String[] themes = {"Красный (Светлый)", "Красный (Тёмный)", "Коричневый (Светлый)", "Коричневый (Тёмный)"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выберите тему")
                .setItems(themes, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            applyTheme(R.style.Theme_Red_Light);
                            break;
                        case 1:
                            applyTheme(R.style.Theme_Red_Dark);
                            break;
                        case 2:
                            applyTheme(R.style.Theme_Brown_Light);
                            break;
                        case 3:
                            applyTheme(R.style.Theme_Brown_Dark);
                            break;
                    }
                });
        builder.create().show();
    }
}
