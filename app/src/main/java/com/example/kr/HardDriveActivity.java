package com.example.kr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.CompoundButtonCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HardDriveActivity extends AppCompatActivity {

    private static final String TAG = "HardDriveActivity";
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<HardDrive> hardDriveList;
    private FirebaseFirestore db;
    private String manufacturerName;

    private EditText editTextSearch;
    private Button buttonFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hard_drive);

        listView = findViewById(R.id.listViewHardDrives);
        editTextSearch = findViewById(R.id.editTextSearch);
        buttonFilter = findViewById(R.id.buttonFilter);

        hardDriveList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        manufacturerName = getIntent().getStringExtra("manufacturerName");
        if (manufacturerName != null) {
            Log.d(TAG, "Received manufacturer name: " + manufacturerName);
            getHardDriveModels(manufacturerName);
        } else {
            Toast.makeText(this, "Произошла ошибка. Попробуйте снова.", Toast.LENGTH_SHORT).show();
        }

        editTextSearch.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterHardDrives(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        buttonFilter.setOnClickListener(v -> showFilterDialog());

        listView.setOnItemClickListener((parent, view, position, id) -> {
            HardDrive selectedHardDrive = hardDriveList.get(position);
            Intent intent = new Intent(HardDriveActivity.this, HardDriveDetailActivity.class);
            intent.putExtra("manufacturerName", manufacturerName);
            intent.putExtra("hardDriveModel", selectedHardDrive.getModel());
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
                            Long capacity = document.getLong("capacity");
                            Double price = document.getDouble("price");
                            if (model != null && capacity != null && price != null) {
                                hardDriveList.add(new HardDrive(model, capacity, price));
                            }
                        }
                        filterHardDrives(editTextSearch.getText().toString());
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    private void filterHardDrives(String query) {
        List<String> filteredList = new ArrayList<>();
        for (HardDrive hardDrive : hardDriveList) {
            if (hardDrive.getModel().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(hardDrive.getModel());
            }
        }
        adapter.clear();
        adapter.addAll(filteredList);
        adapter.notifyDataSetChanged();
    }

    private void showFilterDialog() {
        String[] filterOptions = {"Цена по возрастанию", "Цены по убыванию", "Ёмкость диска по возрастанию", "Ёмкость диска по убыванию", "По алфавиту"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выберите фильтр")
                .setItems(filterOptions, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            sortHardDrivesByPrice(true);
                            break;
                        case 1:
                            sortHardDrivesByPrice(false);
                            break;
                        case 2:
                            sortHardDrivesByCapacity(true);
                            break;
                        case 3:
                            sortHardDrivesByCapacity(false);
                            break;
                        case 4:
                            sortHardDrivesAlphabetically();
                            break;
                    }
                });
        builder.create().show();
    }

    private void sortHardDrivesByPrice(boolean ascending) {
        if (ascending) {
            Collections.sort(hardDriveList, Comparator.comparingDouble(HardDrive::getPrice));
        } else {
            Collections.sort(hardDriveList, (hd1, hd2) -> Double.compare(hd2.getPrice(), hd1.getPrice()));
        }
        filterHardDrives(editTextSearch.getText().toString());
    }

    private void sortHardDrivesByCapacity(boolean ascending) {
        if (ascending) {
            Collections.sort(hardDriveList, Comparator.comparingLong(HardDrive::getCapacity));
        } else {
            Collections.sort(hardDriveList, (hd1, hd2) -> Long.compare(hd2.getCapacity(), hd1.getCapacity()));
        }
        filterHardDrives(editTextSearch.getText().toString());
    }

    private void sortHardDrivesAlphabetically() {
        Collections.sort(hardDriveList, Comparator.comparing(HardDrive::getModel));
        filterHardDrives(editTextSearch.getText().toString());
    }

    private static class HardDrive {
        private String model;
        private long capacity;
        private double price;

        public HardDrive(String model, long capacity, double price) {
            this.model = model;
            this.capacity = capacity;
            this.price = price;
        }

        public String getModel() {
            return model;
        }

        public long getCapacity() {
            return capacity;
        }

        public double getPrice() {
            return price;
        }
    }
}
