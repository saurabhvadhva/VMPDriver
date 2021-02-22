package app.vmp.driver.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import app.vmp.driver.Database.DbHelper;
import app.vmp.driver.R;
import app.vmp.driver.adapter.TodayTripAdapter;
import app.vmp.driver.api.APIClient;
import app.vmp.driver.api.APIInterface;
import app.vmp.driver.model.DirectionResponse;
import app.vmp.driver.model.GeneralResponse;
import app.vmp.driver.model.Options;
import app.vmp.driver.model.RouteData;
import app.vmp.driver.model.RouteScheduleSlotDetails;
import app.vmp.driver.model.SubRouteData;
import app.vmp.driver.utils.Constant;
import app.vmp.driver.utils.LocationService;
import app.vmp.driver.utils.Util;
import app.vmp.driver.utils.VerticalViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.vmp.driver.utils.Constant.DEVICE_ID;
import static app.vmp.driver.utils.Constant.EMPLOYEE_ID;
import static app.vmp.driver.utils.Constant.PROFESSION_TYPE;
import static app.vmp.driver.utils.Constant.SEL_LANG;
import static app.vmp.driver.utils.Constant.TOKEN;
import static app.vmp.driver.utils.Constant.USER_ID;
import static app.vmp.driver.utils.Constant.VEHICLE_ID;

public class TodayTripActivity extends BaseActivity {

    @BindView(R.id.viewPager)
    VerticalViewPager viewPager;
    @BindView(R.id.tvCnt)
    AppCompatTextView tvCnt;
    private Context mContext;
    private ProgressDialog pd;
    private APIInterface apiInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_today_trip);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
//        rvPassenger.setLayoutManager(new LinearLayoutManager(mContext,RecyclerView.VERTICAL,false));
//        Toast toast = Toast.makeText(TodayTripActivity.this,getResources().getString(R.string.swipe_up), Toast.LENGTH_LONG);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.show();
        mContext = TodayTripActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
        pd.setMessage(mContext.getResources().getString(R.string.please_wait));


    }

    @OnClick(R.id.imgLanguage)
    public void onLangChange() {
        Intent in = new Intent(TodayTripActivity.this, LanguageActivity.class);
        in.putExtra("from","home");
        startActivity(in);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRoutes();
    }

    private void getRoutes() {

        if (Util.isNetworkAvailable(mContext)) {

            pd.show();

            Date currentTime = Calendar.getInstance().getTime();

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = df.format(currentTime);
            date = date.replace(" ", "T");
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("DriverId", Util.getPrefData(EMPLOYEE_ID, mContext));
//            jsonObject.addProperty("DriverId","2");
//            jsonObject.addProperty("FromDate",date);
//            jsonObject.addProperty("ToDate",date);
            jsonObject.addProperty("FromDate", date);
            jsonObject.addProperty("ToDate", date);
            jsonObject.addProperty("TripStatus", "active");
            jsonObject.addProperty("LanguageCode", Util.getPrefData(SEL_LANG,TodayTripActivity.this));

            apiInterface.getRoute(Util.getPrefData(TOKEN, mContext),Util.getPrefData(DEVICE_ID, mContext),Constant.CONTENT_TYPE, Constant.CONTENT_TYPE, jsonObject).enqueue(new Callback<ArrayList<RouteData>>() {
                @Override
                public void onResponse(Call<ArrayList<RouteData>> call, Response<ArrayList<RouteData>> response) {
                    pd.dismiss();
                    if (response.code()==401){
                        Util.redirecttoLogin(mContext);
                        return;
                    }
                    if (response.code() == 200) {
                        if (response.body() != null && response.body().size() > 0) {
                            viewPager.setVisibility(View.VISIBLE);
                            TodayTripAdapter todayTripAdapter = new TodayTripAdapter(mContext, response.body());
                            viewPager.setAdapter(todayTripAdapter);
                            Util.showToast(mContext, getResources().getString(R.string.swipe_up));
                            tvCnt.setText("(" + response.body().size() + ")");
                        } else {
                            viewPager.setVisibility(View.GONE);
                            tvCnt.setText("");
                        }
                    } else {
                        viewPager.setVisibility(View.GONE);
                        tvCnt.setText("");
                        Util.showToast(mContext, getResources().getString(R.string.smthng_wrong));
                    }

                }

                @Override
                public void onFailure(Call<ArrayList<RouteData>> call, Throwable t) {
                    pd.dismiss();
                    Util.showToast(mContext, getResources().getString(R.string.smthng_wrong));
                }
            });

        } else {
            Util.showToast(mContext, getResources().getString(R.string.no_internet));
        }

    }

    @OnClick(R.id.imgBack)
    public void onBack() {
        finish();
    }

    private static final int PERMISSION_REQUEST_CODE = 104;

    public void startTrip(final RouteData routeData) {

        if (Util.isNetworkAvailable(mContext)) {
            if (Util.isNetworkAvailable(mContext)) {
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }, PERMISSION_REQUEST_CODE);
                } else {
                    LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        //isGPSEnabled = true;
                        new AlertDialog.Builder(this)
                                .setMessage(mContext.getResources().getString(R.string.enable_location))
                                .setPositiveButton(mContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .show();
                    } else {

                        Util.setPrefData(VEHICLE_ID, routeData.getVehicleId(), mContext);

                        try {
                            Intent serviceIntent = new Intent(mContext, LocationService.class);
                            mContext.stopService(serviceIntent);
                        } catch (Exception e) {
                        }
                        Intent serviceIntent = new Intent(mContext, LocationService.class);
                        ContextCompat.startForegroundService(mContext, serviceIntent);

                        pd.show();

                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("DriverId", Util.getPrefData(EMPLOYEE_ID, mContext));
                        jsonObject.addProperty("UserId", Util.getPrefData(USER_ID, mContext));
//            jsonObject.addProperty("DriverId","2");
//            jsonObject.addProperty("UserId","2");
                        jsonObject.addProperty("StartTime", routeData.getTimeSlot());
                        jsonObject.addProperty("RouteId", routeData.getRouteId());
                        if (routeData.getStartLocation() != null) {
                            jsonObject.addProperty("LocationId", routeData.getStartLocation().getID());
                        } else {
                            jsonObject.addProperty("LocationId", "0");
                        }

                        apiInterface.startTrip(Util.getPrefData(TOKEN, mContext), Util.getPrefData(DEVICE_ID, mContext), Constant.CONTENT_TYPE, Constant.CONTENT_TYPE, jsonObject).enqueue(new Callback<GeneralResponse>() {
                            @Override
                            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                                pd.dismiss();
                                if (response.code()==401){
                                    Util.redirecttoLogin(mContext);
                                    return;
                                }
                                if (response.code() == 200) {
                                    if (response.body() != null) {
                                        Util.showToast(mContext, response.body().getMsg());
//                            if (response.body().getCode().equals("000")){
                                        try {
                                            DbHelper db = new DbHelper(mContext);
                                            db.open();
                                            db.deleteOnBoard();
                                            db.deleteDeBoard();
                                            db.deleteONB();
                                            db.deleteADDP();
                                            db.deleteDEB();
                                            db.close();
                                        } catch (Exception e){}
                                        Util.setPrefData("tripEndId","",mContext);
                                        Intent in = new Intent(mContext, TripActiity.class);
                                        in.putExtra("RouteId", routeData.getRouteId());
                                        in.putExtra("TimeSlot", routeData.getTimeSlot());
                                        in.putExtra("RouteName", routeData.getRouteName());
                                        startActivity(in);
                                        overridePendingTransition(R.anim.enter, R.anim.exit);
//                            }

                                    }
                                } else {
                                    Util.showToast(mContext, getResources().getString(R.string.smthng_wrong));
                                }

                            }

                            @Override
                            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                                pd.dismiss();
                                Util.showToast(mContext, getResources().getString(R.string.smthng_wrong));
                            }
                        });
                    }
                }
            }
        } else {
            Util.showToast(mContext, getResources().getString(R.string.no_internet));
        }

    }

    public void callShg(final RouteData routeData) {

        if (Util.isNetworkAvailable(mContext)) {

            pd.show();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("RouteScheduleSlotId", routeData.getRouteId());
            apiInterface.getShgDetails(Util.getPrefData(TOKEN, mContext), Util.getPrefData(DEVICE_ID, mContext), Constant.CONTENT_TYPE, Constant.CONTENT_TYPE, jsonObject).enqueue(new Callback<ArrayList<RouteScheduleSlotDetails>>() {
                @Override
                public void onResponse(Call<ArrayList<RouteScheduleSlotDetails>> call, Response<ArrayList<RouteScheduleSlotDetails>> response) {
                    pd.dismiss();
                    if (response.code()==401){
                        Util.redirecttoLogin(mContext);
                        return;
                    }
                    if (response.code() == 200) {
                        if (response.body() != null) {

                            try {
                                mContext.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + response.body().get(0).getSHGContactNumber())));
                            } catch (Exception e) {
                            }

                        }
                    } else {
                        Util.showToast(mContext, getResources().getString(R.string.smthng_wrong));
                    }

                }

                @Override
                public void onFailure(Call<ArrayList<RouteScheduleSlotDetails>> call, Throwable t) {
                    pd.dismiss();
                    Util.showToast(mContext, getResources().getString(R.string.smthng_wrong));
                }
            });
        } else {
            Util.showToast(mContext, getResources().getString(R.string.no_internet));
        }

    }

}
