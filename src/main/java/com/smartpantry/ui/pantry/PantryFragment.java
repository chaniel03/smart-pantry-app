package com.smartpantry.ui.pantry;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.smartpantry.R;
import com.smartpantry.data.db.entity.PantryItem;
import com.smartpantry.ui.MainActivity;
import com.smartpantry.ui.adapters.PantryAdapter;
import com.smartpantry.viewmodel.PantryViewModel;

/**
 * Fragment for displaying and managing pantry items
 * Features: View list, search, add, edit, delete
 */
public class PantryFragment extends Fragment {

    private PantryViewModel viewModel;
    private PantryAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayout emptyState;
    private TextInputEditText searchEditText;
    private FloatingActionButton fabAdd;

    private static final int REQUEST_ADD_INGREDIENT = 1;
    private static final int REQUEST_EDIT_INGREDIENT = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pantry, container, false);

        initViews(view);
        setupRecyclerView();
        setupViewModel();
        setupListeners();

        return view;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.pantry_recycler_view);
        emptyState = view.findViewById(R.id.empty_state);
        searchEditText = view.findViewById(R.id.search_edit_text);
        fabAdd = view.findViewById(R.id.fab_add_ingredient);
    }

    private void setupRecyclerView() {
        adapter = new PantryAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Item click listener (for editing)
        adapter.setOnItemClickListener(item -> {
            Intent intent = new Intent(getActivity(), AddEditIngredientActivity.class);
            intent.putExtra("item_id", item.getId());
            startActivityForResult(intent, REQUEST_EDIT_INGREDIENT);
        });

        // Delete click listener
        adapter.setOnDeleteClickListener(this::showDeleteConfirmation);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(PantryViewModel.class);

        // Observe all pantry items
        viewModel.getAllItems().observe(getViewLifecycleOwner(), items -> {
            adapter.submitList(items);
            
            // Show/hide empty state
            if (items == null || items.isEmpty()) {
                emptyState.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                emptyState.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupListeners() {
        // FAB click - add new ingredient
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddEditIngredientActivity.class);
            startActivityForResult(intent, REQUEST_ADD_INGREDIENT);
        });

        // Search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    viewModel.searchItems(s.toString()).observe(getViewLifecycleOwner(), 
                        items -> adapter.submitList(items));
                } else {
                    viewModel.getAllItems().observe(getViewLifecycleOwner(), 
                        items -> adapter.submitList(items));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void showDeleteConfirmation(PantryItem item) {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.delete_ingredient)
                .setMessage(R.string.confirm_delete)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    viewModel.delete(item);
                    notifyRecipesRefresh();
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == AddEditIngredientActivity.RESULT_OK) {
            // Ingredient was added or updated, refresh recipes
            notifyRecipesRefresh();
        }
    }

    private void notifyRecipesRefresh() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).refreshRecipes();
        }
    }
}
