package com.smartpantry.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smartpantry.R;
import com.smartpantry.ui.pantry.PantryFragment;
import com.smartpantry.ui.recipes.RecipesFragment;
import com.smartpantry.ui.settings.SettingsFragment;

/**
 * Main Activity with bottom navigation
 * Hosts three fragments: Pantry, Recipes, Settings
 */
public class MainActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupNavigation();

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(new PantryFragment());
            toolbar.setTitle(R.string.pantry_title);
        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        setSupportActionBar(toolbar);
    }

    private void setupNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            String title = "";

            int itemId = item.getItemId();
            if (itemId == R.id.nav_pantry) {
                selectedFragment = new PantryFragment();
                title = getString(R.string.pantry_title);
            } else if (itemId == R.id.nav_recipes) {
                selectedFragment = new RecipesFragment();
                title = getString(R.string.recipes_title);
            } else if (itemId == R.id.nav_settings) {
                selectedFragment = new SettingsFragment();
                title = getString(R.string.settings_title);
            }

            if (selectedFragment != null) {
                toolbar.setTitle(title);
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    /**
     * Refresh recipes when pantry changes
     * Called from PantryFragment after add/edit/delete
     */
    public void refreshRecipes() {
        Fragment currentFragment = getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        
        if (currentFragment instanceof RecipesFragment) {
            ((RecipesFragment) currentFragment).refreshRecipes();
        }
    }
}
