package com.vlad17021995m.android.usersapplication.fragments.users;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vlad17021995m.android.usersapplication.R;
import com.vlad17021995m.android.usersapplication.data.gateway.LocalStorage;
import com.vlad17021995m.android.usersapplication.data.orm.entity.Lock;
import com.vlad17021995m.android.usersapplication.data.orm.entity.User;

import java.util.Date;

public class BanUserFragment extends DialogFragment implements View.OnClickListener {

    private String  login;

    private User user;

    public static final String KEY_LOGIN = "user_login";

    private TextView user_name_view, time_ban;
    private EditText reason_edit_text;
    private Button ban_butt;

    public BanUserFragment() {
        // Required empty public constructor
    }
    public static BanUserFragment newInstance(String login) {
        BanUserFragment fragment = new BanUserFragment();
        Bundle arguments = new Bundle();
        arguments.putString(KEY_LOGIN, login);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            login = getArguments().getString(KEY_LOGIN);
        }
        LocalStorage storage = LocalStorage.getInstance(getActivity());
        user = storage.getUserByLogin(login);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ban_user, container, false);
        user_name_view = (TextView) view.findViewById(R.id.user_name_view);
        time_ban = (TextView) view.findViewById(R.id.time_ban);
        reason_edit_text = (EditText) view.findViewById(R.id.reason_edit_text);
        ban_butt = (Button) view.findViewById(R.id.ban_butt);
        ban_butt.setOnClickListener(this);
        time_ban.setOnClickListener(this);
        user_name_view.setText(user.getName() + " " + user.getSurname());
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ban_butt:
                Lock lock = new Lock();
                lock.setUser(user.getId());
                lock.setReason(reason_edit_text.getText().toString());
                lock.setTime_end(time_ban.getText().toString());
                lock.setTime_start(new Date(System.currentTimeMillis()).toString());
                LocalStorage storage = LocalStorage.getInstance(getActivity());
                storage.banUser(lock);
                Toast.makeText(getActivity(), "пользователь " + user.getName() + " "
                        + user.getSurname() + " забанен", Toast.LENGTH_SHORT).show();
                getDialog().dismiss();

                break;
            case R.id.time_ban:
                new DatePickerDialog(getActivity(), d, 2017, 6, 3).show();
                break;
        }
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            time_ban.setText(dayOfMonth + "." + (month + 1) + "." + year);
        }
    };
}
