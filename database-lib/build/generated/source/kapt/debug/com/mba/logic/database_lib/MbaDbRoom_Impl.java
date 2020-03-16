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
import com.mba.logic.database_lib.coroutine.HDLDao;
import com.mba.logic.database_lib.coroutine.HDLDao_Impl;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class MbaDbRoom_Impl extends MbaDbRoom {
  private volatile HDLDao _hDLDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `TSEntity` (`tsUrl` TEXT NOT NULL, `hlsUrl` TEXT NOT NULL, `localPath` TEXT NOT NULL, `size` INTEGER NOT NULL, `state` INTEGER NOT NULL, `uuid` TEXT NOT NULL, PRIMARY KEY(`tsUrl`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `HDLEntity` (`uuid` TEXT NOT NULL, `hlsUrl` TEXT NOT NULL, `extraEntity` TEXT NOT NULL, `localDir` TEXT NOT NULL, `state` INTEGER NOT NULL, `updateTime` INTEGER NOT NULL, PRIMARY KEY(`uuid`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"1b83a3da4a611edd73628d6f09b96092\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `TSEntity`");
        _db.execSQL("DROP TABLE IF EXISTS `HDLEntity`");
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
        final HashMap<String, TableInfo.Column> _columnsTSEntity = new HashMap<String, TableInfo.Column>(6);
        _columnsTSEntity.put("tsUrl", new TableInfo.Column("tsUrl", "TEXT", true, 1));
        _columnsTSEntity.put("hlsUrl", new TableInfo.Column("hlsUrl", "TEXT", true, 0));
        _columnsTSEntity.put("localPath", new TableInfo.Column("localPath", "TEXT", true, 0));
        _columnsTSEntity.put("size", new TableInfo.Column("size", "INTEGER", true, 0));
        _columnsTSEntity.put("state", new TableInfo.Column("state", "INTEGER", true, 0));
        _columnsTSEntity.put("uuid", new TableInfo.Column("uuid", "TEXT", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTSEntity = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTSEntity = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTSEntity = new TableInfo("TSEntity", _columnsTSEntity, _foreignKeysTSEntity, _indicesTSEntity);
        final TableInfo _existingTSEntity = TableInfo.read(_db, "TSEntity");
        if (! _infoTSEntity.equals(_existingTSEntity)) {
          throw new IllegalStateException("Migration didn't properly handle TSEntity(com.mba.logic.database_lib.TSEntity).\n"
                  + " Expected:\n" + _infoTSEntity + "\n"
                  + " Found:\n" + _existingTSEntity);
        }
        final HashMap<String, TableInfo.Column> _columnsHDLEntity = new HashMap<String, TableInfo.Column>(6);
        _columnsHDLEntity.put("uuid", new TableInfo.Column("uuid", "TEXT", true, 1));
        _columnsHDLEntity.put("hlsUrl", new TableInfo.Column("hlsUrl", "TEXT", true, 0));
        _columnsHDLEntity.put("extraEntity", new TableInfo.Column("extraEntity", "TEXT", true, 0));
        _columnsHDLEntity.put("localDir", new TableInfo.Column("localDir", "TEXT", true, 0));
        _columnsHDLEntity.put("state", new TableInfo.Column("state", "INTEGER", true, 0));
        _columnsHDLEntity.put("updateTime", new TableInfo.Column("updateTime", "INTEGER", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysHDLEntity = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesHDLEntity = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoHDLEntity = new TableInfo("HDLEntity", _columnsHDLEntity, _foreignKeysHDLEntity, _indicesHDLEntity);
        final TableInfo _existingHDLEntity = TableInfo.read(_db, "HDLEntity");
        if (! _infoHDLEntity.equals(_existingHDLEntity)) {
          throw new IllegalStateException("Migration didn't properly handle HDLEntity(com.mba.logic.database_lib.HDLEntity).\n"
                  + " Expected:\n" + _infoHDLEntity + "\n"
                  + " Found:\n" + _existingHDLEntity);
        }
      }
    }, "1b83a3da4a611edd73628d6f09b96092", "4f9c978bff67d1ed71a64548b8ad8fa6");
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
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "TSEntity","HDLEntity");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `TSEntity`");
      _db.execSQL("DELETE FROM `HDLEntity`");
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
  public HDLDao cMbaDao() {
    if (_hDLDao != null) {
      return _hDLDao;
    } else {
      synchronized(this) {
        if(_hDLDao == null) {
          _hDLDao = new HDLDao_Impl(this);
        }
        return _hDLDao;
      }
    }
  }
}
