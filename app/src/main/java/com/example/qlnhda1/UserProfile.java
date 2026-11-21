package com.example.qlnhda1;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public final class UserProfile {

    public static final String ROLE_OWNER = "owner";
    public static final String ROLE_STAFF = "staff";

    private final String uid;
    private final String fullName;
    private final String email;
    private final String role;
    private final String inviteCode;
    private final long createdAt;

    public UserProfile(String uid, String fullName, String email, String role,
                       String inviteCode, long createdAt) {
        this.uid = uid;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.inviteCode = inviteCode;
        this.createdAt = createdAt;
    }

    public static UserProfile fromAuth(FirebaseUser user) {
        String email = user.getEmail() == null ? "" : user.getEmail();
        String displayName = user.getDisplayName();
        if (displayName == null || displayName.trim().isEmpty()) {
            int at = email.indexOf('@');
            displayName = at > 0 ? email.substring(0, at) : email;
        }
        return new UserProfile(user.getUid(), displayName, email, ROLE_STAFF, "", 0L);
    }

    public static UserProfile fromDocument(DocumentSnapshot document, FirebaseUser user) {
        if (document == null || !document.exists()) {
            return fromAuth(user);
        }

        String fallbackName = fromAuth(user).getFullName();
        String fullName = document.getString("fullName");
        String email = document.getString("email");
        String role = document.getString("role");
        String inviteCode = document.getString("inviteCode");
        Long created = document.getLong("createdAt");

        return new UserProfile(
                document.getId(),
                fullName == null || fullName.trim().isEmpty() ? fallbackName : fullName,
                email == null ? "" : email,
                ROLE_OWNER.equals(role) ? ROLE_OWNER : ROLE_STAFF,
                inviteCode == null ? "" : inviteCode,
                created == null ? 0L : created
        );
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("fullName", fullName);
        values.put("email", email);
        values.put("role", role);
        values.put("inviteCode", inviteCode);
        values.put("createdAt", createdAt);
        return values;
    }

    public String getUid() {
        return uid;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}
