package com.example.kr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HardDriveActivity extends AppCompatActivity {

    private static final String TAG = "HardDriveActivity";
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> hardDriveList;
    private FirebaseFirestore db;
    private String manufacturerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hard_drive);

        listView = findViewById(R.id.listViewHardDrives);
        hardDriveList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, hardDriveList);
        listView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        // Получаем имя производителя из Intent
        manufacturerName = getIntent().getStringExtra("manufacturerName");
        if (manufacturerName != null) {
            Log.d(TAG, "Received manufacturer name: " + manufacturerName);
            getHardDriveModels(manufacturerName);
        } else {
            Toast.makeText(this, "Произошла ошибка. Попробуйте снова.", Toast.LENGTH_SHORT).show();
        }

        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Получаем выбранный элемент
            String selectedHardDrive = hardDriveList.get(position);
            Intent intent = new Intent(HardDriveActivity.this, HardDriveDetailActivity.class);
            intent.putExtra("manufacturerName", manufacturerName);
            intent.putExtra("hardDriveModel", selectedHardDrive);
            startActivity(intent);
        });
    }

    private void getHardDriveModels(String manufacturerName) {
        db.collection("manufacturers")
                .document(manufacturerName)
                .collection("harddrives")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        hardDriveList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String model = document.getString("model");
                            if (model != null) {
                                hardDriveList.add(model);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }
}
