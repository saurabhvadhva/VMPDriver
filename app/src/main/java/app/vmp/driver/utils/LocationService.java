package app.vmp.driver.utils;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonObject;

import app.vmp.driver.R;
import app.vmp.driver.activity.MainActivity;
import app.vmp.driver.api.APIClientLocation;
import app.vmp.driver.api.APIInterface;
import app.vmp.driver.model.GeneralResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.vmp.driver.utils.Constant.DEVICE_ID;
import static app.vmp.driver.utils.Constant.LOCATION_UPDATE_TIME;
import static app.vmp.driver.utils.Constant.TOKEN;
import static app.vmp.driver.utils.Constant.USER_ID;
import static app.vmp.driver.utils.Constant.VEHICLE_ID;

public class LocationService extends Service {

//    Handler hnd;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    LocationCallback mLocationCallback;
    private APIInterface apiInterface;
    private Context mContext;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        hnd = new Handler();
        mContext = LocationService.this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        apiInterface = APIClientLocation.getClient().create(APIInterface.class);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText("Location is being updated...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        try {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            createLocationRequest();
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    if (locationResult != null && locationResult.getLocations() != null && locationResult.getLocations().size() > 0) {
                        Location location = locationResult.getLocations().get(0);
                        if (Util.isNetworkAvailable(mContext)) {

                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("UserId", Util.getPrefData(USER_ID, mContext));
                        jsonObject.addProperty("VehicleId", Util.getPrefData(VEHICLE_ID, mContext));
                        jsonObject.addProperty("Lat", "" + location.getLatitude());
                        jsonObject.addProperty("Lng", location.getLongitude());
                        apiInterface.submitLocation(Util.getPrefData(TOKEN, mContext), Util.getPrefData(DEVICE_ID, mContext), Constant.CONTENT_TYPE, Constant.CONTENT_TYPE, jsonObject).enqueue(new Callback<GeneralResponse>() {
                            @Override
                            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                                if (response.code()==401){
                                    try {
                                        stopForeground(true);
                                        stopSelf();
                                    } catch (Exception e){}
                                    return;
                                }
                                try {
                                } catch (Exception e) {
                                }

                            }

                            @Override
                            public void onFailure(Call<GeneralResponse> call, Throwable t) {

                            }
                        });
                    }
                    }
                }
            };
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                        mLocationCallback,
                        null);
            }

        } catch (Exception e) {
        }

//        hnd.postDelayed(rnb,5000);
        return START_STICKY;
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(LOCATION_UPDATE_TIME);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public static final String CHANNEL_ID = "LocationChannel";

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Location Updating service",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
//            hnd.removeCallbacks(rnb);
        } catch (Exception e) {
        }

    }

}