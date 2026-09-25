package com.smartpantry.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.smartpantry.R;
import com.smartpantry.data.db.entity.PantryItem;
import com.smartpantry.util.DateUtils;

/**
 * Adapter for displaying pantry items in RecyclerView
 */
public class PantryAdapter extends ListAdapter<PantryItem, PantryAdapter.PantryViewHolder> {

    private OnItemClickListener itemClickListener;
    private OnDeleteClickListener deleteClickListener;

    public PantryAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<PantryItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<PantryItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull PantryItem oldItem, @NonNull PantryItem newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull PantryItem oldItem, @NonNull PantryItem newItem) {
            return oldItem.getIngredientName().equals(newItem.getIngredientName()) &&
                   oldItem.getQuantity() == newItem.getQuantity() &&
                   oldItem.getUnit().equals(newItem.getUnit());
        }
    };

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem item = getItem(position);
        holder.bind(item);
    }

    class PantryViewHolder extends RecyclerView.ViewHolder {
        private final TextView ingredientName;
        private final TextView ingredientQuantity;
        private final TextView expiryDate;
        private final ImageButton deleteButton;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.ingredient_name);
            ingredientQuantity = itemView.findViewById(R.id.ingredient_quantity);
            expiryDate = itemView.findViewById(R.id.expiry_date);
            deleteButton = itemView.findViewById(R.id.delete_button);

            // Click listener for editing
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && itemClickListener != null) {
                    itemClickListener.onItemClick(getItem(position));
                }
            });

            // Delete button listener
            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && deleteClickListener != null) {
                    deleteClickListener.onDeleteClick(getItem(position));
                }
            });
        }

        public void bind(PantryItem item) {
            ingredientName.setText(item.getIngredientName());
            
            String quantityText = item.getQuantity() + " " + item.getUnit();
            ingredientQuantity.setText(quantityText);

            if (item.getExpiryDate() != null && !item.getExpiryDate().isEmpty()) {
                String expiryText = "Expires: " + DateUtils.formatForDisplay(item.getExpiryDate());
                expiryDate.setText(expiryText);
            } else {
                expiryDate.setText(R.string.no_expiry);
            }
        }
    }

    // Interfaces for click listeners
    public interface OnItemClickListener {
        void onItemClick(PantryItem item);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(PantryItem item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }

    public PantryItem getItemAt(int position) {
        return getItem(position);
    }
}
