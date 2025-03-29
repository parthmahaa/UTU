package com.example.utu;

import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.ViewHolder> {
    private List<Faculty> facultyList;

    public DirectoryAdapter(List<Faculty> facultyList) {
        this.facultyList = facultyList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_staff, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Faculty faculty = facultyList.get(position);

        holder.nameTextView.setText(faculty.getName());
        String phoneText = "Phone: " + (faculty.getPhoneNumber() != null ? faculty.getPhoneNumber() : "N/A");
        SpannableString phoneSpannable = new SpannableString(phoneText);
        if (faculty.getPhoneNumber() != null) {
            phoneSpannable.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + faculty.getPhoneNumber()));
                    widget.getContext().startActivity(intent);
                }
            }, 7, phoneText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.phoneTextView.setMovementMethod(LinkMovementMethod.getInstance());
        }
        holder.phoneTextView.setText(phoneSpannable);

        String emailText = "Email: " + (faculty.getEmail() != null ? faculty.getEmail() : "N/A");
        SpannableString emailSpannable = new SpannableString(emailText);
        if (faculty.getEmail() != null) {
            emailSpannable.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + faculty.getEmail()));
                    widget.getContext().startActivity(intent);
                }
            }, 7, emailText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.emailTextView.setMovementMethod(LinkMovementMethod.getInstance());
        }
        holder.emailTextView.setText(emailSpannable);

        holder.departmentTextView.setText("Department: " + (faculty.getDepartment() != null ? faculty.getDepartment() : "N/A"));
        holder.officeHoursTextView.setText("Office Hours: " + (faculty.getOfficeHours() != null ? faculty.getOfficeHours() : "N/A"));
    }

    @Override
    public int getItemCount() {
        return facultyList != null ? facultyList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, phoneTextView, emailTextView, departmentTextView, officeHoursTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.staffName);
            phoneTextView = itemView.findViewById(R.id.staffPhone);
            emailTextView = itemView.findViewById(R.id.staffEmail);
            departmentTextView = itemView.findViewById(R.id.staffDepartment);
            officeHoursTextView = itemView.findViewById(R.id.staffOfficeHours);
        }
    }
}