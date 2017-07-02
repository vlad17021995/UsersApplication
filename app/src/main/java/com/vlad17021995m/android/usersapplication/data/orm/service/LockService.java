package com.vlad17021995m.android.usersapplication.data.orm.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.vlad17021995m.android.usersapplication.data.gateway.DataBaseHelper;
import com.vlad17021995m.android.usersapplication.data.orm.entity.Lock;
import com.vlad17021995m.android.usersapplication.data.orm.entity.User;

import java.sql.SQLException;

/**
 * Created by qwerty on 21.06.2017.
 */

public class LockService {
    private DataBaseHelper helper;

    public LockService(Context context){
        helper = new DataBaseHelper(context);
    }

    public void banUser(Lock lock){
        helper.getWritableDatabase().delete(DataBaseHelper.TABLE_LOCKS,
                DataBaseHelper.KEY_USER_ID + " = " + lock.getUser(), null);
        helper.saveEntity(lock);
    }

    public Lock getLockByUser(User user){
        Cursor cursor = helper.getReadableDatabase().query(DataBaseHelper.TABLE_LOCKS,
                null, DataBaseHelper.KEY_USER_ID + " = " + user.getId(), null, null,
                null, null);
        if (cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(DataBaseHelper.KEY_ID);
            int startIndex = cursor.getColumnIndex(DataBaseHelper.KEY_TIME_START);
            int endIndex = cursor.getColumnIndex(DataBaseHelper.KEY_TIME_END);
            int reasonIndex = cursor.getColumnIndex(DataBaseHelper.KEY_REASON);
            int userIndex = cursor.getColumnIndex(DataBaseHelper.KEY_USER_ID);
            Lock lock = new Lock();
            lock.setId(cursor.getInt(idIndex));
            lock.setTime_start(cursor.getString(startIndex));
            lock.setTime_end(cursor.getString(endIndex));
            lock.setReason(cursor.getString(reasonIndex));
            lock.setUser(cursor.getInt(userIndex));
            cursor.close();
            return lock;
        }
        return null;
    }

}
