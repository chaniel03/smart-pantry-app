package com.smartpantry.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.smartpantry.data.db.AppDatabase;
import com.smartpantry.data.db.dao.PantryItemDao;
import com.smartpantry.data.db.entity.PantryItem;

import java.util.List;

/**
 * Repository for PantryItem data operations
 * Abstracts data source and provides clean API for ViewModels
 */
public class PantryRepository {

    private final PantryItemDao pantryItemDao;

    public PantryRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        this.pantryItemDao = database.pantryItemDao();
    }

    // For testing
    public PantryRepository(PantryItemDao pantryItemDao) {
        this.pantryItemDao = pantryItemDao;
    }

    /**
     * Get all pantry items (LiveData for UI observation)
     */
    public LiveData<List<PantryItem>> getAllItems() {
        return pantryItemDao.getAllItems();
    }

    /**
     * Get pantry item by ID
     */
    public LiveData<PantryItem> getItemById(long id) {
        return pantryItemDao.getItemById(id);
    }

    /**
     * Search pantry items by name
     */
    public LiveData<List<PantryItem>> searchItems(String query) {
        return pantryItemDao.searchItems(query);
    }

    /**
     * Insert new pantry item (async)
     */
    public void insert(PantryItem item) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            pantryItemDao.insert(item);
        });
    }

    /**
     * Update existing pantry item (async)
     */
    public void update(PantryItem item) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            pantryItemDao.update(item);
        });
    }

    /**
     * Delete pantry item (async)
     */
    public void delete(PantryItem item) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            pantryItemDao.delete(item);
        });
    }

    /**
     * Get all items synchronously (for matching engine)
     */
    public List<PantryItem> getAllItemsSync() {
        return pantryItemDao.getAllItemsSync();
    }

    /**
     * Delete all pantry items (for testing/reset)
     */
    public void deleteAll() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            pantryItemDao.deleteAll();
        });
    }
}
