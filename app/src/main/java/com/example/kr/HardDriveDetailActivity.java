package com.example.kr;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HardDriveDetailActivity extends AppCompatActivity {

    private static final String TAG = "HardDriveDetailActivity";
    private TextView textViewModel;
    private TextView textViewCapacity;
    private TextView textViewPrice;
    private FirebaseFirestore db;
    private String manufacturerName;
    private String hardDriveModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hard_drive_detail);

        textViewModel = findViewById(R.id.textViewModel);
        textViewCapacity = findViewById(R.id.textViewCapacity);
        textViewPrice = findViewById(R.id.textViewPrice);

        db = FirebaseFirestore.getInstance();

        // Получаем имя производителя и модель жесткого диска из Intent
        manufacturerName = getIntent().getStringExtra("manufacturerName");
        hardDriveModel = getIntent().getStringExtra("hardDriveModel");
        if (manufacturerName != null && hardDriveModel != null) {
            Log.d(TAG, "Received manufacturer name: " + manufacturerName);
            Log.d(TAG, "Received hard drive model: " + hardDriveModel);
            getHardDriveDetails(manufacturerName, hardDriveModel);
        } else {
            Toast.makeText(this, "Произошла ошибка. Попробуйте снова.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getHardDriveDetails(String manufacturerName, String hardDriveModel) {
        db.collection("manufacturers")
                .document(manufacturerName)
                .collection("harddrives")
                .document(hardDriveModel)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String model = document.getString("model");
                            String capacity = document.getString("capacity");
                            Number price = document.getDouble("price");

                            textViewModel.setText("Model: " + model);
                            textViewCapacity.setText("Capacity: " + capacity);
                            textViewPrice.setText("Price: " + (price != null ? price.toString() : "N/A"));
                        } else {
                            Log.d(TAG, "No such document");
                            Toast.makeText(HardDriveDetailActivity.this, "Документ не найден.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.w(TAG, "Error getting document.", task.getException());
                    }
                });
    }
}
