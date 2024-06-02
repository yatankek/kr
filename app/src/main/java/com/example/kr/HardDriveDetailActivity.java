package com.example.kr;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Locale;

public class HardDriveDetailActivity extends BaseActivity {

    private static final String TAG = "HardDriveDetailActivity";
    private TextView textViewModel;
    private TextView textViewCapacity;
    private TextView textViewPrice;
    private TextView textViewDescription;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hard_drive_detail);

        textViewModel = findViewById(R.id.textViewModel);
        textViewCapacity = findViewById(R.id.textViewCapacity);
        textViewPrice = findViewById(R.id.textViewPrice);
        textViewDescription = findViewById(R.id.textViewDescription);

        db = FirebaseFirestore.getInstance();

        String manufacturerName = getIntent().getStringExtra("manufacturerName");
        String hardDriveModel = getIntent().getStringExtra("hardDriveModel");

        if (manufacturerName != null && hardDriveModel != null) {
            getHardDriveDetails(manufacturerName, hardDriveModel);
        } else {
            Toast.makeText(this, "Произошла ошибка. Попробуйте снова.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getHardDriveDetails(String manufacturerName, String hardDriveModel) {
        db.collection("manufacturers")
                .document(manufacturerName)
                .collection("harddrives")
                .whereEqualTo("model", hardDriveModel)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                            String model = document.getString("model");
                            Long capacity = document.getLong("capacity");
                            Double price = document.getDouble("price");

                            String language = getResources().getConfiguration().locale.getLanguage();

                            String descriptionField;
                            if (language.equals("ru")) {
                                descriptionField = "descru";
                            } else {
                                descriptionField = "descen";
                            }

                            String description = document.getString(descriptionField);

                            if (model != null && capacity != null && price != null && description != null) {
                                textViewModel.setText(getString(R.string.model) + " " + model);
                                textViewCapacity.setText(getString(R.string.capacity) + " " + capacity + " GB");
                                textViewPrice.setText(getString(R.string.price) + " $" + String.format("%.2f", price));
                                textViewDescription.setText(getString(R.string.description) + " " + description);
                            } else {
                                Toast.makeText(this, getString(R.string.errorload), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Документ не найден.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.w(TAG, "Ошибка при получении документов.", task.getException());
                        Toast.makeText(this, "Ошибка при загрузке данных.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}