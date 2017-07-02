package com.vlad17021995m.android.usersapplication.fragments.place.dummy;

import android.app.Activity;

import com.vlad17021995m.android.usersapplication.data.gateway.LocalStorage;
import com.vlad17021995m.android.usersapplication.data.orm.entity.Place;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DummyContent {
    public static List<Place> getPlaces(Activity activity){
        return LocalStorage.getInstance(activity).getPlaces();
    }
}
