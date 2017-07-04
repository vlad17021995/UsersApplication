package com.vlad17021995m.android.usersapplication.data.gateway;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vlad17021995m.android.usersapplication.data.orm.entity.Lock;
import com.vlad17021995m.android.usersapplication.data.orm.entity.Place;
import com.vlad17021995m.android.usersapplication.data.orm.entity.User;
import com.vlad17021995m.android.usersapplication.data.orm.service.LockService;
import com.vlad17021995m.android.usersapplication.data.orm.service.PlaceService;
import com.vlad17021995m.android.usersapplication.data.orm.service.UserService;
import com.vlad17021995m.android.usersapplication.retrofit.api.IMailApi;
import com.vlad17021995m.android.usersapplication.retrofit.model.Mail;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by qwerty on 21.06.2017.
 */

public class LocalStorage {
    private static LocalStorage localStorage;
    public static final String KEY_USER = "user";
    public static final String BASE_URL = "http://apilayer.net/";
    private Context context;

    private boolean mailExist = false;

    private LocalStorage(Context context) {
        this.context = context;
    }

    public static LocalStorage getInstance(Context context) {
        if (localStorage == null) {
            localStorage = new LocalStorage(context);
        }
        return localStorage;
    }

    public int registerAccount(String mail, String pass, String name, String surname, String father,
                               String number, String nick, String city, String avatar) {
        UserService service = new UserService(context);
        if (service.getUser(mail, null) == null) {
            User user = new User();
            user.setMail(mail);
            user.setPass(md5Custom(pass));
            if (service.getAll().size() == 0) {
                user.setType("admin");
            } else {
                user.setType("user");
            }
            user.setName(name);
            user.setSurname(surname);
            user.setFather(father);
            user.setNumber(number);
            user.setNickname(nick);
            user.setCity(city);
            user.setAvatar_path(avatar);
            service.registerUser(user);
            return 0;
        } else {
            return 1;
        }
    }

    public int loginAccount(String mail, String pass) {
        UserService service = new UserService(context);
        User user = service.getUser(mail, null);
        if (user == null) {
            return 1;
        } else {
            String hash = md5Custom(pass);
            if (hash.equals(user.getPass())) {
                return 0;
            } else {
                return 2;
            }
        }
    }

    public User getUserByLogin(String login) {
        UserService service = new UserService(context);
        return service.getUser(login, null);
    }

    public String checkBanUser(User user) {
        LockService service = new LockService(context);
        Lock lock = service.getLockByUser(user);
        if (lock == null) {
            return "";
        } else {
            return "Вы заблокированы из - за " + lock.getReason()
                    + " до " + lock.getTime_end();
        }
    }

    public List<User> getAllUsers() {
        UserService service = new UserService(context);
        List<User> userList = service.getAll();
        List<User> users = new ArrayList<>();
        for (User u : userList) {
            if (!u.getType().equals("admin")) {
                users.add(u);
            }
        }
        return users;
    }


    public void editUser(User data) {
        UserService service = new UserService(context);
        service.updateUser(data);
    }

    public void deleteUser(User user) {
        UserService service = new UserService(context);
        service.deleteUser(user);
    }

    public User currentUser() {
        SharedPreferences preferences =
                context.getSharedPreferences(KEY_USER, Context.MODE_PRIVATE);
        String login = preferences.getString(KEY_USER, "");
        UserService service = new UserService(context);
        return service.getUser(login, null);
    }


    public void setCurrentUser(String login) {
        SharedPreferences preferences =
                context.getSharedPreferences(KEY_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_USER, login);
        editor.apply();
    }

    public void addPlace(Place place) {
        PlaceService placeService = new PlaceService(context);
        User user = currentUser();
        place.setUser(user.getId());
        placeService.addPlace(place);
    }

    public void editPlace(Place place) {
        PlaceService service = new PlaceService(context);
        service.updatePlace(place);
    }

    public List<Place> getPlaces() {
        PlaceService service = new PlaceService(context);
        return service.getPlacesByUser(currentUser());
    }

    public Place GetPlaceById(int id) {
        PlaceService service = new PlaceService(context);
        return service.getById(id);
    }

    public void deletePlace(Place place) {
        PlaceService service = new PlaceService(context);
        service.deletePlace(place);
    }

    public void banUser(Lock lock) {
        LockService service = new LockService(context);
        service.banUser(lock);
    }


    public static String md5Custom(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(password.getBytes());

        byte byteData[] = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public static boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }

    public void checkExistEmail(String email) {
        final Context needForCheck = context;
        OkHttpClient okHttpClient = new OkHttpClient();
        Gson gson = new Gson();
        Retrofit retrofit = null;
        try {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        IMailApi api = retrofit.create(IMailApi.class);
        Call<Mail> call = api.getMailInfo("2e3f9982efd9b981f342a0309f9c1864", email, "1");
        call.enqueue(new Callback<Mail>() {
            @Override
            public void onResponse(Call<Mail> call, Response<Mail> response) {
                Mail mail = response.body();
                if (mail.smtp_check){
                    mailExist = true;
                    Toast.makeText(needForCheck, "почтовый адресс существует, регистрацию разрешено", Toast.LENGTH_SHORT)
                            .show();
                }else {
                    mailExist = false;
                    Toast.makeText(needForCheck, "почтовый адресс не существует, в регистрации отказано", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<Mail> call, Throwable t) {
                Toast.makeText(needForCheck, "не удалось проверить существование адреса", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    public boolean isMailExist() {
        return mailExist;
    }

    public void setMailExist(boolean mailExist) {
        this.mailExist = mailExist;
    }
}
