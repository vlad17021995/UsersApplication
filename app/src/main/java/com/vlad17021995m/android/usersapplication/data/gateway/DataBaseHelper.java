package com.vlad17021995m.android.usersapplication.data.gateway;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vlad17021995m.android.usersapplication.data.orm.entity.Lock;
import com.vlad17021995m.android.usersapplication.data.orm.entity.Place;
import com.vlad17021995m.android.usersapplication.data.orm.entity.User;
import com.vlad17021995m.android.usersapplication.data.orm.entity.entity;


public class DataBaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Users";
    public static final String TABLE_USERS = "users";
    public static final String TABLE_PLACES = "places";
    public static final String TABLE_LOCKS = "locks";

    public static final String KEY_ID = "_id";

    //******* users ************
    public static final String KEY_MAIL = "mail";
    public static final String KEY_PASS = "pass";
    public static final String KEY_TYPE = "type";
    public static final String KEY_NAME = "name";
    public static final String KEY_SURNAME = "surname";
    public static final String KEY_FATHER = "father";
    public static final String KEY_NUMBER = "number";
    public static final String KEY_NICK = "nick";
    public static final String KEY_CITY = "city";
    public static final String KEY_AVATAR = "avatar_path";


    //********* places **********
    public static final String KEY_HEADER = "header";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_POS = "position";
    public static final String KEY_USER_ID = "user_id";


    //********* locks ***********
    public static final String KEY_TIME_START = "time_start";
    public static final String KEY_TIME_END = "time_end";
    public static final String KEY_REASON = "reason";


    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DataBaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_USERS + " (" + KEY_ID + " integer primary key, "
        + KEY_MAIL + " text, " + KEY_PASS + " text, " + KEY_TYPE + " text, "
        + KEY_NAME + " text, " + KEY_SURNAME + " text, " + KEY_FATHER + " text, "
        + KEY_NUMBER + " text, " + KEY_NICK + " text, " + KEY_CITY + " text, "
        + KEY_AVATAR + " text)");
        db.execSQL("create table " + TABLE_PLACES + " (" + KEY_ID + " integer primary key, "
        + KEY_HEADER + " text, " + KEY_DESCRIPTION + " text, " + KEY_CITY + " text, "
        + KEY_POS + " text, " + KEY_AVATAR + " text, " + KEY_USER_ID + " integer)");
        db.execSQL("create table " + TABLE_LOCKS + " (" + KEY_ID + " integer primary key, "
        + KEY_TIME_START + " text, " + KEY_TIME_END + " text, " + KEY_REASON + " text, "
        + KEY_USER_ID + " integer)");
    }

    public void saveEntity(entity e){
        if (e instanceof User){
            User user = (User)e;
            ContentValues cv = new ContentValues();
            cv.put(KEY_MAIL, user.getMail());
            cv.put(KEY_PASS, user.getPass());
            cv.put(KEY_TYPE, user.getType());
            cv.put(KEY_NAME, user.getName());
            cv.put(KEY_SURNAME, user.getSurname());
            cv.put(KEY_FATHER, user.getFather());
            cv.put(KEY_NUMBER, user.getNumber());
            cv.put(KEY_NICK, user.getNickname());
            cv.put(KEY_CITY, user.getCity());
            cv.put(KEY_AVATAR, user.getAvatar_path());
            getWritableDatabase().insert(TABLE_USERS, null, cv);
        }else if (e instanceof Place){
            Place place = (Place)e;
            ContentValues cv = new ContentValues();
            cv.put(KEY_HEADER, place.getHeader());
            cv.put(KEY_DESCRIPTION, place.getDescription());
            cv.put(KEY_CITY, place.getCity());
            cv.put(KEY_POS, place.getCoordinates());
            cv.put(KEY_AVATAR, place.getAvatar_path());
            cv.put(KEY_USER_ID, place.getUser());
            getWritableDatabase().insert(TABLE_PLACES, null, cv);
        }else if (e instanceof Lock){
            Lock lock = (Lock)e;
            ContentValues cv = new ContentValues();
            cv.put(KEY_TIME_START, lock.getTime_start());
            cv.put(KEY_TIME_END, lock.getTime_end());
            cv.put(KEY_REASON, lock.getReason());
            cv.put(KEY_USER_ID, lock.getUser());
            getWritableDatabase().insert(TABLE_LOCKS, null, cv);
        }
    }

    public void deleteEntity(entity e){
        if (e instanceof User){
            User user = (User)e;
            getWritableDatabase().delete(TABLE_USERS, KEY_ID + " = " + user.getId(), null);
        }else if (e instanceof Place){
            Place place = (Place)e;
            getWritableDatabase().delete(TABLE_PLACES, KEY_ID + " = " + place.getId(), null);
        }else if (e instanceof Lock){
            Lock lock = (Lock)e;
            getWritableDatabase().delete(TABLE_LOCKS, KEY_ID + " = " + lock.getId(), null);
        }
    }

    public void updateEntity(entity e){
        if (e instanceof User){
            User user = (User)e;
            ContentValues cv = new ContentValues();
            cv.put(KEY_MAIL, user.getMail());
            cv.put(KEY_PASS, user.getPass());
            cv.put(KEY_TYPE, user.getType());
            cv.put(KEY_NAME, user.getName());
            cv.put(KEY_SURNAME, user.getSurname());
            cv.put(KEY_FATHER, user.getFather());
            cv.put(KEY_NUMBER, user.getNumber());
            cv.put(KEY_NICK, user.getNickname());
            cv.put(KEY_CITY, user.getCity());
            cv.put(KEY_AVATAR, user.getAvatar_path());
            getWritableDatabase().update(TABLE_USERS, cv, KEY_ID + " = " + user.getId(), null);
        }else if (e instanceof Place){
            Place place = (Place)e;
            ContentValues cv = new ContentValues();
            cv.put(KEY_HEADER, place.getHeader());
            cv.put(KEY_DESCRIPTION, place.getDescription());
            cv.put(KEY_CITY, place.getCity());
            cv.put(KEY_POS, place.getCoordinates());
            cv.put(KEY_AVATAR, place.getAvatar_path());
            cv.put(KEY_USER_ID, place.getUser());
            getWritableDatabase().update(TABLE_PLACES, cv, KEY_ID + " = " + place.getId(), null);
        }else if (e instanceof Lock){
            Lock lock = (Lock)e;
            ContentValues cv = new ContentValues();
            cv.put(KEY_TIME_START, lock.getTime_start());
            cv.put(KEY_TIME_END, lock.getTime_end());
            cv.put(KEY_REASON, lock.getReason());
            cv.put(KEY_USER_ID, lock.getUser());
            getWritableDatabase().update(TABLE_LOCKS, cv, KEY_ID + " = " + lock.getId(), null);
        }
    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
