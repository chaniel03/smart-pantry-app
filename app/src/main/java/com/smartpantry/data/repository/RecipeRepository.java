package com.smartpantry.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.smartpantry.data.db.AppDatabase;
import com.smartpantry.data.db.dao.RecipeDao;
import com.smartpantry.data.db.dao.RecipeIngredientDao;
import com.smartpantry.data.db.entity.Recipe;
import com.smartpantry.data.db.entity.RecipeIngredient;

import java.util.List;

/**
 * Repository for Recipe data operations
 * Handles recipes and their ingredients
 */
public class RecipeRepository {

    private final RecipeDao recipeDao;
    private final RecipeIngredientDao recipeIngredientDao;

    public RecipeRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        this.recipeDao = database.recipeDao();
        this.recipeIngredientDao = database.recipeIngredientDao();
    }

    // For testing
    public RecipeRepository(RecipeDao recipeDao, RecipeIngredientDao recipeIngredientDao) {
        this.recipeDao = recipeDao;
        this.recipeIngredientDao = recipeIngredientDao;
    }

    /**
     * Get all recipes (LiveData)
     */
    public LiveData<List<Recipe>> getAllRecipes() {
        return recipeDao.getAllRecipes();
    }

    /**
     * Get recipe by ID (LiveData)
     */
    public LiveData<Recipe> getRecipeById(long id) {
        return recipeDao.getRecipeById(id);
    }

    /**
     * Get ingredients for a specific recipe (LiveData)
     */
    public LiveData<List<RecipeIngredient>> getIngredientsForRecipe(long recipeId) {
        return recipeIngredientDao.getIngredientsForRecipe(recipeId);
    }

    /**
     * Get all recipes synchronously (for matching engine)
     */
    public List<Recipe> getAllRecipesSync() {
        return recipeDao.getAllRecipesSync();
    }

    /**
     * Get ingredients for recipe synchronously (for matching engine)
     */
    public List<RecipeIngredient> getIngredientsForRecipeSync(long recipeId) {
        return recipeIngredientDao.getIngredientsForRecipeSync(recipeId);
    }

    /**
     * Insert new recipe with ingredients (async)
     */
    public void insertRecipeWithIngredients(Recipe recipe, List<RecipeIngredient> ingredients) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            long recipeId = recipeDao.insert(recipe);
            
            // Set recipe ID for all ingredients
            for (RecipeIngredient ingredient : ingredients) {
                ingredient.setRecipeId(recipeId);
            }
            
            recipeIngredientDao.insertAll(ingredients);
        });
    }
}
