package com.smartpantry.data.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entity for user settings/preferences.
 * FR11: Settings screen with expiry notifications and measurement preferences
 */
@Entity(tableName = "settings")
public class Settings {

    @PrimaryKey
    private long id = 1; // Single row for app settings

    private boolean expiryNotificationsEnabled;
    private String measurementPreference; // "metric" or "imperial"

    public Settings() {
        this.expiryNotificationsEnabled = true;
        this.measurementPreference = "metric";
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public boolean isExpiryNotificationsEnabled() { return expiryNotificationsEnabled; }
    public void setExpiryNotificationsEnabled(boolean enabled) { this.expiryNotificationsEnabled = enabled; }

    public String getMeasurementPreference() { return measurementPreference; }
    public void setMeasurementPreference(String preference) { this.measurementPreference = preference; }
}
