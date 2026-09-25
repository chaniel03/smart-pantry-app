package com.smartpantry.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.smartpantry.data.db.entity.Settings;

@Dao
public interface SettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Settings settings);

    @Update
    void update(Settings settings);

    @Query("SELECT * FROM settings WHERE id = 1 LIMIT 1")
    LiveData<Settings> getSettings();

    @Query("SELECT * FROM settings WHERE id = 1 LIMIT 1")
    Settings getSettingsSync();
}
