package com.vlad17021995m.android.usersapplication.fragments.view;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vlad17021995m.android.usersapplication.R;
import com.vlad17021995m.android.usersapplication.activities.LoginActivity;
import com.vlad17021995m.android.usersapplication.activities.PlaceActivity;
import com.vlad17021995m.android.usersapplication.activities.UsersActivity;
import com.vlad17021995m.android.usersapplication.activities.ViewActivity;
import com.vlad17021995m.android.usersapplication.data.gateway.LocalStorage;
import com.vlad17021995m.android.usersapplication.data.orm.entity.User;
import com.vlad17021995m.android.usersapplication.data.orm.service.UserService;
import com.vlad17021995m.android.usersapplication.fragments.users.BanUserFragment;

public class ViewFragment extends Fragment implements View.OnClickListener {

    private Button exit_butt, places_butt, edit_butt;
    private ImageView avatar_view;
    private TextView nick_text_view, mail_text_view, name_text_view, surname_text_view,
                     father_text_view, city_text_view, number_text_view;

    public ViewFragment() {
    }
    public static ViewFragment newInstance() {
        ViewFragment fragment = new ViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view, container, false);
        exit_butt = (Button)view.findViewById(R.id.exit_butt);
        places_butt = (Button)view.findViewById(R.id.places_butt);
        edit_butt = (Button)view.findViewById(R.id.edit_butt);
        avatar_view = (ImageView)view.findViewById(R.id.avatar_view);
        nick_text_view = (TextView)view.findViewById(R.id.nick_text_view);
        mail_text_view = (TextView)view.findViewById(R.id.mail_text_view);
        name_text_view = (TextView)view.findViewById(R.id.name_text_view);
        surname_text_view = (TextView)view.findViewById(R.id.surname_text_view);
        father_text_view = (TextView)view.findViewById(R.id.father_text_view);
        city_text_view = (TextView)view.findViewById(R.id.city_text_view);
        number_text_view = (TextView)view.findViewById(R.id.number_text_view);
        exit_butt.setOnClickListener(this);
        places_butt.setOnClickListener(this);
        edit_butt.setOnClickListener(this);
        updateData();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.exit_butt:
                if (((ViewActivity)(getActivity())).getUser() != null){
                    Intent intent = new Intent(getActivity(), UsersActivity.class);
                    startActivity(intent);
                }else {
                    LocalStorage storage = LocalStorage.getInstance(getActivity());
                    storage.setCurrentUser("");
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.places_butt:
                if (((ViewActivity)(getActivity())).getUser() != null){
                    BanUserFragment fragment = BanUserFragment.newInstance(((ViewActivity)(getActivity())).getUser().getMail());
                    fragment.show(getActivity().getSupportFragmentManager(), "ban_user");
                }else {
                    Intent intent = new Intent(getActivity(), PlaceActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.edit_butt:
                EditUserFragment fragment = EditUserFragment.newInstance();
                fragment.setFragment(this);
                fragment.setUser(LocalStorage.getInstance(getActivity()).currentUser());
                fragment.show(getActivity().getSupportFragmentManager(), "edit_user_fragment");
                break;
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void updateData(){
        LocalStorage storage = LocalStorage.getInstance(getActivity());
        User user = null;
        if (((ViewActivity)(getActivity())).getUser() != null){
            user = ((ViewActivity)(getActivity())).getUser();
        }else {
            user = storage.currentUser();
        }
        String path = getPath(Uri.parse(user.getAvatar_path()));
        avatar_view.setImageURI(Uri.parse(path));
        nick_text_view.setText(user.getNickname());
        mail_text_view.setText(user.getMail());
        name_text_view.setText(user.getName());
        surname_text_view.setText(user.getSurname());
        father_text_view.setText(user.getFather());
        city_text_view.setText(user.getCity());
        number_text_view.setText(user.getNumber());
        if (((ViewActivity)(getActivity())).getUser() != null){
            exit_butt.setText("список пользователей");
            places_butt.setText("забанить");
            edit_butt.setVisibility(View.INVISIBLE);
            mail_text_view.setVisibility(View.INVISIBLE);
        }
    }
}
