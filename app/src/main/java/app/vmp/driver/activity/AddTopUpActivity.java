package app.vmp.driver.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.gson.JsonObject;
import com.pax.dal.entity.EM1KeyType;
import com.pax.dal.entity.EPiccType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import app.vmp.driver.Database.DbHelper;
import app.vmp.driver.R;
import app.vmp.driver.api.APIClient;
import app.vmp.driver.api.APIInterface;
import app.vmp.driver.bottomsheet.TopUpDialog;
import app.vmp.driver.model.GeneralResponse;
import app.vmp.driver.model.NFCCardDetails;
import app.vmp.driver.picc.PiccTester;
import app.vmp.driver.util.Convert;
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
import static app.vmp.driver.utils.Constant.SHG_NUMBER;
import static app.vmp.driver.utils.Constant.TOKEN;
import static app.vmp.driver.utils.Constant.USER_ID;

public class AddTopUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private ProgressDialog pd;
    private APIInterface apiInterface;

    Double newdata=0.00;
    DbHelper db;
    public DetectMThread detectMThread;
    public DetectMThreadnew detectMThreadnew;
    private EPiccType piccType;
    String part1,part2,part3,part4,part5;
    String allow ="3";
    private EM1KeyType m1KeyType = EM1KeyType.TYPE_A;
    String messagedata="";

    @OnClick(R.id.btnShg)
    public void callShg() {
        try {
            mContext.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Util.getPrefData(SHG_NUMBER, mContext))));
        } catch (Exception e) {
        }
    }
    @BindView(R.id.btnShg)
    ImageView btnShg;

    @OnClick(R.id.icClose)
    public void onCancel(){
        finish();
    }


    @BindView(R.id.llEnterNumber)
    LinearLayout llEnterNumber;
    @BindView(R.id.llTapCard)
    LinearLayout llTapCard;
    @BindView(R.id.llCardDetails)
    LinearLayout llCardDetails;

    @BindView(R.id.llBack)
    LinearLayout llBack;

    @BindView(R.id.etCardNumber)
    AppCompatEditText etCardNumber;
    @BindView(R.id.etAmount)
    AppCompatEditText etAmount;

    @BindView(R.id.tvCardNumber)
    AppCompatTextView tvCardNumber;
    @BindView(R.id.tvName)
    AppCompatTextView tvName;
    @BindView(R.id.tvBalance)
    AppCompatTextView tvBalance;
    @BindView(R.id.tvMsg)
    AppCompatTextView tvMsg;

    @BindView(R.id.tvTopUpValue1)
    AppCompatTextView tvTopUpValue1;
    @BindView(R.id.tvTopUpValue2)
    AppCompatTextView tvTopUpValue2;
    @BindView(R.id.tvTopUpValue3)
    AppCompatTextView tvTopUpValue3;
    @BindView(R.id.tvTopUpValue4)
    AppCompatTextView tvTopUpValue4;

    private int status = 0;

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
    @BindView(R.id.llNext)
    LinearLayout llNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.dialog_topup_card);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        mContext = AddTopUpActivity.this;
        isShg = getIntent().getBooleanExtra("isShg",false);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext,ProgressDialog.STYLE_SPINNER);
        pd.setMessage(mContext.getResources().getString(R.string.please_wait));
        //-------By Bsd ----------//
        etCardNumber.setMovementMethod(ScrollingMovementMethod.getInstance());
        etCardNumber.setEnabled(false);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        PiccTester.getInstance(piccType).close();
        if (detectMThreadnew != null) {
            detectMThreadnew.interrupt();
            detectMThreadnew = null;
        }
        if (detectMThread != null) {
            detectMThread.interrupt();
            detectMThread = null;
        }
        PiccTester.getInstance(piccType).open();
        detectMThreadnew = new DetectMThreadnew();
        detectMThreadnew.start();
        piccType= EPiccType.valueOf("INTERNAL");
        keyTypeBt.setOnClickListener(this);
//-------------close ----------//

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
        if (!isShg){
            btnShg.setVisibility(View.GONE);
        }
    }

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:

                    if (status == 0) {


                        if(msg.obj.toString().equalsIgnoreCase("can't find card !")){
                            textView.setText("can't find card !");
                            etCardNumber.setText("can't find card !");
                            {
                                new AlertDialog.Builder(AddTopUpActivity.this)
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
                                                if (detectMThread != null) {
                                                    detectMThread.interrupt();
                                                    detectMThread = null;
                                                }
                                                PiccTester.getInstance(piccType).open();
                                                detectMThreadnew = new DetectMThreadnew();
                                                detectMThreadnew.start();

                                            }
                                        }).create().show();
                            }
                        }else {

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

                                nfcsearchcard();

                            }
                        }
                    } else   if (status == 1){

                        if(msg.obj.toString().equalsIgnoreCase("can't find card !")){

                            new AlertDialog.Builder(AddTopUpActivity.this)
                                    .setTitle("")
                                    .setMessage(mContext.getResources().getString(R.string.attach_nfc))
                                    .setCancelable(true)
                                    .setPositiveButton(mContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            //   dismiss();
                                        }


                                    }).create().show();


                        }else{


                            {

                                //if internet is ON then STATUS = 1 otherwise STATUS = 0
                                String InternetStatus = "1";

                                // If dATA IS UPLOADED on server = 1 otherwise STATUS = 0
                                String UploadedStatus = "1"; // as this feature work on interent only so status will be 1 only .


                                // Current Date
                                Date date = new Date();
                                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy"); // getting date in this format
                                String currentdate = df.format(date.getTime());

                                String transactiontype = "TOPUP";

                                String topupinternetstatus = "0";
                                String topupserveruploadstatus = "0";
                                if (Util.isNetworkAvailable(mContext)) {

                                    topupinternetstatus = "1";  // we assume if internet is ON , then  status will be 1;

                                    topupserveruploadstatus = "1";  // we assume if internet is ON , then data will be uploaded and status will be 1;

                                } else {
                                    topupinternetstatus = "0";
                                    topupserveruploadstatus = "0";
                                    PiccTester.getInstance(piccType).close();
                                    if (detectMThreadnew != null) {
                                        detectMThreadnew.interrupt();
                                        detectMThreadnew = null;
                                    }
                                    if (detectMThread != null) {
                                        detectMThread.interrupt();
                                        detectMThread = null;
                                    }
                                    PiccTester.getInstance(piccType).open();
                                    detectMThread = new DetectMThread();
                                    detectMThread.start();



                                }


                                db = DbHelper.getInstance(AddTopUpActivity.this);
                                db.open();

                                int validatetopupcardstring = db.validatetopupcard(etCardNumber.getText().toString().trim());
                                if(etAmount.getText().toString().length()>0){


                                }else{
                                    etAmount.setText("0.00");


                                }
                                if(tvBalance.getText().toString().length()>0){


                                }else{
                                    tvBalance.setText("0.00");


                                }
                                double newupdatedamount;
                                if (tvBalance.getText().toString().trim().length()>0){
                                    newupdatedamount = Double.parseDouble(etAmount.getText().toString()) + Double.parseDouble(tvBalance.getText().toString());
                                } else {
                                    newupdatedamount = Double.parseDouble(etAmount.getText().toString());
                                }


                                if (validatetopupcardstring > 0) {

                                    db = new DbHelper(AddTopUpActivity.this);
                                    db.open();
                                    db.updateTopupData(String.valueOf(newupdatedamount), etCardNumber.getText().toString(), topupinternetstatus, topupserveruploadstatus, currentdate);
                                    db.close();


                                } else {

                                    db = new DbHelper(AddTopUpActivity.this);
                                    db.open();
                                    db.inserttopupdetails(part5, transactiontype, String.valueOf(newupdatedamount), Util.getPrefData(USER_ID, mContext), topupinternetstatus, topupserveruploadstatus, currentdate, etCardNumber.getText().toString());
                                    db.close();

                                }

                                db.close();

                                if (!isCalled){
                                    isCalled = true;
                                    if (Util.isNetworkAvailable(mContext)) {
                                        saveInCard();
                                        topupCard();

                                    } else {

                                        db = new DbHelper(mContext);
                                        db.open();
                                        db.insertTopUp(NFCCardId,Util.valE(etAmount),"" + System.currentTimeMillis());
                                        db.close();

                                        status = 2;
                                        tvMsg.setText("NFC Card Top Up successfully");
                                        llBack.setVisibility(View.GONE);
                                        llCardDetails.setVisibility(View.GONE);
                                        llTapCard.setVisibility(View.VISIBLE);
                                        nfclayout.setVisibility(View.GONE);
                                        saveInCard();

                                    }
                                }

                            }



                        }

                    }
                    break;
                default:
                    break;
            }
        };

    };

    private boolean isCalled = false;

    private boolean isShg = false;

    private void saveInCard(){
        PiccTester.getInstance(piccType).close();
        if (detectMThreadnew != null) {
            detectMThreadnew.interrupt();
            detectMThreadnew = null;
        }
        if (detectMThread != null) {
            detectMThread.interrupt();
            detectMThread = null;
        }
        PiccTester.getInstance(piccType).open();
        detectMThread = new DetectMThread();
        detectMThread.start();
    }

    @OnClick(R.id.llNext)
    public void onNext(){
        if (status == 0){
            if (Util.isNullE(etCardNumber)){
                Util.showToast(mContext,mContext.getResources().getString(R.string.please_enter_card_number));
            } else {
                searchCard();
            }
        } else if(status == 1) {
            if (Util.isNullE(etAmount)){
                Util.showToast(mContext,mContext.getResources().getString(R.string.please_enter_amount));
            } else {
                try {
                    PiccTester.getInstance(piccType).close();
                    if (detectMThread != null) {
                        detectMThread.interrupt();
                        detectMThread = null;
                    }
                    if (detectMThreadnew != null) {
                        detectMThreadnew.interrupt();
                        detectMThreadnew = null;
                    }
                    PiccTester.getInstance(piccType).open();
                    detectMThreadnew = new DetectMThreadnew();
                    detectMThreadnew.start();

                } catch (final Exception e) {

                }
            }
        } else {
            finish();
        }
    }

    private void topupCard(){
        if (Util.isNetworkAvailable(mContext)){

            pd.show();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("NFCTagId",NFCCardId);
            jsonObject.addProperty("TransactionType","TOPUP");
            jsonObject.addProperty("TransactionAmount",Util.valE(etAmount));
            jsonObject.addProperty("UserID",Util.getPrefData(USER_ID,mContext));
//            jsonObject.addProperty("UserID","5");
            Log.e("ttt","respTopUp"+jsonObject.toString());
            apiInterface.topupNFCcard(Util.getPrefData(TOKEN, mContext),Util.getPrefData(DEVICE_ID, mContext), Constant.CONTENT_TYPE,Constant.CONTENT_TYPE,jsonObject).enqueue(new Callback<GeneralResponse>() {
                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                    pd.dismiss();
                    if (response.code()==401){
                        Util.redirecttoLogin(mContext);
                        return;
                    }
                    if (response.code()==200) {
//
                        if (response.body().getCode().equals("000")){
                            status = 2;
                            tvMsg.setText(response.body().getMsg());
                            llBack.setVisibility(View.GONE);
                            llCardDetails.setVisibility(View.GONE);
                            llTapCard.setVisibility(View.VISIBLE);
                            nfclayout.setVisibility(View.GONE);

                        } else {
                            System.out.println(response.body().getMsg()+"   ggg");
                            Util.showToast(mContext, response.body().getMsg());
                        }
                    } else {
                        System.out.println(getResources().getString(R.string.smthng_wrong)+"   gggqqq");

                        Util.showToast(mContext,getResources().getString(R.string.smthng_wrong));
                    }
                }

                @Override
                public void onFailure(Call<GeneralResponse> call, Throwable t) {
                    pd.dismiss();
//                    Util.showToast(mContext,getResources().getString(R.string.smthng_wrong));
                }
            });

        } else {
            Util.showToast(mContext,getResources().getString(R.string.no_internet));
        }
    }

    private NFCCardDetails nfc;
    private String NFCCardId = "";

    private void searchCard(){
        if (Util.isNetworkAvailable(mContext)){

            pd.show();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("SearchKey",Util.valE(etCardNumber));
            jsonObject.addProperty("NGOId",Util.getPrefData(NGO_ID,mContext));

            apiInterface.searchNFCcard(Util.getPrefData(TOKEN, mContext),Util.getPrefData(DEVICE_ID, mContext),Constant.CONTENT_TYPE,Constant.CONTENT_TYPE,jsonObject).enqueue(new Callback<NFCCardDetails>() {
                @Override
                public void onResponse(Call<NFCCardDetails> call, Response<NFCCardDetails> response) {
                    pd.dismiss();
                    if (response.code()==401){
                        Util.redirecttoLogin(mContext);
                        return;
                    }
                    if (response.code()==200 && response.body()!=null){
                        if(etCardNumber.getText().toString().equalsIgnoreCase("can't find card !")){

                            try {
                                new AlertDialog.Builder(AddTopUpActivity.this)
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
                                                if (detectMThread != null) {
                                                    detectMThread.interrupt();
                                                    detectMThread = null;
                                                }
                                                PiccTester.getInstance(piccType).open();
                                                detectMThreadnew = new DetectMThreadnew();
                                                detectMThreadnew.start();

                                            }
                                        }).create().show();
                            } catch (final Exception e) {

                            }

                        }else {
                            nfc = response.body();
                            NFCCardId = nfc.getNFCTagId();
                            tvCardNumber.setText(nfc.getNFCCardNumber());
                            tvName.setText(nfc.getCardHolderName());
                            tvBalance.setText(nfc.getBalanceAmount());
                            status = 1;
                            llBack.setVisibility(View.VISIBLE);
                            llEnterNumber.setVisibility(View.GONE);
                            llCardDetails.setVisibility(View.VISIBLE);
                            nfclayout.setVisibility(View.GONE);
                            llNext.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Util.showToast(mContext,getResources().getString(R.string.card_not_found));

                        PiccTester.getInstance(piccType).close();
                        if (detectMThread != null) {
                            detectMThread.interrupt();
                            detectMThread = null;
                        }
                        if (detectMThreadnew != null) {
                            detectMThreadnew.interrupt();
                            detectMThreadnew = null;
                        }
                        PiccTester.getInstance(piccType).open();
                        detectMThreadnew = new DetectMThreadnew();
                        detectMThreadnew.start();
                    }

                }

                @Override
                public void onFailure(Call<NFCCardDetails> call, Throwable t) {
                    pd.dismiss();
                    Util.showToast(mContext,getResources().getString(R.string.smthng_wrong));
                }
            });


        } else {
            Util.showToast(mContext,getResources().getString(R.string.no_internet));
        }
    }

    @OnClick(R.id.llBack)
    public void onPrev(){
        llBack.setVisibility(View.GONE);
        status = 0;
        llEnterNumber.setVisibility(View.VISIBLE);
        llCardDetails.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        {

            switch (view.getId()) {
                case R.id.fragment_picc_detect_m_keytype:
                    if (keyTypeBt.getText().equals("A")) {
                        keyTypeBt.setText("B");
                        m1KeyType = EM1KeyType.TYPE_B;
                    } else {
                        keyTypeBt.setText("A");
                        m1KeyType = EM1KeyType.TYPE_A;
                    }
                    break;

                default:
                    break;
            }
        }
    }

    class DetectMThread extends Thread {
        @Override
        public void run() {
            super.run();

            String blockStr = blockNumEt.getText().toString();
            int blockNum = Integer.parseInt(blockStr.equals("") ? blockNumEt.getHint().toString() : blockStr);
            String password = passwordEt.getText().toString().equals("") ? passwordEt.getHint().toString()
                    : passwordEt.getText().toString();
            Log.i("Test", "keyType:"+m1KeyType.name()+" blockNum:" + blockNum + " password:" + password+"  "+piccType);

            if (tvBalance.getText().toString().trim().length()>0){
                newdata = Double.parseDouble(etAmount.getText().toString())+Double.parseDouble(tvBalance.getText().toString());
            } else {
                newdata = Double.parseDouble(etAmount.getText().toString());
            }

            PiccTester.getInstance(piccType).detectMTopUpCard(handler, m1KeyType, blockNum, String.valueOf(newdata),
                    Convert.getInstance().strToBcd(password.toUpperCase(Locale.ENGLISH), Convert.EPaddingPosition.PADDING_RIGHT));

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
            Log.i("Test", "keyType:"+m1KeyType.name()+" blockNum:" + blockNum + " password:" + password+"  "+piccType);

            PiccTester.getInstance(piccType).detectMTopUpnew(handler, m1KeyType, blockNum, etAmount.getText().toString(),
                    Convert.getInstance().strToBcd(password.toUpperCase(Locale.ENGLISH), Convert.EPaddingPosition.PADDING_RIGHT));

        }
    }

    @Override
    public void onDestroy() {
        PiccTester.getInstance(piccType).close();
        if (detectMThread != null) {
            detectMThread.interrupt();
            detectMThread = null;
        }

        if (detectMThreadnew != null) {
            detectMThreadnew.interrupt();
            detectMThreadnew = null;
        }
        super.onDestroy();
    }
    public void nfcsearchcard(){

        {
            NFCCardId = part1;
            tvCardNumber.setText(part1);
            tvName.setText(part4);
            tvBalance.setText(part3);

            if(tvCardNumber.getText().toString().length()>0){

                status = 1;
                llBack.setVisibility(View.GONE);
                llEnterNumber.setVisibility(View.GONE);
                llCardDetails.setVisibility(View.VISIBLE);
                nfclayout.setVisibility(View.GONE);
                llNext.setVisibility(View.VISIBLE);

            }

            else {

                new AlertDialog.Builder(AddTopUpActivity.this)
                        .setTitle("")
                        .setMessage("Please Use Valid NFC Card")
                        .setCancelable(true)
                        .setPositiveButton(mContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                finish();
                            }
                        }).create().show();


            }


        }
    }

}
