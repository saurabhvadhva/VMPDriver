package app.vmp.driver.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import app.vmp.driver.R;
import app.vmp.driver.api.APIClient;
import app.vmp.driver.api.APIInterface;
import app.vmp.driver.model.GeneralResponse;
import app.vmp.driver.model.LoginResponse;
import app.vmp.driver.model.TokenDetails;
import app.vmp.driver.utils.Constant;
import app.vmp.driver.utils.CryptoHandler;
import app.vmp.driver.utils.Util;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.vmp.driver.utils.Constant.DEVICE_ID;
import static app.vmp.driver.utils.Constant.EMPLOYEE_ID;
import static app.vmp.driver.utils.Constant.FULL_NAME;
import static app.vmp.driver.utils.Constant.NGO_ID;
import static app.vmp.driver.utils.Constant.STATE_ID;
import static app.vmp.driver.utils.Constant.TOKEN;
import static app.vmp.driver.utils.Constant.USER_ID;
import static app.vmp.driver.utils.Constant.USER_NAME;
import static app.vmp.driver.utils.Constant.USER_PHOTO;

public class OTPActivity extends BaseActivity {


    @BindView(R.id.etOtp)
    AppCompatEditText etOtp;
    @BindView(R.id.tvResendLable)
    AppCompatTextView tvResendLable;
    @BindView(R.id.btnResend)
    AppCompatTextView btnResend;

    private Context mContext;

    private ProgressDialog pd;
    APIInterface apiInterface;

    private String number = "";
    private String token = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_otp);
        ButterKnife.bind(this);
        init();
        startTimer();
    }

    CountDownTimer countDownTimer = null;

    private void startTimer() {
        countDownTimer = new CountDownTimer(30000, 1000) { //timer to enable resend otp text
            public void onTick(long millisUntilFinished) {
                tvResendLable.setText(getResources().getString(R.string.resend_otp_lable) + " " + String.format("%02d",
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)));
            }

            public void onFinish() {
                tvResendLable.setVisibility(View.GONE);
                btnResend.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    @OnClick(R.id.btnResend)
    public void onResend() {
        if (Util.isNetworkAvailable(mContext)) {

            pd.show();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("MobileNumber", number);
            jsonObject.addProperty("RoleID", "7BC5FF04-0AE5-4B11-BCF3-06B1C8FCBC51");

            apiInterface.getOTP(Constant.CONTENT_TYPE, Constant.CONTENT_TYPE, jsonObject).enqueue(new Callback<GeneralResponse>() {
                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                    pd.dismiss();
                    if (response.code() == 200) {
                        Util.showToast(mContext, response.body().getMsg());
//                        if (response.body().getCode().equals("000")){
                        tvResendLable.setVisibility(View.VISIBLE);
                        btnResend.setVisibility(View.GONE);
                        startTimer();
//                        }
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

    private void init() {
        mContext = OTPActivity.this;
        number = getIntent().getStringExtra("number");
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
        pd.setMessage(mContext.getResources().getString(R.string.please_wait));
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();

                    }
                });
    }

    public void onNext(View view) {
        if (etOtp.getText().toString().trim().length() < 5) {
            Toast.makeText(mContext, getResources().getString(R.string.msg_entr_vdl_otp), Toast.LENGTH_LONG).show();
        } else {
            verifyOtp();
        }
    }

    public void onPrev(View view) {
        finish();
    }

    private void verifyOtp() {
        if (Util.isNetworkAvailable(mContext)) {

            pd.show();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("MobileNumber", number);
            jsonObject.addProperty("Otp", Util.valE(etOtp));
//            jsonObject.addProperty("Token", token);
            jsonObject.addProperty("Token", "sdgdagaa");
            jsonObject.addProperty("RoleID", "7BC5FF04-0AE5-4B11-BCF3-06B1C8FCBC51");
//            jsonObject.addProperty("deviceId", token);

//            Log.e("ttt", token);
            String deviceId = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            jsonObject.addProperty("deviceId", deviceId);
            Log.e("ttt","Req"+jsonObject.toString());
            apiInterface.verifyOTP(Constant.CONTENT_TYPE, Constant.CONTENT_TYPE, jsonObject).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                    if (response.code() == 200) {
                        Util.showToast(mContext, response.body().getMsg());
                        if (response.body().getCode().equals("000")) {
                            LoginResponse loginResponse = response.body();
                            String decryptAES = "";

                            try {
                                decryptAES = CryptoHandler.getInstance().decrypt(loginResponse.getJwtToken());
                            } catch (Exception e){}

                            Gson gson = new Gson();
                            String json = Util.decoded(decryptAES);;
                            Type type = new TypeToken<TokenDetails>(){}.getType();
                            TokenDetails tokenDetails = gson.fromJson(json, type);

                            Util.setPrefData(USER_ID, tokenDetails.getUSER_ID(), mContext);
                            Util.setPrefData(EMPLOYEE_ID, tokenDetails.getEMP_ID(), mContext);
                            Util.setPrefData(NGO_ID, tokenDetails.getNGO_ID(), mContext);
                            Util.setPrefData(STATE_ID, tokenDetails.getSTATE_ID(), mContext);
                            Util.setPrefData(USER_NAME, tokenDetails.getUnique_name(), mContext);
                            Util.setPrefData(FULL_NAME, tokenDetails.getNAME(), mContext);
                            Util.setPrefData(USER_PHOTO, tokenDetails.getPROFILE_PHOTO(), mContext);
//                            Util.setPrefData(TOKEN, "Bearer " + loginResponse.getJwtToken(), mContext);
                            Util.setPrefData(TOKEN,  "Token "+loginResponse.getJwtToken(), mContext);
                            Util.setPrefData(DEVICE_ID, deviceId, mContext);
                            Intent in = new Intent(OTPActivity.this, MainActivity.class);
                            startActivity(in);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            try {
                                countDownTimer.cancel();
                            } catch (Exception e) {
                            }
                            pd.dismiss();
                            finish();
                        } else {
                            pd.dismiss();
                        }
                    } else {
                        pd.dismiss();
                        Log.e("ttt","c"+response.code());
                        Util.showToast(mContext, getResources().getString(R.string.smthng_wrong));
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    pd.dismiss();
                    Log.e("ttt","t"+t.getLocalizedMessage());
                    Util.showToast(mContext, getResources().getString(R.string.smthng_wrong));
                }
            });

        } else {
            Util.showToast(mContext, getResources().getString(R.string.no_internet));
        }
    }

}
