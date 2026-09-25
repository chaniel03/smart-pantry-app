package com.smartpantry.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for IngredientNormalizer
 * Tests smart matching: plurals, case sensitivity, whitespace
 */
public class IngredientNormalizerTest {

    @Test
    public void normalize_handlesLowercase() {
        assertEquals("tomato", IngredientNormalizer.normalize("TOMATO"));
        assertEquals("tomato", IngredientNormalizer.normalize("Tomato"));
        assertEquals("tomato", IngredientNormalizer.normalize("tomato"));
    }

    @Test
    public void normalize_trimsWhitespace() {
        assertEquals("tomato", IngredientNormalizer.normalize("  tomato  "));
        assertEquals("tomato", IngredientNormalizer.normalize(" tomato"));
        assertEquals("tomato", IngredientNormalizer.normalize("tomato "));
    }

    @Test
    public void normalize_handlesMultipleSpaces() {
        assertEquals("bell pepper", IngredientNormalizer.normalize("bell  pepper"));
        assertEquals("bell pepper", IngredientNormalizer.normalize("bell    pepper"));
    }

    @Test
    public void normalize_handlesPluralToSingular() {
        assertEquals("tomato", IngredientNormalizer.normalize("tomatoes"));
        assertEquals("potato", IngredientNormalizer.normalize("potatoes"));
        assertEquals("onion", IngredientNormalizer.normalize("onions"));
        assertEquals("egg", IngredientNormalizer.normalize("eggs"));
        assertEquals("carrot", IngredientNormalizer.normalize("carrots"));
    }

    @Test
    public void normalize_combinedTransformations() {
        assertEquals("tomato", IngredientNormalizer.normalize("  TOMATOES  "));
        assertEquals("potato", IngredientNormalizer.normalize(" Potatoes "));
        assertEquals("onion", IngredientNormalizer.normalize("ONIONS"));
    }

    @Test
    public void normalize_handlesEmptyAndNull() {
        assertEquals("", IngredientNormalizer.normalize(""));
        assertEquals("", IngredientNormalizer.normalize(null));
    }

    @Test
    public void matches_returnsTrueForEquivalentNames() {
        assertTrue(IngredientNormalizer.matches("tomato", "tomatoes"));
        assertTrue(IngredientNormalizer.matches("TOMATO", "tomatoes"));
        assertTrue(IngredientNormalizer.matches("  tomato  ", "Tomatoes"));
        assertTrue(IngredientNormalizer.matches("Potato", "potatoes"));
    }

    @Test
    public void matches_returnsFalseForDifferentNames() {
        assertFalse(IngredientNormalizer.matches("tomato", "potato"));
        assertFalse(IngredientNormalizer.matches("onion", "garlic"));
    }

    @Test
    public void normalizeForDisplay_capitalizesFirstLetter() {
        assertEquals("Tomato", IngredientNormalizer.normalizeForDisplay("tomatoes"));
        assertEquals("Potato", IngredientNormalizer.normalizeForDisplay("POTATOES"));
        assertEquals("Onion", IngredientNormalizer.normalizeForDisplay("  onions  "));
    }
}
