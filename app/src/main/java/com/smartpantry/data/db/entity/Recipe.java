package com.smartpantry.data.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entity representing a recipe.
 * FR4: Display recipes that can be made with available ingredients
 */
@Entity(tableName = "recipes")
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String recipeName;
    private String recipeDescription;
    private String preparationSteps;

    public Recipe() {}

    public Recipe(String recipeName, String recipeDescription, String preparationSteps) {
        this.recipeName = recipeName;
        this.recipeDescription = recipeDescription;
        this.preparationSteps = preparationSteps;
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getRecipeName() { return recipeName; }
    public void setRecipeName(String recipeName) { this.recipeName = recipeName; }

    public String getRecipeDescription() { return recipeDescription; }
    public void setRecipeDescription(String recipeDescription) { this.recipeDescription = recipeDescription; }

    public String getPreparationSteps() { return preparationSteps; }
    public void setPreparationSteps(String preparationSteps) { this.preparationSteps = preparationSteps; }
}
