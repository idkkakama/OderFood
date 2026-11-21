package com.example.qlnhda1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());

        TextView headerTitle = findViewById(R.id.tvHeaderTitle);
        if (headerTitle != null) {
            headerTitle.setText(getTitleRes());
        }

        setupBottomNav();
    }

    protected abstract int getLayoutRes();

    @StringRes
    protected abstract int getTitleRes();

    protected abstract int getSelectedNavId();

    private void setupBottomNav() {
        int selectedId = getSelectedNavId();

        View navTable = findViewById(R.id.navItemTable);
        View navKitchen = findViewById(R.id.navItemKitchen);
        View navMenu = findViewById(R.id.navItemMenu);
        View navManagement = findViewById(R.id.navItemManagement);

        setNavSelected(navTable, selectedId == R.id.navItemTable);
        setNavSelected(navKitchen, selectedId == R.id.navItemKitchen);
        setNavSelected(navMenu, selectedId == R.id.navItemMenu);
        setNavSelected(navManagement, selectedId == R.id.navItemManagement);

        if (selectedId != R.id.navItemTable) {
            UiUtils.onTap(navTable, () -> open(MainActivity.class));
        }
        if (selectedId != R.id.navItemKitchen) {
            UiUtils.onTap(navKitchen, () -> open(KitchenActivity.class));
        }
        if (selectedId != R.id.navItemMenu) {
            UiUtils.onTap(navMenu, () -> open(OrderMenuActivity.class));
        }
        if (selectedId != R.id.navItemManagement) {
            UiUtils.onTap(navManagement, () -> open(ManagementActivity.class));
        }
    }

    private void setNavSelected(View view, boolean selected) {
        if (view != null) {
            view.setSelected(selected);
        }
    }

    private void open(Class<? extends AppCompatActivity> target) {
        Intent intent = new Intent(this, target);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
