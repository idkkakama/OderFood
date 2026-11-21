package com.example.qlnhda1;

import android.os.Bundle;

public class KitchenActivity extends BaseActivity {

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_kitchen;
    }

    @Override
    protected int getTitleRes() {
        return R.string.screen_kitchen;
    }

    @Override
    protected int getSelectedNavId() {
        return R.id.navItemKitchen;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindTickets();
    }

    private void bindTickets() {
        UiUtils.onTap(findViewById(R.id.btnUrge04),
                () -> UiUtils.toast(this, R.string.toast_sent_kitchen));
        UiUtils.onTap(findViewById(R.id.btnDone04),
                () -> UiUtils.toast(this, R.string.toast_confirm_done));
        UiUtils.onTap(findViewById(R.id.btnUrge02),
                () -> UiUtils.toast(this, R.string.toast_sent_kitchen));
        UiUtils.onTap(findViewById(R.id.btnCook02),
                () -> UiUtils.toast(this, R.string.toast_sent_kitchen));
        UiUtils.onTap(findViewById(R.id.btnUrge07),
                () -> UiUtils.toast(this, R.string.toast_sent_kitchen));
        UiUtils.onTap(findViewById(R.id.btnCook07),
                () -> UiUtils.toast(this, R.string.toast_sent_kitchen));
    }
}
