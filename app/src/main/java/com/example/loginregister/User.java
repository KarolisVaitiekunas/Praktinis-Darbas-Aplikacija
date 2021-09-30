package com.example.loginregister;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.view.Display;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class User {
    public String name, email;
    public Boolean status, stayLoggedIn;

    private final String url = "http://keris.ro.lt:3001/auth/loggedIn";
    public boolean result;

    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;

    private static final String IS_LOGIN = "status";
    public static final String TOKEN_NAME = "token";
    public static final String KEY_NAME = "name";


    public User(Context _context) {
        context = _context;
        userSession = context.getSharedPreferences("userSession", Context.MODE_PRIVATE);
        editor = userSession.edit();
    }

    public void createLoginSession(String _name, String token) {
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_NAME, _name);

        editor.putString(TOKEN_NAME, token);

        editor.commit();
    }

    public HashMap<String, String> getUsersDetailFromSession() {
        HashMap<String, String> userData = new HashMap<String, String>();

        userData.put(KEY_NAME, userSession.getString(KEY_NAME, null));
        userData.put(TOKEN_NAME, userSession.getString(TOKEN_NAME, null));

        return userData;

    }

    public boolean checkLogin() {
        if (userSession.getBoolean(IS_LOGIN, false)) {
            return true;
        } else {
            return false;
        }
    }

    public void logoutUserSession() {
        editor.clear();
        editor.commit();
    }
}

