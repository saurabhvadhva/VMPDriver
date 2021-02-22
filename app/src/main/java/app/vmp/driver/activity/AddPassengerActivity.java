package app.vmp.driver.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import app.vmp.driver.Database.DbHelper;
import app.vmp.driver.R;
import app.vmp.driver.api.APIClient;
import app.vmp.driver.api.APIInterface;
import app.vmp.driver.model.PassengerData;
import app.vmp.driver.model.StartLocation;
import app.vmp.driver.utils.Util;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static app.vmp.driver.utils.Constant.LOCATION_LIST;
import static app.vmp.driver.utils.Constant.SHG_NUMBER;

public class AddPassengerActivity extends AppCompatActivity {

    private Context mContext;

    @BindView(R.id.spinStartLocation)
    AppCompatSpinner spinStartLocation;
    @BindView(R.id.spinEndLocation)
    AppCompatSpinner spinEndLocation;
    @BindView(R.id.etName)
    AppCompatEditText etName;
    @BindView(R.id.etNumber)
    AppCompatEditText etNumber;

    private String startLocationId = "", endLocationId = "";
    public static Context mContextAP;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.dialog_add_passenger);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        mContext = AddPassengerActivity.this;
        getLocationList();
    }

    private void getLocationList() {

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

    }

    @OnClick(R.id.btnShg)
    public void callShg() {
        try {
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Util.getPrefData(SHG_NUMBER, mContext))));
        } catch (Exception e) {
        }
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
        finish();
    }

    private void addPassenger() {
        Date currentTime = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(currentTime);
        date = date.replace(" ", "T");

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
        ((TripActiity) mContextAP).getPassengers("ONBOARD");
          finish();
    }



}
