package com.vlad17021995m.android.usersapplication.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.vlad17021995m.android.usersapplication.R;
import com.vlad17021995m.android.usersapplication.data.orm.entity.User;
import com.vlad17021995m.android.usersapplication.data.orm.service.UserService;

public class ViewActivity extends AppCompatActivity {

    public static final String KEY_USER_LOGIN = "login";

    private User user = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String login = intent.getStringExtra(KEY_USER_LOGIN);
            UserService service = new UserService(this);
            setUser(service.getUser(login, null));
        }
        setContentView(R.layout.activity_view);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
