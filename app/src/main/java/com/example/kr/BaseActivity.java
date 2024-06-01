package com.example.kr;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "prefs";
    private static final String KEY_THEME = "theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int themeId = sharedPreferences.getInt(KEY_THEME, R.style.AppTheme);
        setTheme(themeId);
        super.onCreate(savedInstanceState);
    }

    protected void applyTheme(int themeId) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_THEME, themeId);
        editor.apply();
        recreate();
    }
}
