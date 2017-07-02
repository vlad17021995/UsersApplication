package com.vlad17021995m.android.usersapplication.fragments.users.dummy;

import android.content.Context;

import com.vlad17021995m.android.usersapplication.data.gateway.LocalStorage;
import com.vlad17021995m.android.usersapplication.data.orm.entity.User;

import java.util.List;

public class DummyContent {
    public static List<User> getUsersWithoutCurrent(Context context){
        LocalStorage storage = LocalStorage.getInstance(context);
        return storage.getAllUsers();
    }
}
