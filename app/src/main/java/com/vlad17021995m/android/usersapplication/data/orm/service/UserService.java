package com.vlad17021995m.android.usersapplication.data.orm.service;

import android.content.Context;
import android.database.Cursor;

import com.vlad17021995m.android.usersapplication.data.gateway.DataBaseHelper;
import com.vlad17021995m.android.usersapplication.data.orm.entity.User;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qwerty on 21.06.2017.
 */

public class UserService {
    private DataBaseHelper helper;

    public UserService(Context context){
        helper = new DataBaseHelper(context);
    }

    public void registerUser(User user){
        helper.saveEntity(user);
    }

    public User getUser(String login, String pass){
        String selection = "";
        if (pass == null){
            selection = DataBaseHelper.KEY_MAIL + " = '" + login + "'";
        }else {
            selection = DataBaseHelper.KEY_MAIL + " = '" + login + "' AND " + DataBaseHelper.KEY_PASS
                    + " = '" + pass + "'";
        }
        Cursor cursor = helper.getReadableDatabase().query(DataBaseHelper.TABLE_USERS, null,
                selection, null, null, null, null);
        if (cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(DataBaseHelper.KEY_ID);
            int mailIndex = cursor.getColumnIndex(DataBaseHelper.KEY_MAIL);
            int passIndex = cursor.getColumnIndex(DataBaseHelper.KEY_PASS);
            int typeIndex = cursor.getColumnIndex(DataBaseHelper.KEY_TYPE);
            int nameIndex = cursor.getColumnIndex(DataBaseHelper.KEY_NAME);
            int surnameIndex = cursor.getColumnIndex(DataBaseHelper.KEY_SURNAME);
            int fatherIndex = cursor.getColumnIndex(DataBaseHelper.KEY_FATHER);
            int numberIndex = cursor.getColumnIndex(DataBaseHelper.KEY_NUMBER);
            int nickIndex = cursor.getColumnIndex(DataBaseHelper.KEY_NICK);
            int cityIndex = cursor.getColumnIndex(DataBaseHelper.KEY_CITY);
            int avatarIndex = cursor.getColumnIndex(DataBaseHelper.KEY_AVATAR);
            User user = new User();
            user.setId(cursor.getInt(idIndex));
            user.setMail(cursor.getString(mailIndex));
            user.setPass(cursor.getString(passIndex));
            user.setType(cursor.getString(typeIndex));
            user.setName(cursor.getString(nameIndex));
            user.setSurname(cursor.getString(surnameIndex));
            user.setFather(cursor.getString(fatherIndex));
            user.setNumber(cursor.getString(numberIndex));
            user.setNickname(cursor.getString(nickIndex));
            user.setCity(cursor.getString(cityIndex));
            user.setAvatar_path(cursor.getString(avatarIndex));
            cursor.close();
            return user;
        }
        return null;
    }

    public List<User> getAll(){
        List<User> users = new ArrayList<>();
        Cursor cursor = helper.getReadableDatabase().query(DataBaseHelper.TABLE_USERS,
                null, null, null, null, null, null);
        if (cursor.moveToFirst()){
            do {
                int idIndex = cursor.getColumnIndex(DataBaseHelper.KEY_ID);
                int mailIndex = cursor.getColumnIndex(DataBaseHelper.KEY_MAIL);
                int passIndex = cursor.getColumnIndex(DataBaseHelper.KEY_PASS);
                int typeIndex = cursor.getColumnIndex(DataBaseHelper.KEY_TYPE);
                int nameIndex = cursor.getColumnIndex(DataBaseHelper.KEY_NAME);
                int surnameIndex = cursor.getColumnIndex(DataBaseHelper.KEY_SURNAME);
                int fatherIndex = cursor.getColumnIndex(DataBaseHelper.KEY_FATHER);
                int numberIndex = cursor.getColumnIndex(DataBaseHelper.KEY_NUMBER);
                int nickIndex = cursor.getColumnIndex(DataBaseHelper.KEY_NICK);
                int cityIndex = cursor.getColumnIndex(DataBaseHelper.KEY_CITY);
                int avatarIndex = cursor.getColumnIndex(DataBaseHelper.KEY_AVATAR);
                User user = new User();
                user.setId(cursor.getInt(idIndex));
                user.setMail(cursor.getString(mailIndex));
                user.setPass(cursor.getString(passIndex));
                user.setType(cursor.getString(typeIndex));
                user.setName(cursor.getString(nameIndex));
                user.setSurname(cursor.getString(surnameIndex));
                user.setFather(cursor.getString(fatherIndex));
                user.setNumber(cursor.getString(numberIndex));
                user.setNickname(cursor.getString(nickIndex));
                user.setCity(cursor.getString(cityIndex));
                user.setAvatar_path(cursor.getString(avatarIndex));
                users.add(user);
            }while (cursor.moveToNext());
            cursor.close();
        }
        return users;
    }

    public void updateUser(User user){
        helper.updateEntity(user);
    }

    public void deleteUser(User user){
        helper.getWritableDatabase().delete(DataBaseHelper.TABLE_PLACES,
                DataBaseHelper.KEY_USER_ID + " = " + user.getId(), null);
        helper.getWritableDatabase().delete(DataBaseHelper.TABLE_LOCKS,
                DataBaseHelper.KEY_USER_ID + " = " + user.getId(), null);
        helper.deleteEntity(user);
    }
}
