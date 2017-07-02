package com.vlad17021995m.android.usersapplication.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.vlad17021995m.android.usersapplication.R;
import com.vlad17021995m.android.usersapplication.data.gateway.LocalStorage;
import com.vlad17021995m.android.usersapplication.data.orm.entity.User;
import com.vlad17021995m.android.usersapplication.data.orm.service.UserService;
import com.vlad17021995m.android.usersapplication.fragments.register.RegisterFragment;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UserService service = new UserService(this);
        List<User> users = service.getAll();
        for (User u : users) {
            Log.d(RegisterFragment.REGISTER_LOGGER, u.getId() + " " + u.getMail() + " "
            + u.getPass() + " " + u.getType() + " " + u.getName() + " "
            + u.getSurname() + " " + u.getFather() + " " + u.getNickname()
            + " " + u.getNumber() + " " + u.getCity() + " " + u.getAvatar_path());
        }
        LocalStorage storage = LocalStorage.getInstance(this);
        User user = storage.currentUser();
        if (user != null){
            Intent intent = null;
            if (user.getType().equals("admin")){
                intent = new Intent(this, UsersActivity.class);
            }else {
                intent = new Intent(this, ViewActivity.class);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}
