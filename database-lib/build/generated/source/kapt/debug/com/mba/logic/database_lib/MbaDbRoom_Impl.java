package com.mba.logic.database_lib;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import com.mba.logic.database_lib.coroutine.HLSDDao;
import com.mba.logic.database_lib.coroutine.HLSDDao_Impl;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class MbaDbRoom_Impl extends MbaDbRoom {
  private volatile HLSDDao _hLSDDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `TSModel` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `hlsUrl` TEXT NOT NULL, `tsUrl` TEXT NOT NULL, `localPath` TEXT NOT NULL, `size` INTEGER NOT NULL, `state` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"25dba9b7a822ece7bfcd0efbea074e1e\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `TSModel`");
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsTSModel = new HashMap<String, TableInfo.Column>(6);
        _columnsTSModel.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
        _columnsTSModel.put("hlsUrl", new TableInfo.Column("hlsUrl", "TEXT", true, 0));
        _columnsTSModel.put("tsUrl", new TableInfo.Column("tsUrl", "TEXT", true, 0));
        _columnsTSModel.put("localPath", new TableInfo.Column("localPath", "TEXT", true, 0));
        _columnsTSModel.put("size", new TableInfo.Column("size", "INTEGER", true, 0));
        _columnsTSModel.put("state", new TableInfo.Column("state", "INTEGER", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTSModel = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTSModel = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTSModel = new TableInfo("TSModel", _columnsTSModel, _foreignKeysTSModel, _indicesTSModel);
        final TableInfo _existingTSModel = TableInfo.read(_db, "TSModel");
        if (! _infoTSModel.equals(_existingTSModel)) {
          throw new IllegalStateException("Migration didn't properly handle TSModel(com.mba.logic.database_lib.TSModel).\n"
                  + " Expected:\n" + _infoTSModel + "\n"
                  + " Found:\n" + _existingTSModel);
        }
      }
    }, "25dba9b7a822ece7bfcd0efbea074e1e", "44b8ff0f5fefbd0a8cc9abce9c807387");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "TSModel");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `TSModel`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public HLSDDao cMbaDao() {
    if (_hLSDDao != null) {
      return _hLSDDao;
    } else {
      synchronized(this) {
        if(_hLSDDao == null) {
          _hLSDDao = new HLSDDao_Impl(this);
        }
        return _hLSDDao;
      }
    }
  }
}
