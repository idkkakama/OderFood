package com.example.qlnhda1;

import com.google.firebase.firestore.DocumentSnapshot;
import java.util.HashMap;
import java.util.Map;

public class Food {
    public String id, name, category, image, description, station;
    public double price;
    public boolean available;

    public Food() {}

    public Food(String id, String name, double price, String category, String image, String description, boolean available) {
        this(id, name, price, category, image, description, "Bếp chính", available);
    }

    public Food(String id, String name, double price, String category, String image, String description, String station, boolean available) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.image = image;
        this.description = description;
        this.station = station == null || station.trim().isEmpty() ? "Bếp chính" : station;
        this.available = available;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> m = new HashMap<>();
        m.put("name", name);
        m.put("price", price);
        m.put("category", category);
        m.put("image", image);
        m.put("description", description);
        m.put("station", station);
        m.put("available", available);
        m.put("updatedAt", com.google.firebase.firestore.FieldValue.serverTimestamp());
        return m;
    }

    public static Food from(DocumentSnapshot d) {
        Double p = d.getDouble("price");
        Boolean a = d.getBoolean("available");
        String st = d.getString("station");
        return new Food(
            d.getId(),
            d.getString("name"),
            p == null ? 0 : p,
            d.getString("category"),
            d.getString("image"),
            d.getString("description"),
            st == null ? "Bếp chính" : st,
            a == null || a
        );
    }
}
