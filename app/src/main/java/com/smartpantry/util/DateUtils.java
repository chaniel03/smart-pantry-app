package com.smartpantry.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utility class for date formatting and manipulation
 */
public class DateUtils {

    private static final SimpleDateFormat DISPLAY_FORMAT = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
    private static final SimpleDateFormat STORAGE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    /**
     * Format date for display (e.g., "Dec 31, 2024")
     * 
     * @param dateStr Date string in yyyy-MM-dd format
     * @return Formatted date string for display
     */
    public static String formatForDisplay(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return "No expiry date";
        }

        try {
            Date date = STORAGE_FORMAT.parse(dateStr);
            return DISPLAY_FORMAT.format(date);
        } catch (Exception e) {
            return dateStr;
        }
    }

    /**
     * Get current date in storage format (yyyy-MM-dd)
     * 
     * @return Current date string
     */
    public static String getCurrentDate() {
        return STORAGE_FORMAT.format(new Date());
    }

    /**
     * Format timestamp to display string
     * 
     * @param timestamp Unix timestamp in milliseconds
     * @return Formatted date string
     */
    public static String formatTimestamp(long timestamp) {
        return DISPLAY_FORMAT.format(new Date(timestamp));
    }

    /**
     * Check if date string is valid
     * 
     * @param dateStr Date string
     * @return true if valid date format
     */
    public static boolean isValidDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return false;
        }

        try {
            STORAGE_FORMAT.setLenient(false);
            STORAGE_FORMAT.parse(dateStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
