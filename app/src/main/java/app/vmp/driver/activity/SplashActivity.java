package app.vmp.driver.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.google.gson.JsonObject;

import app.vmp.driver.R;
import app.vmp.driver.api.APIClient;
import app.vmp.driver.api.APIClientLocation;
import app.vmp.driver.api.APIInterface;
import app.vmp.driver.model.FetchAPKResponse;
import app.vmp.driver.utils.Constant;
import app.vmp.driver.utils.CryptoHandler;
import app.vmp.driver.utils.DownloadVideoDialog;
import app.vmp.driver.utils.RootUtil;
import app.vmp.driver.utils.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.vmp.driver.utils.Constant.USER_ID;
import static app.vmp.driver.utils.Constant.splash_time;

public class SplashActivity extends BaseActivity {

    APIInterface apiInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

         if (!RootUtil.isDeviceRooted()){
             init();
         } else {
             Util.showToast(SplashActivity.this,"Device is rooted");
             finish();
         }
    }

    private void init() {

        Util.changeLang(SplashActivity.this);
        apiInterface = APIClientLocation.getClient().create(APIInterface.class);

        if (Util.isNetworkAvailable(SplashActivity.this)) {
            fetchAPK();
        } else {
            Handler hnd = new Handler();
            hnd.postDelayed(new Runnable() {
                @Override
                public void run() {
                    redirection();
                }
            }, splash_time);
        }

    }

    private String urlApk = "";

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (urlApk.trim().length()>0){
            if (ActivityCompat.checkSelfPermission(mContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {
                DownloadVideoDialog downloadVideoDialog = new DownloadVideoDialog(SplashActivity.this, urlApk);
                downloadVideoDialog.show();
            } else {
                showDialog();
            }
        }
    }

    private void showDialog(){
        new AlertDialog.Builder(SplashActivity.this)
                .setTitle("Update App")
                .setMessage("New Version is available now, please update it")
                .setCancelable(false)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (ActivityCompat.checkSelfPermission(mContext,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                                    PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(
                                        new String[]{
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 22);
                            } else {
                                DownloadVideoDialog downloadVideoDialog = new DownloadVideoDialog(SplashActivity.this, urlApk);
                                downloadVideoDialog.show();
                            }
                        } else {
                            DownloadVideoDialog downloadVideoDialog = new DownloadVideoDialog(SplashActivity.this, urlApk);
                            downloadVideoDialog.show();
                        }

                    }
                }).show();
    }

    private void fetchAPK() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("RoleId", "7BC5FF04-0AE5-4B11-BCF3-06B1C8FCBC51");
        apiInterface.fetchAPK(Constant.CONTENT_TYPE, Constant.CONTENT_TYPE, jsonObject).enqueue(new Callback<FetchAPKResponse>() {
            @Override
            public void onResponse(Call<FetchAPKResponse> call, Response<FetchAPKResponse> response) {
                try {
                    PackageManager manager = getPackageManager();
                    PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
                    String versionName = info.versionName;
                    FetchAPKResponse fetchAPKResponse = response.body();
                    Log.e("ttt", versionName + "+" + fetchAPKResponse.getVersion());
                    if (versionName.equals(fetchAPKResponse.getVersion())) {
                        redirection();
                    } else {
                        urlApk = fetchAPKResponse.getLocation();
                        showDialog();
                    }
                } catch (Exception e) {
                    redirection();
                }
            }

            @Override
            public void onFailure(Call<FetchAPKResponse> call, Throwable t) {
                redirection();
            }
        });

    }

    private void redirection() {
        if (Util.getPrefData(USER_ID, mContext).trim().length() == 0) {
            Intent in = new Intent(SplashActivity.this, LanguageActivity.class);
            in.putExtra("from", "splash");
            startActivity(in);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        } else {
            Intent in = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(in);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        finish();
    }

}
