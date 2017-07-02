package com.vlad17021995m.android.usersapplication.fragments.place;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.vlad17021995m.android.usersapplication.R;
import com.vlad17021995m.android.usersapplication.activities.RegisterActivity;
import com.vlad17021995m.android.usersapplication.data.gateway.LocalStorage;
import com.vlad17021995m.android.usersapplication.data.orm.entity.Place;
import com.vlad17021995m.android.usersapplication.fragments.register.RegisterFragment;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddPlaceFragment extends DialogFragment implements View.OnClickListener, RegisterActivity.ILoadImageListener {

    public static final int PLACE_PICKER_REQUEST = 1;
    public static final int SELECT_PHOTO = 2;

    private EditText header_edit_text, description_edit_text, city_edit_text, position_edit_text;
    private Button get_pos_butt, load_image_butt, add_place_butt;
    private ImageView avatar_view;

    private Uri imageUri;

    public AddPlaceFragment() {
        // Required empty public constructor
    }
    public static AddPlaceFragment newInstance() {
        AddPlaceFragment fragment = new AddPlaceFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_NoTitleBar_Fullscreen);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_place, container, false);
        header_edit_text = (EditText) view.findViewById(R.id.header_edit_text);
        description_edit_text = (EditText) view.findViewById(R.id.description_edit_text);
        city_edit_text = (EditText) view.findViewById(R.id.city_edit_text);
        position_edit_text = (EditText) view.findViewById(R.id.position_edit_text);
        get_pos_butt = (Button) view.findViewById(R.id.get_pos_butt);
        load_image_butt = (Button) view.findViewById(R.id.load_image_butt);
        add_place_butt = (Button) view.findViewById(R.id.add_place_butt);
        avatar_view = (ImageView) view.findViewById(R.id.avatar_view);
        get_pos_butt.setOnClickListener(this);
        add_place_butt.setOnClickListener(this);
        load_image_butt.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.get_pos_butt:
                selectPos();
                break;
            case R.id.load_image_butt:
                takeAvatar();
                break;
            case R.id.add_place_butt:
                addPlace();
                getActivity().recreate();
                break;
        }
    }

    @Override
    public void onLoadImage(Uri uri) {
        this.imageUri = uri;
        avatar_view.setImageURI(uri);
    }

    private void selectPos(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case PLACE_PICKER_REQUEST:
                    com.google.android.gms.location.places.Place place = PlacePicker.getPlace(data, getActivity());
                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                        if (addresses != null) {
                            Address address = addresses.get(0);
                            city_edit_text.setText(address.getAddressLine(1));
                            position_edit_text.setText(place.getLatLng().latitude + " " + place.getLatLng().longitude);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case SELECT_PHOTO:
                    Uri selectedImage = data.getData();
                    Log.d(RegisterFragment.REGISTER_LOGGER, " 1 : " + selectedImage.toString());
                    onLoadImage(selectedImage);
                    break;
            }
        }
    }

    private void takeAvatar(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    private void addPlace(){
        LocalStorage storage = LocalStorage.getInstance(getActivity());
        Place place = new Place();
        place.setCity(city_edit_text.getText().toString());
        place.setUser(storage.currentUser().getId());
        place.setCoordinates(position_edit_text.getText().toString());
        place.setAvatar_path(imageUri.toString());
        place.setDescription(description_edit_text.getText().toString());
        place.setHeader(header_edit_text.getText().toString());
        storage.addPlace(place);
    }
}
