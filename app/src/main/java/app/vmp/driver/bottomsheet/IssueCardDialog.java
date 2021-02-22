package app.vmp.driver.bottomsheet;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pax.dal.entity.EM1KeyType;
import com.pax.dal.entity.EPiccType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import app.vmp.driver.BuildConfig;
import app.vmp.driver.Database.DbHelper;
import app.vmp.driver.R;
import app.vmp.driver.activity.TripActiity;
import app.vmp.driver.api.APIClient;
import app.vmp.driver.api.APIInterface;
import app.vmp.driver.model.DistrictData;
import app.vmp.driver.model.GeneralResponse;
import app.vmp.driver.model.NFCCardDetails;
import app.vmp.driver.model.Options;
import app.vmp.driver.model.TalukaData;
import app.vmp.driver.model.VillageData;
import app.vmp.driver.picc.PiccTester;
import app.vmp.driver.util.Convert;
import app.vmp.driver.utils.Constant;
import app.vmp.driver.utils.FileUtil;
import app.vmp.driver.utils.Util;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static app.vmp.driver.utils.Constant.API_GET_LOCATIONS;
import static app.vmp.driver.utils.Constant.API_GET_SECURITY_AMOUNT;
import static app.vmp.driver.utils.Constant.DEVICE_ID;
import static app.vmp.driver.utils.Constant.NGO_ID;
import static app.vmp.driver.utils.Constant.PROFESSION_TYPE;
import static app.vmp.driver.utils.Constant.SEL_LANG;
import static app.vmp.driver.utils.Constant.SHG_NUMBER;
import static app.vmp.driver.utils.Constant.STATE_ID;
import static app.vmp.driver.utils.Constant.TOKEN;
import static app.vmp.driver.utils.Constant.USER_ID;
import static app.vmp.driver.utils.Constant.USER_PHOTO;

public class IssueCardDialog extends DialogFragment implements View.OnClickListener {

    private Context mContext;
    private ProgressDialog pd;
    private APIInterface apiInterface;

    //----------- By Bsd -----------//
    DbHelper db;
    String allow ="0";
    public DetectMThread detectMThread;
    public DetectMThreadnew detectMThreadnew;
    private EPiccType piccType;
    private EM1KeyType m1KeyType = EM1KeyType.TYPE_A;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    if (status == 0) {
                        textView.setText(msg.obj.toString());
                        etCardNumber.setText(msg.obj.toString());

//                        if (Util.isNullE(etCardNumber)) {
//                            Util.showToast(mContext, mContext.getResources().getString(R.string.please_enter_card_number));
//                    System.out.println("www"+" ddd");
//
//                        } else {
//                            System.out.println("www11"+" ddd");
//                            searchCard();
//                        }

                        if (textView.getText().toString().equalsIgnoreCase("can't find card !")) {

                            try {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("")
                                        .setMessage(mContext.getResources().getString(R.string.nfc_not_found))
                                        .setCancelable(true)
                                        .setPositiveButton(mContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {
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
                                        }).create().show();
                            } catch (final Exception e) {

                            }

                        } else {

                            searchCard();
                        }

                    }


                    else


                        if (status == 1){

                        if(msg.obj.toString().equalsIgnoreCase("can't find card !")){

                            new AlertDialog.Builder(getActivity())
                                        .setTitle("")
                                        .setMessage(mContext.getResources().getString(R.string.attach_nfc))
                                        .setCancelable(true)
                                        .setPositiveButton(mContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {
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
                                                detectMThread = new DetectMThread();
                                                detectMThread.start();

                                                //dismiss();

                                            }


                                        }).create().show();






                        }else{

                          topupCard();

                        }


                    }
                    break;
                default:
                    break;
            }
        };
    };

    //---------- close -------------//
    private boolean isShg = false;
    public IssueCardDialog(Context context,boolean isShg){
        mContext = context;
        this.isShg = isShg;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext,ProgressDialog.STYLE_SPINNER);
        pd.setMessage(mContext.getResources().getString(R.string.please_wait));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,android.R.style.Theme_Translucent_NoTitleBar);
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
        View rootView = inflater.inflate(R.layout.dialog_issue_card, container,false);
        ButterKnife.bind(this,rootView);

        //----------- By Bsd ----------//

        etCardNumber.setMovementMethod(ScrollingMovementMethod.getInstance());
        etCardNumber.setEnabled(false);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
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

        piccType= EPiccType.valueOf("INTERNAL");
        keyTypeBt.setOnClickListener(this);

        //------------- close ----------//

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

        Gson gson = new Gson();
        String json = Util.getPrefData(PROFESSION_TYPE,mContext).trim();
        Type type = new TypeToken<ArrayList<Options>>(){}.getType();
        spinData = gson.fromJson(json, type);
        ArrayList<String> list = new ArrayList<>();
        for (int i=0;i<spinData.size();i++){
            Options options = spinData.get(i);
            list.add(options.getText());

        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(mContext,  android.R.layout.simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

        spinProfession.setAdapter(adapter);
        spinProfession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                professionId = spinData.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.rbMale){
                    gender = "MALE";
                } else {
                    gender = "FEMALE";
                }
            }
        });

//        getDistrictList();
        getSecutityAmount();
        if (!isShg){
            btnShg.setVisibility(View.GONE);
        }
        return rootView;
    }

    private void getSecutityAmount(){
        if (Util.isNetworkAvailable(mContext)){
            String url = API_GET_SECURITY_AMOUNT + "NgoId=" + Util.getPrefData(NGO_ID, mContext);
            apiInterface.getSecurityAmount(Util.getPrefData(TOKEN, mContext), Util.getPrefData(DEVICE_ID, mContext), Constant.CONTENT_TYPE, Constant.CONTENT_TYPE, url).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.code()==200 && response.body()!=null) {
                        try {
                            String amount = response.body().string();
                            tvSecurityAmount.setText(amount);
                        } catch (Exception e){}
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }

    @OnClick(R.id.btnShg)
    public void callShg() {
        try {
            mContext.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Util.getPrefData(SHG_NUMBER, mContext))));
        } catch (Exception e) {
        }
    }
    @BindView(R.id.btnShg)
    ImageView btnShg;

    private String professionId = "",villageId="";

    private ArrayList<Options> spinData;
    private ArrayList<DistrictData> districtData;
    private ArrayList<TalukaData> talukaData;
    private ArrayList<VillageData> villageData;

    @OnClick(R.id.icClose)
    public void onCancel(){
        dismiss();
    }

    @OnClick(R.id.llBack)
    public void onPrev(){
        llBack.setVisibility(View.GONE);
        status = 0;
        llEnterNumber.setVisibility(View.VISIBLE);
        llCardDetails.setVisibility(View.GONE);
    }

    @BindView(R.id.tvTopUpValue1)
    AppCompatTextView tvTopUpValue1;
    @BindView(R.id.tvTopUpValue2)
    AppCompatTextView tvTopUpValue2;
    @BindView(R.id.tvTopUpValue3)
    AppCompatTextView tvTopUpValue3;
    @BindView(R.id.tvTopUpValue4)
    AppCompatTextView tvTopUpValue4;
    @BindView(R.id.imgPhoto)
    ImageView imgPhoto;

    @BindView(R.id.tvCardNumber)
    AppCompatTextView tvCardNumber;

    private int status = 0;
    private String gender = "MALE";

    @BindView(R.id.etAmount)
    AppCompatEditText etAmount;
    @BindView(R.id.etName)
    AppCompatEditText etName;
    @BindView(R.id.etNumber)
    AppCompatEditText etNumber;
    @BindView(R.id.etAadhar)
    AppCompatEditText etAadhar;
    @BindView(R.id.etAge)
    AppCompatEditText etAge;
    @BindView(R.id.spinProfession)
    AppCompatSpinner spinProfession;
    @BindView(R.id.spinDistrict)
    AppCompatSpinner spinDistrict;
    @BindView(R.id.spinTaluka)
    AppCompatSpinner spinTaluka;
    @BindView(R.id.spinVillage)
    AppCompatSpinner spinVillage;
    @BindView(R.id.rgGender)
    RadioGroup rgGender;

    @BindView(R.id.llEnterNumber)
    LinearLayout llEnterNumber;
    @BindView(R.id.llTapCard)
    LinearLayout llTapCard;
    @BindView(R.id.llCardDetails)
    LinearLayout llCardDetails;

    @BindView(R.id.etCardNumber)
    AppCompatEditText etCardNumber;

    @BindView(R.id.tvSecurityAmount)
    AppCompatTextView tvSecurityAmount;

    @BindView(R.id.tvMsg)
    AppCompatTextView tvMsg;

    @BindView(R.id.chk)
    AppCompatCheckBox chk;

    @BindView(R.id.llBack)
    LinearLayout llBack;
    PopupMenu popup;

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

    @OnClick(R.id.imgPhoto)
    public void onAddPhoto(){
        if (ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED
        ) {
             requestPermissions(
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, REQUEST_PERMISSION);
        } else {
            popup = new PopupMenu(mContext, imgPhoto);
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.gallery:
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_PICK);
                            startActivityForResult(Intent.createChooser(intent, "Choose Image"),
                                    PICK_IMAGE_REQUEST);
                            break;
                        case R.id.camera:
                            Uri outputFileUri = getCaptureImageOutputUri();
                            Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent1.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                            startActivityForResult(intent1, CAMERA_IMAGE_REQUEST);
                            break;
                    }
                    return true;
                }
            });
            popup.inflate(R.menu.menu_camera);
            popup.show();

        }
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            File getImage = mContext.getExternalCacheDir();
            if (getImage != null) {
                outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpeg"));
            }
        } else {
            File getImage = mContext.getExternalCacheDir();
            if (getImage != null) {
                outputFileUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider",
                        new File(getImage.getPath(), "pickImageResult.jpeg"));
            }
        }
        return outputFileUri;
    }

    private static final int REQUEST_PERMISSION = 200;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_IMAGE_REQUEST = 2;
    private File actualImage;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(mContext, "Failed to open picture!", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                actualImage = FileUtil.from(mContext, data.getData());
                imgPhoto.setImageBitmap
                        (BitmapFactory.decodeFile(actualImage.getAbsolutePath()));
                ConverImagetoBase64();
            } catch (IOException e) {
                Toast.makeText(mContext, "Failed to read picture data!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri imageUri = getPickImageResultUri(data);
            try {
                actualImage = FileUtil.from(mContext, imageUri);
                imgPhoto.setImageBitmap
                        (BitmapFactory.decodeFile(actualImage.getAbsolutePath()));
                ConverImagetoBase64();
            } catch (IOException e) {
                Toast.makeText(mContext, "Failed to read picture data!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }


    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null && data.getData() != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    Bitmap bmp = null;
    ByteArrayOutputStream bos = null;
    byte[] bt = null;
    private String encodeString = "";
    public void ConverImagetoBase64() {

        try {
            bmp = BitmapFactory.decodeFile(actualImage.getPath());
            bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bt = bos.toByteArray();
            encodeString = Base64.encodeToString(bt, Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.llNext)
    public void onNext(){
        if (status == 0){
            if (Util.isNullE(etCardNumber)){
                Util.showToast(mContext,mContext.getResources().getString(R.string.please_enter_card_number));
            } else {
                searchCard();
//                tvCardNumber.setText(Util.valE(etCardNumber));
//                status = 1;
//                llBack.setVisibility(View.VISIBLE);
//                llEnterNumber.setVisibility(View.GONE);
//                llCardDetails.setVisibility(View.VISIBLE);
            }
        } else if(status == 1) {
            if (Util.isNullE(etAmount)){
                Util.showToast(mContext,mContext.getResources().getString(R.string.please_enter_amount));
            } else if (Util.isNullE(etName)){
                Util.showToast(mContext,mContext.getResources().getString(R.string.please_enter_name));
            } else if (Util.isNullE(etNumber)){
                Util.showToast(mContext,mContext.getResources().getString(R.string.please_enter_mobile));
            } else if ((tvSecurityAmount.getText() != null && tvSecurityAmount.getText().toString().trim().length() > 0 && Double.parseDouble(Util.valE(etAmount))<Double.parseDouble(tvSecurityAmount.getText().toString().trim()))){
                Util.showToast(mContext,mContext.getResources().getString(R.string.security_amount_msg));
            }

//            else if (villageId.trim().length()==0){
//                Util.showToast(mContext,mContext.getResources().getString(R.string.please_select_village));
//            }  else if (encodeString.trim().length()==0){
//                Util.showToast(mContext,mContext.getResources().getString(R.string.please_add_photo));
//            }

            else {
                if (chk.isChecked()){
                    try {
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
                } else {
                    Util.showToast(mContext,mContext.getResources().getString(R.string.msg_payment));
                }
            }
        } else {
            dismiss();
        }
    }

//    private void getDistrictList(){
//        if (Util.isNetworkAvailable(mContext)){
//
////            pd.show();
//
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("StateID",Util.getPrefData(STATE_ID,mContext));
////            jsonObject.addProperty("StateID","2");
//
//            apiInterface.getDistrictList(Util.getPrefData(TOKEN, mContext),Util.getPrefData(DEVICE_ID, mContext),Constant.CONTENT_TYPE,Constant.CONTENT_TYPE,jsonObject).enqueue(new Callback<ArrayList<DistrictData>>() {
//                @Override
//                public void onResponse(Call<ArrayList<DistrictData>> call, Response<ArrayList<DistrictData>> response) {
////                    pd.dismiss();
//                    if (response.code()==200 && response.body()!=null){
//                         districtData = response.body();
//                        ArrayList<String> list = new ArrayList<>();
//                        list.add("Select District");
//                        for (int i=0;i<districtData.size();i++){
//                            DistrictData data = districtData.get(i);
//                            list.add(data.getDistrictName());
//                        }
//
//                        ArrayAdapter<String> adapter =
//                                new ArrayAdapter<String>(mContext,  android.R.layout.simple_spinner_dropdown_item, list);
//                        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
//
//                        spinDistrict.setAdapter(adapter);
//                        spinDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                                villageId = "";
//                                if (i!=0){
//                                  getTalukaList(districtData.get(i-1).getID());
//                                }
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> adapterView) {
//
//                            }
//                        });
//
//                    } else {
//                        Util.showToast(mContext,getResources().getString(R.string.smthng_wrong));
//                    }
//
//                }
//
//                @Override
//                public void onFailure(Call<ArrayList<DistrictData>> call, Throwable t) {
////                    pd.dismiss();
//                    Util.showToast(mContext,getResources().getString(R.string.smthng_wrong));
//                }
//            });
//
//
//        } else {
//            Util.showToast(mContext,getResources().getString(R.string.no_internet));
//        }
//    }

//    private void getTalukaList(String id){
//        if (Util.isNetworkAvailable(mContext)){
//
//            pd.show();
//
//            apiInterface.getTalukaList(Util.getPrefData(TOKEN, mContext),Util.getPrefData(DEVICE_ID, mContext),Constant.CONTENT_TYPE,Constant.CONTENT_TYPE,id).enqueue(new Callback<ArrayList<TalukaData>>() {
//                @Override
//                public void onResponse(Call<ArrayList<TalukaData>> call, Response<ArrayList<TalukaData>> response) {
//                    pd.dismiss();
//                    if (response.code()==200 && response.body()!=null){
//                        talukaData = response.body();
//                        ArrayList<String> list = new ArrayList<>();
//                        list.add("Select Taluka");
//                        for (int i=0;i<talukaData.size();i++){
//                            TalukaData data = talukaData.get(i);
//                            list.add(data.getTalukaName());
//                        }
//
//                        ArrayAdapter<String> adapter =
//                                new ArrayAdapter<String>(mContext,  android.R.layout.simple_spinner_dropdown_item, list);
//                        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
//
//                        spinTaluka.setAdapter(adapter);
//                        spinTaluka.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                                villageId = "";
//                                if (i!=0){
//                                    getVillageList(talukaData.get(i-1).getTalukaID());
//                                }
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> adapterView) {
//
//                            }
//                        });
//
//                    } else {
//                        Util.showToast(mContext,getResources().getString(R.string.smthng_wrong));
//                    }
//
//                }
//
//                @Override
//                public void onFailure(Call<ArrayList<TalukaData>> call, Throwable t) {
//                    pd.dismiss();
//                    Util.showToast(mContext,getResources().getString(R.string.smthng_wrong));
//                }
//            });
//
//
//        } else {
//            Util.showToast(mContext,getResources().getString(R.string.no_internet));
//        }
//    }

//    private void getVillageList(String id){
//        if (Util.isNetworkAvailable(mContext)){
//
//            pd.show();
//
//            apiInterface.getVillageList(Util.getPrefData(TOKEN, mContext),Util.getPrefData(DEVICE_ID, mContext),Constant.CONTENT_TYPE,Constant.CONTENT_TYPE,id).enqueue(new Callback<ArrayList<VillageData>>() {
//                @Override
//                public void onResponse(Call<ArrayList<VillageData>> call, Response<ArrayList<VillageData>> response) {
//                    pd.dismiss();
//                    if (response.code()==200 && response.body()!=null){
//                        villageData = response.body();
//                        ArrayList<String> list = new ArrayList<>();
//                        list.add("Select Village");
//                        for (int i=0;i<villageData.size();i++){
//                            VillageData data = villageData.get(i);
//                            list.add(data.getVillageName());
//                        }
//
//                        ArrayAdapter<String> adapter =
//                                new ArrayAdapter<String>(mContext,  android.R.layout.simple_spinner_dropdown_item, list);
//                        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
//
//                        spinVillage.setAdapter(adapter);
//                        spinVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                                if (i!=0){
//                                    villageId = villageData.get(i-1).getId();
//                                } else {
//                                    villageId = "";
//                                }
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> adapterView) {
//
//                            }
//                        });
//
//                    } else {
//                        Util.showToast(mContext,getResources().getString(R.string.smthng_wrong));
//                    }
//
//                }
//
//                @Override
//                public void onFailure(Call<ArrayList<VillageData>> call, Throwable t) {
//                    pd.dismiss();
//                    Util.showToast(mContext,getResources().getString(R.string.smthng_wrong));
//                }
//            });
//
//
//        } else {
//            Util.showToast(mContext,getResources().getString(R.string.no_internet));
//        }
//    }

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
                    if (response.code()==200 && response.body()!=null && response.body().getNFCCardId()!=null && response.body().getNFCCardId().trim().length()!=0 && !response.body().getNFCCardId().equals("0") && ( response.body().getCardHolderName()==null || response.body().getCardHolderName().trim().length()==0)){

                            NFCCardId = response.body().getNFCCardId();
                            System.out.println(NFCCardId+"  NFCCardId");
//                        nfc = response.body();
//                        tvCardNumber.setText(nfc.getNFCCardNumber());
//                        tvName.setText(nfc.getCardHolderName());
//                        tvBalance.setText(nfc.getBalanceAmount());
                            etName.setText(response.body().getCardHolderName());
                            tvCardNumber.setText(Util.valE(etCardNumber));
                            status = 1;
                            llBack.setVisibility(View.GONE);
                            llEnterNumber.setVisibility(View.GONE);
                            llCardDetails.setVisibility(View.VISIBLE);
                            llNext.setVisibility(View.VISIBLE);

                    } else {

                        if (response.code()==200 && response.body()!=null && response.body().getCardHolderName()!=null && response.body().getCardHolderName().trim().length()>0){

                            try {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("")
                                        .setMessage(getResources().getString(R.string.card_assigned)+" . Please attach another NFC Card at the side of device then click ok")
                                        .setCancelable(true)
                                        .setPositiveButton(mContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {
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
                                        }).create().show();
                            } catch (final Exception e) {

                            }


                        } else {

                            try {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("")
                                        .setMessage(getResources().getString(R.string.card_not_found)+" . Please attach another NFC Card at the side of device then click ok")
                                        .setCancelable(true)
                                        .setPositiveButton(mContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {
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
                                        }).create().show();
                            } catch (final Exception e) {

                            }

                        }

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

    private void topupCard(){
        if (Util.isNetworkAvailable(mContext)){

            pd.show();

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("NFCCardId",NFCCardId);
            jsonObject.addProperty("PassengerName",Util.valE(etName));
            jsonObject.addProperty("MobileNumber",Util.valE(etNumber));
            String amount = ""+etAmount.getText().toString();

            if (amount.trim().length()>0&&tvSecurityAmount.getText() != null && tvSecurityAmount.getText().toString().trim().length() > 0 && Double.parseDouble(Util.valE(etAmount))<Double.parseDouble(tvSecurityAmount.getText().toString().trim())){
                amount = ""+(Double.parseDouble(amount)-Double.parseDouble(tvSecurityAmount.getText().toString().trim()));
            }
            jsonObject.addProperty("Amount",amount);
            jsonObject.addProperty("MobileNumber2","");
            jsonObject.addProperty("ProfileImage",encodeString);
            jsonObject.addProperty("AadharNumber",Util.valE(etAadhar));
            jsonObject.addProperty("Age",Util.valE(etAge));
            jsonObject.addProperty("Gender",gender);
            jsonObject.addProperty("ProfessionTypeId",professionId);
            jsonObject.addProperty("VillageId",villageId);
            jsonObject.addProperty("NGOId",Util.getPrefData(NGO_ID,mContext));
//            jsonObject.addProperty("NGOId","0");
            jsonObject.addProperty("UserID",Util.getPrefData(USER_ID,mContext));
//            jsonObject.addProperty("UserID","5");
            apiInterface.issueNFCcard(Util.getPrefData(TOKEN, mContext),Util.getPrefData(DEVICE_ID, mContext),Constant.CONTENT_TYPE,Constant.CONTENT_TYPE,jsonObject).enqueue(new Callback<GeneralResponse>() {
                @Override
                public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                    pd.dismiss();
                    if (response.code()==401){
                        Util.redirecttoLogin(mContext);
                        return;
                    }
                    if (response.code()==200) {
                        if (response.body().getCode().equals("000")){
                            status = 2;
                            tvMsg.setText(response.body().getMsg());
                            llBack.setVisibility(View.GONE);
                            llCardDetails.setVisibility(View.GONE);
                            llTapCard.setVisibility(View.VISIBLE);

                            nfclayout.setVisibility(View.GONE);
                            //if internet is ON then STATUS = 1
                            String InternetStatus = "1";

                            // Server Uploaded Status = 1
                            String UploadedStatus = "1"; // as this feature work on interent only so status will be 1 only .


                            // Current Date
                            Date date = new Date();
                            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy"); // getting date in this format
                            String currentdate = df.format(date.getTime());


                            db = new DbHelper(getActivity());
                            db.open();
                            db.insertissuedetails(NFCCardId,Util.valE(etName),Util.valE(etNumber), Util.valE(etAmount),"", encodeString, Util.valE(etAadhar),professionId,villageId,Util.getPrefData(NGO_ID,mContext),Util.getPrefData(USER_ID,mContext),InternetStatus,UploadedStatus,currentdate, etCardNumber.getText().toString());
                            db.close();

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
                            detectMThread = new DetectMThread();
                            detectMThread.start();

                        } else {
                            Util.showToast(mContext, response.body().getMsg());
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

    class DetectMThread extends Thread {
        @Override
        public void run() {
            super.run();
            String blockStr = blockNumEt.getText().toString();
            int blockNum = Integer.parseInt(blockStr.equals("") ? blockNumEt.getHint().toString() : blockStr);
            String password = passwordEt.getText().toString().equals("") ? passwordEt.getHint().toString()
                    : passwordEt.getText().toString();
            Log.i("Test", "keyType:"+m1KeyType.name()+" blockNum:" + blockNum + " password:" + password+"  "+piccType);
            String amount = ""+etAmount.getText().toString();

            if (amount.trim().length()>0&&tvSecurityAmount.getText() != null && tvSecurityAmount.getText().toString().trim().length() > 0 && Double.parseDouble(Util.valE(etAmount))<Double.parseDouble(tvSecurityAmount.getText().toString().trim())){
                amount = ""+(Double.parseDouble(amount)-Double.parseDouble(tvSecurityAmount.getText().toString().trim()));
            }
            PiccTester.getInstance(piccType).detectMIssueCard(handler, m1KeyType, blockNum, amount,tvCardNumber.getText().toString(),etName.getText().toString(),NFCCardId,
                    Convert.getInstance().strToBcd(password.toUpperCase(Locale.ENGLISH), Convert.EPaddingPosition.PADDING_RIGHT));
//            PiccTester.getInstance(piccType).detectM(handler, m1KeyType, blockNum, etAmount.getText().toString(),
//                    Convert.getInstance().strToBcd(password.toUpperCase(Locale.ENGLISH), Convert.EPaddingPosition.PADDING_RIGHT));


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

            String amount = ""+etAmount.getText().toString();

            if (amount.trim().length()>0&&tvSecurityAmount.getText() != null && tvSecurityAmount.getText().toString().trim().length() > 0 && Double.parseDouble(Util.valE(etAmount))<Double.parseDouble(tvSecurityAmount.getText().toString().trim())){
                amount = ""+(Double.parseDouble(amount)-Double.parseDouble(tvSecurityAmount.getText().toString().trim()));
            }

            PiccTester.getInstance(piccType).detectMnew(handler, m1KeyType, blockNum,amount ,
                    Convert.getInstance().strToBcd(password.toUpperCase(Locale.ENGLISH), Convert.EPaddingPosition.PADDING_RIGHT));
        }
    }

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

}
