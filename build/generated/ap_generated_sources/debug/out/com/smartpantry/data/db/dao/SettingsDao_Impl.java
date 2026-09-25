package com.smartpantry.data.db.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.smartpantry.data.db.entity.Settings;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class SettingsDao_Impl implements SettingsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Settings> __insertionAdapterOfSettings;

  private final EntityDeletionOrUpdateAdapter<Settings> __updateAdapterOfSettings;

  public SettingsDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSettings = new EntityInsertionAdapter<Settings>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `settings` (`id`,`expiryNotificationsEnabled`,`measurementPreference`) VALUES (?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Settings entity) {
        statement.bindLong(1, entity.getId());
        final int _tmp = entity.isExpiryNotificationsEnabled() ? 1 : 0;
        statement.bindLong(2, _tmp);
        if (entity.getMeasurementPreference() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getMeasurementPreference());
        }
      }
    };
    this.__updateAdapterOfSettings = new EntityDeletionOrUpdateAdapter<Settings>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `settings` SET `id` = ?,`expiryNotificationsEnabled` = ?,`measurementPreference` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Settings entity) {
        statement.bindLong(1, entity.getId());
        final int _tmp = entity.isExpiryNotificationsEnabled() ? 1 : 0;
        statement.bindLong(2, _tmp);
        if (entity.getMeasurementPreference() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getMeasurementPreference());
        }
        statement.bindLong(4, entity.getId());
      }
    };
  }

  @Override
  public void insert(final Settings settings) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfSettings.insert(settings);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Settings settings) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfSettings.handle(settings);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<Settings> getSettings() {
    final String _sql = "SELECT * FROM settings WHERE id = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"settings"}, false, new Callable<Settings>() {
      @Override
      @Nullable
      public Settings call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfExpiryNotificationsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "expiryNotificationsEnabled");
          final int _cursorIndexOfMeasurementPreference = CursorUtil.getColumnIndexOrThrow(_cursor, "measurementPreference");
          final Settings _result;
          if (_cursor.moveToFirst()) {
            _result = new Settings();
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _result.setId(_tmpId);
            final boolean _tmpExpiryNotificationsEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfExpiryNotificationsEnabled);
            _tmpExpiryNotificationsEnabled = _tmp != 0;
            _result.setExpiryNotificationsEnabled(_tmpExpiryNotificationsEnabled);
            final String _tmpMeasurementPreference;
            if (_cursor.isNull(_cursorIndexOfMeasurementPreference)) {
              _tmpMeasurementPreference = null;
            } else {
              _tmpMeasurementPreference = _cursor.getString(_cursorIndexOfMeasurementPreference);
            }
            _result.setMeasurementPreference(_tmpMeasurementPreference);
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
  public Settings getSettingsSync() {
    final String _sql = "SELECT * FROM settings WHERE id = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfExpiryNotificationsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "expiryNotificationsEnabled");
      final int _cursorIndexOfMeasurementPreference = CursorUtil.getColumnIndexOrThrow(_cursor, "measurementPreference");
      final Settings _result;
      if (_cursor.moveToFirst()) {
        _result = new Settings();
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        _result.setId(_tmpId);
        final boolean _tmpExpiryNotificationsEnabled;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfExpiryNotificationsEnabled);
        _tmpExpiryNotificationsEnabled = _tmp != 0;
        _result.setExpiryNotificationsEnabled(_tmpExpiryNotificationsEnabled);
        final String _tmpMeasurementPreference;
        if (_cursor.isNull(_cursorIndexOfMeasurementPreference)) {
          _tmpMeasurementPreference = null;
        } else {
          _tmpMeasurementPreference = _cursor.getString(_cursorIndexOfMeasurementPreference);
        }
        _result.setMeasurementPreference(_tmpMeasurementPreference);
      } else {
        _result = null;
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
