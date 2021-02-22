package app.vmp.driver.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;

import app.vmp.driver.Database.DbHelper;
import app.vmp.driver.activity.LoginActivity;

import static app.vmp.driver.utils.Constant.PREF_NAME;
import static app.vmp.driver.utils.Constant.SEL_LANG;
import static app.vmp.driver.utils.Constant.USER_ID;

public class Util {

    public static boolean isNullE(AppCompatEditText et) {
        if (et == null || et.getText() == null || et.getText().toString().trim().length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static String valE(AppCompatEditText et) {
       return et.getText().toString().trim();
    }

    public static boolean isPermission(Activity activity){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (ActivityCompat.checkSelfPermission(activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED
                ) {
                    activity.requestPermissions(
                            new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 8);
                    return false;
                }
                return true;
            } else {
                return true;
            }

    }


    public static boolean checkPermissionContact(Activity activity){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ActivityCompat.checkSelfPermission(activity,
                    Manifest.permission.READ_CONTACTS) !=
                    PackageManager.PERMISSION_GRANTED
            ) {
                activity.requestPermissions(
                        new String[]{
                                Manifest.permission.READ_CONTACTS}, 12);
                return false;
            }
            return true;
        } else {
            return true;
        }

    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void setPrefData(String prefName, String value, Context context) {
        SharedPreferences pref;
        SharedPreferences.Editor editor;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
        editor.putString(prefName, value);
        editor.commit();
    }

    public static String decoded(String JWTEncoded) {
        try {
            String[] split = JWTEncoded.split("\\.");
            return getJson(split[1]);
        } catch (Exception e) {
            //Error
            e.printStackTrace();
            return "";
        }
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException{
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }

    public static String getPrefData(String prefName, Context context) {
        SharedPreferences pref;
        String prefValue;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefValue = pref.getString(prefName, "");
        return prefValue;
    }

    public static void showToast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

    public static void changeLang(final Activity activity){
         String lang = "en";
        if (getPrefData(SEL_LANG,activity).trim().length()>0){
            lang = getPrefData(SEL_LANG,activity);
        }
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config,
                activity.getBaseContext().getResources().getDisplayMetrics());
    }

    public static void redirecttoLogin(Context mContext){
        Util.setPrefData(USER_ID, "", mContext);
        try {
            Intent serviceIntent = new Intent(mContext, LocationService.class);
            mContext.stopService(serviceIntent);
        } catch (Exception e) {
        }
        try {
            DbHelper dbHelper = new DbHelper(mContext);
            dbHelper.open();
            dbHelper.deleteTopUp();
            dbHelper.close();
        } catch (Exception e){}
        mContext.startActivity(new Intent(mContext, LoginActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

}
