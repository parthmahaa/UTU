package com.example.utu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class LostFoundFragment extends Fragment {
    private RecyclerView recyclerView;
    private LostFoundAdapter adapter;
    private EditText itemNameEditText, descriptionEditText;
    private Button submitButton;
    private List<LostItem> lostItems;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lost_found, container, false);

        recyclerView = view.findViewById(R.id.lostFoundRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        itemNameEditText = view.findViewById(R.id.itemNameEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        submitButton = view.findViewById(R.id.submitButton);

        lostItems = new ArrayList<>();
        adapter = new LostFoundAdapter(lostItems);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        fetchLostItems();
        submitButton.setOnClickListener(v -> submitLostItem());

        return view;
    }

    private void submitLostItem() {
        String name = itemNameEditText.getText().toString().trim();
        String desc = descriptionEditText.getText().toString().trim();

        if (!name.isEmpty() && !desc.isEmpty()) {
            LostItem newItem = new LostItem(name, desc);
            db.collection("lost_found")
                    .add(newItem)
                    .addOnSuccessListener(documentReference -> {
                        lostItems.add(newItem);
                        adapter.notifyItemInserted(lostItems.size() - 1);
                        recyclerView.scrollToPosition(lostItems.size() - 1);
                        itemNameEditText.setText("");
                        descriptionEditText.setText("");
                        Toast.makeText(getContext(), "Item added to Firestore!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchLostItems() {
        db.collection("lost_found")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(getContext(), "Error fetching items: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    lostItems.clear();
                    if (value != null) {
                        for (QueryDocumentSnapshot doc : value) {
                            LostItem item = doc.toObject(LostItem.class);
                            lostItems.add(item);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}