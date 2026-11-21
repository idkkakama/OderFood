package com.example.qlnhda1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private AuthRepository authRepository;
    private SessionManager sessionManager;

    private EditText etEmail;
    private EditText etPassword;
    private MaterialButton btnLogin;
    private MaterialCheckBox cbRemember;
    private ProgressBar progressLogin;
    private TextView btnForgotPassword;
    private TextView btnRegister;
    private View btnPinLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authRepository = new AuthRepository();
        sessionManager = new SessionManager(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        cbRemember = findViewById(R.id.cbRemember);
        progressLogin = findViewById(R.id.progressLogin);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnPinLogin = findViewById(R.id.btnPinLogin);

        String rememberedEmail = sessionManager.getRememberedEmail();
        if (!TextUtils.isEmpty(rememberedEmail)) {
            etEmail.setText(rememberedEmail);
            etEmail.setSelection(rememberedEmail.length());
        }

        configurePasswordToggle(etPassword, findViewById(R.id.btnTogglePassword));
        UiUtils.onTap(btnLogin, this::attemptLogin);
        UiUtils.onTap(btnForgotPassword, this::confirmPasswordReset);
        UiUtils.onTap(btnRegister, this::openRegister);
        UiUtils.onTap(btnPinLogin, this::showPinDialog);

        etPassword.setOnEditorActionListener((view, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                attemptLogin();
                return true;
            }
            return false;
        });

        FirebaseUser currentUser = authRepository.getCurrentUser();
        if (currentUser != null) {
            if (sessionManager.isSessionRemembered()) {
                restoreProfile(currentUser);
                openMain();
            } else {
                authRepository.signOut();
                sessionManager.clearSession();
            }
        }
    }

    private void configurePasswordToggle(EditText input, TextView toggle) {
        final boolean[] visible = {false};
        UiUtils.onTap(toggle, () -> {
            visible[0] = !visible[0];
            int start = input.getSelectionStart();
            int end = input.getSelectionEnd();
            if (visible[0]) {
                input.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                toggle.setText(R.string.sym_visibility_off);
            } else {
                input.setTransformationMethod(PasswordTransformationMethod.getInstance());
                toggle.setText(R.string.sym_visibility);
            }
            int length = input.getText().length();
            if (start >= 0 && end >= 0) {
                input.setSelection(Math.min(start, length), Math.min(end, length));
            }
        });
    }

    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        if (!validateCredentials(email, password)) {
            return;
        }

        UiUtils.hideKeyboard(this);
        setLoading(true);
        authRepository.signIn(email, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess() {
                if (isFinishing() || isDestroyed()) {
                    return;
                }
                setLoading(false);
                if (cbRemember.isChecked()) {
                    sessionManager.setRememberedEmail(email);
                } else {
                    sessionManager.clearRememberedEmail();
                }
                sessionManager.setSessionRemembered(cbRemember.isChecked());
                FirebaseUser user = authRepository.getCurrentUser();
                if (user != null) {
                    restoreProfile(user);
                }
                openMain();
            }

            @Override
            public void onError(Exception error) {
                if (isFinishing() || isDestroyed()) {
                    return;
                }
                setLoading(false);
                UiUtils.toast(LoginActivity.this, AuthErrorMapper.resolve(error));
            }
        });
    }

    private boolean validateCredentials(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            etEmail.setError(getString(R.string.auth_error_email_required));
            etEmail.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getString(R.string.auth_error_email_invalid));
            etEmail.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError(getString(R.string.auth_error_password_required));
            etPassword.requestFocus();
            return false;
        }
        return true;
    }

    private void restoreProfile(FirebaseUser user) {
        UserProfile cached = sessionManager.getCachedProfile();
        if (cached == null || !user.getUid().equals(cached.getUid())) {
            sessionManager.saveProfile(UserProfile.fromAuth(user));
        }
        authRepository.loadProfile(user, new AuthRepository.ProfileCallback() {
            @Override
            public void onProfile(UserProfile profile) {
                sessionManager.saveProfile(profile);
            }

            @Override
            public void onError(Exception error) {
                sessionManager.saveProfile(UserProfile.fromAuth(user));
            }
        });
    }

    private void setLoading(boolean loading) {
        btnLogin.setEnabled(!loading);
        btnLogin.setText(loading ? "" : getString(R.string.auth_login_action));
        btnLogin.setAlpha(loading ? 0.7f : 1f);
        progressLogin.setVisibility(loading ? View.VISIBLE : View.GONE);
        etEmail.setEnabled(!loading);
        etPassword.setEnabled(!loading);
        cbRemember.setEnabled(!loading);
        btnForgotPassword.setEnabled(!loading);
        btnRegister.setEnabled(!loading);
        btnPinLogin.setEnabled(!loading);
    }

    private void confirmPasswordReset() {
        String email = etEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            UiUtils.toast(this, R.string.auth_reset_missing_email);
            etEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getString(R.string.auth_error_email_invalid));
            etEmail.requestFocus();
            return;
        }

        String message = getString(R.string.auth_reset_message) + "\n\n" + email;
        new AlertDialog.Builder(this)
                .setTitle(R.string.auth_reset_title)
                .setMessage(message)
                .setNegativeButton(R.string.auth_cancel, null)
                .setPositiveButton(R.string.auth_send, (dialog, which) -> sendPasswordReset(email))
                .show();
    }

    private void sendPasswordReset(String email) {
        authRepository.sendPasswordReset(email, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess() {
                if (!isFinishing() && !isDestroyed()) {
                    UiUtils.toast(LoginActivity.this, R.string.auth_reset_sent);
                }
            }

            @Override
            public void onError(Exception error) {
                if (!isFinishing() && !isDestroyed()) {
                    UiUtils.toast(LoginActivity.this, AuthErrorMapper.resolve(error));
                }
            }
        });
    }

    private void openRegister() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void openMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void showPinDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_pin);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.BOTTOM);
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setDimAmount(0.7f);
        }

        StringBuilder pin = new StringBuilder();
        UiUtils.onTap(dialog.findViewById(R.id.btnClosePin), dialog::dismiss);

        int[] digitIds = {
                R.id.pinKey1, R.id.pinKey2, R.id.pinKey3,
                R.id.pinKey4, R.id.pinKey5, R.id.pinKey6,
                R.id.pinKey7, R.id.pinKey8, R.id.pinKey9,
                R.id.pinKey0
        };
        for (int id : digitIds) {
            TextView key = dialog.findViewById(id);
            UiUtils.onTap(key, () -> {
                if (pin.length() < 4) {
                    pin.append(key.getText());
                    updatePinDots(dialog, pin.length());
                    if (pin.length() == 4) {
                        dialog.dismiss();
                        UiUtils.toast(this, R.string.auth_pin_demo);
                    }
                }
            });
        }

        UiUtils.onTap(dialog.findViewById(R.id.pinKeyClear), () -> {
            pin.setLength(0);
            updatePinDots(dialog, 0);
        });

        UiUtils.onTap(dialog.findViewById(R.id.pinKeyBack), () -> {
            if (pin.length() > 0) {
                pin.setLength(pin.length() - 1);
                updatePinDots(dialog, pin.length());
            }
        });
    }

    private void updatePinDots(Dialog dialog, int filled) {
        int[] dotIds = {R.id.pinDot1, R.id.pinDot2, R.id.pinDot3, R.id.pinDot4};
        for (int i = 0; i < dotIds.length; i++) {
            View dot = dialog.findViewById(dotIds[i]);
            dot.setBackgroundResource(i < filled
                    ? R.drawable.bg_pin_dot_active
                    : R.drawable.bg_pin_dot_inactive);
        }
    }
}
