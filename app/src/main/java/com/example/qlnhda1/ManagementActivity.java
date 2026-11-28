package com.example.qlnhda1;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

public class ManagementActivity extends BaseActivity {

    private AuthRepository authRepository;
    private SessionManager sessionManager;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_management;
    }

    @Override
    protected int getTitleRes() {
        return R.string.screen_management;
    }

    @Override
    protected int getSelectedNavId() {
        return R.id.navItemManagement;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authRepository = new AuthRepository();
        sessionManager = new SessionManager(this);

        UiUtils.onTap(findViewById(R.id.btnOpenMap),
                () -> startActivity(new Intent(this, MainActivity.class)));
        UiUtils.onTap(findViewById(R.id.btnAddDishMgmt),
        UiUtils.onTap(findViewById(R.id.btnStockPrice),
        UiUtils.onTap(findViewById(R.id.btnViewTeam),
                () -> UiUtils.toast(this, R.string.toast_settings));
        UiUtils.onTap(findViewById(R.id.btnLogout), this::confirmLogout);
    }

    private void confirmLogout() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.logout_confirm_title)
                .setMessage(R.string.logout_confirm_message)
                .setNegativeButton(R.string.auth_cancel, null)
                .setPositiveButton(R.string.logout, (dialog, which) -> logout())
                .show();
    }

    private void logout() {
        authRepository.signOut();
        sessionManager.clearSession();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

        }
    }
}
