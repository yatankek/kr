package com.example.kr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class HomeActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;
    private boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            db.collection("users").document(currentUser.getUid()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String role = document.getString("role");
                        isAdmin = "admin".equals(role);
                    }
                } else {
                    Toast.makeText(this, "Ошибка при получении пользовательских данных", Toast.LENGTH_SHORT).show();
                }
                setupBottomNavigation();
            });
        } else {
            setupBottomNavigation();
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().clear();
        bottomNavigationView.inflateMenu(R.menu.bottom_navigation_menu);

        if (isAdmin) {
            bottomNavigationView.getMenu().add(Menu.NONE, R.id.action_admin, Menu.NONE, getString(R.string.admin))
                    .setIcon(R.drawable.ic_admin);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_manufacturers) {
                openManufacturersActivity();
                return true;
            }  else if (itemId == R.id.action_language_theme) {
                showLanguageThemeDialog();
                return true;
            } else if (itemId == R.id.action_admin && isAdmin) {
                openAdminActivity();
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

    private void openAdminActivity() {
        Intent intent = new Intent(HomeActivity.this, AdminActivity.class);
        startActivity(intent);
    }

    private void openManufacturersActivity() {
        Intent intent = new Intent(HomeActivity.this, ManufacturersActivity.class);
        startActivity(intent);
    }

    private void logoutUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertTheme);
        builder.setMessage(getString(R.string.logout))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    mAuth.signOut();
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Закрываем HomeActivity, чтобы пользователь не мог вернуться назад
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    private void changeLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void showLanguageDialog() {
        String[] languages = {getString(R.string.russian), getString(R.string.english)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.chooseln))
                .setItems(languages, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            changeLanguage("ru");
                            Toast.makeText(HomeActivity.this, getString(R.string.ruselected), Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            changeLanguage("en");
                            Toast.makeText(HomeActivity.this, getString(R.string.enselected), Toast.LENGTH_SHORT).show();
                            break;
                    }
                });
        builder.create().show();
    }

    private void showThemeDialog() {
        String[] themes = {getString(R.string.redl), getString(R.string.redd), getString(R.string.brownl), getString(R.string.brownd)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.chooseth))
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

    private void showLanguageThemeDialog() {
        String[] options = {getString(R.string.lan), getString(R.string.Theme)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.choose_option))
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            showLanguageDialog();
                            break;
                        case 1:
                            showThemeDialog();
                            break;
                    }
                });
        builder.create().show();
    }
}
