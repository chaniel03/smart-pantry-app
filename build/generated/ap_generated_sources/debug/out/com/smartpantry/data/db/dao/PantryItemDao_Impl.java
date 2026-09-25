package com.smartpantry.data.db.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.smartpantry.data.db.entity.PantryItem;
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
public final class PantryItemDao_Impl implements PantryItemDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PantryItem> __insertionAdapterOfPantryItem;

  private final EntityDeletionOrUpdateAdapter<PantryItem> __deletionAdapterOfPantryItem;

  private final EntityDeletionOrUpdateAdapter<PantryItem> __updateAdapterOfPantryItem;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public PantryItemDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPantryItem = new EntityInsertionAdapter<PantryItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `pantry_items` (`id`,`ingredientName`,`quantity`,`unit`,`expiryDate`,`createdDate`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final PantryItem entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getIngredientName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getIngredientName());
        }
        statement.bindDouble(3, entity.getQuantity());
        if (entity.getUnit() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getUnit());
        }
        if (entity.getExpiryDate() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getExpiryDate());
        }
        statement.bindLong(6, entity.getCreatedDate());
      }
    };
    this.__deletionAdapterOfPantryItem = new EntityDeletionOrUpdateAdapter<PantryItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `pantry_items` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final PantryItem entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfPantryItem = new EntityDeletionOrUpdateAdapter<PantryItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `pantry_items` SET `id` = ?,`ingredientName` = ?,`quantity` = ?,`unit` = ?,`expiryDate` = ?,`createdDate` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final PantryItem entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getIngredientName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getIngredientName());
        }
        statement.bindDouble(3, entity.getQuantity());
        if (entity.getUnit() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getUnit());
        }
        if (entity.getExpiryDate() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getExpiryDate());
        }
        statement.bindLong(6, entity.getCreatedDate());
        statement.bindLong(7, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM pantry_items";
        return _query;
      }
    };
  }

  @Override
  public long insert(final PantryItem item) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfPantryItem.insertAndReturnId(item);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final PantryItem item) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfPantryItem.handle(item);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final PantryItem item) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfPantryItem.handle(item);
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
  public LiveData<List<PantryItem>> getAllItems() {
    final String _sql = "SELECT * FROM pantry_items ORDER BY ingredientName ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"pantry_items"}, false, new Callable<List<PantryItem>>() {
      @Override
      @Nullable
      public List<PantryItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfIngredientName = CursorUtil.getColumnIndexOrThrow(_cursor, "ingredientName");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfExpiryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "expiryDate");
          final int _cursorIndexOfCreatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "createdDate");
          final List<PantryItem> _result = new ArrayList<PantryItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PantryItem _item;
            _item = new PantryItem();
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item.setId(_tmpId);
            final String _tmpIngredientName;
            if (_cursor.isNull(_cursorIndexOfIngredientName)) {
              _tmpIngredientName = null;
            } else {
              _tmpIngredientName = _cursor.getString(_cursorIndexOfIngredientName);
            }
            _item.setIngredientName(_tmpIngredientName);
            final double _tmpQuantity;
            _tmpQuantity = _cursor.getDouble(_cursorIndexOfQuantity);
            _item.setQuantity(_tmpQuantity);
            final String _tmpUnit;
            if (_cursor.isNull(_cursorIndexOfUnit)) {
              _tmpUnit = null;
            } else {
              _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            }
            _item.setUnit(_tmpUnit);
            final String _tmpExpiryDate;
            if (_cursor.isNull(_cursorIndexOfExpiryDate)) {
              _tmpExpiryDate = null;
            } else {
              _tmpExpiryDate = _cursor.getString(_cursorIndexOfExpiryDate);
            }
            _item.setExpiryDate(_tmpExpiryDate);
            final long _tmpCreatedDate;
            _tmpCreatedDate = _cursor.getLong(_cursorIndexOfCreatedDate);
            _item.setCreatedDate(_tmpCreatedDate);
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
  public LiveData<PantryItem> getItemById(final long id) {
    final String _sql = "SELECT * FROM pantry_items WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return __db.getInvalidationTracker().createLiveData(new String[] {"pantry_items"}, false, new Callable<PantryItem>() {
      @Override
      @Nullable
      public PantryItem call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfIngredientName = CursorUtil.getColumnIndexOrThrow(_cursor, "ingredientName");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfExpiryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "expiryDate");
          final int _cursorIndexOfCreatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "createdDate");
          final PantryItem _result;
          if (_cursor.moveToFirst()) {
            _result = new PantryItem();
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _result.setId(_tmpId);
            final String _tmpIngredientName;
            if (_cursor.isNull(_cursorIndexOfIngredientName)) {
              _tmpIngredientName = null;
            } else {
              _tmpIngredientName = _cursor.getString(_cursorIndexOfIngredientName);
            }
            _result.setIngredientName(_tmpIngredientName);
            final double _tmpQuantity;
            _tmpQuantity = _cursor.getDouble(_cursorIndexOfQuantity);
            _result.setQuantity(_tmpQuantity);
            final String _tmpUnit;
            if (_cursor.isNull(_cursorIndexOfUnit)) {
              _tmpUnit = null;
            } else {
              _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            }
            _result.setUnit(_tmpUnit);
            final String _tmpExpiryDate;
            if (_cursor.isNull(_cursorIndexOfExpiryDate)) {
              _tmpExpiryDate = null;
            } else {
              _tmpExpiryDate = _cursor.getString(_cursorIndexOfExpiryDate);
            }
            _result.setExpiryDate(_tmpExpiryDate);
            final long _tmpCreatedDate;
            _tmpCreatedDate = _cursor.getLong(_cursorIndexOfCreatedDate);
            _result.setCreatedDate(_tmpCreatedDate);
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
  public LiveData<List<PantryItem>> searchItems(final String searchQuery) {
    final String _sql = "SELECT * FROM pantry_items WHERE ingredientName LIKE '%' || ? || '%' ORDER BY ingredientName ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (searchQuery == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, searchQuery);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"pantry_items"}, false, new Callable<List<PantryItem>>() {
      @Override
      @Nullable
      public List<PantryItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfIngredientName = CursorUtil.getColumnIndexOrThrow(_cursor, "ingredientName");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
          final int _cursorIndexOfExpiryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "expiryDate");
          final int _cursorIndexOfCreatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "createdDate");
          final List<PantryItem> _result = new ArrayList<PantryItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PantryItem _item;
            _item = new PantryItem();
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item.setId(_tmpId);
            final String _tmpIngredientName;
            if (_cursor.isNull(_cursorIndexOfIngredientName)) {
              _tmpIngredientName = null;
            } else {
              _tmpIngredientName = _cursor.getString(_cursorIndexOfIngredientName);
            }
            _item.setIngredientName(_tmpIngredientName);
            final double _tmpQuantity;
            _tmpQuantity = _cursor.getDouble(_cursorIndexOfQuantity);
            _item.setQuantity(_tmpQuantity);
            final String _tmpUnit;
            if (_cursor.isNull(_cursorIndexOfUnit)) {
              _tmpUnit = null;
            } else {
              _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
            }
            _item.setUnit(_tmpUnit);
            final String _tmpExpiryDate;
            if (_cursor.isNull(_cursorIndexOfExpiryDate)) {
              _tmpExpiryDate = null;
            } else {
              _tmpExpiryDate = _cursor.getString(_cursorIndexOfExpiryDate);
            }
            _item.setExpiryDate(_tmpExpiryDate);
            final long _tmpCreatedDate;
            _tmpCreatedDate = _cursor.getLong(_cursorIndexOfCreatedDate);
            _item.setCreatedDate(_tmpCreatedDate);
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
  public List<PantryItem> getAllItemsSync() {
    final String _sql = "SELECT * FROM pantry_items";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfIngredientName = CursorUtil.getColumnIndexOrThrow(_cursor, "ingredientName");
      final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
      final int _cursorIndexOfUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "unit");
      final int _cursorIndexOfExpiryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "expiryDate");
      final int _cursorIndexOfCreatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "createdDate");
      final List<PantryItem> _result = new ArrayList<PantryItem>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final PantryItem _item;
        _item = new PantryItem();
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpIngredientName;
        if (_cursor.isNull(_cursorIndexOfIngredientName)) {
          _tmpIngredientName = null;
        } else {
          _tmpIngredientName = _cursor.getString(_cursorIndexOfIngredientName);
        }
        _item.setIngredientName(_tmpIngredientName);
        final double _tmpQuantity;
        _tmpQuantity = _cursor.getDouble(_cursorIndexOfQuantity);
        _item.setQuantity(_tmpQuantity);
        final String _tmpUnit;
        if (_cursor.isNull(_cursorIndexOfUnit)) {
          _tmpUnit = null;
        } else {
          _tmpUnit = _cursor.getString(_cursorIndexOfUnit);
        }
        _item.setUnit(_tmpUnit);
        final String _tmpExpiryDate;
        if (_cursor.isNull(_cursorIndexOfExpiryDate)) {
          _tmpExpiryDate = null;
        } else {
          _tmpExpiryDate = _cursor.getString(_cursorIndexOfExpiryDate);
        }
        _item.setExpiryDate(_tmpExpiryDate);
        final long _tmpCreatedDate;
        _tmpCreatedDate = _cursor.getLong(_cursorIndexOfCreatedDate);
        _item.setCreatedDate(_tmpCreatedDate);
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
