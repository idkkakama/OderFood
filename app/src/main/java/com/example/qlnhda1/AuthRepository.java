package com.example.qlnhda1;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public final class AuthRepository {

    public interface AuthCallback {
        void onSuccess();

        void onError(Exception error);
    }

    public interface ProfileCallback {
        void onProfile(UserProfile profile);

        void onError(Exception error);
    }

    public static final class ProfileSaveException extends Exception {
        public ProfileSaveException(Throwable cause) {
            super(cause);
        }
    }

    private final FirebaseAuth auth;
    private final FirebaseFirestore firestore;

    public AuthRepository() {
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public void signIn(String email, String password, AuthCallback callback) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> callback.onSuccess())
                .addOnFailureListener(error -> callback.onError(asException(error)));
    }

    public void register(String fullName, String email, String password, String role,
                         String inviteCode, AuthCallback callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> {
                    FirebaseUser user = result.getUser();
                    if (user == null) {
                        callback.onError(new IllegalStateException("Missing Firebase user"));
                        return;
                    }

                    UserProfile profile = new UserProfile(
                            user.getUid(),
                            fullName,
                            email,
                            UserProfile.ROLE_OWNER.equals(role)
                                    ? UserProfile.ROLE_OWNER : UserProfile.ROLE_STAFF,
                            inviteCode == null ? "" : inviteCode,
                            System.currentTimeMillis()
                    );

                    UserProfileChangeRequest update = new UserProfileChangeRequest.Builder()
                            .setDisplayName(fullName)
                            .build();
                    user.updateProfile(update)
                            .addOnCompleteListener(task -> saveProfile(user, profile, callback));
                })
                .addOnFailureListener(error -> callback.onError(asException(error)));
    }

    private void saveProfile(FirebaseUser user, UserProfile profile, AuthCallback callback) {
        firestore.collection("users").document(user.getUid())
                .set(profile.toMap())
                .addOnSuccessListener(unused -> callback.onSuccess())
                .addOnFailureListener(error -> {
                    auth.signOut();
                    callback.onError(new ProfileSaveException(error));
                });
    }

    public void loadProfile(FirebaseUser user, ProfileCallback callback) {
        firestore.collection("users").document(user.getUid())
                .get()
                .addOnSuccessListener(document ->
                        callback.onProfile(UserProfile.fromDocument(document, user)))
                .addOnFailureListener(error -> callback.onError(asException(error)));
    }

    public void sendPasswordReset(String email, AuthCallback callback) {
        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(unused -> callback.onSuccess())
                .addOnFailureListener(error -> callback.onError(asException(error)));
    }

    public void signOut() {
        auth.signOut();
    }

    private Exception asException(Exception error) {
        return error == null ? new IllegalStateException("Firebase request failed") : error;
    }
}
