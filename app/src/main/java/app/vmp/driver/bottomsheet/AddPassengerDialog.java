package app.vmp.driver.bottomsheet;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.DialogFragment;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import app.vmp.driver.Database.DbHelper;
import app.vmp.driver.R;
import app.vmp.driver.activity.TripActiity;
import app.vmp.driver.api.APIClient;
import app.vmp.driver.api.APIInterface;
import app.vmp.driver.model.GeneralResponse;
import app.vmp.driver.model.PassengerData;
import app.vmp.driver.model.StartLocation;
import app.vmp.driver.utils.Constant;
import app.vmp.driver.utils.Util;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.vmp.driver.utils.Constant.API_GET_LOCATIONS;
import static app.vmp.driver.utils.Constant.DEVICE_ID;
import static app.vmp.driver.utils.Constant.LOCATION_LIST;
import static app.vmp.driver.utils.Constant.SHG_NUMBER;
import static app.vmp.driver.utils.Constant.TOKEN;
import static app.vmp.driver.utils.Constant.USER_ID;

public class AddPassengerDialog extends DialogFragment {

    private Context mContext;
    private ProgressDialog pd;
    private APIInterface apiInterface;
    private String routeId, timeSlot;

    public AddPassengerDialog(Context context, String routeId, String timeSlot) {
        mContext = context;
        this.routeId = routeId;
        this.timeSlot = timeSlot;
        apiInterface = APIClient.getClient().create(APIInterface.class);
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

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_add_passenger, container, false);
        ButterKnife.bind(this, rootView);

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

    @BindView(R.id.spinStartLocation)
    AppCompatSpinner spinStartLocation;
    @BindView(R.id.spinEndLocation)
    AppCompatSpinner spinEndLocation;
    @BindView(R.id.etName)
    AppCompatEditText etName;
    @BindView(R.id.etNumber)
    AppCompatEditText etNumber;

    private String startLocationId = "", endLocationId = "";

    private void getLocationList() {
//        if (Util.isNetworkAvailable(mContext)){
//             pd.show();
//            String url = API_GET_LOCATIONS+"RouteId="+routeId;
//            apiInterface.getLocationList(Util.getPrefData(TOKEN, mContext),Util.getPrefData(DEVICE_ID, mContext),Constant.CONTENT_TYPE,Constant.CONTENT_TYPE,url).enqueue(new Callback<ArrayList<StartLocation>>() {
//                @Override
//                public void onResponse(Call<ArrayList<StartLocation>> call, Response<ArrayList<StartLocation>> response) {
//                   pd.dismiss();
//                    if (response.code()==200 && response.body()!=null){
//                        ArrayList<StartLocation> districtData = response.body();
        Gson gson = new Gson();
        String json = Util.getPrefData(LOCATION_LIST, mContext).trim();
        Type type = new TypeToken<ArrayList<StartLocation>>() {
        }.getType();
        ArrayList<StartLocation> districtData = gson.fromJson(json, type);
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> listId = new ArrayList<>();
        list.add("Select Location");
        listId.add("0");
        for (int i = 0; i < districtData.size(); i++) {
            StartLocation data = districtData.get(i);
            listId.add(data.getID());
            if (i == 0) {
                startLocationId = data.getID();
            } else if (i == 1) {
                endLocationId = data.getID();
            }
            list.add(data.getLocationName());
        }
        ArrayAdapter<String> adapter2 =
                new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, list);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinStartLocation.setAdapter(adapter2);
        spinStartLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                startLocationId = "";
                if (i != 0) {
                    startLocationId = listId.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinEndLocation.setAdapter(adapter);
        spinEndLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                endLocationId = "";
                if (i != 0) {
                    endLocationId = listId.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//                    } else {
//                        Util.showToast(mContext,getResources().getString(R.string.smthng_wrong));
//                    }

//                }
//
//                @Override
//                public void onFailure(Call<ArrayList<StartLocation>> call, Throwable t) {
//                    pd.dismiss();
//                    Util.showToast(mContext,getResources().getString(R.string.smthng_wrong));
//                }
//            });


//        } else {
//            Util.showToast(mContext,getResources().getString(R.string.no_internet));
//        }
    }

    @OnClick(R.id.llNext)
    public void onNext() {
        if (Util.isNullE(etName)) {
            Util.showToast(mContext, mContext.getResources().getString(R.string.please_enter_name));
        } else if (Util.isNullE(etNumber)) {
            Util.showToast(mContext, mContext.getResources().getString(R.string.please_enter_mobile));
        } else if (startLocationId.trim().length() == 0) {
            Util.showToast(mContext, mContext.getResources().getString(R.string.please_enter_sloc));
        } else if (endLocationId.trim().length() == 0) {
            Util.showToast(mContext, mContext.getResources().getString(R.string.please_enter_eloc));
        } else if (endLocationId.equals(startLocationId)) {
            Util.showToast(mContext, mContext.getResources().getString(R.string.please_enter_loc_match));
        } else {
            addPassenger();
        }
    }

    @OnClick(R.id.icClose)
    public void onCancel() {
        dismiss();
    }

    private void addPassenger() {
//        if (Util.isNetworkAvailable(mContext)) {

//            pd.show();
        Date currentTime = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(currentTime);
        date = date.replace(" ", "T");
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("RouteId",routeId);
//            jsonObject.addProperty("TimeSlot",timeSlot);
//            jsonObject.addProperty("PassengerName",Util.valE(etName));
//            jsonObject.addProperty("PassengerMobile",Util.valE(etNumber));
//            jsonObject.addProperty("StartLocationId",startLocationId);
//            jsonObject.addProperty("EndLocationId",endLocationId);
//            jsonObject.addProperty("Date",date);
//            jsonObject.addProperty("MaleCount","0");
//            jsonObject.addProperty("FemaleCount","0");
//            jsonObject.addProperty("ChildrenCount","0");
//            jsonObject.addProperty("UserId",Util.getPrefData(USER_ID,mContext));
//               Log.e("ttt",jsonObject.toString());

        DbHelper db = new DbHelper(mContext);
        db.open();
        String timestamp = "" + System.currentTimeMillis();
        db.insertAddPassenger(Util.valE(etName), Util.valE(etNumber), date, timestamp, startLocationId, endLocationId);
        PassengerData pp = new PassengerData();
        pp.setPassengerName(Util.valE(etName));
        pp.setMobileNumber(Util.valE(etNumber));
        pp.setIsOffline("true");
        pp.setTimestamp(timestamp);
        ArrayList<PassengerData> ppl = new ArrayList<>();
        ppl.add(pp);
        db.insertOnBoardData(ppl);
        db.close();
        dismiss();
        ((TripActiity) mContext).getPassengers("ONBOARD");
//
//            apiInterface.addPassenger(Util.getPrefData(TOKEN, mContext),Util.getPrefData(DEVICE_ID, mContext),Constant.CONTENT_TYPE,Constant.CONTENT_TYPE,jsonObject).enqueue(new Callback<GeneralResponse>() {
//                @Override
//                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
//                    pd.dismiss();
//                    if (response.code()==200) {
//                        Util.showToast(mContext, response.body().getMsg());
//                        if (response.body().getCode().equals("000")) {
//                            dismiss();
//                            ((TripActiity) mContext).getPassengers("ONBOARD");
//                        }
//                    }  else {
//                        Util.showToast(mContext,getResources().getString(R.string.smthng_wrong));
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<GeneralResponse> call, Throwable t) {
//                    pd.dismiss();
//                    Util.showToast(mContext,getResources().getString(R.string.smthng_wrong));
//                }
//            });

//        }  else {
//            Util.showToast(mContext,getResources().getString(R.string.no_internet));
//        }

    }

}
