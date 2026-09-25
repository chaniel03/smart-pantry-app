package com.smartpantry.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.smartpantry.data.db.entity.RecipeIngredient;

import java.util.List;

@Dao
public interface RecipeIngredientDao {

    @Insert
    void insert(RecipeIngredient ingredient);

    @Insert
    void insertAll(List<RecipeIngredient> ingredients);

    @Query("SELECT * FROM recipe_ingredients WHERE recipeId = :recipeId")
    LiveData<List<RecipeIngredient>> getIngredientsForRecipe(long recipeId);

    @Query("SELECT * FROM recipe_ingredients WHERE recipeId = :recipeId")
    List<RecipeIngredient> getIngredientsForRecipeSync(long recipeId);

    @Query("DELETE FROM recipe_ingredients")
    void deleteAll();
}
