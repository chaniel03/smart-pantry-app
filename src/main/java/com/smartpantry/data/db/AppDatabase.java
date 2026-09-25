package com.smartpantry.data.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.smartpantry.data.db.dao.PantryItemDao;
import com.smartpantry.data.db.dao.RecipeDao;
import com.smartpantry.data.db.dao.RecipeIngredientDao;
import com.smartpantry.data.db.dao.SettingsDao;
import com.smartpantry.data.db.entity.PantryItem;
import com.smartpantry.data.db.entity.Recipe;
import com.smartpantry.data.db.entity.RecipeIngredient;
import com.smartpantry.data.db.entity.Settings;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Room Database for Smart Pantry Manager
 * Singleton pattern ensures only one instance exists
 */
@Database(
    entities = {
        PantryItem.class,
        Recipe.class,
        RecipeIngredient.class,
        Settings.class
    },
    version = 1,
    exportSchema = true
)
public abstract class AppDatabase extends RoomDatabase {

    // DAOs
    public abstract PantryItemDao pantryItemDao();
    public abstract RecipeDao recipeDao();
    public abstract RecipeIngredientDao recipeIngredientDao();
    public abstract SettingsDao settingsDao();

    // Singleton instance
    private static volatile AppDatabase INSTANCE;
    
    // Executor for database operations
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = 
        Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Get database instance (Singleton)
     */
    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        "smart_pantry_database"
                    )
                    .addCallback(roomCallback)
                    .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Callback to seed database on first creation
     */
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            
            // Seed database with initial data
            databaseWriteExecutor.execute(() -> {
                if (INSTANCE != null) {
                    seedDatabase(INSTANCE);
                }
            });
        }
    };

    /**
     * Seed database with 20 recipes and default settings
     */
    private static void seedDatabase(AppDatabase db) {
        RecipeDao recipeDao = db.recipeDao();
        RecipeIngredientDao ingredientDao = db.recipeIngredientDao();
        SettingsDao settingsDao = db.settingsDao();

        // Insert default settings
        Settings defaultSettings = new Settings();
        settingsDao.insert(defaultSettings);

        // Recipe 1: Omelette
        long recipeId = recipeDao.insert(new Recipe(
            "Omelette",
            "A classic fluffy omelette perfect for breakfast",
            "1. Beat eggs in a bowl\n2. Heat butter in pan\n3. Pour eggs into pan\n4. Cook until set\n5. Fold and serve"
        ));
        ingredientDao.insert(new RecipeIngredient(recipeId, "eggs", 3, "pieces"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "butter", 1, "tablespoon"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "salt", 0.5, "teaspoon"));

        // Recipe 2: Pancakes
        recipeId = recipeDao.insert(new Recipe(
            "Pancakes",
            "Fluffy breakfast pancakes",
            "1. Mix flour, sugar, baking powder, and salt\n2. Add milk, egg, and melted butter\n3. Heat griddle\n4. Pour batter and cook until bubbles form\n5. Flip and cook until golden"
        ));
        ingredientDao.insert(new RecipeIngredient(recipeId, "flour", 2, "cups"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "milk", 1.5, "cups"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "eggs", 1, "pieces"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "sugar", 2, "tablespoons"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "butter", 3, "tablespoons"));

        // Recipe 3: Tomato Soup
        recipeId = recipeDao.insert(new Recipe(
            "Tomato Soup",
            "Classic creamy tomato soup",
            "1. Sauté onions in butter\n2. Add tomatoes and water\n3. Simmer for 20 minutes\n4. Blend until smooth\n5. Add cream and season"
        ));
        ingredientDao.insert(new RecipeIngredient(recipeId, "tomatoes", 6, "pieces"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "onion", 1, "pieces"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "water", 500, "ml"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "butter", 2, "tablespoons"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "cream", 0.5, "cups"));

        // Recipe 4: Grilled Cheese Sandwich
        recipeId = recipeDao.insert(new Recipe(
            "Grilled Cheese Sandwich",
            "Crispy golden grilled cheese",
            "1. Butter bread slices\n2. Place cheese between bread\n3. Grill in pan until golden\n4. Flip and cook other side\n5. Serve hot"
        ));
        ingredientDao.insert(new RecipeIngredient(recipeId, "bread", 2, "slices"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "cheese", 2, "slices"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "butter", 1, "tablespoon"));

        // Recipe 5: Fried Rice
        recipeId = recipeDao.insert(new Recipe(
            "Fried Rice",
            "Quick and easy fried rice",
            "1. Heat oil in wok\n2. Scramble eggs and set aside\n3. Fry vegetables\n4. Add rice and soy sauce\n5. Mix in eggs and serve"
        ));
        ingredientDao.insert(new RecipeIngredient(recipeId, "rice", 3, "cups"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "eggs", 2, "pieces"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "soy sauce", 3, "tablespoons"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "oil", 2, "tablespoons"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "peas", 1, "cups"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "carrots", 0.5, "cups"));

        // Recipe 6: Vegetable Stir Fry
        recipeId = recipeDao.insert(new Recipe(
            "Vegetable Stir Fry",
            "Healthy mixed vegetable stir fry",
            "1. Heat oil in wok\n2. Add garlic and ginger\n3. Add vegetables\n4. Stir fry for 5-7 minutes\n5. Season with soy sauce"
        ));
        ingredientDao.insert(new RecipeIngredient(recipeId, "broccoli", 1, "cups"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "bell pepper", 1, "pieces"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "carrots", 1, "pieces"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "soy sauce", 2, "tablespoons"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "oil", 2, "tablespoons"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "garlic", 3, "cloves"));

        // Recipe 7: Pasta Salad
        recipeId = recipeDao.insert(new Recipe(
            "Pasta Salad",
            "Fresh and colorful pasta salad",
            "1. Cook pasta and drain\n2. Chop vegetables\n3. Mix pasta with vegetables\n4. Add dressing\n5. Chill and serve"
        ));
        ingredientDao.insert(new RecipeIngredient(recipeId, "pasta", 300, "grams"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "tomatoes", 2, "pieces"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "cucumber", 1, "pieces"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "olive oil", 3, "tablespoons"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "cheese", 100, "grams"));

        // Recipe 8: French Toast
        recipeId = recipeDao.insert(new Recipe(
            "French Toast",
            "Sweet and delicious French toast",
            "1. Beat eggs with milk and cinnamon\n2. Dip bread slices in mixture\n3. Fry in buttered pan until golden\n4. Flip and cook other side\n5. Serve with syrup"
        ));
        ingredientDao.insert(new RecipeIngredient(recipeId, "bread", 4, "slices"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "eggs", 2, "pieces"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "milk", 0.5, "cups"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "butter", 2, "tablespoons"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "cinnamon", 1, "teaspoon"));

        // Recipe 9: Chicken Wrap
        recipeId = recipeDao.insert(new Recipe(
            "Chicken Wrap",
            "Healthy chicken and vegetable wrap",
            "1. Cook chicken pieces\n2. Warm tortilla\n3. Add lettuce, tomato, and chicken\n4. Drizzle with sauce\n5. Wrap and serve"
        ));
        ingredientDao.insert(new RecipeIngredient(recipeId, "chicken", 200, "grams"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "tortilla", 1, "pieces"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "lettuce", 1, "cups"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "tomatoes", 1, "pieces"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "mayonnaise", 2, "tablespoons"));

        // Recipe 10: Tuna Salad
        recipeId = recipeDao.insert(new Recipe(
            "Tuna Salad",
            "Quick and protein-rich tuna salad",
            "1. Drain tuna\n2. Mix with mayonnaise\n3. Add chopped vegetables\n4. Season with salt and pepper\n5. Serve chilled"
        ));
        ingredientDao.insert(new RecipeIngredient(recipeId, "tuna", 200, "grams"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "mayonnaise", 3, "tablespoons"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "lettuce", 2, "cups"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "tomatoes", 1, "pieces"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "cucumber", 0.5, "pieces"));

        // Recipe 11: Scrambled Eggs
        recipeId = recipeDao.insert(new Recipe(
            "Scrambled Eggs",
            "Simple and creamy scrambled eggs",
            "1. Beat eggs with milk\n2. Melt butter in pan\n3. Pour eggs and stir gently\n4. Cook until just set\n5. Season and serve"
        ));
        ingredientDao.insert(new RecipeIngredient(recipeId, "eggs", 3, "pieces"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "milk", 2, "tablespoons"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "butter", 1, "tablespoon"));

        // Recipe 12: Caesar Salad
        recipeId = recipeDao.insert(new Recipe(
            "Caesar Salad",
            "Classic Caesar salad with croutons",
            "1. Chop romaine lettuce\n2. Make dressing with mayonnaise and lemon\n3. Toss lettuce with dressing\n4. Add croutons and cheese\n5. Serve immediately"
        ));
        ingredientDao.insert(new RecipeIngredient(recipeId, "lettuce", 4, "cups"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "mayonnaise", 3, "tablespoons"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "cheese", 50, "grams"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "bread", 2, "slices"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "lemon", 1, "pieces"));

        // Recipe 13: Vegetable Soup
        recipeId = recipeDao.insert(new Recipe(
            "Vegetable Soup",
            "Hearty mixed vegetable soup",
            "1. Sauté onion and garlic\n2. Add chopped vegetables\n3. Pour in water and bring to boil\n4. Simmer for 30 minutes\n5. Season and serve hot"
        ));
        ingredientDao.insert(new RecipeIngredient(recipeId, "carrots", 2, "pieces"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "potatoes", 2, "pieces"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "onion", 1, "pieces"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "tomatoes", 2, "pieces"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "water", 1000, "ml"));

        // Recipe 14: Spaghetti Carbonara
        recipeId = recipeDao.insert(new Recipe(
            "Spaghetti Carbonara",
            "Creamy Italian pasta dish",
            "1. Cook spaghetti\n2. Fry bacon until crispy\n3. Beat eggs with cheese\n4. Toss hot pasta with egg mixture\n5. Add bacon and serve"
        ));
        ingredientDao.insert(new RecipeIngredient(recipeId, "spaghetti", 400, "grams"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "bacon", 150, "grams"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "eggs", 3, "pieces"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "cheese", 100, "grams"));

        // Recipe 15: Garlic Bread
        recipeId = recipeDao.insert(new Recipe(
            "Garlic Bread",
            "Crispy garlic butter bread",
            "1. Mix butter with crushed garlic\n2. Slice bread\n3. Spread garlic butter on bread\n4. Bake until golden\n5. Serve hot"
        ));
        ingredientDao.insert(new RecipeIngredient(recipeId, "bread", 1, "loaf"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "butter", 100, "grams"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "garlic", 6, "cloves"));

        // Recipe 16: Mashed Potatoes
        recipeId = recipeDao.insert(new Recipe(
            "Mashed Potatoes",
            "Smooth and creamy mashed potatoes",
            "1. Peel and boil potatoes\n2. Drain and mash\n3. Add butter and milk\n4. Mix until smooth\n5. Season and serve"
        ));
        ingredientDao.insert(new RecipeIngredient(recipeId, "potatoes", 4, "pieces"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "butter", 50, "grams"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "milk", 0.5, "cups"));

        // Recipe 17: Egg Fried Noodles
        recipeId = recipeDao.insert(new Recipe(
            "Egg Fried Noodles",
            "Quick Asian-style fried noodles",
            "1. Cook noodles and drain\n2. Scramble eggs\n3. Fry vegetables in oil\n4. Add noodles and soy sauce\n5. Mix in eggs and serve"
        ));
        ingredientDao.insert(new RecipeIngredient(recipeId, "noodles", 300, "grams"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "eggs", 2, "pieces"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "soy sauce", 3, "tablespoons"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "oil", 2, "tablespoons"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "vegetables", 1, "cups"));

        // Recipe 18: Cheese Omelette
        recipeId = recipeDao.insert(new Recipe(
            "Cheese Omelette",
            "Fluffy omelette with melted cheese",
            "1. Beat eggs with salt\n2. Heat butter in pan\n3. Pour eggs and cook\n4. Add cheese when half cooked\n5. Fold and serve"
        ));
        ingredientDao.insert(new RecipeIngredient(recipeId, "eggs", 3, "pieces"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "cheese", 50, "grams"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "butter", 1, "tablespoon"));

        // Recipe 19: Caprese Salad
        recipeId = recipeDao.insert(new Recipe(
            "Caprese Salad",
            "Fresh Italian tomato and mozzarella salad",
            "1. Slice tomatoes and mozzarella\n2. Arrange alternately on plate\n3. Add basil leaves\n4. Drizzle with olive oil\n5. Season and serve"
        ));
        ingredientDao.insert(new RecipeIngredient(recipeId, "tomatoes", 3, "pieces"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "mozzarella", 200, "grams"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "olive oil", 3, "tablespoons"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "basil", 10, "leaves"));

        // Recipe 20: Mushroom Risotto
        recipeId = recipeDao.insert(new Recipe(
            "Mushroom Risotto",
            "Creamy Italian rice dish",
            "1. Sauté mushrooms and onions\n2. Add rice and toast briefly\n3. Add water gradually, stirring\n4. Cook until creamy\n5. Stir in butter and cheese"
        ));
        ingredientDao.insert(new RecipeIngredient(recipeId, "rice", 2, "cups"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "mushrooms", 250, "grams"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "onion", 1, "pieces"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "water", 1000, "ml"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "butter", 50, "grams"));
        ingredientDao.insert(new RecipeIngredient(recipeId, "cheese", 100, "grams"));
    }
}
