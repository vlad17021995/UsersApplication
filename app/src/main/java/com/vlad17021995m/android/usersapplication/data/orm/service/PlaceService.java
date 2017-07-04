package com.vlad17021995m.android.usersapplication.data.orm.service;

import android.content.Context;
import android.database.Cursor;

import com.vlad17021995m.android.usersapplication.data.gateway.DataBaseHelper;
import com.vlad17021995m.android.usersapplication.data.orm.entity.Place;
import com.vlad17021995m.android.usersapplication.data.orm.entity.User;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qwerty on 21.06.2017.
 */

public class PlaceService {
    private DataBaseHelper helper;

    public PlaceService(Context context){
        helper = new DataBaseHelper(context);
    }

    public List<Place> getPlacesByUser(User user){
        List<Place> places = new ArrayList<>();
        Cursor cursor = helper.getReadableDatabase().query(DataBaseHelper.TABLE_PLACES,
                null, DataBaseHelper.KEY_USER_ID + " = " + user.getId(), null, null, null, null);
        if (cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(DataBaseHelper.KEY_ID);
            int headerIndex = cursor.getColumnIndex(DataBaseHelper.KEY_HEADER);
            int descriptionIndex = cursor.getColumnIndex(DataBaseHelper.KEY_DESCRIPTION);
            int cityIndex = cursor.getColumnIndex(DataBaseHelper.KEY_CITY);
            int positionIndex = cursor.getColumnIndex(DataBaseHelper.KEY_POS);
            int avatarIndex = cursor.getColumnIndex(DataBaseHelper.KEY_AVATAR);
            int userIndex = cursor.getColumnIndex(DataBaseHelper.KEY_USER_ID);
            do {
                Place place = new Place();
                place.setId(cursor.getInt(idIndex));
                place.setHeader(cursor.getString(headerIndex));
                place.setDescription(cursor.getString(descriptionIndex));
                place.setCity(cursor.getString(cityIndex));
                place.setCoordinates(cursor.getString(positionIndex));
                place.setAvatar_path(cursor.getString(avatarIndex));
                place.setUser(cursor.getInt(userIndex));
                places.add(place);
            }while (cursor.moveToNext());
            cursor.close();
        }
        return places;
    }

    public Place getById(int id){
        Cursor cursor = helper.getReadableDatabase().query(DataBaseHelper.TABLE_PLACES,
                null, DataBaseHelper.KEY_ID + " = " + id, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DataBaseHelper.KEY_ID);
            int headerIndex = cursor.getColumnIndex(DataBaseHelper.KEY_HEADER);
            int descriptionIndex = cursor.getColumnIndex(DataBaseHelper.KEY_DESCRIPTION);
            int cityIndex = cursor.getColumnIndex(DataBaseHelper.KEY_CITY);
            int positionIndex = cursor.getColumnIndex(DataBaseHelper.KEY_POS);
            int avatarIndex = cursor.getColumnIndex(DataBaseHelper.KEY_AVATAR);
            int userIndex = cursor.getColumnIndex(DataBaseHelper.KEY_USER_ID);
            Place place = new Place();
            place.setId(cursor.getInt(idIndex));
            place.setHeader(cursor.getString(headerIndex));
            place.setDescription(cursor.getString(descriptionIndex));
            place.setCity(cursor.getString(cityIndex));
            place.setCoordinates(cursor.getString(positionIndex));
            place.setAvatar_path(cursor.getString(avatarIndex));
            place.setUser(cursor.getInt(userIndex));
            cursor.close();
            return place;
        }
        return null;
    }

    public void addPlace(Place place){
        helper.saveEntity(place);
    }

    public void deletePlace(Place place){
        helper.deleteEntity(place);
    }

    public void updatePlace(Place place){
        helper.updateEntity(place);
    }
}
