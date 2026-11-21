package com.example.qlnhda1;

import android.content.Context;
import android.content.SharedPreferences;

public final class SessionManager {

    private static final String PREFS = "auth_session";
    private static final String KEY_UID = "uid";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ROLE = "role";
    private static final String KEY_INVITE_CODE = "invite_code";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_REMEMBERED_EMAIL = "remembered_email";
    private static final String KEY_REMEMBER_SESSION = "remember_session";

    private final SharedPreferences preferences;

    public SessionManager(Context context) {
        preferences = context.getApplicationContext()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public void saveProfile(UserProfile profile) {
        preferences.edit()
                .putString(KEY_UID, profile.getUid())
                .putString(KEY_FULL_NAME, profile.getFullName())
                .putString(KEY_EMAIL, profile.getEmail())
                .putString(KEY_ROLE, profile.getRole())
                .putString(KEY_INVITE_CODE, profile.getInviteCode())
                .putLong(KEY_CREATED_AT, profile.getCreatedAt())
                .apply();
    }

    public UserProfile getCachedProfile() {
        String uid = preferences.getString(KEY_UID, "");
        if (uid == null || uid.isEmpty()) {
            return null;
        }
        return new UserProfile(
                uid,
                preferences.getString(KEY_FULL_NAME, ""),
                preferences.getString(KEY_EMAIL, ""),
                preferences.getString(KEY_ROLE, UserProfile.ROLE_STAFF),
                preferences.getString(KEY_INVITE_CODE, ""),
                preferences.getLong(KEY_CREATED_AT, 0L)
        );
    }

    public void clearSession() {
        preferences.edit()
                .remove(KEY_UID)
                .remove(KEY_FULL_NAME)
                .remove(KEY_EMAIL)
                .remove(KEY_ROLE)
                .remove(KEY_INVITE_CODE)
                .remove(KEY_CREATED_AT)
                .remove(KEY_REMEMBER_SESSION)
                .apply();
    }

    public boolean isSessionRemembered() {
        return preferences.getBoolean(KEY_REMEMBER_SESSION, true);
    }

    public void setSessionRemembered(boolean remembered) {
        preferences.edit().putBoolean(KEY_REMEMBER_SESSION, remembered).apply();
    }

    public String getRememberedEmail() {
        return preferences.getString(KEY_REMEMBERED_EMAIL, "");
    }

    public void setRememberedEmail(String email) {
        preferences.edit().putString(KEY_REMEMBERED_EMAIL, email).apply();
    }

    public void clearRememberedEmail() {
        preferences.edit().remove(KEY_REMEMBERED_EMAIL).apply();
    }
}
