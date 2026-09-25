package com.smartpantry.ui.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smartpantry.R;
import com.smartpantry.ui.adapters.RecipeAdapter;
import com.smartpantry.viewmodel.RecipeViewModel;

/**
 * Fragment for displaying suggested recipes
 * Shows two sections:
 * 1. Recipes You Can Make (STRICT MATCH)
 * 2. Almost There (missing exactly 1 ingredient)
 */
public class RecipesFragment extends Fragment {

    private RecipeViewModel viewModel;
    private RecipeAdapter matchingAdapter;
    private RecipeAdapter almostMatchingAdapter;
    
    private RecyclerView matchingRecyclerView;
    private RecyclerView almostMatchingRecyclerView;
    private TextView noRecipesText;
    private TextView noAlmostRecipesText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);

        initViews(view);
        setupRecyclerViews();
        setupViewModel();

        return view;
    }

    private void initViews(View view) {
        matchingRecyclerView = view.findViewById(R.id.matching_recipes_recycler_view);
        almostMatchingRecyclerView = view.findViewById(R.id.almost_matching_recipes_recycler_view);
        noRecipesText = view.findViewById(R.id.no_recipes_text);
        noAlmostRecipesText = view.findViewById(R.id.no_almost_recipes_text);
    }

    private void setupRecyclerViews() {
        // Matching recipes adapter (can make these)
        matchingAdapter = new RecipeAdapter();
        matchingAdapter.setShowMissingIngredients(false);
        matchingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        matchingRecyclerView.setAdapter(matchingAdapter);

        matchingAdapter.setOnItemClickListener(recipe -> {
            Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
            intent.putExtra("recipe_id", recipe.getId());
            startActivity(intent);
        });

        // Almost matching recipes adapter (missing 1 ingredient)
        almostMatchingAdapter = new RecipeAdapter();
        almostMatchingAdapter.setShowMissingIngredients(true);
        almostMatchingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        almostMatchingRecyclerView.setAdapter(almostMatchingAdapter);

        almostMatchingAdapter.setOnItemClickListener(recipe -> {
            Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
            intent.putExtra("recipe_id", recipe.getId());
            startActivity(intent);
        });
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        // Observe matching recipes (STRICT)
        viewModel.getMatchingRecipes().observe(getViewLifecycleOwner(), recipes -> {
            matchingAdapter.submitList(recipes);
            
            if (recipes == null || recipes.isEmpty()) {
                noRecipesText.setVisibility(View.VISIBLE);
                matchingRecyclerView.setVisibility(View.GONE);
            } else {
                noRecipesText.setVisibility(View.GONE);
                matchingRecyclerView.setVisibility(View.VISIBLE);
            }
        });

        // Observe almost matching recipes
        viewModel.getAlmostMatchingRecipes().observe(getViewLifecycleOwner(), recipes -> {
            almostMatchingAdapter.submitList(recipes);
            
            if (recipes == null || recipes.isEmpty()) {
                noAlmostRecipesText.setVisibility(View.VISIBLE);
                almostMatchingRecyclerView.setVisibility(View.GONE);
            } else {
                noAlmostRecipesText.setVisibility(View.GONE);
                almostMatchingRecyclerView.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * Refresh recipes (called when pantry changes)
     */
    public void refreshRecipes() {
        if (viewModel != null) {
            viewModel.refreshMatchingRecipes();
            viewModel.refreshAlmostMatchingRecipes();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh recipes when returning to this fragment
        refreshRecipes();
    }
}
