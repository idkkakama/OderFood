package com.example.qlnhda1;

import android.os.Bundle;

public class OrderMenuActivity extends BaseActivity {

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_order_menu;
    }

    @Override
    protected int getTitleRes() {
        return R.string.screen_order_menu;
    }

    @Override
    protected int getSelectedNavId() {
        return R.id.navItemMenu;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
