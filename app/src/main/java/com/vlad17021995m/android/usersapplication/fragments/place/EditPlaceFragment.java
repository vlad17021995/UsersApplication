package com.vlad17021995m.android.usersapplication.fragments.place;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vlad17021995m.android.usersapplication.R;
import com.vlad17021995m.android.usersapplication.data.gateway.LocalStorage;
import com.vlad17021995m.android.usersapplication.data.orm.entity.Place;

public class EditPlaceFragment extends DialogFragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "id";
    private int id;
    private Place place;

    private ImageView avatar_view;
    private Button show_map_butt;
    private TextView header_text_view, description_text_view, city_text_view;

    public EditPlaceFragment() {
        // Required empty public constructor
    }

    public static EditPlaceFragment newInstance(int param1) {
        EditPlaceFragment fragment = new EditPlaceFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(ARG_PARAM1);
            LocalStorage storage = LocalStorage.getInstance(getActivity());
            place = storage.GetPlaceById(id);
        }
        //setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_NoTitleBar_Fullscreen);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_place, container, false);
        avatar_view = (ImageView) view.findViewById(R.id.avatar_view);
        show_map_butt = (Button) view.findViewById(R.id.show_map_butt);
        header_text_view = (TextView) view.findViewById(R.id.header_text_view);
        description_text_view = (TextView) view.findViewById(R.id.description_text_view);
        city_text_view = (TextView) view.findViewById(R.id.city_text_view);
        avatar_view.setImageURI(Uri.parse(getPath(Uri.parse(place.getAvatar_path()))));
        header_text_view.setText(place.getHeader());
        description_text_view.setText(place.getDescription());
        city_text_view.setText(place.getCity());
        show_map_butt.setOnClickListener(this);
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private String getGeoString(){
        StringBuilder sb = new StringBuilder();
        String pos = place.getCoordinates();
        String []coordinates = pos.split(" ");
        sb.append("geo:" + coordinates[0] + "," + coordinates[1] + "?z=15");
        return sb.toString();
    }

    private void showOnTheMap(){
        Uri geo = Uri.parse(getGeoString());
        Intent geoIntent = new Intent(Intent.ACTION_VIEW, geo);
        startActivity(geoIntent);
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };

        CursorLoader cursorLoader = new CursorLoader(getActivity(), uri, projection, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.show_map_butt:
                showOnTheMap();
                break;
        }
    }
}
