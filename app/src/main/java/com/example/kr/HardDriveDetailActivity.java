package com.example.kr;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class HardDriveDetailActivity extends AppCompatActivity {

    private static final String TAG = "HardDriveDetailActivity";

    private TextView textViewModel, textViewCapacity, textViewPrice;
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
        DocumentReference docRef = db.collection("manufacturers")
                .document(manufacturerName)
                .collection("harddrives")
                .document(hardDriveModel);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String model = document.getString("model");
                    Long capacity = document.getLong("capacity");
                    Double price = document.getDouble("price");

                    textViewModel.setText("Model: " + model);
                    textViewCapacity.setText("Capacity: " + (capacity != null ? capacity.toString() : "N/A") + " GB");
                    textViewPrice.setText("Price: $" + (price != null ? price.toString() : "N/A"));
                } else {
                    Log.d(TAG, "No such document");
                    Toast.makeText(HardDriveDetailActivity.this, "Документ не найден", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
                Toast.makeText(HardDriveDetailActivity.this, "Ошибка при получении документа", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
