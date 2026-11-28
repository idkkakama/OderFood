package com.example.qlnhda1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TableDetailActivity extends BaseActivity {

    private View payCash;
    private View payQr;
    private View checkoutSheet;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_table_detail;
    }

    @Override
    protected int getTitleRes() {
        return R.string.screen_table_detail;
    }

    @Override
    protected int getSelectedNavId() {
        return R.id.navItemTable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payCash = findViewById(R.id.payCash);
        payQr = findViewById(R.id.payQr);
        checkoutSheet = findViewById(R.id.sheetCheckout);
        bindPaymentMethods();
        bindActions();
    }

    private void bindPaymentMethods() {
        if (payCash != null) {
            payCash.setOnClickListener(v -> selectPayment(false));
        }
        if (payQr != null) {
            payQr.setOnClickListener(v -> selectPayment(true));
        }
    }

    private void selectPayment(boolean qrSelected) {
        if (payCash != null) {
            payCash.setSelected(!qrSelected);
        }
        if (payQr != null) {
            payQr.setSelected(qrSelected);
        }
    }

    private void bindActions() {

        UiUtils.onTap(findViewById(R.id.btnCheckout), () -> {
            if (checkoutSheet != null) {
                checkoutSheet.setVisibility(View.VISIBLE);
            }
        });

        UiUtils.onTap(findViewById(R.id.btnCloseSheet), () -> {
            if (checkoutSheet != null) {
                checkoutSheet.setVisibility(View.GONE);
            }
        });

        if (checkoutSheet != null) {
            checkoutSheet.setVisibility(View.GONE);
        }
        });
    }
        }
