package com.smartpantry.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.smartpantry.data.db.entity.PantryItem;
import com.smartpantry.data.repository.PantryRepository;

import java.util.List;

/**
 * ViewModel for Pantry screen
 * Manages pantry item data and operations
 */
public class PantryViewModel extends AndroidViewModel {

    private final PantryRepository repository;
    private final LiveData<List<PantryItem>> allItems;
    private final MutableLiveData<String> searchQuery;
    private final LiveData<List<PantryItem>> filteredItems;

    public PantryViewModel(@NonNull Application application) {
        super(application);
        repository = new PantryRepository(application);
        allItems = repository.getAllItems();
        
        searchQuery = new MutableLiveData<>("");
        filteredItems = Transformations.switchMap(searchQuery, query -> {
            if (query == null || query.trim().isEmpty()) {
                return repository.getAllItems();
            } else {
                return repository.searchItems(query);
            }
        });
    }

    /**
     * Get all pantry items (LiveData)
     */
    public LiveData<List<PantryItem>> getAllItems() {
        return allItems;
    }

    /**
     * Get filtered/searched pantry items (LiveData)
     */
    public LiveData<List<PantryItem>> getFilteredItems() {
        return filteredItems;
    }

    /**
     * Set search query for filtering
     */
    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    /**
     * Get pantry item by ID
     */
    public LiveData<PantryItem> getItemById(long id) {
        return repository.getItemById(id);
    }

    /**
     * Search pantry items
     */
    public LiveData<List<PantryItem>> searchItems(String query) {
        return repository.searchItems(query);
    }

    /**
     * Insert new pantry item
     */
    public void insert(PantryItem item) {
        repository.insert(item);
    }

    /**
     * Update existing pantry item
     */
    public void update(PantryItem item) {
        repository.update(item);
    }

    /**
     * Delete pantry item
     */
    public void delete(PantryItem item) {
        repository.delete(item);
    }

    /**
     * Delete all items (for testing/reset)
     */
    public void deleteAll() {
        repository.deleteAll();
    }
}
