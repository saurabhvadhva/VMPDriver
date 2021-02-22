package app.vmp.driver.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.google.gson.JsonObject;

import app.vmp.driver.R;
import app.vmp.driver.api.APIClient;
import app.vmp.driver.api.APIInterface;
import app.vmp.driver.model.GeneralResponse;
import app.vmp.driver.utils.Constant;
import app.vmp.driver.utils.Util;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.etNumber)
    AppCompatEditText etNumber;
    private Context mContext;

    private ProgressDialog pd;
    private APIInterface apiInterface;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        init();
    }

    private void init(){
        mContext = LoginActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext,ProgressDialog.STYLE_SPINNER);
        pd.setMessage(mContext.getResources().getString(R.string.please_wait));
    }

    public void onNext(View view){
        if (etNumber.getText().toString().trim().length()==0){
            Toast.makeText(mContext,getResources().getString(R.string.msg_entr_mbl),Toast.LENGTH_LONG).show();
        } else if (etNumber.getText().toString().trim().length()<10){
            Toast.makeText(mContext,getResources().getString(R.string.msg_entr_vdl_mbl),Toast.LENGTH_LONG).show();
        } else {
            login(); // uncomment after nfc integration
        }
    }

    private void login(){
        if (Util.isNetworkAvailable(mContext)){

            pd.show();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("MobileNumber",Util.valE(etNumber));
            jsonObject.addProperty("RoleID","7BC5FF04-0AE5-4B11-BCF3-06B1C8FCBC51");

          apiInterface.getOTP(Constant.CONTENT_TYPE,Constant.CONTENT_TYPE,jsonObject).enqueue(new Callback<GeneralResponse>() {
              @Override
              public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                  pd.dismiss();
                  if (response.code()==200){
                      Util.showToast(mContext,response.body().getMsg());
                      if (response.body().getCode().equals("000")){
                          Intent in = new Intent(LoginActivity.this, OTPActivity.class);
                          in.putExtra("number",Util.valE(etNumber));
                          startActivity(in);
                          overridePendingTransition(R.anim.enter, R.anim.exit);
                      }
                  } else {
                      Util.showToast(mContext,getResources().getString(R.string.smthng_wrong));
                  }
              }

              @Override
              public void onFailure(Call<GeneralResponse> call, Throwable t) {
                  pd.dismiss();
                  Util.showToast(mContext,getResources().getString(R.string.smthng_wrong));
              }
          });

        } else {
            Util.showToast(mContext,getResources().getString(R.string.no_internet));
        }
    }

}
