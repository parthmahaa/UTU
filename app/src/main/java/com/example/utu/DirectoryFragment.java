package com.example.utu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.PropertyName;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DirectoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private DirectoryAdapter facultyAdapter;
    private DepartmentAdapter departmentAdapter;
    private List<Faculty> facultyList;
    private List<String> departmentList;
    private FirebaseFirestore db;
    private boolean showingDepartments = true;
    private ImageButton backButton;
    private TextView toolbarTitle;
    private boolean facultyLoaded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_directory, container, false);

        recyclerView = view.findViewById(R.id.directoryRecyclerView);
        backButton = view.findViewById(R.id.back_button);
        toolbarTitle = view.findViewById(R.id.toolbar_title);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        facultyList = new ArrayList<>();
        departmentList = new ArrayList<>();
        facultyAdapter = new DirectoryAdapter(facultyList);
        departmentAdapter = new DepartmentAdapter(departmentList, dept -> {
            fetchFacultyByDepartment(dept);
            toolbarTitle.setText(dept);
        });

        recyclerView.setAdapter(departmentAdapter);
        db = FirebaseFirestore.getInstance();
        fetchDepartments();

        backButton.setOnClickListener(v -> {
            showingDepartments = true;
            facultyLoaded = false;
            fetchDepartments();
            updateToolbarVisibility();
        });

        updateToolbarVisibility();
        return view;
    }

    private void fetchDepartments() {
        showingDepartments = true;
        departmentList.clear();
        departmentList.addAll(Arrays.asList("Computer Engineering", "Mechanical Engineering"));
        Log.d("DirectoryFragment", "Departments set: " + departmentList);
        recyclerView.setAdapter(departmentAdapter);
        departmentAdapter.notifyDataSetChanged();
        updateToolbarVisibility();
    }

    private void fetchFacultyByDepartment(String department) {
        if (facultyLoaded) {
            Log.d("DirectoryFragment", "Faculty already loaded for " + department + ", skipping fetch");
            return;
        }

        showingDepartments = false;
        Log.d("DirectoryFragment", "Fetching faculty for: " + department);
        db.collection("faculty_directory")
                .whereEqualTo("department", department)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    facultyList.clear();
                    Set<String> uniqueFaculty = new HashSet<>();
                    int count = 0;
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Faculty faculty = doc.toObject(Faculty.class);
                        if (uniqueFaculty.add(faculty.getName())) {
                            facultyList.add(faculty);
                            count++;
                            Log.d("DirectoryFragment", "Added faculty: " + faculty.getName());
                        }
                    }
                    Log.d("DirectoryFragment", "Total faculty fetched: " + count);
                    if (facultyList.isEmpty()) {
                        facultyList.add(new Faculty("No faculty available", department, "", "", ""));
                        Log.d("DirectoryFragment", "No faculty found for " + department);
                    }
                    recyclerView.setAdapter(facultyAdapter);
                    facultyAdapter.notifyDataSetChanged();
                    facultyLoaded = !facultyList.isEmpty() && !facultyList.get(0).getName().equals("No faculty available");
                    updateToolbarVisibility();
                })
                .addOnFailureListener(e -> {
                    Log.e("DirectoryFragment", "Error fetching faculty", e);
                    Toast.makeText(getContext(), "Error fetching faculty: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    facultyList.clear();
                    facultyList.add(new Faculty("Error loading faculty", department, "", "", ""));
                    recyclerView.setAdapter(facultyAdapter);
                    facultyAdapter.notifyDataSetChanged();
                    updateToolbarVisibility();
                });
    }

    private void updateToolbarVisibility() {
        if (showingDepartments) {
            backButton.setVisibility(View.GONE);
            toolbarTitle.setText("Faculty Directory");
        } else {
            backButton.setVisibility(View.VISIBLE);
            // toolbarTitle is already set to the department name in fetchFacultyByDepartment
        }
    }

    public boolean isShowingDepartments() {
        return showingDepartments;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (showingDepartments && !facultyLoaded) {
            fetchDepartments();
        }
        updateToolbarVisibility();
    }
}

class Faculty {
    private String name;
    private String department;
    private String email;
    private String phoneNumber;
    private String officeHours;
    private Timestamp timestamp;

    public Faculty() {}

    public Faculty(String name, String department, String email, String phoneNumber, String officeHours) {
        this.name = name;
        this.department = department;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.officeHours = officeHours;
        this.timestamp = Timestamp.now();
    }

    @PropertyName("name") public String getName() { return name; }
    @PropertyName("name") public void setName(String name) { this.name = name; }

    @PropertyName("department") public String getDepartment() { return department; }
    @PropertyName("department") public void setDepartment(String department) { this.department = department; }

    @PropertyName("email") public String getEmail() { return email; }
    @PropertyName("email") public void setEmail(String email) { this.email = email; }

    @PropertyName("phoneNumber") public String getPhoneNumber() { return phoneNumber; }
    @PropertyName("phoneNumber") public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    @PropertyName("officeHours") public String getOfficeHours() { return officeHours; }
    @PropertyName("officeHours") public void setOfficeHours(String officeHours) { this.officeHours = officeHours; }

    @PropertyName("timestamp") public Timestamp getTimestamp() { return timestamp; }
    @PropertyName("timestamp") public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }

    public long getTimestampMillis() { return timestamp != null ? timestamp.toDate().getTime() : 0; }
}