package com.mba.logic.database_lib.coroutine;

import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.mba.logic.database_lib.TSModel;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;

@SuppressWarnings({"unchecked", "deprecation"})
public final class HLSDDao_Impl extends HLSDDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfTSModel;

  public HLSDDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTSModel = new EntityInsertionAdapter<TSModel>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `TSModel`(`id`,`hlsUrl`,`tsUrl`,`localPath`,`size`,`state`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TSModel value) {
        stmt.bindLong(1, value.getId());
        if (value.getHlsUrl() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getHlsUrl());
        }
        if (value.getTsUrl() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getTsUrl());
        }
        if (value.getLocalPath() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getLocalPath());
        }
        stmt.bindLong(5, value.getSize());
        stmt.bindLong(6, value.getState());
      }
    };
  }

  @Override
  public void insertTSModel(final TSModel... tsModel) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTSModel.insert(tsModel);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }
}
