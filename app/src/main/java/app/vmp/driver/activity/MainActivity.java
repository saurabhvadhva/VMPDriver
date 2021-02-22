package app.vmp.driver.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pax.dal.IDAL;
import com.pax.neptunelite.api.NeptuneLiteUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import app.vmp.driver.Database.DbHelper;
import app.vmp.driver.R;
import app.vmp.driver.api.APIClient;
import app.vmp.driver.api.APIInterface;
import app.vmp.driver.bottomsheet.IssueCardDialog;
import app.vmp.driver.bottomsheet.OnBoardDialog;
import app.vmp.driver.bottomsheet.TopUpDialog;
import app.vmp.driver.model.GeneralResponse;
import app.vmp.driver.model.Options;
import app.vmp.driver.model.TripDetail;
import app.vmp.driver.picc.PiccTester;
import app.vmp.driver.utils.CircleTransform;
import app.vmp.driver.utils.Constant;
import app.vmp.driver.utils.LocationService;
import app.vmp.driver.utils.MyApp;
import app.vmp.driver.utils.Util;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.vmp.driver.Database.DbHelper.TAG_TOPUP_AMOUNT;
import static app.vmp.driver.Database.DbHelper.TAG_TOPUP_NFCID;
import static app.vmp.driver.Database.DbHelper.TAG_TOPUP_TIMESTAMP;
import static app.vmp.driver.utils.Constant.BASE_URL;
import static app.vmp.driver.utils.Constant.CURONG_ROUTE_ID;
import static app.vmp.driver.utils.Constant.CURONG_ROUTE_NAME;
import static app.vmp.driver.utils.Constant.CURONG_TIME_SLOT;
import static app.vmp.driver.utils.Constant.DEVICE_ID;
import static app.vmp.driver.utils.Constant.EMPLOYEE_ID;
import static app.vmp.driver.utils.Constant.FULL_NAME;
import static app.vmp.driver.utils.Constant.NGO_ID;
import static app.vmp.driver.utils.Constant.PROFESSION_TYPE;
import static app.vmp.driver.utils.Constant.TOKEN;
import static app.vmp.driver.utils.Constant.USER_ID;
import static app.vmp.driver.utils.Constant.USER_NAME;
import static app.vmp.driver.utils.Constant.USER_PHOTO;

public class MainActivity extends BaseActivity {

    private ProgressDialog pd;
    private APIInterface apiInterface;
    private Context mContext;

    @BindView(R.id.imgUser)
    ImageView imgUser;
    @BindView(R.id.tvName)
    AppCompatTextView tvName;

    public static IDAL idal = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        try {
            idal = NeptuneLiteUser.getInstance().getDal(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        init();
    }

    @OnClick(R.id.llAddExpense)
    public void goToAddExpense(){
        if (Util.isNetworkAvailable(mContext)) {
            Intent in = new Intent(mContext,AddExpenseActivity.class);
            startActivity(in);
        } else {
            Util.showToast(mContext, getResources().getString(R.string.no_internet));
        }
    }

    private void init() {
        mContext = MainActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
        pd.setMessage(mContext.getResources().getString(R.string.please_wait));
        Log.e("ttt",Util.getPrefData(TOKEN, mContext));
        Log.e("ttt",Util.getPrefData(DEVICE_ID, mContext));
        Log.e("ttt",Util.getPrefData(NGO_ID, mContext));
        Picasso.get().load( BASE_URL+Util.getPrefData(USER_PHOTO, mContext)).transform(new CircleTransform()).error(R.drawable.icon_male).into(imgUser);
        tvName.setText(Util.getPrefData(FULL_NAME, mContext));
        getOptions();
        getTripDetails();
    }

    @OnClick(R.id.imgLanguage)
    public void onLangChange() {
        Intent in = new Intent(MainActivity.this, LanguageActivity.class);
        in.putExtra("from","home");
        startActivity(in);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    @OnClick(R.id.imgLogout)
    public void onLogout() {
        if (Util.isNetworkAvailable(mContext)) {
            DbHelper db = new DbHelper(mContext);
            db.open();
            ArrayList<HashMap<String, String>> list = db.getTopUpListList();
            db.close();
            if (list == null || list.size() == 0) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(getResources().getString(R.string.logout_msg))
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                apiLogout();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).show();
            } else {
                if (!MyApp.getInstance().returnTopUpStarted()){
                    MyApp.getInstance().checkforTopUp();
                }
                Util.showToast(mContext, getResources().getString(R.string.topuop_pending));
            }
        } else {
            Util.showToast(mContext, getResources().getString(R.string.no_internet));
        }

    }

    private void apiLogout() {

        if (Util.isNetworkAvailable(mContext)) {
            pd.show();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("UserId",Util.getPrefData(USER_ID,mContext));
            apiInterface.logout(Util.getPrefData(TOKEN, mContext),Util.getPrefData(DEVICE_ID, mContext),Constant.CONTENT_TYPE, Constant.CONTENT_TYPE, jsonObject).enqueue(new Callback<GeneralResponse>() {
                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                  pd.dismiss();
                    if (response.code()==401){
                        Util.redirecttoLogin(mContext);
                        return;
                    }
                    if (response.code() == 200 && response.body()!=null) {

                        try {
                            Intent serviceIntent = new Intent(mContext, LocationService.class);
                            stopService(serviceIntent);
                        } catch (Exception e){}

                            Util.setPrefData(USER_ID, "", mContext);
                            startActivity(new Intent(mContext, LoginActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
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



    @Override
    protected void onResume() {
        super.onResume();

    }





    @OnClick(R.id.cvPickup)
    public void onPickupCall() {
        Intent in = new Intent(mContext, TodayTripActivity.class);
        startActivity(in);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    @OnClick(R.id.cvIssueCard)
    public void issueCard() {
//        IssueCardDialog issueCardDialog = new IssueCardDialog(MainActivity.this,false);
//        issueCardDialog.show(getSupportFragmentManager(), "");
        Intent in = new Intent(mContext,IssueCardActivity.class);
        in.putExtra("isShg",false);
        startActivity(in);
    }

    @OnClick(R.id.cvTopUpCard)
    public void topupCard() {
//        TopUpDialog topUpDialog = new TopUpDialog(MainActivity.this,false);
//        topUpDialog.show(getSupportFragmentManager(), "");
        Intent in = new Intent(mContext,AddTopUpActivity.class);
        in.putExtra("isShg",false);
        startActivity(in);
    }

    private void getTripDetails() {

        if (Util.isNetworkAvailable(mContext)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("DriverId",Util.getPrefData(EMPLOYEE_ID,mContext));
            apiInterface.getTripDetails(Util.getPrefData(TOKEN, mContext),Util.getPrefData(DEVICE_ID, mContext),Constant.CONTENT_TYPE, Constant.CONTENT_TYPE, jsonObject).enqueue(new Callback<TripDetail>() {
                @Override
                public void onResponse(Call<TripDetail> call, Response<TripDetail> response) {
                    if (response.code()==401){
                        Util.redirecttoLogin(mContext);
                        return;
                    }
                    if (response.code() == 200) {
                        if (response.body()!=null && response.body().getRouteId()!=null){
                            Intent in = new Intent(mContext, TripActiity.class);
                            in.putExtra("RouteId",response.body().getRouteId());
                            in.putExtra("TimeSlot",response.body().getTimeSlot());
                            in.putExtra("RouteName",response.body().getRouteName());
                            startActivity(in);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                        }
                    }
                }
                @Override
                public void onFailure(Call<TripDetail> call, Throwable t) {
                }
            });

        } else {
            if (Util.getPrefData(CURONG_ROUTE_ID,mContext)!=null && Util.getPrefData(CURONG_ROUTE_ID,mContext).trim().length()>0){
                Intent in = new Intent(mContext, TripActiity.class);
                in.putExtra("RouteId",Util.getPrefData(CURONG_ROUTE_ID,mContext));
                in.putExtra("TimeSlot",Util.getPrefData(CURONG_TIME_SLOT,mContext));
                in.putExtra("RouteName",Util.getPrefData(CURONG_ROUTE_NAME,mContext));
                startActivity(in);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        }

    }

    private void getOptions() {

        if (Util.isNetworkAvailable(mContext)) {

            pd.show();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("OptionType", "PROFESSIONTYPE");
            apiInterface.getOptionsType(Util.getPrefData(TOKEN, mContext),Util.getPrefData(DEVICE_ID, mContext),Constant.CONTENT_TYPE, Constant.CONTENT_TYPE, jsonObject).enqueue(new Callback<ArrayList<Options>>() {
                @Override
                public void onResponse(Call<ArrayList<Options>> call, Response<ArrayList<Options>> response) {
                    pd.dismiss();
                    if (response.code()==401){
                        Util.redirecttoLogin(mContext);
                        return;
                    }
                    if (response.code() == 200) {

                        Gson gson = new Gson();
                        String json = gson.toJson(response.body());

                        Util.setPrefData(PROFESSION_TYPE, json, mContext);
                    } else {
                        Util.showToast(mContext, getResources().getString(R.string.smthng_wrong));
                    }

                }

                @Override
                public void onFailure(Call<ArrayList<Options>> call, Throwable t) {
                    pd.dismiss();
                    Util.showToast(mContext, getResources().getString(R.string.smthng_wrong));
                }
            });

        } else {
            Util.showToast(mContext, getResources().getString(R.string.no_internet));
        }

    }


}
