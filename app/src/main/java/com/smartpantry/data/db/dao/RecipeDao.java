package com.smartpantry.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.smartpantry.data.db.entity.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {

    @Insert
    long insert(Recipe recipe);

    @Query("SELECT * FROM recipes ORDER BY recipeName ASC")
    LiveData<List<Recipe>> getAllRecipes();

    @Query("SELECT * FROM recipes WHERE id = :id")
    LiveData<Recipe> getRecipeById(long id);

    @Query("SELECT * FROM recipes")
    List<Recipe> getAllRecipesSync();

    @Query("DELETE FROM recipes")
    void deleteAll();
}
