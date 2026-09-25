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
import com.smartpantry.data.db.entity.Recipe;
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
public final class RecipeDao_Impl implements RecipeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Recipe> __insertionAdapterOfRecipe;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public RecipeDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRecipe = new EntityInsertionAdapter<Recipe>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `recipes` (`id`,`recipeName`,`recipeDescription`,`preparationSteps`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Recipe entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getRecipeName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getRecipeName());
        }
        if (entity.getRecipeDescription() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getRecipeDescription());
        }
        if (entity.getPreparationSteps() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPreparationSteps());
        }
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM recipes";
        return _query;
      }
    };
  }

  @Override
  public long insert(final Recipe recipe) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfRecipe.insertAndReturnId(recipe);
      __db.setTransactionSuccessful();
      return _result;
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
  public LiveData<List<Recipe>> getAllRecipes() {
    final String _sql = "SELECT * FROM recipes ORDER BY recipeName ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"recipes"}, false, new Callable<List<Recipe>>() {
      @Override
      @Nullable
      public List<Recipe> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRecipeName = CursorUtil.getColumnIndexOrThrow(_cursor, "recipeName");
          final int _cursorIndexOfRecipeDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "recipeDescription");
          final int _cursorIndexOfPreparationSteps = CursorUtil.getColumnIndexOrThrow(_cursor, "preparationSteps");
          final List<Recipe> _result = new ArrayList<Recipe>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Recipe _item;
            _item = new Recipe();
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item.setId(_tmpId);
            final String _tmpRecipeName;
            if (_cursor.isNull(_cursorIndexOfRecipeName)) {
              _tmpRecipeName = null;
            } else {
              _tmpRecipeName = _cursor.getString(_cursorIndexOfRecipeName);
            }
            _item.setRecipeName(_tmpRecipeName);
            final String _tmpRecipeDescription;
            if (_cursor.isNull(_cursorIndexOfRecipeDescription)) {
              _tmpRecipeDescription = null;
            } else {
              _tmpRecipeDescription = _cursor.getString(_cursorIndexOfRecipeDescription);
            }
            _item.setRecipeDescription(_tmpRecipeDescription);
            final String _tmpPreparationSteps;
            if (_cursor.isNull(_cursorIndexOfPreparationSteps)) {
              _tmpPreparationSteps = null;
            } else {
              _tmpPreparationSteps = _cursor.getString(_cursorIndexOfPreparationSteps);
            }
            _item.setPreparationSteps(_tmpPreparationSteps);
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
  public LiveData<Recipe> getRecipeById(final long id) {
    final String _sql = "SELECT * FROM recipes WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return __db.getInvalidationTracker().createLiveData(new String[] {"recipes"}, false, new Callable<Recipe>() {
      @Override
      @Nullable
      public Recipe call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRecipeName = CursorUtil.getColumnIndexOrThrow(_cursor, "recipeName");
          final int _cursorIndexOfRecipeDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "recipeDescription");
          final int _cursorIndexOfPreparationSteps = CursorUtil.getColumnIndexOrThrow(_cursor, "preparationSteps");
          final Recipe _result;
          if (_cursor.moveToFirst()) {
            _result = new Recipe();
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _result.setId(_tmpId);
            final String _tmpRecipeName;
            if (_cursor.isNull(_cursorIndexOfRecipeName)) {
              _tmpRecipeName = null;
            } else {
              _tmpRecipeName = _cursor.getString(_cursorIndexOfRecipeName);
            }
            _result.setRecipeName(_tmpRecipeName);
            final String _tmpRecipeDescription;
            if (_cursor.isNull(_cursorIndexOfRecipeDescription)) {
              _tmpRecipeDescription = null;
            } else {
              _tmpRecipeDescription = _cursor.getString(_cursorIndexOfRecipeDescription);
            }
            _result.setRecipeDescription(_tmpRecipeDescription);
            final String _tmpPreparationSteps;
            if (_cursor.isNull(_cursorIndexOfPreparationSteps)) {
              _tmpPreparationSteps = null;
            } else {
              _tmpPreparationSteps = _cursor.getString(_cursorIndexOfPreparationSteps);
            }
            _result.setPreparationSteps(_tmpPreparationSteps);
          } else {
            _result = null;
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
  public List<Recipe> getAllRecipesSync() {
    final String _sql = "SELECT * FROM recipes";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfRecipeName = CursorUtil.getColumnIndexOrThrow(_cursor, "recipeName");
      final int _cursorIndexOfRecipeDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "recipeDescription");
      final int _cursorIndexOfPreparationSteps = CursorUtil.getColumnIndexOrThrow(_cursor, "preparationSteps");
      final List<Recipe> _result = new ArrayList<Recipe>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Recipe _item;
        _item = new Recipe();
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpRecipeName;
        if (_cursor.isNull(_cursorIndexOfRecipeName)) {
          _tmpRecipeName = null;
        } else {
          _tmpRecipeName = _cursor.getString(_cursorIndexOfRecipeName);
        }
        _item.setRecipeName(_tmpRecipeName);
        final String _tmpRecipeDescription;
        if (_cursor.isNull(_cursorIndexOfRecipeDescription)) {
          _tmpRecipeDescription = null;
        } else {
          _tmpRecipeDescription = _cursor.getString(_cursorIndexOfRecipeDescription);
        }
        _item.setRecipeDescription(_tmpRecipeDescription);
        final String _tmpPreparationSteps;
        if (_cursor.isNull(_cursorIndexOfPreparationSteps)) {
          _tmpPreparationSteps = null;
        } else {
          _tmpPreparationSteps = _cursor.getString(_cursorIndexOfPreparationSteps);
        }
        _item.setPreparationSteps(_tmpPreparationSteps);
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
