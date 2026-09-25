package com.smartpantry.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.smartpantry.data.db.AppDatabase;
import com.smartpantry.data.db.entity.Recipe;
import com.smartpantry.data.db.entity.RecipeIngredient;
import com.smartpantry.data.repository.PantryRepository;
import com.smartpantry.data.repository.RecipeMatchingEngine;
import com.smartpantry.data.repository.RecipeRepository;

import java.util.List;

/**
 * ViewModel for Recipe screens
 * Manages recipe data and matching logic
 */
public class RecipeViewModel extends AndroidViewModel {

    private final RecipeRepository recipeRepository;
    private final PantryRepository pantryRepository;
    private final RecipeMatchingEngine matchingEngine;
    
    private final LiveData<List<Recipe>> allRecipes;
    private final MutableLiveData<List<Recipe>> matchingRecipes;
    private final MutableLiveData<List<Recipe>> almostMatchingRecipes;

    public RecipeViewModel(@NonNull Application application) {
        super(application);
        
        recipeRepository = new RecipeRepository(application);
        pantryRepository = new PantryRepository(application);
        matchingEngine = new RecipeMatchingEngine(recipeRepository, pantryRepository);
        
        allRecipes = recipeRepository.getAllRecipes();
        matchingRecipes = new MutableLiveData<>();
        almostMatchingRecipes = new MutableLiveData<>();
    }

    /**
     * Get all recipes
     */
    public LiveData<List<Recipe>> getAllRecipes() {
        return allRecipes;
    }

    /**
     * Get recipe by ID
     */
    public LiveData<Recipe> getRecipeById(long id) {
        return recipeRepository.getRecipeById(id);
    }

    /**
     * Get ingredients for a recipe
     */
    public LiveData<List<RecipeIngredient>> getIngredientsForRecipe(long recipeId) {
        return recipeRepository.getIngredientsForRecipe(recipeId);
    }

    /**
     * Get matching recipes (STRICT MATCH ONLY)
     * Runs on background thread
     */
    public LiveData<List<Recipe>> getMatchingRecipes() {
        refreshMatchingRecipes();
        return matchingRecipes;
    }

    /**
     * Get almost matching recipes (missing exactly 1 ingredient)
     * Runs on background thread
     */
    public LiveData<List<Recipe>> getAlmostMatchingRecipes() {
        refreshAlmostMatchingRecipes();
        return almostMatchingRecipes;
    }

    /**
     * Refresh matching recipes from database
     */
    public void refreshMatchingRecipes() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Recipe> matches = matchingEngine.getMatchingRecipes();
            matchingRecipes.postValue(matches);
        });
    }

    /**
     * Refresh almost matching recipes from database
     */
    public void refreshAlmostMatchingRecipes() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Recipe> almostMatches = matchingEngine.getAlmostMatchingRecipes();
            almostMatchingRecipes.postValue(almostMatches);
        });
    }

    /**
     * Get missing ingredients for a recipe
     * Returns via callback on background thread
     */
    public void getMissingIngredients(Recipe recipe, MissingIngredientsCallback callback) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<String> missing = matchingEngine.getMissingIngredients(recipe);
            callback.onResult(missing);
        });
    }

    /**
     * Check if recipe can be made
     */
    public void canMakeRecipe(Recipe recipe, CanMakeCallback callback) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            boolean canMake = matchingEngine.canMakeRecipe(recipe);
            callback.onResult(canMake);
        });
    }

    /**
     * Callback for missing ingredients
     */
    public interface MissingIngredientsCallback {
        void onResult(List<String> missingIngredients);
    }

    /**
     * Callback for can make check
     */
    public interface CanMakeCallback {
        void onResult(boolean canMake);
    }
}
