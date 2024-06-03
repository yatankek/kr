package com.example.kr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminActivity extends BaseActivity {

    private EditText editTextHDDModel, editTextHDDCapacity, editTextHDDDescEn, editTextHDDDescRu, editTextHDDPrice;
    private Button btnAddHDD, btnDeleteHDD, btnAddManufacturer, btnDeleteManufacturer, btnGoHome;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        editTextHDDModel = findViewById(R.id.editTextHDDModel);
        editTextHDDCapacity = findViewById(R.id.editTextHDDCapacity);
        editTextHDDDescEn = findViewById(R.id.editTextHDDDescEn);
        editTextHDDDescRu = findViewById(R.id.editTextHDDDescRu);
        editTextHDDPrice = findViewById(R.id.editTextHDDPrice);
        btnAddHDD = findViewById(R.id.btnAddHDD);
        btnDeleteHDD = findViewById(R.id.btnDeleteHDD);
        btnAddManufacturer = findViewById(R.id.btnAddManufacturer);
        btnDeleteManufacturer = findViewById(R.id.btnDeleteManufacturer);
        btnGoHome = findViewById(R.id.btnGoHome);

        db = FirebaseFirestore.getInstance();

        btnAddHDD.setOnClickListener(v -> showManufacturerDialogForAdding());

        btnDeleteHDD.setOnClickListener(v -> showHDDDialogForDeleting());

        btnAddManufacturer.setOnClickListener(v -> showAddManufacturerDialog());

        btnDeleteManufacturer.setOnClickListener(v -> showDeleteManufacturerDialog());

        btnGoHome.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void showManufacturerDialogForAdding() {
        db.collection("manufacturers").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                List<String> manufacturers = new ArrayList<>();
                for (DocumentSnapshot document : querySnapshot) {
                    manufacturers.add(document.getId());
                }
                String[] manufacturersArray = manufacturers.toArray(new String[0]);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.selectm));
                builder.setItems(manufacturersArray, (dialog, which) -> {
                    String selectedManufacturer = manufacturersArray[which];
                    addHDD(selectedManufacturer);
                });
                builder.show();
            } else {
                Toast.makeText(this, getString(R.string.errorgettingm), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addHDD(String manufacturer) {
        String model = editTextHDDModel.getText().toString();
        String capacityStr = editTextHDDCapacity.getText().toString();
        String descEn = editTextHDDDescEn.getText().toString();
        String descRu = editTextHDDDescRu.getText().toString();
        String priceStr = editTextHDDPrice.getText().toString();

        if (!model.isEmpty() && !capacityStr.isEmpty() && !descEn.isEmpty() && !descRu.isEmpty() && !priceStr.isEmpty()) {
            int capacity = Integer.parseInt(capacityStr);
            double price = Double.parseDouble(priceStr);

            Map<String, Object> hddData = new HashMap<>();
            hddData.put("model", model);
            hddData.put("capacity", capacity);
            hddData.put("descen", descEn);
            hddData.put("descru", descRu);
            hddData.put("price", price);

            db.collection("manufacturers").document(manufacturer)
                    .collection("harddrives").document(model)
                    .set(hddData)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(AdminActivity.this, getString(R.string.hddadd), Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AdminActivity.this, getString(R.string.erroradd), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void showHDDDialogForDeleting() {
        db.collectionGroup("harddrives").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                List<String> harddrives = new ArrayList<>();
                Map<String, String> hddToManufacturerMap = new HashMap<>();
                for (DocumentSnapshot document : querySnapshot) {
                    String hddPath = document.getReference().getPath();
                    String manufacturer = hddPath.split("/")[1]; // Получаем производителя из пути документа
                    String model = document.getId();
                    harddrives.add(manufacturer + " - " + model);
                    hddToManufacturerMap.put(manufacturer + " - " + model, manufacturer);
                }
                String[] harddrivesArray = harddrives.toArray(new String[0]);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.selecthdd));
                builder.setItems(harddrivesArray, (dialog, which) -> {
                    String selectedHDD = harddrivesArray[which];
                    String manufacturer = hddToManufacturerMap.get(selectedHDD);
                    String model = selectedHDD.split(" - ")[1];
                    deleteHDD(manufacturer, model);
                });
                builder.show();
            } else {
                Toast.makeText(this, getString(R.string.errorrec), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteHDD(String manufacturer, String model) {
        db.collection("manufacturers").document(manufacturer)
                .collection("harddrives").document(model)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AdminActivity.this, getString(R.string.hdddel), Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AdminActivity.this, getString(R.string.errorhdddel), Toast.LENGTH_SHORT).show();
                });
    }

    private void showAddManufacturerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.addm));

        final EditText input = new EditText(this);
        input.setHint(getString(R.string.enterm));
        builder.setView(input);

        builder.setPositiveButton(getString(R.string.add), (dialog, which) -> {
            String manufacturerName = input.getText().toString();
            addManufacturer(manufacturerName);
        });
        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void addManufacturer(String manufacturerName) {
        if (!manufacturerName.isEmpty()) {
            Map<String, Object> manufacturerData = new HashMap<>();
            manufacturerData.put("name", manufacturerName);

            db.collection("manufacturers").document(manufacturerName)
                    .set(manufacturerData)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(AdminActivity.this, getString(R.string.addedm), Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AdminActivity.this, getString(R.string.erroraddedm), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void showDeleteManufacturerDialog() {
        db.collection("manufacturers").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                List<String> manufacturers = new ArrayList<>();
                for (DocumentSnapshot document : querySnapshot) {
                    manufacturers.add(document.getId());
                }
                String[] manufacturersArray = manufacturers.toArray(new String[0]);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.selmrem));
                builder.setItems(manufacturersArray, (dialog, which) -> {
                    String selectedManufacturer = manufacturersArray[which];
                    deleteManufacturer(selectedManufacturer);
                });
                builder.show();
            } else {
                Toast.makeText(this, getString(R.string.errorgetm), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteManufacturer(String manufacturer) {
        db.collection("manufacturers").document(manufacturer)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AdminActivity.this, getString(R.string.mrem), Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AdminActivity.this, getString(R.string.errormrem), Toast.LENGTH_SHORT).show();
                });
    }
}
