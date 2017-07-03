package com.vlad17021995m.android.usersapplication.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.vlad17021995m.android.usersapplication.R;
import com.vlad17021995m.android.usersapplication.data.orm.entity.User;
import com.vlad17021995m.android.usersapplication.data.orm.service.UserService;

public class ViewActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 123;
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
        if (hasPermissions()) {
            setContentView(R.layout.activity_view);
        }else {
            requestPermissionWithRationale();
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean hasPermissions(){
        int res = 0;
        String []permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION};
        for (String perm : permissions){
            res = checkCallingOrSelfPermission(perm);
            if (!(res == PackageManager.PERMISSION_GRANTED)){
                return false;
            }
        }
        return true;
    }

    public void requestPerms(){
        String []permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allowed = true;
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                for (int res : grantResults){
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }
                break;
            default:
                allowed = false;
                break;
        }
    }

    public void showNoPermission(){
        openApplicationSettings();
        Toast.makeText(getApplicationContext(), "откройте настройки и" +
                        " разрешите чтение карты памяти и определение местоположения",
                Toast.LENGTH_SHORT).show();
    }

    public void openApplicationSettings(){
        Intent appSettingsIntent =
                new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingsIntent, PERMISSION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PERMISSION_REQUEST_CODE){
            setContentView(R.layout.activity_view);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void requestPermissionWithRationale(){
        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) &&
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("разрешите чтение карты памяти и определение местоположения")
                    .setPositiveButton("разрешыть", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPerms();
                        }
                    }).create().show();
        }else {
            requestPerms();
        }
    }
}
