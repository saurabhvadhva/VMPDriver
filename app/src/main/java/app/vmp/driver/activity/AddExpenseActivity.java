package app.vmp.driver.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import app.vmp.driver.R;
import app.vmp.driver.api.APIClient;
import app.vmp.driver.api.APIInterface;
import app.vmp.driver.model.GeneralResponse;
import app.vmp.driver.model.VehicleData;
import app.vmp.driver.utils.Constant;
import app.vmp.driver.utils.Util;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.vmp.driver.utils.Constant.DEVICE_ID;
import static app.vmp.driver.utils.Constant.NGO_ID;
import static app.vmp.driver.utils.Constant.TOKEN;
import static app.vmp.driver.utils.Constant.USER_ID;

public class AddExpenseActivity extends BaseActivity {

    private Context mContext;
    private ProgressDialog pd;
    private APIInterface apiInterface;
    private ArrayList<String> listRegId = new ArrayList<>();
    private ArrayList<String> listRegNo = new ArrayList<>();
    private Calendar myCalendar = Calendar.getInstance();

    @BindView(R.id.spinVehicle)
    SearchableSpinner spinVehicle;
    @BindView(R.id.tvDate)
    AppCompatTextView tvDate;
    @BindView(R.id.etRemarks)
    AppCompatEditText etRemarks;
    @BindView(R.id.etOtherCosts)
    AppCompatEditText etOtherCosts;
    @BindView(R.id.etPollution)
    AppCompatEditText etPollution;
    @BindView(R.id.etInsurance)
    AppCompatEditText etInsurance;
    @BindView(R.id.etMaintenance)
    AppCompatEditText etMaintenance;
    @BindView(R.id.etFuelAmt)
    AppCompatEditText etFuelAmt;
    @BindView(R.id.etFuelCost)
    AppCompatEditText etFuelCost;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        ButterKnife.bind(this);
        init();

    }

    @OnClick(R.id.imgBack)
    public void onBack() {
        finish();
    }


    @OnClick(R.id.tvDate)
    public void selectDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));  //date is dateSetListener as per your code in question
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }


    private void init() {
        mContext = AddExpenseActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
        pd.setMessage(mContext.getResources().getString(R.string.please_wait));
        Date currentTime = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
        String date = df.format(currentTime);
        time = date.split(" ")[1];
        selectedDate = date.replace(" ", "T");
        tvDate.setText(df2.format(currentTime));
        getVehicleList();
    }

    String time = "";

    private void getVehicleList() {
        if (Util.isNetworkAvailable(mContext)) {

            pd.show();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("NGOId", Util.getPrefData(NGO_ID, mContext));
            apiInterface.getVehicleList(Util.getPrefData(TOKEN, mContext), Util.getPrefData(DEVICE_ID, mContext), Constant.CONTENT_TYPE, Constant.CONTENT_TYPE, jsonObject).enqueue(new Callback<ArrayList<VehicleData>>() {
                @Override
                public void onResponse(Call<ArrayList<VehicleData>> call, Response<ArrayList<VehicleData>> response) {
                    pd.dismiss();
                    if (response.code() == 401) {
                        Util.redirecttoLogin(mContext);
                        return;
                    }
                    if (response.code() == 200 && response.body() != null && response.body().size() > 0) {
                        for (int i = 0; i < response.body().size(); i++) {
                            VehicleData data = response.body().get(i);
                            listRegId.add(data.getID());
                            listRegNo.add(data.getRegNo());
                        }
                        String[] constituencyListArray = (String[]) listRegNo.toArray(
                                new String[listRegNo.size()]);
                        final ArrayAdapter<String> constituencyApapdter =
                                new ArrayAdapter<String>(mContext,
                                        android.R.layout.simple_spinner_item, constituencyListArray);
                        constituencyApapdter.setDropDownViewResource(R.layout.custom_spinner_profile);
                        constituencyApapdter.notifyDataSetChanged();
                        spinVehicle.setTitle(getResources().getString(R.string.vehicle_no));
                        spinVehicle.setAdapter(constituencyApapdter);
                        spinVehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                vehicleId = listRegId.get(i);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<VehicleData>> call, Throwable t) {
                    pd.dismiss();
                }
            });

        }
    }

    private String vehicleId = "", selectedDate = "";
    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd/MM/yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            tvDate.setText(sdf.format(myCalendar.getTime()));

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            selectedDate = format.format(myCalendar.getTime());
            selectedDate = selectedDate + "T" + time;

        }

    };

    @OnClick(R.id.llProceed)
    public void onProceed() {
        if (Util.isNetworkAvailable(mContext)) {

            if (vehicleId.trim().length()==0){
                Util.showToast(mContext,getResources().getString(R.string.msg_vehicle));
            } else if (Util.isNullE(etFuelCost) && Util.isNullE(etFuelAmt) && Util.isNullE(etMaintenance) &&
                    Util.isNullE(etInsurance) && Util.isNullE(etPollution) && Util.isNullE(etOtherCosts)){
                Util.showToast(mContext,getResources().getString(R.string.msg_cost));
            } else {

                pd.show();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("NGOId", Util.getPrefData(NGO_ID, mContext));
                jsonObject.addProperty("UserId", Util.getPrefData(USER_ID, mContext));
                jsonObject.addProperty("VehicleId", vehicleId);
                jsonObject.addProperty("ExpenseDate", selectedDate);
                jsonObject.addProperty("FuleCost", Util.valE(etFuelCost));
                jsonObject.addProperty("FuleAmount", Util.valE(etFuelAmt));
                jsonObject.addProperty("Maintenance", Util.valE(etMaintenance));
                jsonObject.addProperty("Insurance", Util.valE(etInsurance));
                jsonObject.addProperty("Pollution", Util.valE(etPollution));
                jsonObject.addProperty("OtherCost", Util.valE(etOtherCosts));
                jsonObject.addProperty("Remarks", Util.valE(etRemarks));
                 Log.e("ttt",jsonObject.toString());
                apiInterface.addExpense(Util.getPrefData(TOKEN, mContext), Util.getPrefData(DEVICE_ID, mContext), Constant.CONTENT_TYPE, Constant.CONTENT_TYPE, jsonObject).enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                        pd.dismiss();
                        if (response.code() == 401) {
                            Util.redirecttoLogin(mContext);
                            return;
                        }
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                Util.showToast(mContext, response.body().getMsg());
                                if (response.body().getCode().equals("000")) {
                                    finish();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
                        pd.dismiss();
                    }
                });
            }
        } else {
            Util.showToast(mContext, getResources().getString(R.string.no_internet));
        }
    }

}
