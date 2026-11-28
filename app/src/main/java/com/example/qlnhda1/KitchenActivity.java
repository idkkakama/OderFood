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
                }

    }
}
