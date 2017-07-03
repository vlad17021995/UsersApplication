package com.vlad17021995m.android.usersapplication.fragments.view;

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
import android.support.v4.app.DialogFragment;
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
import com.vlad17021995m.android.usersapplication.data.orm.entity.User;
import com.vlad17021995m.android.usersapplication.fragments.register.RegisterFragment;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static com.vlad17021995m.android.usersapplication.activities.RegisterActivity.SELECT_PHOTO;


public class EditUserFragment extends DialogFragment implements View.OnClickListener, RegisterActivity.ILoadImageListener {

    private ViewFragment fragment;

    private EditText mail_edit_text, pass_edit_text, conf_pass_edit_text,
            name_edit_text, surname_edit_text, father_edit_text,
            number_edit_text, nick_edit_text, city_edit_text;
    private ImageView avatar_view;
    private Button get_pos_butt, load_image_butt, update_butt;

    private LocationManager locationManager;

    private Uri imageUri;

    private User user;

    public EditUserFragment() {

    }

    public static EditUserFragment newInstance() {
        EditUserFragment fragment = new EditUserFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_NoTitleBar_Fullscreen);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_user, container, false);
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
        update_butt = (Button) view.findViewById(R.id.update_butt);
        get_pos_butt.setOnClickListener(this);
        update_butt.setOnClickListener(this);
        load_image_butt.setOnClickListener(this);
        if (user != null){
            mail_edit_text.setText(user.getMail());
            name_edit_text.setText(user.getName());
            surname_edit_text.setText(user.getSurname());
            father_edit_text.setText(user.getFather());
            number_edit_text.setText(user.getNumber());
            nick_edit_text.setText(user.getNickname());
            city_edit_text.setText(user.getCity());
        }
        return view;
    }

    @Override
    public void onPause() {
        locationManager.removeUpdates(locationListener);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 10, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 10, 10, locationListener);
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //city_edit_text.setText(location.getLatitude() + " : " + location.getLongitude());
            locationManager.removeUpdates(locationListener);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addresses != null) {
                            final Address address = addresses.get(0);
                            if (city_edit_text != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        city_edit_text.setText(address.getAddressLine(1));
                                    }
                                });
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public ViewFragment getFragment() {
        return fragment;
    }

    public void setFragment(ViewFragment fragment) {
        this.fragment = fragment;
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
                updateCurrentUser();
                break;

        }
    }

    public void takeAvatar(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    @Override
    public void onLoadImage(Uri uri) {
        this.imageUri = uri;
        avatar_view.setImageURI(uri);
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

    public boolean checkNotEmptyField(){
        boolean result = true;
        result = result && (mail_edit_text.getText().toString().length() != 0);
        result = result && (name_edit_text.getText().toString().length() != 0);
        result = result && (father_edit_text.getText().toString().length() != 0);
        result = result && (surname_edit_text.getText().toString().length() != 0);
        result = result && (number_edit_text.getText().toString().length() != 0);
        result = result && (nick_edit_text.getText().toString().length() != 0);
        result = result && (city_edit_text.getText().toString().length() != 0);
        return result;
    }

    public void updateCurrentUser(){
        if (!LocalStorage.isEmailValid(mail_edit_text.getText().toString())){
            Toast.makeText(getActivity(), "не валидный почтовый адрес", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pass_edit_text.getText().toString().equals(conf_pass_edit_text.getText().toString())){
            Toast.makeText(getActivity(), "пароли не совпадают", Toast.LENGTH_SHORT).show();
            return;
        }else {
            if (pass_edit_text.getText().toString().length() == 0){

            }else {
                if (pass_edit_text.getText().toString().length() < 4) {
                    Toast.makeText(getActivity(), "пароль слишко короткий", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        if (!checkNotEmptyField()){
            Toast.makeText(getActivity(), "все поля обьязательны к заполнению", Toast.LENGTH_SHORT).show();
            return;
        }
        String path = (imageUri == null) ? user.getAvatar_path() : imageUri.toString();

        user.setName(name_edit_text.getText().toString());
        user.setSurname(surname_edit_text.getText().toString());
        user.setFather(father_edit_text.getText().toString());
        if (pass_edit_text.getText().toString().length() != 0){
            user.setPass(LocalStorage.md5Custom(pass_edit_text.getText().toString()));
        }
        user.setMail(mail_edit_text.getText().toString());
        user.setAvatar_path(path);
        user.setNumber(number_edit_text.getText().toString());
        user.setNickname(nick_edit_text.getText().toString());
        user.setCity(city_edit_text.getText().toString());
        LocalStorage storage = LocalStorage.getInstance(getActivity());
        storage.editUser(user);
        fragment.updateData();
        dismiss();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
