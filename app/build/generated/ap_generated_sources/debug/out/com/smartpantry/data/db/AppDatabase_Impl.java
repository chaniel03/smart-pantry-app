package com.smartpantry.data.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.smartpantry.data.db.dao.PantryItemDao;
import com.smartpantry.data.db.dao.PantryItemDao_Impl;
import com.smartpantry.data.db.dao.RecipeDao;
import com.smartpantry.data.db.dao.RecipeDao_Impl;
import com.smartpantry.data.db.dao.RecipeIngredientDao;
import com.smartpantry.data.db.dao.RecipeIngredientDao_Impl;
import com.smartpantry.data.db.dao.SettingsDao;
import com.smartpantry.data.db.dao.SettingsDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile PantryItemDao _pantryItemDao;

  private volatile RecipeDao _recipeDao;

  private volatile RecipeIngredientDao _recipeIngredientDao;

  private volatile SettingsDao _settingsDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `pantry_items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `ingredientName` TEXT, `quantity` REAL NOT NULL, `unit` TEXT, `expiryDate` TEXT, `createdDate` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `recipes` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `recipeName` TEXT, `recipeDescription` TEXT, `preparationSteps` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `recipe_ingredients` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `recipeId` INTEGER NOT NULL, `ingredientName` TEXT, `quantityRequired` REAL NOT NULL, `unit` TEXT, FOREIGN KEY(`recipeId`) REFERENCES `recipes`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_recipe_ingredients_recipeId` ON `recipe_ingredients` (`recipeId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `settings` (`id` INTEGER NOT NULL, `expiryNotificationsEnabled` INTEGER NOT NULL, `measurementPreference` TEXT, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'bd716287b46031cc6d3bd0ead72a039e')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `pantry_items`");
        db.execSQL("DROP TABLE IF EXISTS `recipes`");
        db.execSQL("DROP TABLE IF EXISTS `recipe_ingredients`");
        db.execSQL("DROP TABLE IF EXISTS `settings`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsPantryItems = new HashMap<String, TableInfo.Column>(6);
        _columnsPantryItems.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPantryItems.put("ingredientName", new TableInfo.Column("ingredientName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPantryItems.put("quantity", new TableInfo.Column("quantity", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPantryItems.put("unit", new TableInfo.Column("unit", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPantryItems.put("expiryDate", new TableInfo.Column("expiryDate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPantryItems.put("createdDate", new TableInfo.Column("createdDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPantryItems = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPantryItems = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPantryItems = new TableInfo("pantry_items", _columnsPantryItems, _foreignKeysPantryItems, _indicesPantryItems);
        final TableInfo _existingPantryItems = TableInfo.read(db, "pantry_items");
        if (!_infoPantryItems.equals(_existingPantryItems)) {
          return new RoomOpenHelper.ValidationResult(false, "pantry_items(com.smartpantry.data.db.entity.PantryItem).\n"
                  + " Expected:\n" + _infoPantryItems + "\n"
                  + " Found:\n" + _existingPantryItems);
        }
        final HashMap<String, TableInfo.Column> _columnsRecipes = new HashMap<String, TableInfo.Column>(4);
        _columnsRecipes.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecipes.put("recipeName", new TableInfo.Column("recipeName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecipes.put("recipeDescription", new TableInfo.Column("recipeDescription", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecipes.put("preparationSteps", new TableInfo.Column("preparationSteps", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRecipes = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRecipes = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRecipes = new TableInfo("recipes", _columnsRecipes, _foreignKeysRecipes, _indicesRecipes);
        final TableInfo _existingRecipes = TableInfo.read(db, "recipes");
        if (!_infoRecipes.equals(_existingRecipes)) {
          return new RoomOpenHelper.ValidationResult(false, "recipes(com.smartpantry.data.db.entity.Recipe).\n"
                  + " Expected:\n" + _infoRecipes + "\n"
                  + " Found:\n" + _existingRecipes);
        }
        final HashMap<String, TableInfo.Column> _columnsRecipeIngredients = new HashMap<String, TableInfo.Column>(5);
        _columnsRecipeIngredients.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecipeIngredients.put("recipeId", new TableInfo.Column("recipeId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecipeIngredients.put("ingredientName", new TableInfo.Column("ingredientName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecipeIngredients.put("quantityRequired", new TableInfo.Column("quantityRequired", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecipeIngredients.put("unit", new TableInfo.Column("unit", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRecipeIngredients = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysRecipeIngredients.add(new TableInfo.ForeignKey("recipes", "CASCADE", "NO ACTION", Arrays.asList("recipeId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesRecipeIngredients = new HashSet<TableInfo.Index>(1);
        _indicesRecipeIngredients.add(new TableInfo.Index("index_recipe_ingredients_recipeId", false, Arrays.asList("recipeId"), Arrays.asList("ASC")));
        final TableInfo _infoRecipeIngredients = new TableInfo("recipe_ingredients", _columnsRecipeIngredients, _foreignKeysRecipeIngredients, _indicesRecipeIngredients);
        final TableInfo _existingRecipeIngredients = TableInfo.read(db, "recipe_ingredients");
        if (!_infoRecipeIngredients.equals(_existingRecipeIngredients)) {
          return new RoomOpenHelper.ValidationResult(false, "recipe_ingredients(com.smartpantry.data.db.entity.RecipeIngredient).\n"
                  + " Expected:\n" + _infoRecipeIngredients + "\n"
                  + " Found:\n" + _existingRecipeIngredients);
        }
        final HashMap<String, TableInfo.Column> _columnsSettings = new HashMap<String, TableInfo.Column>(3);
        _columnsSettings.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSettings.put("expiryNotificationsEnabled", new TableInfo.Column("expiryNotificationsEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSettings.put("measurementPreference", new TableInfo.Column("measurementPreference", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSettings = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSettings = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSettings = new TableInfo("settings", _columnsSettings, _foreignKeysSettings, _indicesSettings);
        final TableInfo _existingSettings = TableInfo.read(db, "settings");
        if (!_infoSettings.equals(_existingSettings)) {
          return new RoomOpenHelper.ValidationResult(false, "settings(com.smartpantry.data.db.entity.Settings).\n"
                  + " Expected:\n" + _infoSettings + "\n"
                  + " Found:\n" + _existingSettings);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "bd716287b46031cc6d3bd0ead72a039e", "01cc563254375978f73ed3064cf60110");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "pantry_items","recipes","recipe_ingredients","settings");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `pantry_items`");
      _db.execSQL("DELETE FROM `recipes`");
      _db.execSQL("DELETE FROM `recipe_ingredients`");
      _db.execSQL("DELETE FROM `settings`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(PantryItemDao.class, PantryItemDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RecipeDao.class, RecipeDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RecipeIngredientDao.class, RecipeIngredientDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SettingsDao.class, SettingsDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public PantryItemDao pantryItemDao() {
    if (_pantryItemDao != null) {
      return _pantryItemDao;
    } else {
      synchronized(this) {
        if(_pantryItemDao == null) {
          _pantryItemDao = new PantryItemDao_Impl(this);
        }
        return _pantryItemDao;
      }
    }
  }

  @Override
  public RecipeDao recipeDao() {
    if (_recipeDao != null) {
      return _recipeDao;
    } else {
      synchronized(this) {
        if(_recipeDao == null) {
          _recipeDao = new RecipeDao_Impl(this);
        }
        return _recipeDao;
      }
    }
  }

  @Override
  public RecipeIngredientDao recipeIngredientDao() {
    if (_recipeIngredientDao != null) {
      return _recipeIngredientDao;
    } else {
      synchronized(this) {
        if(_recipeIngredientDao == null) {
          _recipeIngredientDao = new RecipeIngredientDao_Impl(this);
        }
        return _recipeIngredientDao;
      }
    }
  }

  @Override
  public SettingsDao settingsDao() {
    if (_settingsDao != null) {
      return _settingsDao;
    } else {
      synchronized(this) {
        if(_settingsDao == null) {
          _settingsDao = new SettingsDao_Impl(this);
        }
        return _settingsDao;
      }
    }
  }
}
