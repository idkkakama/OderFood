package com.example.qlnhda1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends BaseActivity {

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected int getTitleRes() {
        return R.string.screen_table_map;
    }

    @Override
    protected int getSelectedNavId() {
        return R.id.navItemTable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindTableCards();
        bindQuickActions();
    }

    private void bindTableCards() {
        int[] tableCards = {
                R.id.cardTable01,
                R.id.cardTable02,
                R.id.cardTable03,
                R.id.cardTable04,
                R.id.cardTable05,
                R.id.cardTable06,
                R.id.cardTable07,
                R.id.cardTableVip
        };

        for (int id : tableCards) {
            View card = findViewById(id);
            UiUtils.onTap(card, this::openTableDetail);
        }
    }

    private void bindQuickActions() {
        UiUtils.onTap(findViewById(R.id.btnQuickOpen),
                () -> UiUtils.toast(this, R.string.toast_quick_open));
        UiUtils.onTap(findViewById(R.id.btnSwitchTable),
                () -> UiUtils.toast(this, R.string.toast_switch_table));
        UiUtils.onTap(findViewById(R.id.btnMergeTable),
                () -> UiUtils.toast(this, R.string.toast_merge_table));
    }

    private void openTableDetail() {
        startActivity(new Intent(this, TableDetailActivity.class));
    }
}
