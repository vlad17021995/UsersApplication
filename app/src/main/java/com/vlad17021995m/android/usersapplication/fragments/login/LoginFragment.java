package com.vlad17021995m.android.usersapplication.fragments.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vlad17021995m.android.usersapplication.R;
import com.vlad17021995m.android.usersapplication.activities.RegisterActivity;
import com.vlad17021995m.android.usersapplication.activities.UsersActivity;
import com.vlad17021995m.android.usersapplication.activities.ViewActivity;
import com.vlad17021995m.android.usersapplication.data.gateway.LocalStorage;
import com.vlad17021995m.android.usersapplication.data.orm.entity.User;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private EditText mail_edit_text, pass_edit_text;
    private Button login_butt, register_butt;

    public LoginFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mail_edit_text = (EditText)view.findViewById(R.id.mail_edit_text);
        pass_edit_text = (EditText)view.findViewById(R.id.pass_edit_text);
        login_butt = (Button)view.findViewById(R.id.login_butt);
        register_butt = (Button)view.findViewById(R.id.update_butt);
        login_butt.setOnClickListener(this);
        register_butt.setOnClickListener(this);
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
            case R.id.login_butt:
                LocalStorage storage = LocalStorage.getInstance(getActivity());
                if (!LocalStorage.isEmailValid(mail_edit_text.getText().toString())){
                    Toast.makeText(getActivity(), "Неправильный формат почты", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pass_edit_text.getText().toString().length() < 4){
                    Toast.makeText(getActivity(), "Пароль слишком короткий", Toast.LENGTH_SHORT).show();
                    return;
                }
                int code = storage.loginAccount(mail_edit_text.getText().toString(), pass_edit_text.getText().toString());
                switch (code){
                    case 0:
                        storage.setCurrentUser(mail_edit_text.getText().toString());
                        User user = storage.currentUser();
                        String messBan = storage.checkBanUser(user);
                        if (!messBan.equals("")){
                            Toast.makeText(getActivity(), messBan, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent = null;
                        if (user.getType().equals("admin")){
                            intent = new Intent(getActivity(), UsersActivity.class);
                        }else {
                            intent = new Intent(getActivity(), ViewActivity.class);
                        }
                        startActivity(intent);
                        getActivity().finish();
                        break;
                    case 1:
                        Toast.makeText(getActivity(), "Пользователь не существует", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getActivity(), "Неправильный пароль", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            case R.id.update_butt:
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
        }
    }
}
