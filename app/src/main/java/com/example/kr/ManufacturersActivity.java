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

public class ManufacturersActivity extends BaseActivity {

    private static final String TAG = "ManufacturersActivity";
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> manufacturerList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manufacturers);

        listView = findViewById(R.id.listViewManufacturers);
        manufacturerList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, manufacturerList);
        listView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        getManufacturers();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedManufacturer = manufacturerList.get(position);
            Intent intent = new Intent(ManufacturersActivity.this, HardDriveActivity.class);
            intent.putExtra("manufacturerName", selectedManufacturer);
            startActivity(intent);
        });

    }

    private void getManufacturers() {
        db.collection("manufacturers")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        manufacturerList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String manufacturer = document.getId();
                            manufacturerList.add(manufacturer);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }
}
