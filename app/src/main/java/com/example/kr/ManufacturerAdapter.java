package com.example.kr;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ManufacturerAdapter extends RecyclerView.Adapter<ManufacturerAdapter.ManufacturerViewHolder> {

    private List<Manufacturer> manufacturerList;
    private Context context;
    private OnManufacturerClickListener onManufacturerClickListener;


    public ManufacturerAdapter(List<Manufacturer> manufacturerList, Context context) {
        this.manufacturerList = manufacturerList;
        this.context = context;
    }

    public interface OnManufacturerClickListener {
        void onManufacturerClick(Manufacturer manufacturer);
    }
    public void setOnManufacturerClickListener(OnManufacturerClickListener listener) {
        this.onManufacturerClickListener = listener;
    }

    @NonNull
    @Override
    public ManufacturerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manufacturer, parent, false);
        return new ManufacturerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManufacturerViewHolder holder, int position) {
        Manufacturer manufacturer = manufacturerList.get(position);
        holder.name.setText(manufacturer.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onManufacturerClickListener != null) {
                    onManufacturerClickListener.onManufacturerClick(manufacturer);
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return manufacturerList.size();
    }

    public static class ManufacturerViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        public ManufacturerViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
        }
    }

}
