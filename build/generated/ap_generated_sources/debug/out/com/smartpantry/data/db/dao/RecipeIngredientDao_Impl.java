package com.smartpantry.data.db.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.smartpantry.data.db.entity.RecipeIngredient;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class RecipeIngredientDao_Impl implements RecipeIngredientDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RecipeIngredient> __insertionAdapterOfRecipeIngredient;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public RecipeIngredientDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRecipeIngredient = new EntityInsertionAdapter<RecipeIngredient>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `recipe_ingredients` (`id`,`recipeId`,`ingredientName`,`quantityRequired`,`unit`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final RecipeIngredient entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getRecipeId());
        if (entity.getIngredientName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getIngredientName());
        }
        statement.bindDouble(4, entity.getQuantityRequired());
        if (entity.getUnit() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getUnit());
        }
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM recipe_ingredients";
        return _query;
      }
    };
  }

  @Override
  public void insert(final RecipeIngredient ingredient) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfRecipeIngredient.insert(ingredient);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertAll(final List<RecipeIngredient> ingredients) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfRecipeIngredient.insert(ingredients);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public LiveData<List<RecipeIngredient>> getIngredientsForRecipe(final long recipeId) {
    final String _sql = "SELECT * FROM recipe_ingredients WHERE recipeId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, recipeId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"recipe_ingredients"}, false, new Callable<List<RecipeIngredient>>() {
      @Override
      @Nullable
      public List<RecipeIngredient> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRecipeId = CursorUtil.getColumnIndexOrThrow(_cursor, "recipeId");
          final int _cursorIndexOfIngredientName = CursorUtil.getColumnIndexOrThrow(_cursor, "ingredientName");
          final int _cursorIndexOfQuantityRequired = CursorUtil.getColumnIndexOrThrow(_cursor, "quantityRequired");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final List<RecipeIngredient> _result = new ArrayList<RecipeIngredient>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RecipeIngredient _item;
            _item = new RecipeIngredient();
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item.setId(_tmpId);
            final long _tmpRecipeId;
            _tmpRecipeId = _cursor.getLong(_cursorIndexOfRecipeId);
            _item.setRecipeId(_tmpRecipeId);
            final String _tmpIngredientName;
            if (_cursor.isNull(_cursorIndexOfIngredientName)) {
              _tmpIngredientName = null;
            } else {
              _tmpIngredientName = _cursor.getString(_cursorIndexOfIngredientName);
            }
            _item.setIngredientName(_tmpIngredientName);
            final double _tmpQuantityRequired;
            _tmpQuantityRequired = _cursor.getDouble(_cursorIndexOfQuantityRequired);
            _item.setQuantityRequired(_tmpQuantityRequired);
            final String _tmpUnit;
            if (_cursor.isNull(_cursorIndexOfUnit)) {
              _tmpUnit = null;
            } else {
              _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            }
            _item.setUnit(_tmpUnit);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public List<RecipeIngredient> getIngredientsForRecipeSync(final long recipeId) {
    final String _sql = "SELECT * FROM recipe_ingredients WHERE recipeId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, recipeId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfRecipeId = CursorUtil.getColumnIndexOrThrow(_cursor, "recipeId");
      final int _cursorIndexOfIngredientName = CursorUtil.getColumnIndexOrThrow(_cursor, "ingredientName");
      final int _cursorIndexOfQuantityRequired = CursorUtil.getColumnIndexOrThrow(_cursor, "quantityRequired");
      final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
      final List<RecipeIngredient> _result = new ArrayList<RecipeIngredient>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final RecipeIngredient _item;
        _item = new RecipeIngredient();
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _item.setId(_tmpId);
        final long _tmpRecipeId;
        _tmpRecipeId = _cursor.getLong(_cursorIndexOfRecipeId);
        _item.setRecipeId(_tmpRecipeId);
        final String _tmpIngredientName;
        if (_cursor.isNull(_cursorIndexOfIngredientName)) {
          _tmpIngredientName = null;
        } else {
          _tmpIngredientName = _cursor.getString(_cursorIndexOfIngredientName);
        }
        _item.setIngredientName(_tmpIngredientName);
        final double _tmpQuantityRequired;
        _tmpQuantityRequired = _cursor.getDouble(_cursorIndexOfQuantityRequired);
        _item.setQuantityRequired(_tmpQuantityRequired);
        final String _tmpUnit;
        if (_cursor.isNull(_cursorIndexOfUnit)) {
          _tmpUnit = null;
        } else {
          _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
        }
        _item.setUnit(_tmpUnit);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
