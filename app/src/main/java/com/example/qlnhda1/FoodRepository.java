package com.example.qlnhda1;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodRepository {
    public interface Result {
        void done(Exception error);
    }

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ListenerRegistration listenFoods(EventListener<QuerySnapshot> listener) {
        return db.collection("foods").orderBy("name").addSnapshotListener(listener);
    }

    public void save(Food food, Result result) {
        if (food.id == null || food.id.trim().isEmpty()) {
            db.collection("foods").add(food.toMap())
                    .addOnSuccessListener(r -> result.done(null))
                    .addOnFailureListener(result::done);
        } else {
            db.collection("foods").document(food.id).set(food.toMap())
                    .addOnSuccessListener(v -> result.done(null))
                    .addOnFailureListener(result::done);
        }
    }

    public void delete(String id, Result result) {
        db.collection("foods").document(id).delete()
                .addOnSuccessListener(v -> result.done(null))
                .addOnFailureListener(result::done);
    }

    public void createOrder(String table, List<Map<String, Object>> items, double total, Result result) {
        Map<String, Object> order = new HashMap<>();
        order.put("tableId", table);
        order.put("items", items);
        order.put("total", total);
        order.put("status", "pending");
        order.put("createdAt", FieldValue.serverTimestamp());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        order.put("createdBy", user == null ? "" : user.getUid());

        db.collection("orders").add(order)
                .addOnSuccessListener(r -> result.done(null))
                .addOnFailureListener(result::done);
    }

    public ListenerRegistration listenOrdersForTable(String tableId, EventListener<QuerySnapshot> listener) {
        return db.collection("orders")
                .whereEqualTo("tableId", tableId)
                .addSnapshotListener(listener);
    }

    public void processCheckout(String tableId, String paymentMethod, String billProof, Result result) {
        db.collection("orders")
                .whereEqualTo("tableId", tableId)
                .get()
                .addOnSuccessListener(qs -> {
                    if (qs == null || qs.isEmpty()) {
                        resetTableStatus(tableId, result);
                        return;
                    }
                    WriteBatch batch = db.batch();
                    for (DocumentSnapshot doc : qs.getDocuments()) {
                        String status = doc.getString("status");
                        if (!"paid".equals(status)) {
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("status", "paid");
                            updates.put("paymentMethod", paymentMethod);
                            updates.put("paidAt", FieldValue.serverTimestamp());
                            if (billProof != null && !billProof.trim().isEmpty()) {
                                updates.put("billProof", billProof.trim());
                            }
                            batch.update(doc.getReference(), updates);
                        }
                    }
                    batch.commit()
                            .addOnSuccessListener(v -> resetTableStatus(tableId, result))
                            .addOnFailureListener(result::done);
                })
                .addOnFailureListener(result::done);
    }

    private void resetTableStatus(String tableId, Result result) {
        db.collection("tables")
                .whereEqualTo("name", tableId)
                .get()
                .addOnSuccessListener(qs -> {
                    if (qs != null && !qs.isEmpty()) {
                        WriteBatch batch = db.batch();
                        for (DocumentSnapshot doc : qs.getDocuments()) {
                            batch.update(doc.getReference(), "status", "Sẵn sàng");
                        }
                        batch.commit()
                                .addOnSuccessListener(v -> result.done(null))
                                .addOnFailureListener(result::done);
                    } else {
                        result.done(null);
                    }
                })
                .addOnFailureListener(result::done);
    }

    public void transferTable(String fromTableId, String toTableId, Result result) {
        db.collection("orders")
                .whereEqualTo("tableId", fromTableId)
                .get()
                .addOnSuccessListener(qs -> {
                    WriteBatch batch = db.batch();
                    if (qs != null) {
                        for (DocumentSnapshot doc : qs.getDocuments()) {
                            String status = doc.getString("status");
                            if (!"paid".equals(status)) {
                                batch.update(doc.getReference(), "tableId", toTableId);
                            }
                        }
                    }

                    db.collection("tables")
                            .whereEqualTo("name", fromTableId)
                            .get()
                            .addOnSuccessListener(fromQs -> {
                                if (fromQs != null) {
                                    for (DocumentSnapshot doc : fromQs.getDocuments()) {
                                        batch.update(doc.getReference(), "status", "Sẵn sàng");
                                    }
                                }

                                db.collection("tables")
                                        .whereEqualTo("name", toTableId)
                                        .get()
                                        .addOnSuccessListener(toQs -> {
                                            if (toQs != null) {
                                                for (DocumentSnapshot doc : toQs.getDocuments()) {
                                                    batch.update(doc.getReference(), "status", "Đang ăn");
                                                }
                                            }

                                            batch.commit()
                                                    .addOnSuccessListener(v -> result.done(null))
                                                    .addOnFailureListener(result::done);
                                        })
                                        .addOnFailureListener(result::done);
                            })
                            .addOnFailureListener(result::done);
                })
                .addOnFailureListener(result::done);
    }

    public void deleteTableWithOrders(String docId, String tableName, Result result) {
        db.collection("orders")
                .whereEqualTo("tableId", tableName)
                .get()
                .addOnSuccessListener(qs -> {
                    WriteBatch batch = db.batch();
                    if (qs != null) {
                        for (DocumentSnapshot doc : qs.getDocuments()) {
                            batch.delete(doc.getReference());
                        }
                    }
                    batch.delete(db.collection("tables").document(docId));
                    batch.commit()
                            .addOnSuccessListener(v -> result.done(null))
                            .addOnFailureListener(result::done);
                })
                .addOnFailureListener(result::done);
    }
}

