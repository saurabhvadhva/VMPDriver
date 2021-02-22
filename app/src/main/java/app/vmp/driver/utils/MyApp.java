package app.vmp.driver.utils;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;

import app.vmp.driver.Database.DbHelper;
import app.vmp.driver.api.APIClient;
import app.vmp.driver.api.APIInterface;
import app.vmp.driver.model.GeneralResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.vmp.driver.Database.DbHelper.TAG_TOPUP_AMOUNT;
import static app.vmp.driver.Database.DbHelper.TAG_TOPUP_NFCID;
import static app.vmp.driver.Database.DbHelper.TAG_TOPUP_TIMESTAMP;
import static app.vmp.driver.utils.Constant.DEVICE_ID;
import static app.vmp.driver.utils.Constant.TOKEN;
import static app.vmp.driver.utils.Constant.USER_ID;

public class MyApp extends Application {

    private Context mContext;
    private static MyApp mInstance;
    private APIInterface apiInterface;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mContext = getApplicationContext();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(receiver, filter);
    }

    public static synchronized MyApp getInstance() {
        return mInstance;
    }

    private boolean isTopUpStarted = false;

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //do something based on the intent's action
            if (intent!=null && intent.getAction()!=null && intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")){
                if (Util.isNetworkAvailable(context)){
                    if (!isTopUpStarted){
                        isTopUpStarted = true;
                        checkforTopUp();
                    }
                }
            }

        }
    };

    public boolean returnTopUpStarted(){
        return isTopUpStarted;
    }

    public void checkforTopUp(){
        if (Util.isNetworkAvailable(mContext)){
            DbHelper db = new DbHelper(mContext);
            db.open();
            ArrayList<HashMap<String, String>> list = db.getTopUpListList();
            db.close();
            if (list == null || list.size() == 0) {
                isTopUpStarted = false;
            } else {
                isTopUpStarted = true;
                HashMap<String, String> map = list.get(0);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("NFCTagId",map.get(TAG_TOPUP_NFCID));
                jsonObject.addProperty("TransactionType","TOPUP");
                jsonObject.addProperty("TransactionAmount",map.get(TAG_TOPUP_AMOUNT));
                jsonObject.addProperty("UserID",Util.getPrefData(USER_ID,mContext));
//            jsonObject.addProperty("UserID","5");
                final String timestamp = map.get(TAG_TOPUP_TIMESTAMP);
                apiInterface.topupNFCcard(Util.getPrefData(TOKEN, mContext),Util.getPrefData(DEVICE_ID, mContext),Constant.CONTENT_TYPE,Constant.CONTENT_TYPE,jsonObject).enqueue(new Callback<GeneralResponse>() {
                    @Override
                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                        if (response.code()==401){
                            Util.redirecttoLogin(mContext);
                            return;
                        }
                        if (response.code()==200 && response.body().getCode().equals("000")) {
                            db.open();
                            db.deleteTopUpItemByTimestamp(timestamp);
                            db.close();
                            checkforTopUp();
                        }
                    }

                    @Override
                    public void onFailure(Call<GeneralResponse> call, Throwable t) {

                    }
                });
            }
        }
    }

}
