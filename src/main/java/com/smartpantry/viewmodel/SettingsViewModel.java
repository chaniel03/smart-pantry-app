package com.smartpantry.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.smartpantry.data.db.entity.Settings;
import com.smartpantry.data.repository.SettingsRepository;

/**
 * ViewModel for Settings screen
 * Manages app settings and preferences
 */
public class SettingsViewModel extends AndroidViewModel {

    private final SettingsRepository repository;
    private final LiveData<Settings> settings;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        repository = new SettingsRepository(application);
        settings = repository.getSettings();
    }

    /**
     * Get app settings (LiveData)
     */
    public LiveData<Settings> getSettings() {
        return settings;
    }

    /**
     * Update settings
     */
    public void updateSettings(Settings settings) {
        repository.update(settings);
    }

    /**
     * Toggle expiry notifications
     */
    public void toggleExpiryNotifications(boolean enabled) {
        Settings currentSettings = settings.getValue();
        if (currentSettings != null) {
            currentSettings.setExpiryNotificationsEnabled(enabled);
            repository.update(currentSettings);
        }
    }

    /**
     * Update measurement preference
     */
    public void updateMeasurementPreference(String preference) {
        Settings currentSettings = settings.getValue();
        if (currentSettings != null) {
            currentSettings.setMeasurementPreference(preference);
            repository.update(currentSettings);
        }
    }
}
