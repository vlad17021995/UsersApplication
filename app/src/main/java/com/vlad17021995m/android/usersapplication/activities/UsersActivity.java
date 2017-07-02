package com.vlad17021995m.android.usersapplication.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.vlad17021995m.android.usersapplication.R;

public class UsersActivity extends AppCompatActivity implements View.OnClickListener {

    private Button view_butt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        view_butt = (Button) findViewById(R.id.view_butt);
        view_butt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.view_butt:
                Intent intent = new Intent(this, ViewActivity.class);
                startActivity(intent);
                break;
        }
    }
}
