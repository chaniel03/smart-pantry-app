package com.smartpantry.ui.pantry;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.smartpantry.R;
import com.smartpantry.data.db.entity.PantryItem;
import com.smartpantry.util.ValidationUtils;
import com.smartpantry.viewmodel.PantryViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Activity for adding or editing pantry items
 * Features:
 * - Input validation
 * - Date picker for expiry date
 * - Save/Cancel actions
 */
public class AddEditIngredientActivity extends AppCompatActivity {

    public static final int RESULT_OK = -1;

    private PantryViewModel viewModel;
    private MaterialToolbar toolbar;
    
    private TextInputLayout nameLayout;
    private TextInputLayout quantityLayout;
    private TextInputLayout unitLayout;
    private TextInputLayout expiryLayout;
    
    private TextInputEditText nameEdit;
    private TextInputEditText quantityEdit;
    private TextInputEditText unitEdit;
    private TextInputEditText expiryEdit;
    
    private MaterialButton saveButton;
    private MaterialButton cancelButton;

    private long itemId = -1;
    private boolean isEditMode = false;
    private PantryItem currentItem;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        initViews();
        setupToolbar();
        setupViewModel();
        setupListeners();

        // Check if editing existing item
        if (getIntent().hasExtra("item_id")) {
            itemId = getIntent().getLongExtra("item_id", -1);
            isEditMode = true;
            toolbar.setTitle(R.string.edit_ingredient);
            loadItemData();
        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        
        nameLayout = findViewById(R.id.ingredient_name_layout);
        quantityLayout = findViewById(R.id.quantity_layout);
        unitLayout = findViewById(R.id.unit_layout);
        expiryLayout = findViewById(R.id.expiry_date_layout);
        
        nameEdit = findViewById(R.id.ingredient_name_edit);
        quantityEdit = findViewById(R.id.quantity_edit);
        unitEdit = findViewById(R.id.unit_edit);
        expiryEdit = findViewById(R.id.expiry_date_edit);
        
        saveButton = findViewById(R.id.save_button);
        cancelButton = findViewById(R.id.cancel_button);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(PantryViewModel.class);
    }

    private void loadItemData() {
        viewModel.getItemById(itemId).observe(this, item -> {
            if (item != null) {
                currentItem = item;
                nameEdit.setText(item.getIngredientName());
                quantityEdit.setText(String.valueOf(item.getQuantity()));
                unitEdit.setText(item.getUnit());
                
                if (item.getExpiryDate() != null && !item.getExpiryDate().isEmpty()) {
                    expiryEdit.setText(item.getExpiryDate());
                }
            }
        });
    }

    private void setupListeners() {
        saveButton.setOnClickListener(v -> saveIngredient());
        cancelButton.setOnClickListener(v -> finish());
        
        // Date picker for expiry date
        expiryEdit.setOnClickListener(v -> showDatePicker());
        expiryLayout.setEndIconOnClickListener(v -> showDatePicker());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            this,
            (view, year, month, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                
                String dateString = dateFormat.format(calendar.getTime());
                expiryEdit.setText(dateString);
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        );
        
        datePickerDialog.show();
    }

    private void saveIngredient() {
        // Clear previous errors
        nameLayout.setError(null);
        quantityLayout.setError(null);
        unitLayout.setError(null);
        expiryLayout.setError(null);

        // Get input values
        String name = nameEdit.getText() != null ? nameEdit.getText().toString().trim() : "";
        String quantityStr = quantityEdit.getText() != null ? quantityEdit.getText().toString().trim() : "";
        String unit = unitEdit.getText() != null ? unitEdit.getText().toString().trim() : "";
        String expiryDate = expiryEdit.getText() != null ? expiryEdit.getText().toString().trim() : "";

        // Validate all inputs
        ValidationUtils.ValidationResult result = ValidationUtils.validatePantryItem(
            name, quantityStr, unit, expiryDate
        );

        if (!result.isValid()) {
            // Show first error
            if (!ValidationUtils.validateIngredientName(name).isValid()) {
                nameLayout.setError(result.getErrorMessage());
                nameEdit.requestFocus();
            } else if (!ValidationUtils.validateQuantity(quantityStr).isValid()) {
                quantityLayout.setError(result.getErrorMessage());
                quantityEdit.requestFocus();
            } else if (!ValidationUtils.validateUnit(unit).isValid()) {
                unitLayout.setError(result.getErrorMessage());
                unitEdit.requestFocus();
            } else if (!ValidationUtils.validateExpiryDate(expiryDate).isValid()) {
                expiryLayout.setError(result.getErrorMessage());
                expiryEdit.requestFocus();
            }
            return;
        }

        // All validation passed
        double quantity = Double.parseDouble(quantityStr);

        if (isEditMode && currentItem != null) {
            // Update existing item
            currentItem.setIngredientName(name);
            currentItem.setQuantity(quantity);
            currentItem.setUnit(unit);
            currentItem.setExpiryDate(expiryDate.isEmpty() ? null : expiryDate);
            
            viewModel.update(currentItem);
            Toast.makeText(this, R.string.ingredient_updated, Toast.LENGTH_SHORT).show();
        } else {
            // Insert new item
            PantryItem newItem = new PantryItem(
                name,
                quantity,
                unit,
                expiryDate.isEmpty() ? null : expiryDate
            );
            
            viewModel.insert(newItem);
            Toast.makeText(this, R.string.ingredient_added, Toast.LENGTH_SHORT).show();
        }

        setResult(RESULT_OK);
        finish();
    }
}
