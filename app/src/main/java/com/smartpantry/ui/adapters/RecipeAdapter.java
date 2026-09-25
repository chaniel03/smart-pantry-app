package com.smartpantry.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.smartpantry.R;
import com.smartpantry.data.db.entity.Recipe;

/**
 * Adapter for displaying recipes in RecyclerView
 */
public class RecipeAdapter extends ListAdapter<Recipe, RecipeAdapter.RecipeViewHolder> {

    private OnItemClickListener itemClickListener;
    private boolean showMissingIngredients = false;

    public RecipeAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Recipe> DIFF_CALLBACK = new DiffUtil.ItemCallback<Recipe>() {
        @Override
        public boolean areItemsTheSame(@NonNull Recipe oldItem, @NonNull Recipe newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Recipe oldItem, @NonNull Recipe newItem) {
            return oldItem.getRecipeName().equals(newItem.getRecipeName()) &&
                   oldItem.getRecipeDescription().equals(newItem.getRecipeDescription());
        }
    };

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = getItem(position);
        holder.bind(recipe, showMissingIngredients);
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {
        private final TextView recipeName;
        private final TextView recipeDescription;
        private final TextView canMakeIndicator;
        private final TextView missingIngredients;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipe_name);
            recipeDescription = itemView.findViewById(R.id.recipe_description);
            canMakeIndicator = itemView.findViewById(R.id.can_make_indicator);
            missingIngredients = itemView.findViewById(R.id.missing_ingredients);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(getItem(position));
                }
            });
        }

        public void bind(Recipe recipe, boolean showMissing) {
            recipeName.setText(recipe.getRecipeName());
            recipeDescription.setText(recipe.getRecipeDescription());

            if (showMissing) {
                // For "Almost There" recipes
                canMakeIndicator.setVisibility(View.GONE);
                missingIngredients.setVisibility(View.VISIBLE);
                missingIngredients.setText("Missing: 1 ingredient");
            } else {
                // For "Can Make" recipes
                canMakeIndicator.setVisibility(View.VISIBLE);
                missingIngredients.setVisibility(View.GONE);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Recipe recipe);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setShowMissingIngredients(boolean show) {
        this.showMissingIngredients = show;
    }
}
