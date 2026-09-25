package com.smartpantry.data.repository;

import com.smartpantry.data.db.entity.PantryItem;
import com.smartpantry.data.db.entity.Recipe;
import com.smartpantry.data.db.entity.RecipeIngredient;
import com.smartpantry.util.IngredientNormalizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * STRICT RECIPE MATCHING ENGINE
 * 
 * CORE REQUIREMENT:
 * A recipe is suggested ONLY if ALL required ingredients exist in pantry
 * AND pantry quantity >= required quantity
 * 
 * NO PARTIAL MATCHES allowed in main suggested recipes list.
 * 
 * SMART MATCHING:
 * - Handles plural/singular variations (tomato vs tomatoes)
 * - Case insensitive matching
 * - Whitespace normalization
 */
public class RecipeMatchingEngine {

    /**
     * Result of recipe matching analysis
     */
    public static class MatchResult {
        private final Recipe recipe;
        private final boolean isFullMatch;
        private final int missingIngredientsCount;
        private final List<String> missingIngredients;

        public MatchResult(Recipe recipe, boolean isFullMatch, 
                          int missingIngredientsCount, List<String> missingIngredients) {
            this.recipe = recipe;
            this.isFullMatch = isFullMatch;
            this.missingIngredientsCount = missingIngredientsCount;
            this.missingIngredients = missingIngredients;
        }

        public Recipe getRecipe() {
            return recipe;
        }

        public boolean isFullMatch() {
            return isFullMatch;
        }

        public int getMissingIngredientsCount() {
            return missingIngredientsCount;
        }

        public List<String> getMissingIngredients() {
            return missingIngredients;
        }

        public boolean isAlmostMatch() {
            return !isFullMatch && missingIngredientsCount == 1;
        }
    }

    private final RecipeRepository recipeRepository;
    private final PantryRepository pantryRepository;

    public RecipeMatchingEngine(RecipeRepository recipeRepository, 
                                PantryRepository pantryRepository) {
        this.recipeRepository = recipeRepository;
        this.pantryRepository = pantryRepository;
    }

    /**
     * Get all recipes that can be made with current pantry (STRICT MATCH)
     * 
     * A recipe is included ONLY if:
     * - For EVERY recipe ingredient:
     *   - Pantry has matching ingredient (normalized name match)
     *   - Pantry quantity >= required quantity
     * 
     * @return List of recipes that match ALL requirements
     */
    public List<Recipe> getMatchingRecipes() {
        List<Recipe> allRecipes = recipeRepository.getAllRecipesSync();
        List<PantryItem> pantryItems = pantryRepository.getAllItemsSync();
        
        // Build pantry lookup map (normalized name -> PantryItem)
        Map<String, PantryItem> pantryMap = buildPantryMap(pantryItems);
        
        List<Recipe> matchingRecipes = new ArrayList<>();
        
        for (Recipe recipe : allRecipes) {
            if (isRecipeFullMatch(recipe, pantryMap)) {
                matchingRecipes.add(recipe);
            }
        }
        
        return matchingRecipes;
    }

    /**
     * Get recipes missing exactly 1 ingredient (ALMOST THERE feature)
     * 
     * @return List of recipes missing only 1 ingredient
     */
    public List<Recipe> getAlmostMatchingRecipes() {
        List<Recipe> allRecipes = recipeRepository.getAllRecipesSync();
        List<PantryItem> pantryItems = pantryRepository.getAllItemsSync();
        
        Map<String, PantryItem> pantryMap = buildPantryMap(pantryItems);
        
        List<Recipe> almostMatches = new ArrayList<>();
        
        for (Recipe recipe : allRecipes) {
            MatchResult result = analyzeRecipe(recipe, pantryMap);
            
            // Include only if missing exactly 1 ingredient
            if (result.isAlmostMatch()) {
                almostMatches.add(recipe);
            }
        }
        
        return almostMatches;
    }

    /**
     * Analyze a recipe and return detailed match result
     * 
     * @param recipe Recipe to analyze
     * @return MatchResult with details
     */
    public MatchResult analyzeRecipe(Recipe recipe) {
        List<PantryItem> pantryItems = pantryRepository.getAllItemsSync();
        Map<String, PantryItem> pantryMap = buildPantryMap(pantryItems);
        return analyzeRecipe(recipe, pantryMap);
    }

    /**
     * Analyze recipe against pantry map
     */
    private MatchResult analyzeRecipe(Recipe recipe, Map<String, PantryItem> pantryMap) {
        List<RecipeIngredient> requiredIngredients = 
            recipeRepository.getIngredientsForRecipeSync(recipe.getId());
        
        List<String> missingIngredients = new ArrayList<>();
        
        for (RecipeIngredient required : requiredIngredients) {
            String normalizedName = IngredientNormalizer.normalize(required.getIngredientName());
            
            if (!pantryMap.containsKey(normalizedName)) {
                // Ingredient not in pantry at all
                missingIngredients.add(required.getIngredientName());
            } else {
                PantryItem pantryItem = pantryMap.get(normalizedName);
                
                // Check quantity (units must match or this is simplified version)
                if (pantryItem.getQuantity() < required.getQuantityRequired()) {
                    // Insufficient quantity
                    missingIngredients.add(required.getIngredientName() + 
                        " (need " + required.getQuantityRequired() + 
                        ", have " + pantryItem.getQuantity() + ")");
                }
            }
        }
        
        boolean isFullMatch = missingIngredients.isEmpty();
        
        return new MatchResult(recipe, isFullMatch, missingIngredients.size(), missingIngredients);
    }

    /**
     * Check if recipe fully matches pantry (STRICT)
     * 
     * @param recipe Recipe to check
     * @param pantryMap Pantry lookup map
     * @return true if ALL ingredients match with sufficient quantity
     */
    private boolean isRecipeFullMatch(Recipe recipe, Map<String, PantryItem> pantryMap) {
        List<RecipeIngredient> requiredIngredients = 
            recipeRepository.getIngredientsForRecipeSync(recipe.getId());
        
        // Check every required ingredient
        for (RecipeIngredient required : requiredIngredients) {
            String normalizedName = IngredientNormalizer.normalize(required.getIngredientName());
            
            // Check if ingredient exists in pantry
            if (!pantryMap.containsKey(normalizedName)) {
                return false; // Missing ingredient
            }
            
            PantryItem pantryItem = pantryMap.get(normalizedName);
            
            // Check if quantity is sufficient
            if (pantryItem.getQuantity() < required.getQuantityRequired()) {
                return false; // Insufficient quantity
            }
        }
        
        // All ingredients present with sufficient quantities
        return true;
    }

    /**
     * Build lookup map from pantry items (normalized name -> item)
     * 
     * @param pantryItems List of pantry items
     * @return Map for O(1) lookup
     */
    private Map<String, PantryItem> buildPantryMap(List<PantryItem> pantryItems) {
        Map<String, PantryItem> map = new HashMap<>();
        
        for (PantryItem item : pantryItems) {
            String normalizedName = IngredientNormalizer.normalize(item.getIngredientName());
            
            // If duplicate normalized names exist, keep the one with higher quantity
            if (map.containsKey(normalizedName)) {
                PantryItem existing = map.get(normalizedName);
                if (item.getQuantity() > existing.getQuantity()) {
                    map.put(normalizedName, item);
                }
            } else {
                map.put(normalizedName, item);
            }
        }
        
        return map;
    }

    /**
     * Get missing ingredients for a specific recipe
     * 
     * @param recipe Recipe to check
     * @return List of missing ingredient names
     */
    public List<String> getMissingIngredients(Recipe recipe) {
        MatchResult result = analyzeRecipe(recipe);
        return result.getMissingIngredients();
    }

    /**
     * Check if specific recipe can be made
     * 
     * @param recipe Recipe to check
     * @return true if all ingredients available with sufficient quantities
     */
    public boolean canMakeRecipe(Recipe recipe) {
        MatchResult result = analyzeRecipe(recipe);
        return result.isFullMatch();
    }
}
