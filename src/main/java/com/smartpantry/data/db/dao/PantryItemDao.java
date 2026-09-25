package com.smartpantry.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.smartpantry.data.db.entity.PantryItem;

import java.util.List;

@Dao
public interface PantryItemDao {

    @Insert
    long insert(PantryItem item);

    @Update
    void update(PantryItem item);

    @Delete
    void delete(PantryItem item);

    @Query("SELECT * FROM pantry_items ORDER BY ingredientName ASC")
    LiveData<List<PantryItem>> getAllItems();

    @Query("SELECT * FROM pantry_items WHERE id = :id")
    LiveData<PantryItem> getItemById(long id);

    @Query("SELECT * FROM pantry_items WHERE ingredientName LIKE '%' || :searchQuery || '%' ORDER BY ingredientName ASC")
    LiveData<List<PantryItem>> searchItems(String searchQuery);

    @Query("DELETE FROM pantry_items")
    void deleteAll();

    @Query("SELECT * FROM pantry_items")
    List<PantryItem> getAllItemsSync();
}
