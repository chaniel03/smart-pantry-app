package com.smartpantry.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for normalizing ingredient names to handle variations
 * Handles: plurals, case sensitivity, whitespace, and common variations
 * 
 * REQUIREMENT: Smart Matching - handle "tomato" vs "tomatoes", case, spacing
 */
public class IngredientNormalizer {

    // Common plural to singular mappings
    private static final Map<String, String> PLURAL_MAP = new HashMap<>();
    
    static {
        // Vegetables
        PLURAL_MAP.put("tomatoes", "tomato");
        PLURAL_MAP.put("potatoes", "potato");
        PLURAL_MAP.put("onions", "onion");
        PLURAL_MAP.put("carrots", "carrot");
        PLURAL_MAP.put("cucumbers", "cucumber");
        PLURAL_MAP.put("peppers", "pepper");
        PLURAL_MAP.put("mushrooms", "mushroom");
        PLURAL_MAP.put("peas", "pea");
        
        // Proteins
        PLURAL_MAP.put("eggs", "egg");
        PLURAL_MAP.put("chickens", "chicken");
        
        // Herbs & Spices
        PLURAL_MAP.put("cloves", "clove");
        PLURAL_MAP.put("leaves", "leaf");
        
        // Other
        PLURAL_MAP.put("pieces", "piece");
        PLURAL_MAP.put("slices", "slice");
        PLURAL_MAP.put("loaves", "loaf");
    }

    /**
     * Normalize an ingredient name for matching
     * 
     * Rules:
     * 1. Convert to lowercase
     * 2. Trim leading/trailing whitespace
     * 3. Replace multiple spaces with single space
     * 4. Convert common plurals to singular
     * 5. Remove special characters (except spaces)
     * 
     * @param ingredientName Raw ingredient name
     * @return Normalized ingredient name
     */
    public static String normalize(String ingredientName) {
        if (ingredientName == null || ingredientName.isEmpty()) {
            return "";
        }

        // Step 1: Convert to lowercase
        String normalized = ingredientName.toLowerCase();

        // Step 2 & 3: Trim and normalize whitespace
        normalized = normalized.trim().replaceAll("\\s+", " ");

        // Step 4: Remove special characters (keep letters, numbers, and spaces)
        normalized = normalized.replaceAll("[^a-z0-9\\s]", "");

        // Step 5: Convert plural to singular if mapping exists
        if (PLURAL_MAP.containsKey(normalized)) {
            normalized = PLURAL_MAP.get(normalized);
        }

        return normalized;
    }

    /**
     * Check if two ingredient names match after normalization
     * 
     * @param name1 First ingredient name
     * @param name2 Second ingredient name
     * @return true if normalized names match
     */
    public static boolean matches(String name1, String name2) {
        return normalize(name1).equals(normalize(name2));
    }

    /**
     * Get normalized version for display (capitalized)
     * 
     * @param ingredientName Raw ingredient name
     * @return Normalized and capitalized name
     */
    public static String normalizeForDisplay(String ingredientName) {
        String normalized = normalize(ingredientName);
        if (normalized.isEmpty()) {
            return "";
        }
        
        // Capitalize first letter
        return normalized.substring(0, 1).toUpperCase() + normalized.substring(1);
    }
}
