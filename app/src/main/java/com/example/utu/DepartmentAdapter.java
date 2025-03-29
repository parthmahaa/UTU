package com.example.utu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.ViewHolder> {
    private List<String> departmentList;
    private OnDepartmentClickListener listener;

    public interface OnDepartmentClickListener {
        void onDepartmentClick(String department);
    }

    public DepartmentAdapter(List<String> departmentList, OnDepartmentClickListener listener) {
        this.departmentList = departmentList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_department, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String department = departmentList.get(position);
        holder.textView.setText(department);
        holder.itemView.setOnClickListener(v -> listener.onDepartmentClick(department));
    }

    @Override
    public int getItemCount() {
        return departmentList != null ? departmentList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.departmentName);
        }
    }
}