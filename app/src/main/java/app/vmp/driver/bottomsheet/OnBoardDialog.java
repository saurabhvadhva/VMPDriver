package app.vmp.driver.bottomsheet;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pax.dal.entity.EM1KeyType;
import com.pax.dal.entity.EPiccType;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import app.vmp.driver.Database.DbHelper;
import app.vmp.driver.R;
import app.vmp.driver.activity.TripActiity;
import app.vmp.driver.api.APIClient;
import app.vmp.driver.api.APIClientLocation;
import app.vmp.driver.api.APIInterface;
import app.vmp.driver.model.DistrictData;
import app.vmp.driver.model.GeneralResponse;
import app.vmp.driver.model.NFCCardDetails;
import app.vmp.driver.model.NextStopDetails;
import app.vmp.driver.model.Options;
import app.vmp.driver.model.PassengerData;
import app.vmp.driver.model.StartLocation;
import app.vmp.driver.picc.PiccTester;
import app.vmp.driver.util.Convert;
import app.vmp.driver.utils.CircleTransform;
import app.vmp.driver.utils.Constant;
import app.vmp.driver.utils.Util;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.vmp.driver.utils.Constant.API_GET_LOCATIONS;
import static app.vmp.driver.utils.Constant.BASE_URL;
import static app.vmp.driver.utils.Constant.DEVICE_ID;
import static app.vmp.driver.utils.Constant.EMPLOYEE_ID;
import static app.vmp.driver.utils.Constant.LOCATION_LIST;
import static app.vmp.driver.utils.Constant.NGO_ID;
import static app.vmp.driver.utils.Constant.PROFESSION_TYPE;
import static app.vmp.driver.utils.Constant.SHG_NUMBER;
import static app.vmp.driver.utils.Constant.STATE_ID;
import static app.vmp.driver.utils.Constant.TOKEN;
import static app.vmp.driver.utils.Constant.USER_ID;

public class OnBoardDialog extends DialogFragment {
    String part1, part2, part3, part4, part5;
    Double newdata = 0.00;
    private EPiccType piccType;
    private EM1KeyType m1KeyType = EM1KeyType.TYPE_A;
    private Context mContext;
    private ProgressDialog pd;
    private APIInterface apiInterface;
    private APIInterface apiInterface2;
    private PassengerData passengerData;
    public DetectMThreadnew detectMThreadnew;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:

                    if (status == 0) {


                        if (msg.obj.toString().equalsIgnoreCase("can't find card !")) {
                            textView.setText("can't find card !");
                            etCardNumber.setText("can't find card !");
                            {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("")
                                        .setMessage(mContext.getResources().getString(R.string.nfc_not_found))
                                        .setCancelable(true)
                                        .setPositiveButton(mContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {

                                                PiccTester.getInstance(piccType).close();
                                                if (detectMThreadnew != null) {
                                                    detectMThreadnew.interrupt();
                                                    detectMThreadnew = null;
                                                }
                                                PiccTester.getInstance(piccType).open();
                                                detectMThreadnew = new DetectMThreadnew();
                                                detectMThreadnew.start();

                                            }
                                        }).create().show();
                            }
                        } else {

                            String[] parts = msg.obj.toString().split(" , ", 5);

                            part1 = parts[0];
                            part2 = parts[1];
                            part3 = parts[2];
                            part4 = parts[3];
                            part5 = parts[4];
//

                            textView.setText(part1);
                            etCardNumber.setText(part1);


                            if (Util.isNullE(etCardNumber)) {
                                Util.showToast(mContext, mContext.getResources().getString(R.string.please_enter_card_number));
                            } else {

//                                if (Util.isNetworkAvailable(mContext)) {
//
//                                    searchCard();
//                                } else {
                                    nfcsearchcard();
//                                }


                            }
                        }
                    } else if (status == 1) {

                        if (msg.obj.toString().equalsIgnoreCase("can't find card !")) {

                            new AlertDialog.Builder(getActivity())
                                    .setTitle("")
                                    .setMessage(mContext.getResources().getString(R.string.attach_nfc))
                                    .setCancelable(true)
                                    .setPositiveButton(mContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            //   dismiss();
                                        }


                                    }).create().show();


                        } else {

                        }

                    }
                    break;
                default:
                    break;
            }
        }

        ;

    };

    public OnBoardDialog(Context context, final PassengerData passengerData) {
        mContext = context;
        this.passengerData = passengerData;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        apiInterface2 = APIClientLocation.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
        pd.setMessage(mContext.getResources().getString(R.string.please_wait));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_pickup_passenger, container, false);
        ButterKnife.bind(this, rootView);

        PiccTester.getInstance(piccType).close();
        if (detectMThreadnew != null) {
            detectMThreadnew.interrupt();
            detectMThreadnew = null;
        }

        PiccTester.getInstance(piccType).open();
        detectMThreadnew = new DetectMThreadnew();
        detectMThreadnew.start();

        tvNamePassenger.setText(passengerData.getPassengerName());

//        Picasso.get().load(BASE_URL+ passengerData.getCoPassenger()).transform(new CircleTransform()).error(R.drawable.ic_leader_demo).into(imgUser);
        if (passengerData.getTypeOfPassenger() == null || passengerData.getTypeOfPassenger().trim().length()==0 || passengerData.getTypeOfPassenger().equals("null")) {
            Picasso.get().load(R.drawable.icon_common).transform(new CircleTransform()).into(imgUser);
        } else if (passengerData.getTypeOfPassenger().equals("M")) {
            Picasso.get().load(BASE_URL + passengerData.getProfileImage()).placeholder(R.drawable.icon_male).error(R.drawable.icon_male).transform(new CircleTransform()).into(imgUser);
        } else {
            Picasso.get().load(BASE_URL + passengerData.getProfileImage()).placeholder(R.drawable.icon_female).error(R.drawable.icon_female).transform(new CircleTransform()).into(imgUser);
        }
        tvTopUpValue1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etAmount.setText(tvTopUpValue1.getText().toString());
            }
        });

        tvTopUpValue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etAmount.setText(tvTopUpValue2.getText().toString());
            }
        });

        tvTopUpValue3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etAmount.setText(tvTopUpValue3.getText().toString());
            }
        });

        tvTopUpValue4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etAmount.setText(tvTopUpValue4.getText().toString());
            }
        });
        getLocationList();
        return rootView;
    }

    @OnClick(R.id.btnShg)
    public void callShg() {
        try {
            mContext.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Util.getPrefData(SHG_NUMBER, mContext))));
        } catch (Exception e) {
        }
    }

    @OnClick(R.id.llTopupCard)
    public void onTopUp() {
        llTapcards.setVisibility(View.GONE);
        llSubmit.setVisibility(View.GONE);
        llBalance.setVisibility(View.VISIBLE);
        //  llNext.setVisibility(View.VISIBLE);
        llNext.setVisibility(View.GONE);
    }

    private int status = 0;

    @OnClick(R.id.llTapOtherCard)
    public void onOtherCard() {
        status = 0;
        llTapCard.setVisibility(View.VISIBLE);
        llCardDetails.setVisibility(View.GONE);
        // llNext.setVisibility(View.VISIBLE);
        llNext.setVisibility(View.GONE);
        locationId = "";
        llBalance.setVisibility(View.GONE);
        llTapcards.setVisibility(View.VISIBLE);
        llSubmit.setVisibility(View.VISIBLE);
        try {
            spinLocation.setSelection(0);
        } catch (Exception e) {
        }
    }

    private ArrayList<String> listLocation;

    private void getLocationList() {
//        if (Util.isNetworkAvailable(mContext)) {

//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("RouteId",passengerData.getRouteId());
//             Log.e("ttt",jsonObject.toString());
//             String url = API_GET_LOCATIONS+"RouteId="+passengerData.getRouteId();
//            apiInterface.getLocationList(Util.getPrefData(TOKEN, mContext),Util.getPrefData(DEVICE_ID, mContext),Constant.CONTENT_TYPE,Constant.CONTENT_TYPE,url).enqueue(new Callback<ArrayList<StartLocation>>() {
//                @Override
//                public void onResponse(Call<ArrayList<StartLocation>> call, Response<ArrayList<StartLocation>> response) {
//                    if (response.code()==200 && response.body()!=null){
//                        Log.e("ttt","S"+response.body().size());
        Gson gson = new Gson();
        String json = Util.getPrefData(LOCATION_LIST, mContext).trim();
        Type type = new TypeToken<ArrayList<StartLocation>>() {
        }.getType();
        ArrayList<StartLocation> districtData = gson.fromJson(json, type);
        listLocation = new ArrayList<>();
        listLocation.add("Select Location");
        for (int i = 0; i < districtData.size(); i++) {
            StartLocation data = districtData.get(i);
            listLocation.add(data.getLocationName());
        }
        if (listLocation.size()>1){
            listLocation.remove((listLocation.size()-1));
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, listLocation);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinLocation.setAdapter(adapter);
        spinLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                locationId = "";
                if (i != 0) {
                    locationId = districtData.get(i - 1).getSubRouteId();
                    locationName = districtData.get(i - 1).getLocationName();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        getDriverLocation();
//        } else {
//            Util.showToast(mContext, getResources().getString(R.string.smthng_wrong));
//        }

//                }
//
//                @Override
//                public void onFailure(Call<ArrayList<StartLocation>> call, Throwable t) {
//                    Util.showToast(mContext,getResources().getString(R.string.smthng_wrong));
//                }
//            });
//
//
//        } else {
//            Util.showToast(mContext,getResources().getString(R.string.no_internet));
//        }
    }

    private FusedLocationProviderClient mFusedLocationClient;

    private void getDriverLocation() {
        if (Util.isNetworkAvailable(mContext)) {
            try {
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("RouteId", passengerData.getRouteId());
                            jsonObject.addProperty("DriverId", Util.getPrefData(EMPLOYEE_ID, mContext));
                            jsonObject.addProperty("Lat", "" + location.getLatitude());
                            jsonObject.addProperty("Lng", "" + location.getLongitude());
                            apiInterface2.getDriverLocationDetails(Util.getPrefData(TOKEN, mContext), Util.getPrefData(DEVICE_ID, mContext), Constant.CONTENT_TYPE, Constant.CONTENT_TYPE, jsonObject).enqueue(new Callback<NextStopDetails>() {
                                @Override
                                public void onResponse(Call<NextStopDetails> call, Response<NextStopDetails> response) {
                                    if (response.code()==401){
                                        Util.redirecttoLogin(mContext);
                                        return;
                                    }
                                    try {
                                        if (response.code() == 200 && response.body() != null && response.body().getLocationName() != null) {
//                            tvNextStop.setText(response.body().getLocationName());
                                            if (listLocation.contains(response.body().getLocationName())) {
                                                spinLocation.setSelection(listLocation.indexOf(response.body().getLocationName()));
                                            }
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
                });
            } catch (Exception e) {
            }


        }
    }

    private String locationId = "", locationName = "";

    @OnClick(R.id.icClose)
    public void onCancel() {
        dismiss();
    }

    @BindView(R.id.llTapCard)
    LinearLayout llTapCard;
    @BindView(R.id.llCardDetails)
    LinearLayout llCardDetails;
    @BindView(R.id.llNext)
    LinearLayout llNext;
    @BindView(R.id.llTapcards)
    LinearLayout llTapcards;
    @BindView(R.id.llBalance)
    LinearLayout llBalance;
    @BindView(R.id.llSubmit)
    LinearLayout llSubmit;

    @BindView(R.id.etCardNumber)
    AppCompatEditText etCardNumber;
    @BindView(R.id.spinLocation)
    AppCompatSpinner spinLocation;

    @BindView(R.id.imgUser)
    ImageView imgUser;
    @BindView(R.id.tvNamePassenger)
    AppCompatTextView tvNamePassenger;
    @BindView(R.id.tvBalance)
    AppCompatTextView tvBalance;
    @BindView(R.id.tvCardNumber)
    AppCompatTextView tvCardNumber;
    @BindView(R.id.tvName)
    AppCompatTextView tvName;

    @BindView(R.id.tvTopUpValue1)
    AppCompatTextView tvTopUpValue1;
    @BindView(R.id.tvTopUpValue2)
    AppCompatTextView tvTopUpValue2;
    @BindView(R.id.tvTopUpValue3)
    AppCompatTextView tvTopUpValue3;
    @BindView(R.id.tvTopUpValue4)
    AppCompatTextView tvTopUpValue4;

    @BindView(R.id.chk)
    AppCompatCheckBox chk;

    @BindView(R.id.etAmount)
    AppCompatEditText etAmount;

    @BindView(R.id.fragment_picc_detect_m_textview)
    AppCompatTextView textView;

    @BindView(R.id.fragment_picc_detect_m_keytype)
    Button keyTypeBt;

    @BindView(R.id.fragment_picc_detect_m_blocknum)
    EditText blockNumEt;
    @BindView(R.id.fragment_picc_detect_m_password)
    EditText passwordEt;

    @BindView(R.id.nfclayout)
    LinearLayout nfclayout;

    @OnClick(R.id.llSubmit)
    public void onSubmit() {
        if (locationId.trim().length() == 0) {
            Util.showToast(mContext, getResources().getString(R.string.msg_location));
        } else {
//        if (Util.isNetworkAvailable(mContext)) {
//
//                pd.show();
//                JsonObject jsonObject = new JsonObject();
//                jsonObject.addProperty("DailyCharterId", passengerData.getDailyCharterId());
//                jsonObject.addProperty("TypeOfPassenger", passengerData.getTypeOfPassenger());
//                jsonObject.addProperty("DemandId", passengerData.getID());
//                jsonObject.addProperty("PassengerId", passengerData.getPassengerId());
//                jsonObject.addProperty("StartLocationId", locationId);
//                jsonObject.addProperty("UserId", Util.getPrefData(USER_ID, mContext));
//                jsonObject.addProperty("NFCCardId", NFCCardId);
//                Log.e("ttt", "js" + jsonObject.toString());
//                apiInterface.onBoardPassenger(Util.getPrefData(TOKEN, mContext), Util.getPrefData(DEVICE_ID, mContext), Constant.CONTENT_TYPE, Constant.CONTENT_TYPE, jsonObject).enqueue(new Callback<GeneralResponse>() {
//                    @Override
//                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
//                        pd.dismiss();
//                        if (response.code() == 200 && response.body() != null) {
//                            Log.e("ttt", "js" + response.body().getMsg());
//                            Util.showToast(mContext, response.body().getMsg());
//                            if (response.body().getCode().equals("000")) {
//                                dismiss();
//                                ((TripActiity) mContext).getPassengers("ONBOARD");
//                            }
//
//                        } else {
//                            Util.showToast(mContext, getResources().getString(R.string.card_not_found));
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
//                        pd.dismiss();
//                        Util.showToast(mContext, getResources().getString(R.string.smthng_wrong));
//                    }
//                });
//
//            }
//        else {
            DbHelper db = new DbHelper(mContext);
            db.open();
            boolean isContinue = false;
            if (passengerData.getIsOffline() != null && passengerData.getIsOffline().equals("true")) {
                isContinue = true;
                db.deleteOnBoardItemByTimestamp(passengerData.getTimestamp());
            } else {
                if (passengerData.getID() == null || passengerData.getID().trim().length() == 0) {
                    Util.showToast(mContext, "Demaind ID required");
                } else if (passengerData.getDailyCharterId() == null || passengerData.getDailyCharterId().trim().length() == 0) {
                    Util.showToast(mContext, "DailyCharter ID required");
                } else {
                    isContinue = true;
                    db.deleteOnBoardItem(passengerData.getPassengerId());
                }
            }

            if (isContinue) {
                ArrayList<PassengerData> pp = new ArrayList<>();
                passengerData.setStartLocationId(locationId);
                StartLocation startLocation = new StartLocation();
                startLocation.setLocationName(locationName);
                passengerData.setStartRouteLocation(startLocation);
                pp.add(passengerData);
                db.insertDeBoardData(pp);
                db.insertOnBoardPassenger(passengerData.getDailyCharterId(), passengerData.getTypeOfPassenger(),
                        passengerData.getID(), passengerData.getPassengerId(), locationId, NFCCardId, passengerData.getTimestamp());
                db.close();
                dismiss();
                ((TripActiity) mContext).getPassengers("ONBOARD");
            } else {
                db.close();
            }

//            Util.showToast(mContext, getResources().getString(R.string.no_internet));
//        }
        }
    }

//    @OnClick(R.id.llNext)
//    public void onNext() {
//        searchCard();
//    }

//    @OnClick(R.id.llNext)
//    public void onNext() {
//
//        if (Util.isNullE(etCardNumber)) {
//            Util.showToast(mContext, mContext.getResources().getString(R.string.please_enter_card_number));
//        } else {
//            if (status == 0) {
//                if (Util.isNetworkAvailable(mContext)) {
//                    searchCard();
//                }
//                else{
//
//
//
//                }
//            } else {
//                if (Util.isNullE(etAmount)) {
//                    Util.showToast(mContext, mContext.getResources().getString(R.string.please_enter_amount));
//                } else {
//                    if (chk.isChecked()) {
//                        topupCard();
//                    } else {
//                        Util.showToast(mContext, mContext.getResources().getString(R.string.msg_payment));
//                    }
//                }
//            }
//
//        }
//    }
//


    private String NFCCardId = "";

    private void searchCard() {
        if (Util.isNetworkAvailable(mContext)) {

            pd.show();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("SearchKey", Util.valE(etCardNumber));
            jsonObject.addProperty("NGOId", Util.getPrefData(NGO_ID, mContext));
            Log.e("ttt",jsonObject.toString());
            apiInterface.searchNFCcard(Util.getPrefData(TOKEN, mContext), Util.getPrefData(DEVICE_ID, mContext), Constant.CONTENT_TYPE, Constant.CONTENT_TYPE, jsonObject).enqueue(new Callback<NFCCardDetails>() {
                @Override
                public void onResponse(Call<NFCCardDetails> call, Response<NFCCardDetails> response) {
                    pd.dismiss();
                    if (response.code()==401){
                        Util.redirecttoLogin(mContext);
                        return;
                    }
                    if (response.code() == 200 && response.body() != null && response.body().getNFCCardId() != null && !response.body().getNFCCardId().equals("0")) {
                        status = 1;
                        Gson gson = new Gson();
                           Log.e("ttt",gson.toJson(response.body()));
//        NFCCardId = "A1F63F2F-D53F-480E-9765-AE6A5D6987E0";
//        tvName.setText("samar");
//        tvBalance.setText("1419.0");
                        NFCCardId = response.body().getNFCTagId();
//                        NFCCardId = Util.valE(etCardNumber);
                        tvName.setText(response.body().getCardHolderName());
                        tvBalance.setText(response.body().getBalanceAmount());
                        tvCardNumber.setText(Util.valE(etCardNumber));
                        llNext.setVisibility(View.GONE);
                        llTapCard.setVisibility(View.GONE);
                        llCardDetails.setVisibility(View.VISIBLE);

                    } else {
                        Util.showToast(mContext, getResources().getString(R.string.card_not_found));
                    }

                }

                @Override
                public void onFailure(Call<NFCCardDetails> call, Throwable t) {
                    pd.dismiss();
                    Util.showToast(mContext, getResources().getString(R.string.smthng_wrong));
                }
            });


        } else {
            Util.showToast(mContext, getResources().getString(R.string.no_internet));
        }
    }

    private void topupCard() {
        if (Util.isNetworkAvailable(mContext)) {

            pd.show();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("NFCCardId", NFCCardId);
            jsonObject.addProperty("TransactionType", "TOPUP");
            jsonObject.addProperty("TransactionAmount", Util.valE(etAmount));
            jsonObject.addProperty("UserID", Util.getPrefData(USER_ID, mContext));
//            jsonObject.addProperty("UserID","5");
            apiInterface.topupNFCcard(Util.getPrefData(TOKEN, mContext), Util.getPrefData(DEVICE_ID, mContext), Constant.CONTENT_TYPE, Constant.CONTENT_TYPE, jsonObject).enqueue(new Callback<GeneralResponse>() {
                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                    pd.dismiss();
                    if (response.code()==401){
                        Util.redirecttoLogin(mContext);
                        return;
                    }
                    if (response.code() == 200) {
                        Util.showToast(mContext, response.body().getMsg());
                        if (response.body().getCode().equals("000")) {

                            llNext.setVisibility(View.GONE);
//                            llInsufiBalance.setVisibility(View.GONE);
//                            llSubmit.setVisibility(View.VISIBLE);

                            String balanceAmount = "" + (Double.parseDouble(tvBalance.getText().toString()) + Double.parseDouble(etAmount.getText().toString()));
                            tvBalance.setText(balanceAmount);
                            etAmount.setText("");
                            chk.setChecked(false);

                            status = 1;
                            llBalance.setVisibility(View.GONE);
                            llTapcards.setVisibility(View.VISIBLE);
                            llSubmit.setVisibility(View.VISIBLE);
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

        } else {
            Util.showToast(mContext, getResources().getString(R.string.no_internet));
        }
    }

    class DetectMThreadnew extends Thread {
        @Override
        public void run() {
            super.run();

            String blockStr = blockNumEt.getText().toString();
            int blockNum = Integer.parseInt(blockStr.equals("") ? blockNumEt.getHint().toString() : blockStr);
            String password = passwordEt.getText().toString().equals("") ? passwordEt.getHint().toString()
                    : passwordEt.getText().toString();
            Log.i("Test", "keyType:" + m1KeyType.name() + " blockNum:" + blockNum + " password:" + password + "  " + piccType);

            PiccTester.getInstance(piccType).detectMTopUpnew(handler, m1KeyType, blockNum, etAmount.getText().toString(),
                    Convert.getInstance().strToBcd(password.toUpperCase(Locale.ENGLISH), Convert.EPaddingPosition.PADDING_RIGHT));

        }
    }

    private String showMsg = "";

    private void showAlert(final String title){
        new androidx.appcompat.app.AlertDialog.Builder(mContext)
                .setTitle(title)
                .setMessage(showMsg)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).show();
    }

    public void nfcsearchcard() {

        {
//            NFCCardId = part5;
            NFCCardId = part1;
            tvCardNumber.setText(part1);
            tvName.setText(part4);
            tvBalance.setText(part3);

            if (tvCardNumber.getText().toString().length() > 0) {
                showMsg = "Part 1"+part1+" "+"Part 2"+part2+" "+"Part 3"+part3+" "+"Part 4"+part4+" "+"Part 5"+part5;
//                showAlert("NFC Details");
                status = 1;
                llNext.setVisibility(View.GONE);
                llTapCard.setVisibility(View.GONE);
                llCardDetails.setVisibility(View.VISIBLE);
                nfclayout.setVisibility(View.GONE);

            } else {

                new AlertDialog.Builder(getActivity())
                        .setTitle("")
                        .setMessage("Please Use Valid NFC Card")
                        .setCancelable(true)
                        .setPositiveButton(mContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                dismiss();
                            }
                        }).create().show();


            }


        }
    }

}
