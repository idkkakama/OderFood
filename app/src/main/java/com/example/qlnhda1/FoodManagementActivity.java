package com.example.qlnhda1;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class FoodManagementActivity extends BaseActivity {

    private final FoodRepository repository = new FoodRepository();
    private final ArrayList<Food> foods = new ArrayList<>();
    private final NumberFormat money = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

    private LinearLayout list;
    private EditText etSearch;
    private ListenerRegistration registration;

    private String selectedCategory = "Tất cả";
    private TextView chipAll, chipMains, chipStarters, chipSoups, chipDrinks;

    private static final String[] CATEGORIES = {"Món chính", "Món khai vị", "Món canh & Rau", "Đồ uống", "Tráng miệng"};
    private static final String[] STATIONS = {"Bếp chính", "Bếp nướng", "Quầy pha chế", "Bếp lẩu", "Trạm tráng miệng"};

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_food_management;
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

        SessionManager sm = new SessionManager(this);
        UserProfile p = sm.getCachedProfile();
        if (p == null || !UserProfile.ROLE_OWNER.equals(p.getRole())) {
            Toast.makeText(this, "Chỉ chủ nhà hàng mới được quản lý thực đơn", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        View btnBack = findViewById(R.id.btnBackMgmt);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        list = findViewById(R.id.adminFoodList);
        etSearch = findViewById(R.id.etFoodMgmtSearch);

        findViewById(R.id.btnAddFood).setOnClickListener(v -> showEditor(null));

        setupCategoryChips();

        if (etSearch != null) {
            etSearch.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) { render(); }
                @Override public void afterTextChanged(Editable s) {}
            });
        }

        registration = repository.listenFoods((snap, error) -> {
            if (error != null) {
                toastError(error);
                return;
            }
            foods.clear();
            if (snap != null) {
                for (DocumentSnapshot d : snap.getDocuments()) {
                    foods.add(Food.from(d));
                }
            }
            render();
        });
    }

    private void setupCategoryChips() {
        chipAll = findViewById(R.id.chipMgmtAll);
        chipMains = findViewById(R.id.chipMgmtMains);
        chipStarters = findViewById(R.id.chipMgmtStarters);
        chipSoups = findViewById(R.id.chipMgmtSoups);
        chipDrinks = findViewById(R.id.chipMgmtDrinks);

        View.OnClickListener listener = v -> {
            if (v.getId() == R.id.chipMgmtAll) selectedCategory = "Tất cả";
            else if (v.getId() == R.id.chipMgmtMains) selectedCategory = "Món chính";
            else if (v.getId() == R.id.chipMgmtStarters) selectedCategory = "Món khai vị";
            else if (v.getId() == R.id.chipMgmtSoups) selectedCategory = "Món canh & Rau";
            else if (v.getId() == R.id.chipMgmtDrinks) selectedCategory = "Đồ uống";

            updateChipStyles();
            render();
        };

        if (chipAll != null) chipAll.setOnClickListener(listener);
        if (chipMains != null) chipMains.setOnClickListener(listener);
        if (chipStarters != null) chipStarters.setOnClickListener(listener);
        if (chipSoups != null) chipSoups.setOnClickListener(listener);
        if (chipDrinks != null) chipDrinks.setOnClickListener(listener);

        updateChipStyles();
    }

    private void updateChipStyles() {
        setChipStyle(chipAll, "Tất cả".equals(selectedCategory));
        setChipStyle(chipMains, "Món chính".equals(selectedCategory));
        setChipStyle(chipStarters, "Món khai vị".equals(selectedCategory));
        setChipStyle(chipSoups, "Món canh & Rau".equals(selectedCategory));
        setChipStyle(chipDrinks, "Đồ uống".equals(selectedCategory));
    }

    private void setChipStyle(TextView chip, boolean active) {
        if (chip == null) return;
        if (active) {
            chip.setBackgroundResource(R.drawable.bg_chip_active);
            chip.setTextColor(ContextCompat.getColor(this, R.color.on_primary_container));
        } else {
            chip.setBackgroundResource(R.drawable.bg_chip_inactive);
            chip.setTextColor(ContextCompat.getColor(this, R.color.on_surface));
        }
    }

    private void render() {
        list.removeAllViews();
        if (foods.isEmpty()) {
            TextView t = new TextView(this);
            t.setText("Chưa có món ăn nào. Nhấn nút '+ Thêm món ăn mới' để tạo thực đơn.");
            t.setTextColor(ContextCompat.getColor(this, R.color.on_surface_variant));
            t.setPadding(8, 24, 8, 24);
            list.addView(t);
            return;
        }

        String query = etSearch != null ? etSearch.getText().toString().trim().toLowerCase(new Locale("vi", "VN")) : "";
        int matchedCount = 0;

        for (Food f : foods) {
            if (f.name == null) continue;
            boolean matchesSearch = query.isEmpty()
                    || f.name.toLowerCase(new Locale("vi", "VN")).contains(query)
                    || (f.description != null && f.description.toLowerCase(new Locale("vi", "VN")).contains(query));
            boolean matchesCategory = "Tất cả".equals(selectedCategory)
                    || (f.category != null && f.category.equalsIgnoreCase(selectedCategory));

            if (!matchesSearch || !matchesCategory) continue;
            matchedCount++;

            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setPadding(16, 14, 16, 14);
            card.setBackgroundResource(R.drawable.bg_surface_container_rounded);
            LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-1, -2);
            cp.bottomMargin = 12;
            list.addView(card, cp);

            TextView name = new TextView(this);
            name.setText(f.name);
            name.setTextSize(17);
            name.setTextColor(ContextCompat.getColor(this, R.color.on_surface));
            name.setTypeface(null, android.graphics.Typeface.BOLD);
            card.addView(name);

            TextView detail = new TextView(this);
            String st = f.station == null ? "Bếp chính" : f.station;
            String cat = f.category == null ? "Món chính" : f.category;
            detail.setText(money.format(f.price) + " đ  •  " + cat + "  •  " + st + "  •  " + (f.available ? "Đang bán" : "Tạm ngừng"));
            detail.setTextColor(ContextCompat.getColor(this, R.color.on_surface_variant));
            detail.setPadding(0, 5, 0, 8);
            card.addView(detail);

            if (f.description != null && !f.description.trim().isEmpty()) {
                TextView desc = new TextView(this);
                desc.setText(f.description);
                desc.setTextSize(12);
                desc.setTextColor(ContextCompat.getColor(this, R.color.outline));
                desc.setPadding(0, 0, 0, 8);
                card.addView(desc);
            }

            LinearLayout actions = new LinearLayout(this);
            actions.setOrientation(LinearLayout.HORIZONTAL);
            card.addView(actions);

            Button edit = new Button(this);
            edit.setText("Sửa món");
            edit.setTextSize(13);
            LinearLayout.LayoutParams lpEdit = new LinearLayout.LayoutParams(0, -2, 1);
            lpEdit.rightMargin = 6;
            actions.addView(edit, lpEdit);
            edit.setOnClickListener(v -> showEditor(f));

            Button toggle = new Button(this);
            toggle.setText(f.available ? "Tạm ngừng" : "Mở bán");
            toggle.setTextSize(13);
            LinearLayout.LayoutParams lpToggle = new LinearLayout.LayoutParams(0, -2, 1);
            lpToggle.rightMargin = 6;
            actions.addView(toggle, lpToggle);
            toggle.setOnClickListener(v -> {
                f.available = !f.available;
                repository.save(f, e -> {
                    if (e != null) toastError(e);
                    else Toast.makeText(this, (f.available ? "Đã mở bán " : "Đã tạm ngừng ") + f.name, Toast.LENGTH_SHORT).show();
                });
            });

            Button del = new Button(this);
            del.setText("Xóa");
            del.setTextSize(13);
            actions.addView(del, new LinearLayout.LayoutParams(0, -2, 1));
            del.setOnClickListener(v -> new AlertDialog.Builder(this)
                    .setTitle("Xóa món ăn?")
                    .setMessage("Bạn có chắc muốn xóa '" + f.name + "' khỏi thực đơn không?")
                    .setNegativeButton("Hủy", null)
                    .setPositiveButton("Xóa món", (d, w) -> repository.delete(f.id, e -> {
                        if (e != null) toastError(e);
                        else Toast.makeText(this, "Đã xóa món " + f.name, Toast.LENGTH_SHORT).show();
                    }))
                    .show());
        }

        if (matchedCount == 0 && !foods.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("Không tìm thấy món ăn phù hợp với bộ lọc.");
            empty.setTextColor(ContextCompat.getColor(this, R.color.on_surface_variant));
            empty.setPadding(8, 24, 8, 24);
            list.addView(empty);
        }
    }

    private void showEditor(Food old) {
        LinearLayout box = new LinearLayout(this);
        box.setPadding(32, 16, 32, 8);
        box.setOrientation(LinearLayout.VERTICAL);

        EditText name = field("Tên món ăn (vd: Lẩu thái hải sản)", old == null ? "" : old.name);
        EditText price = field("Giá (VNĐ, vd: 150000)", old == null ? "" : String.valueOf((long) old.price));

        // Category Spinner
        TextView catLabel = new TextView(this);
        catLabel.setText("Danh mục thực đơn:");
        catLabel.setTextSize(12);
        catLabel.setTextColor(ContextCompat.getColor(this, R.color.outline));
        catLabel.setPadding(0, 10, 0, 4);

        Spinner spCategory = new Spinner(this);
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, CATEGORIES);
        spCategory.setAdapter(catAdapter);
        if (old != null && old.category != null) {
            for (int i = 0; i < CATEGORIES.length; i++) {
                if (CATEGORIES[i].equalsIgnoreCase(old.category)) {
                    spCategory.setSelection(i);
                    break;
                }
            }
        }

        // Station Spinner
        TextView stLabel = new TextView(this);
        stLabel.setText("Trạm chế biến (Gửi nhà bếp):");
        stLabel.setTextSize(12);
        stLabel.setTextColor(ContextCompat.getColor(this, R.color.outline));
        stLabel.setPadding(0, 10, 0, 4);

        Spinner spStation = new Spinner(this);
        ArrayAdapter<String> stAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, STATIONS);
        spStation.setAdapter(stAdapter);
        if (old != null && old.station != null) {
            for (int i = 0; i < STATIONS.length; i++) {
                if (STATIONS[i].equalsIgnoreCase(old.station)) {
                    spStation.setSelection(i);
                    break;
                }
            }
        }

        EditText image = field("Tên hình drawable (vd: food_beef, food_chicken)", old == null ? "food_beef" : old.image);
        EditText desc = field("Mô tả món ăn", old == null ? "" : old.description);

        CheckBox available = new CheckBox(this);
        available.setText("Món ăn đang kinh doanh");
        available.setTextColor(ContextCompat.getColor(this, R.color.on_surface));
        available.setChecked(old == null || old.available);

        box.addView(name);
        box.addView(price);
        box.addView(catLabel);
        box.addView(spCategory);
        box.addView(stLabel);
        box.addView(spStation);
        box.addView(image);
        box.addView(desc);
        box.addView(available);

        new AlertDialog.Builder(this)
                .setTitle(old == null ? "Thêm món ăn mới" : "Chỉnh sửa món ăn")
                .setView(box)
                .setNegativeButton("Hủy", null)
                .setPositiveButton("Lưu món", (d, w) -> {
                    String n = name.getText().toString().trim();
                    String ps = price.getText().toString().trim();
                    if (n.isEmpty() || ps.isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập đầy đủ tên món và giá tiền", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        double value = Double.parseDouble(ps.replace(",", "").replace(".", ""));
                        if (value < 0) throw new NumberFormatException();

                        String selCategory = spCategory.getSelectedItem() != null ? spCategory.getSelectedItem().toString() : "Món chính";
                        String selStation = spStation.getSelectedItem() != null ? spStation.getSelectedItem().toString() : "Bếp chính";
                        String imgStr = image.getText().toString().trim();
                        if (imgStr.isEmpty()) imgStr = "food_beef";

                        Food f = new Food(
                                old == null ? null : old.id,
                                n,
                                value,
                                selCategory,
                                imgStr,
                                desc.getText().toString().trim(),
                                selStation,
                                available.isChecked()
                        );

                        repository.save(f, e -> {
                            if (e != null) toastError(e);
                            else Toast.makeText(this, "Đã lưu món ăn vào thực đơn Firebase", Toast.LENGTH_SHORT).show();
                        });
                    } catch (NumberFormatException ex) {
                        Toast.makeText(this, "Giá món ăn không hợp lệ. Vui lòng nhập số dương.", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private EditText field(String hint, String value) {
        EditText e = new EditText(this);
        e.setSingleLine(true);
        e.setHint(hint);
        e.setText(value == null ? "" : value);
        e.setTextColor(ContextCompat.getColor(this, R.color.on_surface));
        e.setHintTextColor(ContextCompat.getColor(this, R.color.outline));
        return e;
    }

    private void toastError(Exception e) {
        Toast.makeText(this, "Lỗi Firebase: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        if (registration != null) {
            registration.remove();
        }
        super.onDestroy();
    }
}

