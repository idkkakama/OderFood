package com.example.qlnhda1;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private AuthRepository authRepository;
    private SessionManager sessionManager;

    private String selectedRole = UserProfile.ROLE_OWNER;

    private View btnRoleOwner;
    private View btnRoleStaff;
    private TextView iconRoleOwner;
    private TextView iconRoleStaff;
    private TextView tvRoleOwner;
    private TextView tvRoleStaff;
    private TextView tvRoleOwnerDesc;
    private TextView tvRoleStaffDesc;
    private View groupInviteCode;

    private EditText etFullName;
    private EditText etRegisterEmail;
    private EditText etInviteCode;
    private EditText etRegisterPassword;
    private EditText etConfirmPassword;
    private TextView btnToggleRegisterPassword;
    private TextView btnToggleConfirmPassword;
    private TextView tvStrength;
    private TextView iconMatch;
    private MaterialCheckBox cbTerms;
    private MaterialButton btnCreateAccount;
    private TextView btnGoLogin;
    private ProgressBar progressRegister;

    private View[] strengthBars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        authRepository = new AuthRepository();
        sessionManager = new SessionManager(this);

        btnRoleOwner = findViewById(R.id.btnRoleOwner);
        btnRoleStaff = findViewById(R.id.btnRoleStaff);
        iconRoleOwner = findViewById(R.id.iconRoleOwner);
        iconRoleStaff = findViewById(R.id.iconRoleStaff);
        tvRoleOwner = findViewById(R.id.tvRoleOwner);
        tvRoleStaff = findViewById(R.id.tvRoleStaff);
        tvRoleOwnerDesc = findViewById(R.id.tvRoleOwnerDesc);
        tvRoleStaffDesc = findViewById(R.id.tvRoleStaffDesc);
        groupInviteCode = findViewById(R.id.groupInviteCode);

        etFullName = findViewById(R.id.etFullName);
        etRegisterEmail = findViewById(R.id.etRegisterEmail);
        etInviteCode = findViewById(R.id.etInviteCode);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnToggleRegisterPassword = findViewById(R.id.btnToggleRegisterPassword);
        btnToggleConfirmPassword = findViewById(R.id.btnToggleConfirmPassword);
        tvStrength = findViewById(R.id.tvStrength);
        iconMatch = findViewById(R.id.iconMatch);
        cbTerms = findViewById(R.id.cbTerms);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        btnGoLogin = findViewById(R.id.btnGoLogin);
        progressRegister = findViewById(R.id.progressRegister);

        strengthBars = new View[]{
                findViewById(R.id.strengthBar1),
                findViewById(R.id.strengthBar2),
                findViewById(R.id.strengthBar3),
                findViewById(R.id.strengthBar4)
        };

        configurePasswordToggle(etRegisterPassword, btnToggleRegisterPassword);
        configurePasswordToggle(etConfirmPassword, btnToggleConfirmPassword);
        selectRole(UserProfile.ROLE_OWNER);
        updateStrength("");
        updateMatchStatus();

        UiUtils.onTap(findViewById(R.id.btnBack), this::finish);
        UiUtils.onTap(btnRoleOwner, () -> selectRole(UserProfile.ROLE_OWNER));
        UiUtils.onTap(btnRoleStaff, () -> selectRole(UserProfile.ROLE_STAFF));
        UiUtils.onTap(findViewById(R.id.tvTerms),
                () -> UiUtils.toast(this, R.string.auth_terms_toast));
        UiUtils.onTap(btnGoLogin, this::openLogin);
        UiUtils.onTap(btnCreateAccount, this::createAccount);

        etRegisterPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateStrength(s.toString());
                updateMatchStatus();
            }
        });

        etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateMatchStatus();
            }
        });
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

    private void selectRole(String role) {
        selectedRole = role;
        boolean owner = UserProfile.ROLE_OWNER.equals(role);

        btnRoleOwner.setSelected(owner);
        btnRoleStaff.setSelected(!owner);

        int primary = ContextCompat.getColor(this, R.color.primary);
        int secondary = ContextCompat.getColor(this, R.color.secondary);
        int onSurface = ContextCompat.getColor(this, R.color.on_surface);
        int onSurfaceVariant = ContextCompat.getColor(this, R.color.on_surface_variant);
        int outline = ContextCompat.getColor(this, R.color.outline);

        iconRoleOwner.setTextColor(owner ? primary : onSurfaceVariant);
        iconRoleStaff.setTextColor(owner ? onSurfaceVariant : secondary);
        tvRoleOwner.setTextColor(owner ? onSurface : onSurfaceVariant);
        tvRoleStaff.setTextColor(owner ? onSurfaceVariant : onSurface);
        tvRoleOwnerDesc.setTextColor(owner ? onSurfaceVariant : outline);
        tvRoleStaffDesc.setTextColor(owner ? outline : onSurfaceVariant);

        groupInviteCode.setVisibility(owner ? View.GONE : View.VISIBLE);
    }

    private void updateStrength(String password) {
        int length = password.length();
        int filled;
        int barColor;
        int textColor;
        int textRes;

        if (length == 0) {
            filled = 0;
            barColor = R.color.surface_container_highest;
            textColor = R.color.outline;
            textRes = R.string.auth_password_empty;
        } else if (length < 6) {
            filled = 1;
            barColor = R.color.error;
            textColor = R.color.error;
            textRes = R.string.auth_password_weak;
        } else if (length < 8) {
            filled = 2;
            barColor = R.color.primary_container;
            textColor = R.color.primary;
            textRes = R.string.auth_password_fair;
        } else if (length < 11) {
            filled = 3;
            barColor = R.color.secondary_container;
            textColor = R.color.secondary;
            textRes = R.string.auth_password_strong;
        } else {
            filled = 4;
            barColor = R.color.tertiary_container;
            textColor = R.color.tertiary;
            textRes = R.string.auth_password_very_strong;
        }

        int filledColor = ContextCompat.getColor(this, barColor);
        int emptyColor = ContextCompat.getColor(this, R.color.surface_container_highest);
        for (int i = 0; i < strengthBars.length; i++) {
            strengthBars[i].setBackgroundTintList(ColorStateList.valueOf(
                    i < filled ? filledColor : emptyColor));
        }
        tvStrength.setText(textRes);
        tvStrength.setTextColor(ContextCompat.getColor(this, textColor));
    }

    private void updateMatchStatus() {
        String password = etRegisterPassword.getText().toString();
        String confirm = etConfirmPassword.getText().toString();

        if (confirm.isEmpty()) {
            iconMatch.setText(R.string.sym_verified_user);
            iconMatch.setTextColor(ContextCompat.getColor(this, R.color.outline));
            return;
        }

        if (password.equals(confirm) && password.length() >= 6) {
            iconMatch.setText(R.string.sym_check_circle);
            iconMatch.setTextColor(ContextCompat.getColor(this, R.color.tertiary));
        } else {
            iconMatch.setText(R.string.sym_cancel);
            iconMatch.setTextColor(ContextCompat.getColor(this, R.color.error));
        }
    }

    private void createAccount() {
        String fullName = etFullName.getText().toString().trim();
        String email = etRegisterEmail.getText().toString().trim();
        String password = etRegisterPassword.getText().toString();
        String confirm = etConfirmPassword.getText().toString();
        String inviteCode = etInviteCode.getText().toString().trim()
                .toUpperCase(Locale.ROOT);

        clearErrors();
        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError(getString(R.string.auth_error_name_required));
            etFullName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            etRegisterEmail.setError(getString(R.string.auth_error_email_required));
            etRegisterEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etRegisterEmail.setError(getString(R.string.auth_error_email_invalid));
            etRegisterEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etRegisterPassword.setError(getString(R.string.auth_error_password_required));
            etRegisterPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            etRegisterPassword.setError(getString(R.string.auth_error_password_short));
            etRegisterPassword.requestFocus();
            return;
        }
        if (!password.equals(confirm)) {
            etConfirmPassword.setError(getString(R.string.auth_error_password_mismatch));
            etConfirmPassword.requestFocus();
            return;
        }
        if (!cbTerms.isChecked()) {
            UiUtils.toast(this, R.string.auth_error_terms_required);
            return;
        }

        UiUtils.hideKeyboard(this);
        setLoading(true);
        authRepository.register(fullName, email, password, selectedRole, inviteCode,
                new AuthRepository.AuthCallback() {
                    @Override
                    public void onSuccess() {
                        if (isFinishing() || isDestroyed()) {
                            return;
                        }
                        setLoading(false);
                        sessionManager.setSessionRemembered(true);
                        FirebaseUser user = authRepository.getCurrentUser();
                        if (user != null) {
                            sessionManager.saveProfile(new UserProfile(
                                    user.getUid(),
                                    fullName,
                                    email,
                                    selectedRole,
                                    inviteCode,
                                    System.currentTimeMillis()
                            ));
                        }
                        UiUtils.toast(RegisterActivity.this, R.string.auth_register_success);
                        openMain();
                    }

                    @Override
                    public void onError(Exception error) {
                        if (isFinishing() || isDestroyed()) {
                            return;
                        }
                        setLoading(false);
                        UiUtils.toast(RegisterActivity.this, AuthErrorMapper.resolve(error));
                    }
                });
    }

    private void clearErrors() {
        etFullName.setError(null);
        etRegisterEmail.setError(null);
        etRegisterPassword.setError(null);
        etConfirmPassword.setError(null);
    }

    private void setLoading(boolean loading) {
        btnCreateAccount.setEnabled(!loading);
        btnCreateAccount.setText(loading ? "" : getString(R.string.auth_create_account));
        btnCreateAccount.setAlpha(loading ? 0.7f : 1f);
        progressRegister.setVisibility(loading ? View.VISIBLE : View.GONE);
        etFullName.setEnabled(!loading);
        etRegisterEmail.setEnabled(!loading);
        etInviteCode.setEnabled(!loading);
        etRegisterPassword.setEnabled(!loading);
        etConfirmPassword.setEnabled(!loading);
        cbTerms.setEnabled(!loading);
        btnRoleOwner.setEnabled(!loading);
        btnRoleStaff.setEnabled(!loading);
        btnGoLogin.setEnabled(!loading);
        btnToggleRegisterPassword.setEnabled(!loading);
        btnToggleConfirmPassword.setEnabled(!loading);
    }

    private void openMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void openLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
