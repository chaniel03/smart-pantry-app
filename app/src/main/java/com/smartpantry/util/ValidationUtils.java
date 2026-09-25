package com.smartpantry.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utility class for input validation
 * 
 * REQUIREMENTS:
 * - Ingredient Name: Required, Min 2 chars
 * - Quantity: Required, Numeric, Greater than 0
 * - Expiry Date: Optional, Must be valid date if supplied
 */
public class ValidationUtils {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    /**
     * Validation result container
     */
    public static class ValidationResult {
        private final boolean isValid;
        private final String errorMessage;

        public ValidationResult(boolean isValid, String errorMessage) {
            this.isValid = isValid;
            this.errorMessage = errorMessage;
        }

        public boolean isValid() {
            return isValid;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public static ValidationResult success() {
            return new ValidationResult(true, null);
        }

        public static ValidationResult error(String message) {
            return new ValidationResult(false, message);
        }
    }

    /**
     * Validate ingredient name
     * Rules:
     * - Required (not null or empty)
     * - Minimum 2 characters
     * 
     * @param ingredientName Name to validate
     * @return ValidationResult with status and error message
     */
    public static ValidationResult validateIngredientName(String ingredientName) {
        if (ingredientName == null || ingredientName.trim().isEmpty()) {
            return ValidationResult.error("Ingredient name is required");
        }

        String trimmed = ingredientName.trim();
        if (trimmed.length() < 2) {
            return ValidationResult.error("Ingredient name must be at least 2 characters");
        }

        return ValidationResult.success();
    }

    /**
     * Validate quantity
     * Rules:
     * - Required (must be provided)
     * - Must be numeric
     * - Must be greater than 0
     * 
     * @param quantityStr Quantity as string
     * @return ValidationResult with status and error message
     */
    public static ValidationResult validateQuantity(String quantityStr) {
        if (quantityStr == null || quantityStr.trim().isEmpty()) {
            return ValidationResult.error("Quantity is required");
        }

        try {
            double quantity = Double.parseDouble(quantityStr.trim());
            
            if (quantity <= 0) {
                return ValidationResult.error("Quantity must be greater than 0");
            }
            
            return ValidationResult.success();
        } catch (NumberFormatException e) {
            return ValidationResult.error("Quantity must be a valid number");
        }
    }

    /**
     * Validate quantity (double value)
     * 
     * @param quantity Quantity value
     * @return ValidationResult with status and error message
     */
    public static ValidationResult validateQuantity(double quantity) {
        if (quantity <= 0) {
            return ValidationResult.error("Quantity must be greater than 0");
        }
        return ValidationResult.success();
    }

    /**
     * Validate unit
     * Rules:
     * - Required (not null or empty)
     * 
     * @param unit Unit to validate
     * @return ValidationResult with status and error message
     */
    public static ValidationResult validateUnit(String unit) {
        if (unit == null || unit.trim().isEmpty()) {
            return ValidationResult.error("Unit is required");
        }

        return ValidationResult.success();
    }

    /**
     * Validate expiry date
     * Rules:
     * - Optional (can be null or empty)
     * - If provided, must be valid date in format yyyy-MM-dd
     * - Should be today or future date (warning only)
     * 
     * @param expiryDateStr Expiry date as string
     * @return ValidationResult with status and error message
     */
    public static ValidationResult validateExpiryDate(String expiryDateStr) {
        // Empty/null is valid (optional field)
        if (expiryDateStr == null || expiryDateStr.trim().isEmpty()) {
            return ValidationResult.success();
        }

        String trimmed = expiryDateStr.trim();
        
        try {
            DATE_FORMAT.setLenient(false);
            Date expiryDate = DATE_FORMAT.parse(trimmed);
            
            // Valid date format
            return ValidationResult.success();
        } catch (ParseException e) {
            return ValidationResult.error("Invalid date format. Use yyyy-MM-dd (e.g., 2024-12-31)");
        }
    }

    /**
     * Check if expiry date is in the past (for warnings)
     * 
     * @param expiryDateStr Expiry date as string
     * @return true if date is in the past
     */
    public static boolean isExpired(String expiryDateStr) {
        if (expiryDateStr == null || expiryDateStr.trim().isEmpty()) {
            return false;
        }

        try {
            Date expiryDate = DATE_FORMAT.parse(expiryDateStr.trim());
            Date today = new Date();
            return expiryDate.before(today);
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Validate complete pantry item
     * 
     * @param ingredientName Ingredient name
     * @param quantityStr Quantity as string
     * @param unit Unit
     * @param expiryDateStr Expiry date (optional)
     * @return ValidationResult with first error encountered, or success
     */
    public static ValidationResult validatePantryItem(String ingredientName, String quantityStr, 
                                                      String unit, String expiryDateStr) {
        ValidationResult nameResult = validateIngredientName(ingredientName);
        if (!nameResult.isValid()) {
            return nameResult;
        }

        ValidationResult quantityResult = validateQuantity(quantityStr);
        if (!quantityResult.isValid()) {
            return quantityResult;
        }

        ValidationResult unitResult = validateUnit(unit);
        if (!unitResult.isValid()) {
            return unitResult;
        }

        ValidationResult dateResult = validateExpiryDate(expiryDateStr);
        if (!dateResult.isValid()) {
            return dateResult;
        }

        return ValidationResult.success();
    }

    /**
     * Format date string to standard format
     * 
     * @param dateStr Date string
     * @return Formatted date or empty string if invalid
     */
    public static String formatDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return "";
        }

        try {
            Date date = DATE_FORMAT.parse(dateStr.trim());
            return DATE_FORMAT.format(date);
        } catch (ParseException e) {
            return "";
        }
    }
}
