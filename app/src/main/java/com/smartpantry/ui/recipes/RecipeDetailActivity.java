package com.smartpantry.ui.recipes;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.smartpantry.R;
import com.smartpantry.ui.adapters.RecipeIngredientAdapter;
import com.smartpantry.viewmodel.RecipeViewModel;

/**
 * Activity for displaying recipe details
 * Shows:
 * - Recipe name
 * - Description
 * - Required ingredients
 * - Preparation steps
 */
public class RecipeDetailActivity extends AppCompatActivity {

    private RecipeViewModel viewModel;
    private MaterialToolbar toolbar;
    
    private TextView recipeName;
    private TextView recipeDescription;
    private RecyclerView ingredientsRecyclerView;
    private TextView preparationSteps;

    private RecipeIngredientAdapter ingredientAdapter;
    private long recipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        recipeId = getIntent().getLongExtra("recipe_id", -1);
        if (recipeId == -1) {
            finish();
            return;
        }

        initViews();
        setupToolbar();
        setupRecyclerView();
        setupViewModel();
        loadRecipeData();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        recipeName = findViewById(R.id.recipe_name);
        recipeDescription = findViewById(R.id.recipe_description);
        ingredientsRecyclerView = findViewById(R.id.ingredients_recycler_view);
        preparationSteps = findViewById(R.id.preparation_steps);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        ingredientAdapter = new RecipeIngredientAdapter();
        ingredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ingredientsRecyclerView.setAdapter(ingredientAdapter);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
    }

    private void loadRecipeData() {
        // Load recipe details
        viewModel.getRecipeById(recipeId).observe(this, recipe -> {
            if (recipe != null) {
                recipeName.setText(recipe.getRecipeName());
                recipeDescription.setText(recipe.getRecipeDescription());
                preparationSteps.setText(recipe.getPreparationSteps());
            }
        });

        // Load recipe ingredients
        viewModel.getIngredientsForRecipe(recipeId).observe(this, ingredients -> {
            if (ingredients != null) {
                ingredientAdapter.submitList(ingredients);
            }
        });
    }
}
