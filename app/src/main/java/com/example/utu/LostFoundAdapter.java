package com.example.utu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LostFoundAdapter extends RecyclerView.Adapter<LostFoundAdapter.ViewHolder> {
    private List<LostItem> lostItemList;

    public LostFoundAdapter(List<LostItem> lostItemList) {
        this.lostItemList = lostItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lost_found, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LostItem item = lostItemList.get(position);
        holder.nameTextView.setText(item.getName());
        holder.descriptionTextView.setText(item.getDescription());
        holder.timestampTextView.setText(new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                .format(new Date(item.getTimestampMillis()))); // Use getTimestampMillis()
        holder.imageView.setVisibility(View.GONE); // No Glide yet
    }

    @Override
    public int getItemCount() {
        return lostItemList != null ? lostItemList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, descriptionTextView, timestampTextView;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.lostFoundName);
            descriptionTextView = itemView.findViewById(R.id.lostFoundDescription);
            timestampTextView = itemView.findViewById(R.id.lostFoundTimestamp);
            imageView = itemView.findViewById(R.id.lostFoundImage);
        }
    }
}