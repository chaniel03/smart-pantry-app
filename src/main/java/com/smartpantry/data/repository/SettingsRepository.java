package com.smartpantry.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.smartpantry.data.db.AppDatabase;
import com.smartpantry.data.db.dao.SettingsDao;
import com.smartpantry.data.db.entity.Settings;

/**
 * Repository for Settings data operations
 */
public class SettingsRepository {

    private final SettingsDao settingsDao;

    public SettingsRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        this.settingsDao = database.settingsDao();
    }

    // For testing
    public SettingsRepository(SettingsDao settingsDao) {
        this.settingsDao = settingsDao;
    }

    /**
     * Get app settings (LiveData)
     */
    public LiveData<Settings> getSettings() {
        return settingsDao.getSettings();
    }

    /**
     * Get settings synchronously
     */
    public Settings getSettingsSync() {
        return settingsDao.getSettingsSync();
    }

    /**
     * Update settings (async)
     */
    public void update(Settings settings) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            settingsDao.update(settings);
        });
    }

    /**
     * Insert or replace settings (async)
     */
    public void insert(Settings settings) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            settingsDao.insert(settings);
        });
    }
}
