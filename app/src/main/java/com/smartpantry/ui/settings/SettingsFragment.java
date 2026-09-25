package com.smartpantry.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.smartpantry.R;
import com.smartpantry.data.db.entity.Settings;
import com.smartpantry.viewmodel.SettingsViewModel;

/**
 * Fragment for app settings
 * Features:
 * - Expiry notifications toggle
 * - Measurement preference (metric/imperial)
 */
public class SettingsFragment extends Fragment {

    private SettingsViewModel viewModel;
    private SwitchMaterial expiryNotificationsSwitch;
    private RadioGroup measurementRadioGroup;
    private RadioButton radioMetric;
    private RadioButton radioImperial;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        initViews(view);
        setupViewModel();
        setupListeners();

        return view;
    }

    private void initViews(View view) {
        expiryNotificationsSwitch = view.findViewById(R.id.expiry_notifications_switch);
        measurementRadioGroup = view.findViewById(R.id.measurement_radio_group);
        radioMetric = view.findViewById(R.id.radio_metric);
        radioImperial = view.findViewById(R.id.radio_imperial);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        // Observe settings and update UI
        viewModel.getSettings().observe(getViewLifecycleOwner(), this::updateUI);
    }

    private void updateUI(Settings settings) {
        if (settings != null) {
            expiryNotificationsSwitch.setChecked(settings.isExpiryNotificationsEnabled());
            
            if ("metric".equals(settings.getMeasurementPreference())) {
                radioMetric.setChecked(true);
            } else {
                radioImperial.setChecked(true);
            }
        }
    }

    private void setupListeners() {
        // Expiry notifications switch
        expiryNotificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewModel.toggleExpiryNotifications(isChecked);
        });

        // Measurement preference radio group
        measurementRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String preference;
            if (checkedId == R.id.radio_metric) {
                preference = "metric";
            } else {
                preference = "imperial";
            }
            viewModel.updateMeasurementPreference(preference);
        });
    }
}
