package com.vlad17021995m.android.usersapplication.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.vlad17021995m.android.usersapplication.R;
import com.vlad17021995m.android.usersapplication.fragments.register.RegisterFragment;

public class RegisterActivity extends AppCompatActivity {

    public static final int SELECT_PHOTO = 10000;
    private Fragment register_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register_fragment = getSupportFragmentManager().findFragmentById(R.id.register_fragment);
    }

    public interface ILoadImageListener{
        public void onLoadImage(Uri uri);
    }
}
