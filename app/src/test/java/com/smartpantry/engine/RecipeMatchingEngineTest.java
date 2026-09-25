package com.smartpantry.engine;

import com.smartpantry.data.db.dao.PantryItemDao;
import com.smartpantry.data.db.dao.RecipeDao;
import com.smartpantry.data.db.dao.RecipeIngredientDao;
import com.smartpantry.data.db.entity.PantryItem;
import com.smartpantry.data.db.entity.Recipe;
import com.smartpantry.data.db.entity.RecipeIngredient;
import com.smartpantry.data.repository.PantryRepository;
import com.smartpantry.data.repository.RecipeMatchingEngine;
import com.smartpantry.data.repository.RecipeRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for RecipeMatchingEngine
 * Tests the STRICT MATCHING RULE:
 * - Recipe suggested ONLY if ALL ingredients exist with sufficient quantity
 * - NO PARTIAL MATCHES
 */
public class RecipeMatchingEngineTest {

    @Mock
    private RecipeDao recipeDao;
    
    @Mock
    private RecipeIngredientDao recipeIngredientDao;
    
    @Mock
    private PantryItemDao pantryItemDao;

    private RecipeRepository recipeRepository;
    private PantryRepository pantryRepository;
    private RecipeMatchingEngine matchingEngine;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        recipeRepository = new RecipeRepository(recipeDao, recipeIngredientDao);
        pantryRepository = new PantryRepository(pantryItemDao);
        matchingEngine = new RecipeMatchingEngine(recipeRepository, pantryRepository);
    }

    @Test
    public void getMatchingRecipes_exactMatch_returnsRecipe() {
        // Setup recipe: Tomato Soup (3 tomatoes, 1 onion, 500ml water)
        Recipe recipe = createRecipe(1, "Tomato Soup");
        List<Recipe> recipes = Arrays.asList(recipe);
        
        List<RecipeIngredient> ingredients = Arrays.asList(
            new RecipeIngredient(1, "tomatoes", 3, "pieces"),
            new RecipeIngredient(1, "onion", 1, "pieces"),
            new RecipeIngredient(1, "water", 500, "ml")
        );

        // Setup pantry: Exact match
        List<PantryItem> pantryItems = Arrays.asList(
            new PantryItem("Tomatoes", 3, "pieces", null),
            new PantryItem("Onion", 1, "pieces", null),
            new PantryItem("Water", 500, "ml", null)
        );

        when(recipeDao.getAllRecipesSync()).thenReturn(recipes);
        when(recipeIngredientDao.getIngredientsForRecipeSync(1)).thenReturn(ingredients);
        when(pantryItemDao.getAllItemsSync()).thenReturn(pantryItems);

        List<Recipe> matches = matchingEngine.getMatchingRecipes();

        assertEquals(1, matches.size());
        assertEquals("Tomato Soup", matches.get(0).getRecipeName());
    }

    @Test
    public void getMatchingRecipes_missingIngredient_returnsEmpty() {
        // Setup recipe: Tomato Soup (3 tomatoes, 1 onion, 500ml water)
        Recipe recipe = createRecipe(1, "Tomato Soup");
        List<Recipe> recipes = Arrays.asList(recipe);
        
        List<RecipeIngredient> ingredients = Arrays.asList(
            new RecipeIngredient(1, "tomatoes", 3, "pieces"),
            new RecipeIngredient(1, "onion", 1, "pieces"),
            new RecipeIngredient(1, "water", 500, "ml")
        );

        // Setup pantry: Missing water (STRICT RULE - should not match)
        List<PantryItem> pantryItems = Arrays.asList(
            new PantryItem("Tomatoes", 3, "pieces", null),
            new PantryItem("Onion", 1, "pieces", null)
        );

        when(recipeDao.getAllRecipesSync()).thenReturn(recipes);
        when(recipeIngredientDao.getIngredientsForRecipeSync(1)).thenReturn(ingredients);
        when(pantryItemDao.getAllItemsSync()).thenReturn(pantryItems);

        List<Recipe> matches = matchingEngine.getMatchingRecipes();

        assertEquals(0, matches.size());
    }

    @Test
    public void getMatchingRecipes_insufficientQuantity_returnsEmpty() {
        // Setup recipe: Tomato Soup (3 tomatoes, 1 onion, 500ml water)
        Recipe recipe = createRecipe(1, "Tomato Soup");
        List<Recipe> recipes = Arrays.asList(recipe);
        
        List<RecipeIngredient> ingredients = Arrays.asList(
            new RecipeIngredient(1, "tomatoes", 3, "pieces"),
            new RecipeIngredient(1, "onion", 1, "pieces"),
            new RecipeIngredient(1, "water", 500, "ml")
        );

        // Setup pantry: Only 2 tomatoes (insufficient)
        List<PantryItem> pantryItems = Arrays.asList(
            new PantryItem("Tomatoes", 2, "pieces", null),
            new PantryItem("Onion", 1, "pieces", null),
            new PantryItem("Water", 500, "ml", null)
        );

        when(recipeDao.getAllRecipesSync()).thenReturn(recipes);
        when(recipeIngredientDao.getIngredientsForRecipeSync(1)).thenReturn(ingredients);
        when(pantryItemDao.getAllItemsSync()).thenReturn(pantryItems);

        List<Recipe> matches = matchingEngine.getMatchingRecipes();

        assertEquals(0, matches.size());
    }

    @Test
    public void getMatchingRecipes_excessQuantity_returnsRecipe() {
        // Setup recipe: Tomato Soup (3 tomatoes, 1 onion, 500ml water)
        Recipe recipe = createRecipe(1, "Tomato Soup");
        List<Recipe> recipes = Arrays.asList(recipe);
        
        List<RecipeIngredient> ingredients = Arrays.asList(
            new RecipeIngredient(1, "tomatoes", 3, "pieces"),
            new RecipeIngredient(1, "onion", 1, "pieces"),
            new RecipeIngredient(1, "water", 500, "ml")
        );

        // Setup pantry: More than required (should match)
        List<PantryItem> pantryItems = Arrays.asList(
            new PantryItem("Tomatoes", 10, "pieces", null),
            new PantryItem("Onion", 5, "pieces", null),
            new PantryItem("Water", 1000, "ml", null)
        );

        when(recipeDao.getAllRecipesSync()).thenReturn(recipes);
        when(recipeIngredientDao.getIngredientsForRecipeSync(1)).thenReturn(ingredients);
        when(pantryItemDao.getAllItemsSync()).thenReturn(pantryItems);

        List<Recipe> matches = matchingEngine.getMatchingRecipes();

        assertEquals(1, matches.size());
    }

    @Test
    public void getMatchingRecipes_pluralMatching_returnsRecipe() {
        // Setup recipe: Uses "tomato" (singular)
        Recipe recipe = createRecipe(1, "Tomato Soup");
        List<Recipe> recipes = Arrays.asList(recipe);
        
        List<RecipeIngredient> ingredients = Arrays.asList(
            new RecipeIngredient(1, "tomato", 3, "pieces")
        );

        // Setup pantry: Has "tomatoes" (plural) - should normalize and match
        List<PantryItem> pantryItems = Arrays.asList(
            new PantryItem("Tomatoes", 3, "pieces", null)
        );

        when(recipeDao.getAllRecipesSync()).thenReturn(recipes);
        when(recipeIngredientDao.getIngredientsForRecipeSync(1)).thenReturn(ingredients);
        when(pantryItemDao.getAllItemsSync()).thenReturn(pantryItems);

        List<Recipe> matches = matchingEngine.getMatchingRecipes();

        assertEquals(1, matches.size());
    }

    @Test
    public void getMatchingRecipes_caseInsensitive_returnsRecipe() {
        // Setup recipe: "TOMATO" (uppercase)
        Recipe recipe = createRecipe(1, "Tomato Soup");
        List<Recipe> recipes = Arrays.asList(recipe);
        
        List<RecipeIngredient> ingredients = Arrays.asList(
            new RecipeIngredient(1, "TOMATO", 3, "pieces")
        );

        // Setup pantry: "tomato" (lowercase) - should match
        List<PantryItem> pantryItems = Arrays.asList(
            new PantryItem("tomato", 3, "pieces", null)
        );

        when(recipeDao.getAllRecipesSync()).thenReturn(recipes);
        when(recipeIngredientDao.getIngredientsForRecipeSync(1)).thenReturn(ingredients);
        when(pantryItemDao.getAllItemsSync()).thenReturn(pantryItems);

        List<Recipe> matches = matchingEngine.getMatchingRecipes();

        assertEquals(1, matches.size());
    }

    @Test
    public void getMatchingRecipes_emptyPantry_returnsEmpty() {
        Recipe recipe = createRecipe(1, "Tomato Soup");
        List<Recipe> recipes = Arrays.asList(recipe);
        
        List<RecipeIngredient> ingredients = Arrays.asList(
            new RecipeIngredient(1, "tomato", 3, "pieces")
        );

        List<PantryItem> pantryItems = new ArrayList<>();

        when(recipeDao.getAllRecipesSync()).thenReturn(recipes);
        when(recipeIngredientDao.getIngredientsForRecipeSync(1)).thenReturn(ingredients);
        when(pantryItemDao.getAllItemsSync()).thenReturn(pantryItems);

        List<Recipe> matches = matchingEngine.getMatchingRecipes();

        assertEquals(0, matches.size());
    }

    @Test
    public void getAlmostMatchingRecipes_missingOneIngredient_returnsRecipe() {
        // Setup recipe: Tomato Soup (3 tomatoes, 1 onion, 500ml water)
        Recipe recipe = createRecipe(1, "Tomato Soup");
        List<Recipe> recipes = Arrays.asList(recipe);
        
        List<RecipeIngredient> ingredients = Arrays.asList(
            new RecipeIngredient(1, "tomatoes", 3, "pieces"),
            new RecipeIngredient(1, "onion", 1, "pieces"),
            new RecipeIngredient(1, "water", 500, "ml")
        );

        // Setup pantry: Missing exactly 1 ingredient (water)
        List<PantryItem> pantryItems = Arrays.asList(
            new PantryItem("Tomatoes", 3, "pieces", null),
            new PantryItem("Onion", 1, "pieces", null)
        );

        when(recipeDao.getAllRecipesSync()).thenReturn(recipes);
        when(recipeIngredientDao.getIngredientsForRecipeSync(1)).thenReturn(ingredients);
        when(pantryItemDao.getAllItemsSync()).thenReturn(pantryItems);

        List<Recipe> almostMatches = matchingEngine.getAlmostMatchingRecipes();

        assertEquals(1, almostMatches.size());
        assertEquals("Tomato Soup", almostMatches.get(0).getRecipeName());
    }

    @Test
    public void getAlmostMatchingRecipes_missingTwoIngredients_returnsEmpty() {
        // Setup recipe: Tomato Soup (3 tomatoes, 1 onion, 500ml water)
        Recipe recipe = createRecipe(1, "Tomato Soup");
        List<Recipe> recipes = Arrays.asList(recipe);
        
        List<RecipeIngredient> ingredients = Arrays.asList(
            new RecipeIngredient(1, "tomatoes", 3, "pieces"),
            new RecipeIngredient(1, "onion", 1, "pieces"),
            new RecipeIngredient(1, "water", 500, "ml")
        );

        // Setup pantry: Missing 2 ingredients (onion and water)
        List<PantryItem> pantryItems = Arrays.asList(
            new PantryItem("Tomatoes", 3, "pieces", null)
        );

        when(recipeDao.getAllRecipesSync()).thenReturn(recipes);
        when(recipeIngredientDao.getIngredientsForRecipeSync(1)).thenReturn(ingredients);
        when(pantryItemDao.getAllItemsSync()).thenReturn(pantryItems);

        List<Recipe> almostMatches = matchingEngine.getAlmostMatchingRecipes();

        assertEquals(0, almostMatches.size());
    }

    private Recipe createRecipe(long id, String name) {
        Recipe recipe = new Recipe(name, "Description", "Steps");
        recipe.setId(id);
        return recipe;
    }
}
