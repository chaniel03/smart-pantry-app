package com.smartpantry;

import android.app.Application;

import com.smartpantry.data.db.AppDatabase;

/**
 * Application class for Smart Pantry Manager
 * Initializes database and provides global context
 */
public class SmartPantryApp extends Application {

    private static SmartPantryApp instance;
    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        
        // Initialize database
        database = AppDatabase.getInstance(this);
    }

    public static SmartPantryApp getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
