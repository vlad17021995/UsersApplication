package com.vlad17021995m.android.usersapplication.fragments.register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.vlad17021995m.android.usersapplication.R;
import com.vlad17021995m.android.usersapplication.activities.RegisterActivity;
import com.vlad17021995m.android.usersapplication.data.gateway.LocalStorage;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static com.vlad17021995m.android.usersapplication.activities.RegisterActivity.SELECT_PHOTO;


public class RegisterFragment extends Fragment implements View.OnClickListener, RegisterActivity.ILoadImageListener {

    public static final String REGISTER_LOGGER = "REGISTER_LOGGER";

    private EditText mail_edit_text, pass_edit_text, conf_pass_edit_text,
            name_edit_text, surname_edit_text, father_edit_text,
            number_edit_text, nick_edit_text, city_edit_text;
    private ImageView avatar_view;
    private Button get_pos_butt, load_image_butt, register_butt;

    private LocationManager locationManager;

    private Uri imageUri;


    public RegisterFragment() {
    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        mail_edit_text = (EditText) view.findViewById(R.id.mail_edit_text);
        pass_edit_text = (EditText) view.findViewById(R.id.pass_edit_text);
        conf_pass_edit_text = (EditText) view.findViewById(R.id.conf_pass_edit_text);
        name_edit_text = (EditText) view.findViewById(R.id.name_edit_text);
        surname_edit_text = (EditText) view.findViewById(R.id.surname_edit_text);
        father_edit_text = (EditText) view.findViewById(R.id.father_edit_text);
        number_edit_text = (EditText) view.findViewById(R.id.number_edit_text);
        nick_edit_text = (EditText) view.findViewById(R.id.nick_edit_text);
        city_edit_text = (EditText) view.findViewById(R.id.city_edit_text);
        avatar_view = (ImageView) view.findViewById(R.id.avatar_view);
        get_pos_butt = (Button) view.findViewById(R.id.get_pos_butt);
        load_image_butt = (Button) view.findViewById(R.id.load_image_butt);
        register_butt = (Button) view.findViewById(R.id.update_butt);
        get_pos_butt.setOnClickListener(this);
        register_butt.setOnClickListener(this);
        load_image_butt.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

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
        switch (v.getId()) {
            case R.id.get_pos_butt:
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 10, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 10, 10, locationListener);
                break;
            case R.id.load_image_butt:
                takeAvatar();
                break;
            case R.id.update_butt:
                registerNewUser();
                break;

        }
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //city_edit_text.setText(location.getLatitude() + " : " + location.getLongitude());
            locationManager.removeUpdates(locationListener);
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addresses != null) {
                    Address address = addresses.get(0);
                    city_edit_text.setText(address.getAddressLine(1));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    public void onLoadImage(Uri uri) {
        avatar_view.setImageURI(uri);
        this.imageUri = uri;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    Log.d(RegisterFragment.REGISTER_LOGGER, " 1 : " + selectedImage.toString());
                    onLoadImage(selectedImage);
                }
                break;
        }
    }

    public void takeAvatar(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    public boolean checkNotEmptyField(){
        boolean result = true;
        result = result && (mail_edit_text.getText().toString().length() != 0);
        result = result && (pass_edit_text.getText().toString().length() != 0);
        result = result && (name_edit_text.getText().toString().length() != 0);
        result = result && (father_edit_text.getText().toString().length() != 0);
        result = result && (surname_edit_text.getText().toString().length() != 0);
        result = result && (number_edit_text.getText().toString().length() != 0);
        result = result && (nick_edit_text.getText().toString().length() != 0);
        result = result && (city_edit_text.getText().toString().length() != 0);
        return result;
    }

    public void registerNewUser(){
        if (!LocalStorage.isEmailValid(mail_edit_text.getText().toString())){
            Toast.makeText(getActivity(), "не валидный почтовый адрес", Toast.LENGTH_SHORT).show();
            return;
        }
        if(pass_edit_text.getText().toString().length() < 4){
            Toast.makeText(getActivity(), "пароль слишко короткий", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!pass_edit_text.getText().toString().equals(conf_pass_edit_text.getText().toString())){
            Toast.makeText(getActivity(), "пароли не совпадают", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!checkNotEmptyField()){
            Toast.makeText(getActivity(), "все поля обьязательны к заполнению", Toast.LENGTH_SHORT).show();
            return;
        }
        LocalStorage storage = LocalStorage.getInstance(getActivity());
        String path = (imageUri == null) ? "" : imageUri.toString();
        int register_result = storage.registerAccount(mail_edit_text.getText().toString(), pass_edit_text.getText().toString(),
                name_edit_text.getText().toString(), surname_edit_text.getText().toString(), father_edit_text.getText().toString(),
                number_edit_text.getText().toString(), nick_edit_text.getText().toString(), city_edit_text.getText().toString(),
                path);
        switch (register_result){
            case 0:
                Toast.makeText(getActivity(), "пользователь зарегистрирован", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(getActivity(), "пользователь с такой почтой уже существует", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
