package com.smartpantry.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for ValidationUtils
 * Tests all validation rules for pantry items
 */
public class ValidationUtilsTest {

    // Ingredient Name Tests
    @Test
    public void validateIngredientName_rejectsNull() {
        ValidationUtils.ValidationResult result = ValidationUtils.validateIngredientName(null);
        assertFalse(result.isValid());
        assertEquals("Ingredient name is required", result.getErrorMessage());
    }

    @Test
    public void validateIngredientName_rejectsEmpty() {
        ValidationUtils.ValidationResult result = ValidationUtils.validateIngredientName("");
        assertFalse(result.isValid());
    }

    @Test
    public void validateIngredientName_rejectsTooShort() {
        ValidationUtils.ValidationResult result = ValidationUtils.validateIngredientName("a");
        assertFalse(result.isValid());
        assertEquals("Ingredient name must be at least 2 characters", result.getErrorMessage());
    }

    @Test
    public void validateIngredientName_acceptsValidNames() {
        assertTrue(ValidationUtils.validateIngredientName("Tomato").isValid());
        assertTrue(ValidationUtils.validateIngredientName("Tomatoes").isValid());
        assertTrue(ValidationUtils.validateIngredientName("Bell Pepper").isValid());
    }

    // Quantity Tests
    @Test
    public void validateQuantity_rejectsNull() {
        ValidationUtils.ValidationResult result = ValidationUtils.validateQuantity((String) null);
        assertFalse(result.isValid());
        assertEquals("Quantity is required", result.getErrorMessage());
    }

    @Test
    public void validateQuantity_rejectsEmpty() {
        ValidationUtils.ValidationResult result = ValidationUtils.validateQuantity("");
        assertFalse(result.isValid());
    }

    @Test
    public void validateQuantity_rejectsNonNumeric() {
        ValidationUtils.ValidationResult result = ValidationUtils.validateQuantity("abc");
        assertFalse(result.isValid());
        assertEquals("Quantity must be a valid number", result.getErrorMessage());
    }

    @Test
    public void validateQuantity_rejectsZero() {
        ValidationUtils.ValidationResult result = ValidationUtils.validateQuantity("0");
        assertFalse(result.isValid());
        assertEquals("Quantity must be greater than 0", result.getErrorMessage());
    }

    @Test
    public void validateQuantity_rejectsNegative() {
        ValidationUtils.ValidationResult result = ValidationUtils.validateQuantity("-5");
        assertFalse(result.isValid());
    }

    @Test
    public void validateQuantity_acceptsValidNumbers() {
        assertTrue(ValidationUtils.validateQuantity("1").isValid());
        assertTrue(ValidationUtils.validateQuantity("3.5").isValid());
        assertTrue(ValidationUtils.validateQuantity("100").isValid());
        assertTrue(ValidationUtils.validateQuantity("0.5").isValid());
    }

    @Test
    public void validateQuantity_double_rejectsZero() {
        ValidationUtils.ValidationResult result = ValidationUtils.validateQuantity(0.0);
        assertFalse(result.isValid());
    }

    @Test
    public void validateQuantity_double_acceptsPositive() {
        assertTrue(ValidationUtils.validateQuantity(1.0).isValid());
        assertTrue(ValidationUtils.validateQuantity(0.5).isValid());
    }

    // Unit Tests
    @Test
    public void validateUnit_rejectsNull() {
        ValidationUtils.ValidationResult result = ValidationUtils.validateUnit(null);
        assertFalse(result.isValid());
    }

    @Test
    public void validateUnit_rejectsEmpty() {
        ValidationUtils.ValidationResult result = ValidationUtils.validateUnit("");
        assertFalse(result.isValid());
    }

    @Test
    public void validateUnit_acceptsValidUnits() {
        assertTrue(ValidationUtils.validateUnit("pieces").isValid());
        assertTrue(ValidationUtils.validateUnit("cups").isValid());
        assertTrue(ValidationUtils.validateUnit("grams").isValid());
        assertTrue(ValidationUtils.validateUnit("ml").isValid());
    }

    // Expiry Date Tests
    @Test
    public void validateExpiryDate_acceptsNull() {
        assertTrue(ValidationUtils.validateExpiryDate(null).isValid());
    }

    @Test
    public void validateExpiryDate_acceptsEmpty() {
        assertTrue(ValidationUtils.validateExpiryDate("").isValid());
    }

    @Test
    public void validateExpiryDate_rejectsInvalidFormat() {
        assertFalse(ValidationUtils.validateExpiryDate("12/31/2024").isValid());
        assertFalse(ValidationUtils.validateExpiryDate("2024-13-01").isValid());
        assertFalse(ValidationUtils.validateExpiryDate("invalid").isValid());
    }

    @Test
    public void validateExpiryDate_acceptsValidFormat() {
        assertTrue(ValidationUtils.validateExpiryDate("2024-12-31").isValid());
        assertTrue(ValidationUtils.validateExpiryDate("2025-01-15").isValid());
    }

    // Complete Item Validation
    @Test
    public void validatePantryItem_rejectsInvalidName() {
        ValidationUtils.ValidationResult result = ValidationUtils.validatePantryItem(
            "a", "3", "pieces", ""
        );
        assertFalse(result.isValid());
    }

    @Test
    public void validatePantryItem_rejectsInvalidQuantity() {
        ValidationUtils.ValidationResult result = ValidationUtils.validatePantryItem(
            "Tomato", "0", "pieces", ""
        );
        assertFalse(result.isValid());
    }

    @Test
    public void validatePantryItem_acceptsValidItem() {
        ValidationUtils.ValidationResult result = ValidationUtils.validatePantryItem(
            "Tomato", "3", "pieces", "2024-12-31"
        );
        assertTrue(result.isValid());
    }

    @Test
    public void validatePantryItem_acceptsValidItemWithoutExpiry() {
        ValidationUtils.ValidationResult result = ValidationUtils.validatePantryItem(
            "Tomato", "3", "pieces", ""
        );
        assertTrue(result.isValid());
    }
}
