package com.vlad17021995m.android.usersapplication.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.vlad17021995m.android.usersapplication.R;
import com.vlad17021995m.android.usersapplication.fragments.place.AddPlaceFragment;

public class PlaceActivity extends AppCompatActivity implements View.OnClickListener {

    private Button add_place_butt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        add_place_butt = (Button) findViewById(R.id.add_place_butt);
        add_place_butt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_place_butt:
                AddPlaceFragment dialogFragment = AddPlaceFragment.newInstance();
                dialogFragment.show(getSupportFragmentManager(), "add_place");
                break;
        }
    }
}
