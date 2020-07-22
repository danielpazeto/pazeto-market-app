package com.pazeto.comercio.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.pazeto.comercio.R;

public class AuthHandler {

    Context context;

    public AuthHandler(Context context) {
        this.context = context;
    }

    public void setLoggedUserID(String id) {
        SharedPreferences sharedPref = this.context.getSharedPreferences(
                this.context.getString(R.string.login_pref_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(this.context.getString(R.string.login_pref_uid), id);
        editor.apply();
    }

    public String getLoggedUserID() {
        SharedPreferences sharedPref = context.getSharedPreferences(
                this.context.getString(R.string.login_pref_file_key), Context.MODE_PRIVATE);
        return sharedPref.getString(this.context.getString(R.string.login_pref_uid), "");

    }

    public void logout() {
        SharedPreferences sharedPref = this.context.getSharedPreferences(
                this.context.getString(R.string.login_pref_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }
}
