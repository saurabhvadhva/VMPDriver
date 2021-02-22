package app.vmp.driver.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import app.vmp.driver.Database.DbHelper;
import app.vmp.driver.R;
import app.vmp.driver.adapter.TodayTripAdapter;
import app.vmp.driver.adapter.TripDropOffAdapter;
import app.vmp.driver.adapter.TripOnBoardAdapter;
import app.vmp.driver.api.APIClient;
import app.vmp.driver.api.APIClientLocation;
import app.vmp.driver.api.APIInterface;
import app.vmp.driver.bottomsheet.AddPassengerDialog;
import app.vmp.driver.bottomsheet.DropOffDialog;
import app.vmp.driver.bottomsheet.IssueCardDialog;
import app.vmp.driver.bottomsheet.OnBoardDialog;
import app.vmp.driver.bottomsheet.TopUpDialog;
import app.vmp.driver.model.AddPassengerResponse;
import app.vmp.driver.model.DirectionResponse;
import app.vmp.driver.model.FixedFareResponse;
import app.vmp.driver.model.GeneralResponse;
import app.vmp.driver.model.NextStopDetails;
import app.vmp.driver.model.PassengerData;
import app.vmp.driver.model.RouteData;
import app.vmp.driver.model.RouteScheduleSlotDetails;
import app.vmp.driver.model.StartLocation;
import app.vmp.driver.model.SubRouteData;
import app.vmp.driver.utils.Constant;
import app.vmp.driver.utils.LocationService;
import app.vmp.driver.utils.Util;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.vmp.driver.Database.DbHelper.TAG_ADDP_DATE;
import static app.vmp.driver.Database.DbHelper.TAG_ADDP_MOBILE;
import static app.vmp.driver.Database.DbHelper.TAG_ADDP_NAME;
import static app.vmp.driver.Database.DbHelper.TAG_ADDP_SLID;
import static app.vmp.driver.Database.DbHelper.TAG_ADDP_TIMESTAMP;
import static app.vmp.driver.Database.DbHelper.TAG_DEB_DCI;
import static app.vmp.driver.Database.DbHelper.TAG_DEB_DEMI;
import static app.vmp.driver.Database.DbHelper.TAG_DEB_ELID;
import static app.vmp.driver.Database.DbHelper.TAG_DEB_FARE;
import static app.vmp.driver.Database.DbHelper.TAG_DEB_NFCID;
import static app.vmp.driver.Database.DbHelper.TAG_DEB_PSGI;
import static app.vmp.driver.Database.DbHelper.TAG_DEB_PTDI;
import static app.vmp.driver.Database.DbHelper.TAG_DEB_TOP;
import static app.vmp.driver.Database.DbHelper.TAG_ONB_DC_ID;
import static app.vmp.driver.Database.DbHelper.TAG_ONB_DEM_ID;
import static app.vmp.driver.Database.DbHelper.TAG_ONB_NFC_ID;
import static app.vmp.driver.Database.DbHelper.TAG_ONB_PSNGR_ID;
import static app.vmp.driver.Database.DbHelper.TAG_ONB_SL_ID;
import static app.vmp.driver.Database.DbHelper.TAG_ONB_TYPE_OF;
import static app.vmp.driver.activity.AddPassengerActivity.mContextAP;
import static app.vmp.driver.utils.Constant.API_GET_LOCATIONS;
import static app.vmp.driver.utils.Constant.CURONG_ROUTE_ID;
import static app.vmp.driver.utils.Constant.CURONG_ROUTE_NAME;
import static app.vmp.driver.utils.Constant.CURONG_TIME_SLOT;
import static app.vmp.driver.utils.Constant.CUR_ROUTE_ID;
import static app.vmp.driver.utils.Constant.DEVICE_ID;
import static app.vmp.driver.utils.Constant.DIRECTION_LIST;
import static app.vmp.driver.utils.Constant.EMPLOYEE_ID;
import static app.vmp.driver.utils.Constant.FIXED_FARE_LIST;
import static app.vmp.driver.utils.Constant.LOCATION_LIST;
import static app.vmp.driver.utils.Constant.NEXT_STOP_UPDATE_TIME;
import static app.vmp.driver.utils.Constant.NGO_ID;
import static app.vmp.driver.utils.Constant.PROFESSION_TYPE;
import static app.vmp.driver.utils.Constant.ROUTE_ID_DIRECTION;
import static app.vmp.driver.utils.Constant.ROUTE_ID_FARE;
import static app.vmp.driver.utils.Constant.ROUTE_ID_SHG;
import static app.vmp.driver.utils.Constant.SEL_LANG;
import static app.vmp.driver.utils.Constant.SHG_NUMBER;
import static app.vmp.driver.utils.Constant.TOKEN;
import static app.vmp.driver.utils.Constant.USER_ID;

public class TripActiity extends BaseActivity {

    @BindView(R.id.rvPassenger)
    RecyclerView rvPassenger;

    @BindView(R.id.llOnBoard)
    LinearLayout llOnBoard;
    @BindView(R.id.llDropOff)
    LinearLayout llDropOff;
    @BindView(R.id.imgOnBoard)
    AppCompatImageView imgOnBoard;
    @BindView(R.id.tvOnBoard)
    AppCompatTextView tvOnBoard;
    @BindView(R.id.imgDropOff)
    AppCompatImageView imgDropOff;
    @BindView(R.id.tvDropOff)
    AppCompatTextView tvDropOff;
    @BindView(R.id.tvTripDate)
    AppCompatTextView tvTripDate;
    @BindView(R.id.tvTitle)
    AppCompatTextView tvTitle;

    @BindView(R.id.imgArwOnBoard)
    ImageView imgArwOnBoard;
    @BindView(R.id.imgArwDropOff)
    ImageView imgArwDropOff;
    @BindView(R.id.tvNextStop)
    AppCompatTextView tvNextStop;

    private String RouteId = "", TimeSlot = "", RouteName = "";

    private Context mContext;
    private ProgressDialog pd;
    private APIInterface apiInterface;
    private APIInterface apiInterface2;
    private TripOnBoardAdapter homeTripAdapter;
    private TripDropOffAdapter tripDropOffAdapter;

    private Handler hnd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_trip);
        ButterKnife.bind(this);

        init();
    }

    private Runnable rnb = new Runnable() {
        @Override
        public void run() {
            hnd.postDelayed(rnb, NEXT_STOP_UPDATE_TIME);
            getNextStop();
        }
    };

    private String showMsg = "";

    private void showAlert(final String title){
        if (pd.isShowing()){
            pd.dismiss();
        }
        new AlertDialog.Builder(TripActiity.this)
                .setTitle(title)
                .setMessage(showMsg)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).show();
    }

    private void getNextStop() {
        if (Util.isNetworkAvailable(mContext)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("DriverId", Util.getPrefData(EMPLOYEE_ID, mContext));
            apiInterface2.getNextStopDetails(Util.getPrefData(TOKEN, mContext), Util.getPrefData(DEVICE_ID, mContext), Constant.CONTENT_TYPE, Constant.CONTENT_TYPE, jsonObject).enqueue(new Callback<NextStopDetails>() {
                @Override
                public void onResponse(Call<NextStopDetails> call, Response<NextStopDetails> response) {
                    if (response.code()==401){
//                        Util.redirecttoLogin(mContext);
                        try {
                            hnd.removeCallbacks(rnb);
                        } catch (Exception e){}
                        return;
                    }
                    try {
                        if (response.code() == 200 && response.body() != null && response.body().getLocationName() != null) {
                            tvNextStop.setText(response.body().getLocationName());
                        } else {
                            tvNextStop.setText("");
                        }

                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(Call<NextStopDetails> call, Throwable t) {

                }
            });

        }
    }

    private void init() {

        mContext = TripActiity.this;
        db = new DbHelper(mContext);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        apiInterface2 = APIClientLocation.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
        pd.setMessage(mContext.getResources().getString(R.string.please_wait));
        pd.setCancelable(false);

        rvPassenger.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        RouteId = getIntent().getStringExtra("RouteId");
        TimeSlot = getIntent().getStringExtra("TimeSlot");
        RouteName = getIntent().getStringExtra("RouteName");
        Util.setPrefData(CURONG_ROUTE_ID, RouteId, mContext);
        Util.setPrefData(CURONG_ROUTE_NAME, RouteName, mContext);
        Util.setPrefData(CURONG_TIME_SLOT, TimeSlot, mContext);
        getFareList();
        getDirectionList();
        getShgDetails();
        getLocationList();
        getPassengers("ONBOARD");
//        setOnBoardList();
        tvTitle.setText("Route Name: " + RouteName);
        Date currentTime = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd MMM");
        tvTripDate.setText(df.format(currentTime));
        hnd = new Handler();
        hnd.postDelayed(rnb, 100);
    }

    private void checkForAddPassenger() {
        if (Util.isNetworkAvailable(mContext)) {
            db.open();
            ArrayList<HashMap<String, String>> list = db.getAddPsngrList();
            db.close();
            if (list == null || list.size() == 0) {
                cheforOnBoard();
            } else {
                if (!pd.isShowing()){
                    pd.show();
                }
                HashMap<String, String> map = list.get(0);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("RouteId", RouteId);
                jsonObject.addProperty("TimeSlot", TimeSlot);
                jsonObject.addProperty("PassengerName", "" + map.get(TAG_ADDP_NAME));
                jsonObject.addProperty("PassengerMobile", "" + map.get(TAG_ADDP_MOBILE));
                jsonObject.addProperty("StartLocationId", "" + map.get(TAG_ADDP_SLID));
                jsonObject.addProperty("EndLocationId", "" + map.get(TAG_ADDP_SLID));
                jsonObject.addProperty("Date", "" + map.get(TAG_ADDP_DATE));
                jsonObject.addProperty("MaleCount", "0");
                jsonObject.addProperty("FemaleCount", "0");
                jsonObject.addProperty("ChildrenCount", "0");
                jsonObject.addProperty("UserId", Util.getPrefData(USER_ID, mContext));
                Log.e("ttt", "ADDP" + jsonObject.toString());
                showMsg = jsonObject.toString();
                final String timestamp = "" + map.get(TAG_ADDP_TIMESTAMP);
                apiInterface.addPassenger(Util.getPrefData(TOKEN, mContext), Util.getPrefData(DEVICE_ID, mContext), Constant.CONTENT_TYPE, Constant.CONTENT_TYPE, jsonObject).enqueue(new Callback<AddPassengerResponse>() {
                    @Override
                    public void onResponse(Call<AddPassengerResponse> call, Response<AddPassengerResponse> response) {

                        if (response.code()==401){
                            Util.redirecttoLogin(mContext);
                            return;
                        }
                        if (response.code() == 200) {
//                        Util.showToast(mContext, response.body().getMsg());
                            if (response.body().getCode().equals("000")) {
                                AddPassengerResponse ap = response.body();
                                db.open();
                                db.deleteAddedPassenger(timestamp);
                                db.updateOnBoard(ap.getPassengerId(), ap.getDailyCharterId(), ap.getDemandId(), timestamp);
                                db.updateDeBoard(ap.getPassengerId(), ap.getDailyCharterId(), ap.getDemandId(), timestamp);
                                db.close();

                                checkForAddPassenger();
                            } else {
                                Gson gson = new Gson();
                                showMsg = showMsg +"Response: "+gson.toJson(response.body());
                                showAlert("AddPassenger");
//                                Util.showToast(mContext, getResources().getString(R.string.smthng_wrong));
                            }
                        } else {
                            showMsg = showMsg +"Response: not 200";
                            showAlert("AddPassenger");
//                            Util.showToast(mContext, getResources().getString(R.string.smthng_wrong));
                        }
                    }

                    @Override
                    public void onFailure(Call<AddPassengerResponse> call, Throwable t) {
                        pd.dismiss();
                        Util.showToast(mContext, getResources().getString(R.string.smthng_wrong));
                    }
                });
            }
        }
    }

    private void cheforOnBoard() {
        if (Util.isNetworkAvailable(mContext)) {

            db.open();
            ArrayList<HashMap<String, String>> list = db.getOnBoardPsngrList();
            db.close();

            if (list == null || list.size() == 0) {
                Log.e("ttt", "FINISH-ONBOARD");
                cheforDeBoard();
            } else {
                if (!pd.isShowing()){
                    pd.show();
                }
                HashMap<String, String> map = list.get(0);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("DailyCharterId", "" + map.get(TAG_ONB_DC_ID));
                jsonObject.addProperty("TypeOfPassenger", "" + map.get(TAG_ONB_TYPE_OF));
                jsonObject.addProperty("DemandId", "" + map.get(TAG_ONB_DEM_ID));
                jsonObject.addProperty("PassengerId", "" + map.get(TAG_ONB_PSNGR_ID));
                jsonObject.addProperty("StartLocationId", "" + map.get(TAG_ONB_SL_ID));
                jsonObject.addProperty("UserId", Util.getPrefData(USER_ID, mContext));
                jsonObject.addProperty("NFCCardId", "" );
                jsonObject.addProperty("NFCTagId", "" + map.get(TAG_ONB_NFC_ID));
                Log.e("ttt", "ADDONB" + jsonObject.toString());
                showMsg = jsonObject.toString();
                final String pasId = map.get(TAG_ONB_PSNGR_ID);
                apiInterface.onBoardPassenger(Util.getPrefData(TOKEN, mContext), Util.getPrefData(DEVICE_ID, mContext), Constant.CONTENT_TYPE, Constant.CONTENT_TYPE, jsonObject).enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                        if (response.code()==401){
                            Util.redirecttoLogin(mContext);
                            return;
                        }
                        if (response.code() == 200 && response.body() != null) {
//                            Util.showToast(mContext, response.body().getMsg());
                            if (response.body().getCode().equals("000")) {
                                db.open();
                                db.deleteOnBoardPassenger(pasId);
                                db.updateDeBoardPtdi(response.body().getPassengerTravelDetailId(), pasId);
                                db.close();
                                cheforOnBoard();
                            } else {
                                Gson gson = new Gson();
                                showMsg = showMsg +"Response: "+gson.toJson(response.body());
                                showAlert("OnBoard");
//                                Util.showToast(mContext, getResources().getString(R.string.smthng_wrong));
                            }

                        } else {
                            showMsg = showMsg +"Response: not 200";
                            showAlert("OnBoard");
//                            Util.showToast(mContext, getResources().getString(R.string.smthng_wrong));
                        }

                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
                        db.close();
                        pd.dismiss();
                        Util.showToast(mContext, getResources().getString(R.string.smthng_wrong));
                    }
                });
            }


        }
    }

    private void cheforDeBoard() {
        if (Util.isNetworkAvailable(mContext)) {

            db.open();
            ArrayList<HashMap<String, String>> list = db.getDeBoardPsngrList();
            db.close();

            if (list == null || list.size() == 0) {
                Log.e("ttt", "FINISH-DEBOARD");
                endTrip();
            } else {
                if (!pd.isShowing()){
                    pd.show();
                }
                HashMap<String, String> map = list.get(0);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("PassengerTravelDetailId", "" + map.get(TAG_DEB_PTDI));
                jsonObject.addProperty("TypeOfPassenger", "" + map.get(TAG_DEB_TOP));
                jsonObject.addProperty("DailyCharterId", "" + map.get(TAG_DEB_DCI));
                jsonObject.addProperty("DemandId", "" + map.get(TAG_DEB_DEMI));
                jsonObject.addProperty("PassengerId", "" + map.get(TAG_DEB_PSGI));
                jsonObject.addProperty("FareAmount", "" + map.get(TAG_DEB_FARE));
                jsonObject.addProperty("NFCCardId", "" );
                jsonObject.addProperty("NFCTagId", "" + map.get(TAG_DEB_NFCID));
                jsonObject.addProperty("EndLocationId", "" + map.get(TAG_DEB_ELID));
                jsonObject.addProperty("UserId", Util.getPrefData(USER_ID, mContext));
                   showMsg = jsonObject.toString();
                Log.e("ttt", "ADDDEB" + jsonObject.toString());
                final String pasId = map.get(TAG_DEB_PSGI);
                apiInterface.deBoardPassenger(Util.getPrefData(TOKEN, mContext), Util.getPrefData(DEVICE_ID, mContext), Constant.CONTENT_TYPE, Constant.CONTENT_TYPE, jsonObject).enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                        if (response.code()==401){
                            Util.redirecttoLogin(mContext);
                            return;
                        }
                        if (response.code() == 200 && response.body() != null) {
//                            Util.showToast(mContext, response.body().getMsg());
                            if (response.body().getCode().equals("000")) {
                                db.open();
                                db.deleteDeBoardPassenger(pasId);
                                db.close();
                                cheforDeBoard();
                            } else {
                                Gson gson = new Gson();
                                showMsg = showMsg +"Response: "+gson.toJson(response.body());
                                showAlert("DeBoard");
//                                Util.showToast(mContext, getResources().getString(R.string.smthng_wrong));
                            }

                        } else {
                            showMsg = showMsg +"Response: not 200";
                            showAlert("DeBoard");
//                            Util.showToast(mContext, getResources().getString(R.string.smthng_wrong));
                        }

                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
                        db.close();
                        pd.dismiss();
                        Util.showToast(mContext, getResources().getString(R.string.smthng_wrong));
                    }
                });
            }


        }
    }

    private void getFareList() {
        if (Util.isNetworkAvailable(mContext)) {
            if (!RouteId.equals(Util.getPrefData(ROUTE_ID_FARE, mContext))) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("RouteId", RouteId);
                apiInterface.getGetFixedRate(Util.getPrefData(TOKEN, mContext), Util.getPrefData(DEVICE_ID, mContext), Constant.CONTENT_TYPE, Constant.CONTENT_TYPE, jsonObject).enqueue(new Callback<ArrayList<FixedFareResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<FixedFareResponse>> call, Response<ArrayList<FixedFareResponse>> response) {
                        if (response.code()==401){
                            Util.redirecttoLogin(mContext);
                            return;
                        }
                        if (response.code() == 200 && response.body() != null) {

                            Gson gson = new Gson();
                            String json = gson.toJson(response.body());
                            Log.e("ttt","FareList"+json);
                            Util.setPrefData(ROUTE_ID_FARE, RouteId, mContext);
                            Util.setPrefData(FIXED_FARE_LIST, json, mContext);

                        }

                    }

                    @Override
                    public void onFailure(Call<ArrayList<FixedFareResponse>> call, Throwable t) {
                    }
                });
            }

        }
    }

    private void getLocationList() {
        if (Util.isNetworkAvailable(mContext)) {
            if (!RouteId.equals(Util.getPrefData(CUR_ROUTE_ID, mContext))) {
                String url = API_GET_LOCATIONS + "RouteId=" + RouteId+"&LanguageCode="+ Util.getPrefData(SEL_LANG,TripActiity.this);
                apiInterface.getLocationList(Util.getPrefData(TOKEN, mContext), Util.getPrefData(DEVICE_ID, mContext), Constant.CONTENT_TYPE, Constant.CONTENT_TYPE, url).enqueue(new Callback<ArrayList<StartLocation>>() {
                    @Override
                    public void onResponse(Call<ArrayList<StartLocation>> call, Response<ArrayList<StartLocation>> response) {
                        if (response.code()==401){
                            Util.redirecttoLogin(mContext);
                            return;
                        }
                        if (response.code() == 200 && response.body() != null) {

                            Gson gson = new Gson();
                            String json = gson.toJson(response.body());
                            Log.e("ttt","LocList"+json);
                            Util.setPrefData(CUR_ROUTE_ID, RouteId, mContext);
                            Util.setPrefData(LOCATION_LIST, json, mContext);

                        } else {
                            Util.showToast(mContext, getResources().getString(R.string.smthng_wrong));
                        }

                    }

                    @Override
                    public void onFailure(Call<ArrayList<StartLocation>> call, Throwable t) {
                        Util.showToast(mContext, getResources().getString(R.string.smthng_wrong));
                    }
                });
            }

        }
    }

    private void endTrip() {
        if (!pd.isShowing()){
            pd.show();
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("DriverId", Util.getPrefData(EMPLOYEE_ID, mContext));
        jsonObject.addProperty("UserId", Util.getPrefData(USER_ID, mContext));
//            jsonObject.addProperty("DriverId","2");
//            jsonObject.addProperty("UserId","2");
        jsonObject.addProperty("StartTime", TimeSlot);
        jsonObject.addProperty("RouteId", RouteId);

        apiInterface.endTrip(Util.getPrefData(TOKEN, mContext), Util.getPrefData(DEVICE_ID, mContext), Constant.CONTENT_TYPE, Constant.CONTENT_TYPE, jsonObject).enqueue(new Callback<GeneralResponse>() {
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
                        if (response.body().getCode().equals("000")) {
                            try {
                                db.open();
                                db.deleteOnBoard();
                                db.deleteDeBoard();
                                db.close();
                            } catch (Exception e){}

                            try {
                                Intent serviceIntent = new Intent(mContext, LocationService.class);
                                stopService(serviceIntent);
                            } catch (Exception e) {
                            }
                            Util.setPrefData("tripEndId","",mContext);
                            finish();
                        }

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

    @OnClick(R.id.tvEndTrip)
    public void onEndTrip() {
        if (Util.isNetworkAvailable(mContext)) {
            db.open();
            ArrayList<PassengerData> list = db.getDeBoardList();
            db.close();
            if (list != null && list.size() > 0) {
                Util.showToast(mContext, getResources().getString(R.string.deboard_passenger));
            } else {

                new AlertDialog.Builder(TripActiity.this)
                        .setTitle(getResources().getString(R.string.end_trip_msg))
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                Util.setPrefData("tripEndId",RouteId,mContext);
                                checkForAddPassenger();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).show();

            }
        } else {
            Util.showToast(mContext, getResources().getString(R.string.no_internet));
        }
    }

//    @OnClick(R.id.llDirections)
//    public void onDirectionClick(){
//        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                Uri.parse("http://maps.google.com/maps?saddr=28.4595,77.0266&daddr=28.5562,77.1000"));
//        startActivity(intent);
//    }

    @Override
    public void onBackPressed() {

    }

    public void onBoardDropOff(View view) {
        if (view.getId() == R.id.llOnBoard) {
            setOnBoardList();
            tvOnBoard.setTextColor(getResources().getColor(R.color.white));
            tvDropOff.setTextColor(getResources().getColor(R.color.black));
            imgOnBoard.setColorFilter(ContextCompat.getColor(mContext, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            imgDropOff.setColorFilter(ContextCompat.getColor(mContext, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
            llOnBoard.setBackgroundResource(R.drawable.bg_top_corner_onboard);
            llDropOff.setBackground(null);
            imgArwOnBoard.setVisibility(View.VISIBLE);
            imgArwDropOff.setVisibility(View.INVISIBLE);
        } else {
            tvDropOff.setTextColor(getResources().getColor(R.color.white));
            tvOnBoard.setTextColor(getResources().getColor(R.color.black));
            imgOnBoard.setColorFilter(ContextCompat.getColor(mContext, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
            imgDropOff.setColorFilter(ContextCompat.getColor(mContext, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            llOnBoard.setBackground(null);
            llDropOff.setBackgroundResource(R.drawable.bg_top_corner_onboard);
            imgArwOnBoard.setVisibility(View.INVISIBLE);
            imgArwDropOff.setVisibility(View.VISIBLE);
            setDropOffList();
        }
    }

    private void setOnBoardList() {
//        rvPassenger.setAdapter(homeTripAdapter);
//        rvPassenger.setVisibility(View.VISIBLE);
        getPassengers("ONBOARD");
    }

    private void setDropOffList() {
//        if (!isDropCalled){
        getPassengers("DEBOARD");
//        } else {
//            rvPassenger.setAdapter(tripDropOffAdapter);
//        }
    }

    public void onBottomClick(View view) {
        switch (view.getId()) {
            case R.id.llAddPassenger:
//                new AddPassengerDialog(mContext,"").show(getSupportFragmentManager(),"");
//                openAddPassenger();
//                AddPassengerDialog addPassengerDialog = new AddPassengerDialog(TripActiity.this, RouteId, TimeSlot);
//                addPassengerDialog.show(getSupportFragmentManager(), "");
                mContextAP = TripActiity.this;
               Intent inap = new Intent(mContext,AddPassengerActivity.class);
               startActivity(inap);
                break;
            case R.id.llIssueCard:
//                new IssueCardDialog(mContext,"").show(getSupportFragmentManager(),"");
//                openIssueCard();
//                IssueCardDialog issueCardDialog = new IssueCardDialog(TripActiity.this,true);
//                issueCardDialog.show(getSupportFragmentManager(), "");
                Intent inIc = new Intent(mContext,IssueCardActivity.class);
                inIc.putExtra("isShg",true);
                startActivity(inIc);
                break;
            case R.id.llTopupCard:
//                new TopUpCard(mContext,"").show(getSupportFragmentManager(),"");
//                openTopupCard();
//                TopUpDialog topUpDialog = new TopUpDialog(TripActiity.this,true);
//                topUpDialog.show(getSupportFragmentManager(), "");
                Intent in = new Intent(mContext,AddTopUpActivity.class);
                in.putExtra("isShg",true);
                startActivity(in);
                break;
        }
    }

    Dialog dialog;

    public void openDropOffDialog(final PassengerData passengerData) {
//        DropOffDialog dropOffDialog = new DropOffDialog(mContext, passengerData);
//        dropOffDialog.show(getSupportFragmentManager(), "");
        DeBoardActivity.mContextDB = TripActiity.this;
        DeBoardActivity.passengerData = passengerData;
        Intent in = new Intent(mContext,DeBoardActivity.class);
        startActivity(in);
    }


    private DbHelper db;
    private boolean isOnbData = false,isDebData = false;

    public void getPassengers(String status) {

        if (Util.isNetworkAvailable(mContext)) {
            if (status.equals("ONBOARD")) {
                db.open();
                ArrayList<PassengerData> list = db.getOnBoardList();
                db.close();
                if (list != null && list.size() > 0) {
                    Gson gson = new Gson();
                    Log.e("ttt","Resp"+gson.toJson(list));
                    isOnbData = true;
                    rvPassenger.setVisibility(View.VISIBLE);
                    homeTripAdapter = new TripOnBoardAdapter(mContext, list, getSupportFragmentManager());
                    rvPassenger.setAdapter(homeTripAdapter);
                    return;
                } else {
                    rvPassenger.setVisibility(View.GONE);
                }
            } else {
                db.open();
                ArrayList<PassengerData> list = db.getDeBoardList();
                db.close();
                if (list != null && list.size() > 0) {
                    Gson gson = new Gson();
                    Log.e("ttt","Resp"+gson.toJson(list));
                    isDebData = true;
                    rvPassenger.setVisibility(View.VISIBLE);
                    tripDropOffAdapter = new TripDropOffAdapter(mContext, list, getSupportFragmentManager());
                    rvPassenger.setAdapter(tripDropOffAdapter);
                    return;
                } else {
                    rvPassenger.setVisibility(View.GONE);
                }
            }

            if (isOnbData){
                return;
            }

            if (isDebData){
                return;
            }

            if (Util.getPrefData("tripEndId",mContext).equals(RouteId)){
                return;
            }

            pd.show();

            Date currentTime = Calendar.getInstance().getTime();

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = df.format(currentTime);
            date = date.replace(" ", "T");
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("RouteId", RouteId);
            jsonObject.addProperty("TimeSlot", TimeSlot);
//            jsonObject.addProperty("TravelDate",date);
            jsonObject.addProperty("TravelDate", date);
            jsonObject.addProperty("TripStatus", status);
            jsonObject.addProperty("LanguageCode", Util.getPrefData(SEL_LANG,TripActiity.this));
            apiInterface.getPassengersList(Util.getPrefData(TOKEN, mContext), Util.getPrefData(DEVICE_ID, mContext), Constant.CONTENT_TYPE, Constant.CONTENT_TYPE, jsonObject).enqueue(new Callback<ArrayList<PassengerData>>() {
                @Override
                public void onResponse(Call<ArrayList<PassengerData>> call, Response<ArrayList<PassengerData>> response) {
                    pd.dismiss();
                    if (response.code()==401){
                        Util.redirecttoLogin(mContext);
                        return;
                    }
                    if (response.code() == 200) {

                        if (response.body() != null && response.body().size() > 0) {
                            Gson gson = new Gson();
                            Log.e("ttt","Resp"+gson.toJson(response.body()));
                            rvPassenger.setVisibility(View.VISIBLE);
                            if (status.equals("ONBOARD")) {
                                db.open();
                                db.insertOnBoardData(response.body());
                                db.close();
                                homeTripAdapter = new TripOnBoardAdapter(mContext, response.body(), getSupportFragmentManager());
                                rvPassenger.setAdapter(homeTripAdapter);
                            } else {
                                db.open();
                                db.insertDeBoardData(response.body());
                                db.close();
                                tripDropOffAdapter = new TripDropOffAdapter(mContext, response.body(), getSupportFragmentManager());
                                rvPassenger.setAdapter(tripDropOffAdapter);
                            }
                        } else {
                            rvPassenger.setVisibility(View.GONE);
                        }
                    } else {
                        Util.showToast(mContext, getResources().getString(R.string.smthng_wrong));
                    }

                }

                @Override
                public void onFailure(Call<ArrayList<PassengerData>> call, Throwable t) {
                    pd.dismiss();
                    Util.showToast(mContext, getResources().getString(R.string.smthng_wrong));
                }
            });

        } else {
            if (status.equals("ONBOARD")) {
                db.open();
                ArrayList<PassengerData> list = db.getOnBoardList();
                db.close();
                if (list != null && list.size() > 0) {
                    rvPassenger.setVisibility(View.VISIBLE);
                    homeTripAdapter = new TripOnBoardAdapter(mContext, list, getSupportFragmentManager());
                    rvPassenger.setAdapter(homeTripAdapter);
                } else {
                    rvPassenger.setVisibility(View.GONE);
                }
            } else {
                db.open();
                ArrayList<PassengerData> list = db.getDeBoardList();
                db.close();
                if (list != null && list.size() > 0) {
                    rvPassenger.setVisibility(View.VISIBLE);
                    tripDropOffAdapter = new TripDropOffAdapter(mContext, list, getSupportFragmentManager());
                    rvPassenger.setAdapter(tripDropOffAdapter);
                    return;
                } else {
                    rvPassenger.setVisibility(View.GONE);
                }
            }
//            Util.showToast(mContext,getResources().getString(R.string.no_internet));
        }

    }


    public void onBoard(final PassengerData passengerData) {
//        OnBoardDialog onBoardDialog = new OnBoardDialog(TripActiity.this, passengerData);
//        onBoardDialog.show(getSupportFragmentManager(), "");

        OnBoardActivity.mContextOB = TripActiity.this;
        OnBoardActivity.passengerData = passengerData;
        Intent in = new Intent(mContext,OnBoardActivity.class);
        startActivity(in);
    }

    @OnClick(R.id.imgCall)
    public void onCall() {

        try {
            mContext.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Util.getPrefData(SHG_NUMBER, mContext))));
        } catch (Exception e) {
        }

    }

    private void getDirectionList() {
        if (Util.isNetworkAvailable(mContext)) {
            if (!RouteId.equals(Util.getPrefData(ROUTE_ID_DIRECTION, mContext))) {
                JsonObject jsonObject = new JsonObject();

                jsonObject.addProperty("Id", RouteId);
                jsonObject.addProperty("LanguageCode", Util.getPrefData(SEL_LANG,TripActiity.this));
                apiInterface.getDirections(Util.getPrefData(TOKEN, mContext), Util.getPrefData(DEVICE_ID, mContext), Constant.CONTENT_TYPE, Constant.CONTENT_TYPE, jsonObject).enqueue(new Callback<DirectionResponse>() {
                    @Override
                    public void onResponse(Call<DirectionResponse> call, Response<DirectionResponse> response) {
                        if (response.code()==401){
                            Util.redirecttoLogin(mContext);
                            return;
                        }
                        if (response.code() == 200 && response.body() != null) {

                            Gson gson = new Gson();
                            String json = gson.toJson(response.body());

                            Util.setPrefData(ROUTE_ID_DIRECTION, RouteId, mContext);
                            Util.setPrefData(DIRECTION_LIST, json, mContext);

                        }

                    }

                    @Override
                    public void onFailure(Call<DirectionResponse> call, Throwable t) {
                    }
                });
            }

        }
    }

    private void getShgDetails() {
        if (Util.isNetworkAvailable(mContext)) {
            if (!RouteId.equals(Util.getPrefData(ROUTE_ID_SHG, mContext))) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("RouteScheduleSlotId", RouteId);
                apiInterface.getShgDetails(Util.getPrefData(TOKEN, mContext), Util.getPrefData(DEVICE_ID, mContext), Constant.CONTENT_TYPE, Constant.CONTENT_TYPE, jsonObject).enqueue(new Callback<ArrayList<RouteScheduleSlotDetails>>() {
                    @Override
                    public void onResponse(Call<ArrayList<RouteScheduleSlotDetails>> call, Response<ArrayList<RouteScheduleSlotDetails>> response) {
                        if (response.code()==401){
                            Util.redirecttoLogin(mContext);
                            return;
                        }
                        if (response.code() == 200 && response.body() != null) {

                            Util.setPrefData(ROUTE_ID_SHG, RouteId, mContext);
                            try {
                                Util.setPrefData(SHG_NUMBER, response.body().get(0).getSHGContactNumber(), mContext);
                            } catch (Exception e) {
                                Util.setPrefData(SHG_NUMBER, "", mContext);
                            }

                        }

                    }

                    @Override
                    public void onFailure(Call<ArrayList<RouteScheduleSlotDetails>> call, Throwable t) {
                    }
                });
            }

        }
    }

    @OnClick(R.id.llDirections)
    public void onDirectionClick() {

        try {


            Gson gson = new Gson();
            String json = Util.getPrefData(DIRECTION_LIST, mContext).trim();
            Log.e("ttt", json);
            Type type = new TypeToken<DirectionResponse>() {
            }.getType();
            DirectionResponse response = gson.fromJson(json, type);
            ArrayList<SubRouteData> list = response.getSubRoutes();


            String origin = "", destination = "", waypoints = "";

            for (int i = 0; i < list.size(); i++) {
                SubRouteData sb = list.get(i);
                if (i == 0) {
                    origin = sb.getLattitude() + "," + sb.getLongitude();
                } else if (i == (list.size() - 1)) {
                    destination = sb.getLattitude() + "," + sb.getLongitude();
                } else {
                    if (waypoints.trim().length() == 0) {
                        waypoints = sb.getLattitude() + "," + sb.getLongitude();
                    } else {
                        waypoints = waypoints + "|" + sb.getLattitude() + "," + sb.getLongitude();
                    }
                }
            }

            if (list != null && list.size() > 0) {
//                                Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=18.519513,73.868315&destination=18.518496,73.879259&waypoints=18.520561,73.872435|18.519254,73.876614|18.52152,73.877327|18.52019,73.879935&travelmode=driving");
                Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=" + origin + "&destination=" + destination + "&waypoints=" + waypoints + "&travelmode=driving");
                Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                intent.setPackage("com.google.android.apps.maps");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    try {
                        Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        startActivity(unrestrictedIntent);
                    } catch (ActivityNotFoundException innerEx) {
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
