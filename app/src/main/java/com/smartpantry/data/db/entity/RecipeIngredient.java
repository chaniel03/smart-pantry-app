package com.smartpantry.data.db.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entity representing an ingredient required for a recipe.
 * Links Recipe to its required ingredients with quantities.
 */
@Entity(
    tableName = "recipe_ingredients",
    foreignKeys = @ForeignKey(
        entity = Recipe.class,
        parentColumns = "id",
        childColumns = "recipeId",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {@Index("recipeId")}
)
public class RecipeIngredient {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long recipeId;
    private String ingredientName;
    private double quantityRequired;
    private String unit;

    public RecipeIngredient() {}

    public RecipeIngredient(long recipeId, String ingredientName, double quantityRequired, String unit) {
        this.recipeId = recipeId;
        this.ingredientName = ingredientName;
        this.quantityRequired = quantityRequired;
        this.unit = unit;
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getRecipeId() { return recipeId; }
    public void setRecipeId(long recipeId) { this.recipeId = recipeId; }

    public String getIngredientName() { return ingredientName; }
    public void setIngredientName(String ingredientName) { this.ingredientName = ingredientName; }

    public double getQuantityRequired() { return quantityRequired; }
    public void setQuantityRequired(double quantityRequired) { this.quantityRequired = quantityRequired; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}
