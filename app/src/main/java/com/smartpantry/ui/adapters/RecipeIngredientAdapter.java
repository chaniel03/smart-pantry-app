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
import com.smartpantry.data.db.entity.RecipeIngredient;

/**
 * Adapter for displaying recipe ingredients in RecyclerView
 * Used in Recipe Detail screen
 */
public class RecipeIngredientAdapter extends ListAdapter<RecipeIngredient, RecipeIngredientAdapter.IngredientViewHolder> {

    public RecipeIngredientAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<RecipeIngredient> DIFF_CALLBACK = new DiffUtil.ItemCallback<RecipeIngredient>() {
        @Override
        public boolean areItemsTheSame(@NonNull RecipeIngredient oldItem, @NonNull RecipeIngredient newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull RecipeIngredient oldItem, @NonNull RecipeIngredient newItem) {
            return oldItem.getIngredientName().equals(newItem.getIngredientName()) &&
                   oldItem.getQuantityRequired() == newItem.getQuantityRequired() &&
                   oldItem.getUnit().equals(newItem.getUnit());
        }
    };

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe_ingredient, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        RecipeIngredient ingredient = getItem(position);
        holder.bind(ingredient);
    }

    static class IngredientViewHolder extends RecyclerView.ViewHolder {
        private final TextView ingredientText;
        private final TextView availabilityIndicator;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientText = itemView.findViewById(R.id.ingredient_text);
            availabilityIndicator = itemView.findViewById(R.id.availability_indicator);
        }

        public void bind(RecipeIngredient ingredient) {
            // Format: "3 pieces Tomatoes" or "500 ml Water"
            String text = ingredient.getQuantityRequired() + " " + 
                         ingredient.getUnit() + " " + 
                         ingredient.getIngredientName();
            ingredientText.setText(text);

            // For now, always show checkmark (availability check can be added later)
            availabilityIndicator.setVisibility(View.VISIBLE);
        }
    }
}
