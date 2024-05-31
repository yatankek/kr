package com.example.kr;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HardDriveAdapter extends RecyclerView.Adapter<HardDriveAdapter.HardDriveViewHolder> {

    private List<HardDrive> hardDriveList;

    public HardDriveAdapter(List<HardDrive> hardDriveList) {
        this.hardDriveList = hardDriveList;
    }

    @NonNull
    @Override
    public HardDriveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hard_drive, parent, false);
        return new HardDriveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HardDriveViewHolder holder, int position) {
        HardDrive hardDrive = hardDriveList.get(position);
        holder.model.setText(hardDrive.getModel());
        holder.capacity.setText(String.format("Capacity: %s", hardDrive.getCapacity()));
        holder.price.setText(String.format("Price: %s", hardDrive.getPrice()));
    }

    @Override
    public int getItemCount() {
        return hardDriveList.size();
    }

    public static class HardDriveViewHolder extends RecyclerView.ViewHolder {
        TextView model, capacity, price;

        public HardDriveViewHolder(View itemView) {
            super(itemView);
            model = itemView.findViewById(R.id.hardDriveModel);
            capacity = itemView.findViewById(R.id.hardDriveCapacity);
            price = itemView.findViewById(R.id.hardDrivePrice);
        }
    }
}
