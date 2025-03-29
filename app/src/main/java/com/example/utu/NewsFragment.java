package com.example.utu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsFragment extends Fragment {
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private NewsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        db = FirebaseFirestore.getInstance();

        recyclerView = view.findViewById(R.id.newsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize adapter with empty list
        adapter = new NewsAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        fetchNews();
        return view;
    }

    private void fetchNews() {
        db.collection("events")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<NewsItem> newsList = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            String title = document.getString("title");
                            String content = document.getString("description");
                            Object timestampObj = document.get("timestamp");
                            long timestamp = 0;
                            if (timestampObj instanceof Number) {
                                timestamp = ((Number) timestampObj).longValue();
                            } else if (timestampObj instanceof String) {
                                try {
                                    timestamp = Long.parseLong((String) timestampObj);
                                } catch (NumberFormatException e) {
                                    timestamp = 0;
                                }
                            }
                            String formattedDate = formatDate(timestamp);
                            newsList.add(new NewsItem(title, content, formattedDate));
                        }
                        // Update existing adapter instead of creating new one
                        adapter.updateNewsList(newsList);
                    } else {
                        Toast.makeText(getContext(), "Failed to fetch news: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String formatDate(long timestamp) {
        if (timestamp == 0) return "Unknown Date";
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
}

class NewsItem {
    private String title;
    private String description;
    private String date;

    public NewsItem(String title, String content, String date) {
        this.title = title;
        this.description = content;
        this.date = date;
    }

    public String getTitle() { return title; }
    public String getContent() { return description; }
    public String getDate() { return date; }
}
