package com.example.utu;

import com.google.firebase.firestore.PropertyName;
import com.google.firebase.Timestamp;

public class LostItem {
    private String name;
    private String description;
    private Timestamp timestamp; // Changed from long to Timestamp
    private String imageUrl;

    // Default constructor required for Firestore
    public LostItem() {}

    public LostItem(String name, String description) {
        this.name = name;
        this.description = description;
        this.timestamp = Timestamp.now(); // Use Timestamp.now() for current time
        this.imageUrl = "";
    }

    @PropertyName("name")
    public String getName() { return name; }
    @PropertyName("name")
    public void setName(String name) { this.name = name; }

    @PropertyName("description")
    public String getDescription() { return description; }
    @PropertyName("description")
    public void setDescription(String description) { this.description = description; }

    @PropertyName("timestamp")
    public Timestamp getTimestamp() { return timestamp; }
    @PropertyName("timestamp")
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }

    // Add a convenience method to get timestamp as long for display
    public long getTimestampMillis() {
        return timestamp != null ? timestamp.toDate().getTime() : 0;
    }

    @PropertyName("imageUrl")
    public String getImageUrl() { return imageUrl; }
    @PropertyName("imageUrl")
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}