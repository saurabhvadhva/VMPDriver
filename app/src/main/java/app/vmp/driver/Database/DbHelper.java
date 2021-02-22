package app.vmp.driver.Database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import app.vmp.driver.model.FixedFareResponse;
import app.vmp.driver.model.PassengerData;
import app.vmp.driver.model.StartLocation;

public class DbHelper extends SQLiteOpenHelper {
    static String DATABASE_NAME = "VMPDriverDatabase";

    public static final String TABLE_ISSUECARD = "Issuecard";
    public static final String TABLE_TOPUPCARD ="Topupcard";
    public static final String TABLE_ONBOARD ="TableOnBoard";
    public static final String TABLE_DEBOARD ="TableDeBoard";
    public static final String TABLE_ADD_PASSENGER ="AddPassenger";
    public static final String TABLE_ONBOARD_PASSENGER ="TableOnBoardPassenger";
    public static final String TABLE_DEBOARD_PASSENGER ="TableDeBoardPassenger";
    public static final String TABLE_SAVE_TOPUP ="TableSaveTopUp";

    public static final String KEY_ISSUECARD_ID = "SqliteIssueCardId";
    public static final String KEY_TOPUPCARD_ID = "SqliteTopUpCardId";
    public static final String KEY_PASSENGER_ID = "SqlitePassengerId";
    public static final String KEY_ONBOARD_ID = "SqliteOnBoardId";
    public static final String KEY_DEBOARD_ID = "SqliteOnBoardId";
    public static final String KEY_SAVE_TOPUP_ID = "SqliteTopUpId";

    public static final String TAG_TOPUP_NFCID = "TpNfcId";
    public static final String TAG_TOPUP_AMOUNT = "TpAmount";
    public static final String TAG_TOPUP_TIMESTAMP = "TpTimeStamp";

    public static final String TAG_ADDP_NAME = "AddpName";
    public static final String TAG_ADDP_MOBILE = "AddpMobile";
    public static final String TAG_ADDP_DATE = "AddpDate";
    public static final String TAG_ADDP_TIMESTAMP = "AddpTimestamp";
    public static final String TAG_ADDP_SLID = "AddpSlid";
    public static final String TAG_ADDP_ELID = "AddpElid";

    public static final String TAG_DEB_PTDI = "DebPTDI";
    public static final String TAG_DEB_TOP = "DebTOP";
    public static final String TAG_DEB_DCI = "DebDCI";
    public static final String TAG_DEB_DEMI = "DebDEMI";
    public static final String TAG_DEB_PSGI = "DebPSGI";
    public static final String TAG_DEB_ELID = "DebELID";
    public static final String TAG_DEB_FARE = "DebFARE";
    public static final String TAG_DEB_NFCID = "DebNFCID";
    public static final String TAG_DEB_TIMESTAMP = "DebTIMESTAMP";

    public static final String TAG_ONB_DC_ID = "OnbDcId";
    public static final String TAG_ONB_TYPE_OF = "OnbTypOf";
    public static final String TAG_ONB_DEM_ID = "OnbDemId";
    public static final String TAG_ONB_PSNGR_ID = "OnbPsngrId";
    public static final String TAG_ONB_SL_ID = "OnbSlId";
    public static final String TAG_ONB_NFC_ID = "OnbNfcId";
    public static final String TAG_ONB_TIMESTAMP = "OnbTimeStamp";

    public static final String TAG_PSNGER_ID = "PsngerId";
    public static final String TAG_PSNGER_MAIN_NAME = "PsngerMainName";
    public static final String TAG_PSNGER_TRAVEL_DETAIL_ID = "PsngerTravelDetailId";
    public static final String TAG_PSNGER_DAILY_CHARTER_ID = "PsngerDailyCharterId";
    public static final String TAG_PSNGER_PASSENGER_ID = "PsngerPassengerId";
    public static final String TAG_PSNGER_PASSENGER_NAME = "PsngerPassengerName";
    public static final String TAG_PSNGER_MOBILE_NUMBER = "PsngerMobileNumber";
    public static final String TAG_PSNGER_PROFILE_IMAGE = "PsngerProfileImage";
    public static final String TAG_PSNGER_START_LOC_ID = "PsngerStartLocId";
    public static final String TAG_PSNGER_END_LOC_ID = "PsngerEndLocId";
    public static final String TAG_PSNGER_TYPE_OF = "PsngerTypeOf";
    public static final String TAG_PSNGER_IS_OFFLINE = "PsngerIsOffline";
    public static final String TAG_PSNGER_TIMESTAMP = "PsngerTimestamp";
    public static final String TAG_PSNGER_START_ROUTE_LOC = "PsngerStartRouteLoc";
    public static final String TAG_PSNGER_END_ROUTE_LOC = "PsngerEndRouteLoc";

    public static final String TAG_Issue_NFCCardId = "issuenfccardid";
    public static final String TAG_Issue_PassengerName = "passengername";
    public static final String TAG_Issue_MobileNumber = "mobilenumber";
    public static final String TAG_Issue_Amount = "amount";
    public static final String TAG_Issue_MobileNumber2 = "mobilenumber2";
    public static final String TAG_Issue_ProfileImage = "profileimage";
    public static final String TAG_Issue_AadharNumber = "aadharnumber";
    public static final String TAG_Issue_ProfessionTypeId = "professiontypeid";
    public static final String TAG_Issue_VillageId = "villageid";
    public static final String TAG_Issue_NGOId = "ngoid";
    public static final String TAG_Issue_UserID = "issueuserid";
    public static final String TAG_Issue_Internet_Staus = "issueinternetstatus";
    public static final String TAG_Issue_serverupload_Staus = "issueserveruploadstatus";
    public static final String TAG_Issue_currentdate = "issuecurrentdate";
    public static final String TAG_Issue_NFCCardNumber = "issuenfccardNumber";

    public static final String TAG_Topup_NFCCardId = "topupnfccardid";
    public static final String TAG_Topup_TransactionType = "transactiontype";
    public static final String TAG_Topup_TransactionAmount = "trnsactionamount";
    public static final String TAG_Topup_UserID = "topupuserid";
    public static final String TAG_topup_Internet_Staus = "topupinternetstatus";
    public static final String TAG_topup_serverupload_Staus = "topupserveruploadstatus";
    public static final String TAG_topup_currentdate = "topupcurrentdate";
    public static final String TAG_topup_NFCCardNumber = "topupnfccardnumber";

    SQLiteDatabase db;
    private static DbHelper mInstance;
    private final Context context;
    public DbHelper(Context context) {

 //super(context, Environment.getExternalStorageDirectory()+ File.separator+"/Vmpdriver/"+DATABASE_NAME+".db", null, 1);
        super(context, DATABASE_NAME, null, 2);
        this.context = context;
    }


    public static DbHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DbHelper(context);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_ISSUECARD + " (" + KEY_ISSUECARD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + TAG_Issue_NFCCardId + " VARCHAR," + TAG_Issue_PassengerName + " VARCHAR," + TAG_Issue_MobileNumber + " VARCHAR," + TAG_Issue_Amount + " VARCHAR," + TAG_Issue_MobileNumber2 + " VARCHAR," + TAG_Issue_ProfileImage + " VARCHAR," + TAG_Issue_AadharNumber + " VARCHAR," + TAG_Issue_ProfessionTypeId + " VARCHAR," + TAG_Issue_VillageId + " VARCHAR," + TAG_Issue_NGOId + " VARCHAR," + TAG_Issue_UserID + " VARCHAR," + TAG_Issue_Internet_Staus + " VARCHAR," + TAG_Issue_serverupload_Staus + " VARCHAR," + TAG_Issue_currentdate + " VARCHAR," + TAG_Issue_NFCCardNumber + " VARCHAR)";
        String CREATE_TABLEEE = "CREATE TABLE " + TABLE_TOPUPCARD + " (" + KEY_TOPUPCARD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + TAG_Topup_NFCCardId + " VARCHAR," + TAG_Topup_TransactionType + " VARCHAR," + TAG_Topup_TransactionAmount + " VARCHAR," + TAG_Topup_UserID + " VARCHAR," + TAG_topup_Internet_Staus + " VARCHAR," + TAG_topup_serverupload_Staus + " VARCHAR," + TAG_topup_currentdate + " VARCHAR," + TAG_topup_NFCCardNumber + " VARCHAR)";
        String CREATE_TABLE_ONBOARD = "CREATE TABLE " + TABLE_ONBOARD + " (" + KEY_PASSENGER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + TAG_PSNGER_ID + " VARCHAR," +
                TAG_PSNGER_MAIN_NAME + " VARCHAR," +
                TAG_PSNGER_TRAVEL_DETAIL_ID + " VARCHAR," +
                TAG_PSNGER_DAILY_CHARTER_ID + " VARCHAR," +
                TAG_PSNGER_PASSENGER_ID + " VARCHAR," +
                TAG_PSNGER_PASSENGER_NAME + " VARCHAR," +
                TAG_PSNGER_MOBILE_NUMBER + " VARCHAR," +
                TAG_PSNGER_PROFILE_IMAGE + " VARCHAR," +
                TAG_PSNGER_START_LOC_ID + " VARCHAR," +
                TAG_PSNGER_END_LOC_ID + " VARCHAR," +
                TAG_PSNGER_TYPE_OF + " VARCHAR," +
                TAG_PSNGER_IS_OFFLINE + " VARCHAR," +
                TAG_PSNGER_TIMESTAMP + " VARCHAR," +
                TAG_PSNGER_START_ROUTE_LOC + " VARCHAR," +
                TAG_PSNGER_END_ROUTE_LOC + " VARCHAR)";
        String CREATE_TABLE_DEBOARD = "CREATE TABLE " + TABLE_DEBOARD + " (" + KEY_PASSENGER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + TAG_PSNGER_ID + " VARCHAR," +
                TAG_PSNGER_MAIN_NAME + " VARCHAR," +
                TAG_PSNGER_TRAVEL_DETAIL_ID + " VARCHAR," +
                TAG_PSNGER_DAILY_CHARTER_ID + " VARCHAR," +
                TAG_PSNGER_PASSENGER_ID + " VARCHAR," +
                TAG_PSNGER_PASSENGER_NAME + " VARCHAR," +
                TAG_PSNGER_MOBILE_NUMBER + " VARCHAR," +
                TAG_PSNGER_PROFILE_IMAGE + " VARCHAR," +
                TAG_PSNGER_START_LOC_ID + " VARCHAR," +
                TAG_PSNGER_END_LOC_ID + " VARCHAR," +
                TAG_PSNGER_TYPE_OF + " VARCHAR," +
                TAG_PSNGER_IS_OFFLINE + " VARCHAR," +
                TAG_PSNGER_TIMESTAMP + " VARCHAR," +
                TAG_PSNGER_START_ROUTE_LOC + " VARCHAR," +
                TAG_PSNGER_END_ROUTE_LOC + " VARCHAR)";
        String CREATE_TABLE_ONBOARD_PASSENGER = "CREATE TABLE " + TABLE_ONBOARD_PASSENGER + " (" + KEY_ONBOARD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                TAG_ONB_DC_ID + " VARCHAR," +
                TAG_ONB_TYPE_OF + " VARCHAR," +
                TAG_ONB_DEM_ID + " VARCHAR," +
                TAG_ONB_PSNGR_ID + " VARCHAR," +
                TAG_ONB_SL_ID + " VARCHAR," +
                TAG_ONB_TIMESTAMP + " VARCHAR," +
                TAG_ONB_NFC_ID + " VARCHAR)";
        String CREATE_TABLE_DEBOARD_PASSENGER = "CREATE TABLE " + TABLE_DEBOARD_PASSENGER + " (" + KEY_DEBOARD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                TAG_DEB_PTDI + " VARCHAR," +
                TAG_DEB_TOP + " VARCHAR," +
                TAG_DEB_DCI + " VARCHAR," +
                TAG_DEB_DEMI + " VARCHAR," +
                TAG_DEB_PSGI + " VARCHAR," +
                TAG_DEB_ELID + " VARCHAR," +
                TAG_DEB_FARE + " VARCHAR," +
                TAG_DEB_TIMESTAMP + " VARCHAR," +
                TAG_DEB_NFCID + " VARCHAR)";
        String CREATE_TABLE_ADD_PASSENGER = "CREATE TABLE " + TABLE_ADD_PASSENGER + " (" + KEY_PASSENGER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                TAG_ADDP_NAME + " VARCHAR," +
                TAG_ADDP_MOBILE + " VARCHAR," +
                TAG_ADDP_DATE + " VARCHAR," +
                TAG_ADDP_SLID + " VARCHAR," +
                TAG_ADDP_ELID + " VARCHAR," +
                TAG_ADDP_TIMESTAMP + " VARCHAR)";
        String CREATE_TABLE_SAVE_TOPUP = "CREATE TABLE " + TABLE_SAVE_TOPUP + " (" + KEY_SAVE_TOPUP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                TAG_TOPUP_NFCID + " VARCHAR," +
                TAG_TOPUP_AMOUNT + " VARCHAR," +
                TAG_TOPUP_TIMESTAMP + " VARCHAR)";
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLEEE);
        db.execSQL(CREATE_TABLE_ONBOARD);
        db.execSQL(CREATE_TABLE_DEBOARD);
        db.execSQL(CREATE_TABLE_ADD_PASSENGER);
        db.execSQL(CREATE_TABLE_ONBOARD_PASSENGER);
        db.execSQL(CREATE_TABLE_DEBOARD_PASSENGER);
        db.execSQL(CREATE_TABLE_SAVE_TOPUP);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ISSUECARD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOPUPCARD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ONBOARD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEBOARD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADD_PASSENGER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ONBOARD_PASSENGER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEBOARD_PASSENGER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVE_TOPUP);
        onCreate(db);

    }

    public synchronized void open() {
        db = getWritableDatabase();
    }

    public void insertissuedetails(String issuenfccardid, String passengername,String mobilenumber,String amount, String mobilenumber2, String profileimage,String aadharnumber,String professiontypeid, String villageid, String ngoid, String issueuserid , String internetstatus, String serveruploadstatus, String currentdate, String nfccardnumber) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("issuenfccardid",issuenfccardid);
            contentValues.put("passengername", passengername);
            contentValues.put("mobilenumber", mobilenumber);
            contentValues.put("amount", amount);
            contentValues.put("mobilenumber2", mobilenumber2);
            contentValues.put("profileimage",profileimage);
            contentValues.put("aadharnumber",aadharnumber);
            contentValues.put("professiontypeid",professiontypeid);
            contentValues.put("villageid",villageid);
            contentValues.put("ngoid", ngoid);
            contentValues.put("issueuserid",issueuserid);
            contentValues.put("issueinternetstatus",internetstatus);
            contentValues.put("issueserveruploadstatus",serveruploadstatus);
            contentValues.put("issuecurrentdate",currentdate);
            contentValues.put("issuenfccardNumber",nfccardnumber);
            long rowId = db.insert(TABLE_ISSUECARD, null, contentValues);


        } catch (Exception e) {
            e.printStackTrace();


        }


    }




    public void inserttopupdetails(String topupnfccardid, String transactiontype,String trnsactionamount,String topupuserid, String topupinternetstatus, String issueserveruploadstatus,String issuecurrentdate,String nfccardnumber) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("topupnfccardid",topupnfccardid);
            contentValues.put("transactiontype", transactiontype);
            contentValues.put("trnsactionamount", trnsactionamount);
            contentValues.put("topupuserid", topupuserid);
            contentValues.put("topupinternetstatus",topupinternetstatus);
            contentValues.put("topupserveruploadstatus",issueserveruploadstatus);
            contentValues.put("topupcurrentdate",issuecurrentdate);
            contentValues.put("topupnfccardnumber",nfccardnumber);
            long rowId = db.insert(TABLE_TOPUPCARD, null, contentValues);


        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void updateTopupData(String Amount,String NFCCardnumber,String topupinternetstatus,String topupserveruploadstatus,String currentdate) {
        db = getWritableDatabase();
        String sql = "UPDATE  Topupcard set trnsactionamount = '"+Amount+"'  ,  topupinternetstatus = '"+topupinternetstatus+"' ,  topupserveruploadstatus = '"+topupserveruploadstatus+"' ,  topupcurrentdate = '"+currentdate+"' where topupnfccardnumber='" + NFCCardnumber + "'";
        db.execSQL(sql);
        db.close();
    }

    public int validatetopupcard(String NFCCardnumber) {

        String countQuery = "SELECT  * FROM " + TABLE_TOPUPCARD +" where topupnfccardnumber='"+NFCCardnumber.trim()+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;

    }

    public void deleteOnBoard(){
        db.delete(TABLE_ONBOARD,null,null);
    }

    public void deleteADDP(){
        db.delete(TABLE_ADD_PASSENGER,null,null);
    }

    public void deleteONB(){
        db.delete(TABLE_ONBOARD_PASSENGER,null,null);
    }

    public void deleteDEB(){
        db.delete(TABLE_DEBOARD_PASSENGER,null,null);
    }

    public void deleteDeBoard(){
        db.delete(TABLE_DEBOARD,null,null);
    }

    public void deleteOnBoardItem(String did){
        db.delete(TABLE_ONBOARD,TAG_PSNGER_PASSENGER_ID + "=?",new String[]{did});
    }

    public void deleteOnBoardItemByTimestamp(String timestamp){
        db.delete(TABLE_ONBOARD,TAG_PSNGER_TIMESTAMP + "=?",new String[]{timestamp});
    }

    public void deleteOnBoardPassenger(String did){
        db.delete(TABLE_ONBOARD_PASSENGER,TAG_ONB_PSNGR_ID + "=?",new String[]{did});
    }

    public ArrayList<HashMap<String,String>> getOnBoardPsngrList(){
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        Cursor  cursor = db.rawQuery("select * from "+TABLE_ONBOARD_PASSENGER,null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                HashMap<String,String> map = new HashMap<>();
                map.put(TAG_ONB_DC_ID,cursor.getString(cursor.getColumnIndex(TAG_ONB_DC_ID)));
                map.put(TAG_ONB_TYPE_OF,cursor.getString(cursor.getColumnIndex(TAG_ONB_TYPE_OF)));
                map.put(TAG_ONB_DEM_ID,cursor.getString(cursor.getColumnIndex(TAG_ONB_DEM_ID)));
                map.put(TAG_ONB_PSNGR_ID,cursor.getString(cursor.getColumnIndex(TAG_ONB_PSNGR_ID)));
                map.put(TAG_ONB_SL_ID,cursor.getString(cursor.getColumnIndex(TAG_ONB_SL_ID)));
                map.put(TAG_ONB_NFC_ID,cursor.getString(cursor.getColumnIndex(TAG_ONB_NFC_ID)));
                map.put(TAG_ONB_TIMESTAMP,cursor.getString(cursor.getColumnIndex(TAG_ONB_TIMESTAMP)));

                list.add(map);
                cursor.moveToNext();
            }
        }
        return list;
    }

    public ArrayList<PassengerData> getOnBoardList(){
        ArrayList<PassengerData> list = new ArrayList<>();
        Cursor  cursor = db.rawQuery("select * from "+TABLE_ONBOARD,null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                PassengerData passengerData = new PassengerData();
                passengerData.setID(cursor.getString(cursor.getColumnIndex(TAG_PSNGER_ID)));
                passengerData.setByMainPassengerName(cursor.getString(cursor.getColumnIndex(TAG_PSNGER_MAIN_NAME)));
                passengerData.setPassengerTravelDetailId(cursor.getString(cursor.getColumnIndex(TAG_PSNGER_TRAVEL_DETAIL_ID)));
                passengerData.setDailyCharterId(cursor.getString(cursor.getColumnIndex(TAG_PSNGER_DAILY_CHARTER_ID)));
                passengerData.setPassengerId(cursor.getString(cursor.getColumnIndex(TAG_PSNGER_PASSENGER_ID)));
                passengerData.setPassengerName(cursor.getString(cursor.getColumnIndex(TAG_PSNGER_PASSENGER_NAME)));
                passengerData.setMobileNumber(cursor.getString(cursor.getColumnIndex(TAG_PSNGER_MOBILE_NUMBER)));
                passengerData.setProfileImage(cursor.getString(cursor.getColumnIndex(TAG_PSNGER_PROFILE_IMAGE)));
                passengerData.setStartLocationId(cursor.getString(cursor.getColumnIndex(TAG_PSNGER_START_LOC_ID)));
                passengerData.setEndLocationId(cursor.getString(cursor.getColumnIndex(TAG_PSNGER_END_LOC_ID)));
                passengerData.setTypeOfPassenger(cursor.getString(cursor.getColumnIndex(TAG_PSNGER_TYPE_OF)));
                passengerData.setIsOffline(cursor.getString(cursor.getColumnIndex(TAG_PSNGER_IS_OFFLINE)));
                passengerData.setTimestamp(cursor.getString(cursor.getColumnIndex(TAG_PSNGER_TIMESTAMP)));
                String strS = cursor.getString(cursor.getColumnIndex(TAG_PSNGER_START_ROUTE_LOC));
                String strE = cursor.getString(cursor.getColumnIndex(TAG_PSNGER_END_ROUTE_LOC));
                Gson gson = new Gson();
                Type type = new TypeToken<StartLocation>() {
                }.getType();

                StartLocation startL = gson.fromJson(strS, type);
                StartLocation endL = gson.fromJson(strE, type);
                passengerData.setStartRouteLocation(startL);
                passengerData.setEndRouteLocation(endL);
                list.add(passengerData);
                cursor.moveToNext();
            }
        }
        return list;
    }
    public ArrayList<PassengerData> getDeBoardList(){
        ArrayList<PassengerData> list = new ArrayList<>();
        Cursor  cursor = db.rawQuery("select * from "+TABLE_DEBOARD,null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                PassengerData passengerData = new PassengerData();
                passengerData.setID(cursor.getString(cursor.getColumnIndex(TAG_PSNGER_ID)));
                passengerData.setByMainPassengerName(cursor.getString(cursor.getColumnIndex(TAG_PSNGER_MAIN_NAME)));
                passengerData.setPassengerTravelDetailId(cursor.getString(cursor.getColumnIndex(TAG_PSNGER_TRAVEL_DETAIL_ID)));
                passengerData.setDailyCharterId(cursor.getString(cursor.getColumnIndex(TAG_PSNGER_DAILY_CHARTER_ID)));
                passengerData.setPassengerId(cursor.getString(cursor.getColumnIndex(TAG_PSNGER_PASSENGER_ID)));
                passengerData.setPassengerName(cursor.getString(cursor.getColumnIndex(TAG_PSNGER_PASSENGER_NAME)));
                passengerData.setMobileNumber(cursor.getString(cursor.getColumnIndex(TAG_PSNGER_MOBILE_NUMBER)));
                passengerData.setProfileImage(cursor.getString(cursor.getColumnIndex(TAG_PSNGER_PROFILE_IMAGE)));
                passengerData.setStartLocationId(cursor.getString(cursor.getColumnIndex(TAG_PSNGER_START_LOC_ID)));
                passengerData.setEndLocationId(cursor.getString(cursor.getColumnIndex(TAG_PSNGER_END_LOC_ID)));
                passengerData.setTypeOfPassenger(cursor.getString(cursor.getColumnIndex(TAG_PSNGER_TYPE_OF)));
                passengerData.setIsOffline(cursor.getString(cursor.getColumnIndex(TAG_PSNGER_IS_OFFLINE)));
                passengerData.setTimestamp(cursor.getString(cursor.getColumnIndex(TAG_PSNGER_TIMESTAMP)));
                String strS = cursor.getString(cursor.getColumnIndex(TAG_PSNGER_START_ROUTE_LOC));
                String strE = cursor.getString(cursor.getColumnIndex(TAG_PSNGER_END_ROUTE_LOC));
                Gson gson = new Gson();
                Type type = new TypeToken<StartLocation>() {
                }.getType();

                StartLocation startL = gson.fromJson(strS, type);
                StartLocation endL = gson.fromJson(strE, type);
                passengerData.setStartRouteLocation(startL);
                passengerData.setEndRouteLocation(endL);
                list.add(passengerData);
                cursor.moveToNext();
            }
        }
        return list;
    }

    public void insertOnBoardData(ArrayList<PassengerData> list){

        for (int i=0;i<list.size();i++){
            PassengerData pData = list.get(i);
            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put(TAG_PSNGER_ID,pData.getID());
                contentValues.put(TAG_PSNGER_MAIN_NAME,pData.getByMainPassengerName());
                contentValues.put(TAG_PSNGER_TRAVEL_DETAIL_ID,pData.getPassengerTravelDetailId());
                contentValues.put(TAG_PSNGER_DAILY_CHARTER_ID,pData.getDailyCharterId());
                contentValues.put(TAG_PSNGER_PASSENGER_ID,pData.getPassengerId());
                contentValues.put(TAG_PSNGER_PASSENGER_NAME,pData.getPassengerName());
                contentValues.put(TAG_PSNGER_MOBILE_NUMBER,pData.getMobileNumber());
                contentValues.put(TAG_PSNGER_PROFILE_IMAGE,pData.getProfileImage());
                contentValues.put(TAG_PSNGER_START_LOC_ID,pData.getStartLocationId());
                contentValues.put(TAG_PSNGER_END_LOC_ID,pData.getEndLocationId());
                contentValues.put(TAG_PSNGER_TYPE_OF,pData.getTypeOfPassenger());
                contentValues.put(TAG_PSNGER_IS_OFFLINE,pData.getIsOffline());
                contentValues.put(TAG_PSNGER_TIMESTAMP,pData.getTimestamp());
                Gson gson  = new Gson();
                contentValues.put(TAG_PSNGER_START_ROUTE_LOC,gson.toJson(pData.getStartRouteLocation()));
                contentValues.put(TAG_PSNGER_END_ROUTE_LOC,gson.toJson(pData.getEndRouteLocation()));
                db.insert(TABLE_ONBOARD, null, contentValues);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public void insertDeBoardData(ArrayList<PassengerData> list){

        for (int i=0;i<list.size();i++){
            PassengerData pData = list.get(i);
            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put(TAG_PSNGER_ID,pData.getID());
                contentValues.put(TAG_PSNGER_MAIN_NAME,pData.getByMainPassengerName());
                contentValues.put(TAG_PSNGER_TRAVEL_DETAIL_ID,pData.getPassengerTravelDetailId());
                contentValues.put(TAG_PSNGER_DAILY_CHARTER_ID,pData.getDailyCharterId());
                contentValues.put(TAG_PSNGER_PASSENGER_ID,pData.getPassengerId());
                contentValues.put(TAG_PSNGER_PASSENGER_NAME,pData.getPassengerName());
                contentValues.put(TAG_PSNGER_MOBILE_NUMBER,pData.getMobileNumber());
                contentValues.put(TAG_PSNGER_PROFILE_IMAGE,pData.getProfileImage());
                contentValues.put(TAG_PSNGER_START_LOC_ID,pData.getStartLocationId());
                contentValues.put(TAG_PSNGER_END_LOC_ID,pData.getEndLocationId());
                contentValues.put(TAG_PSNGER_TYPE_OF,pData.getTypeOfPassenger());
                contentValues.put(TAG_PSNGER_IS_OFFLINE,pData.getIsOffline());
                contentValues.put(TAG_PSNGER_TIMESTAMP,pData.getTimestamp());
                Gson gson  = new Gson();
                contentValues.put(TAG_PSNGER_START_ROUTE_LOC,gson.toJson(pData.getStartRouteLocation()));
                contentValues.put(TAG_PSNGER_END_ROUTE_LOC,gson.toJson(pData.getEndRouteLocation()));
                db.insert(TABLE_DEBOARD, null, contentValues);
            } catch (Exception e){}
        }

    }

    public void insertOnBoardPassenger(String dcId,String typeofP,String demId,String psngrId,String slId,String nfcId,String timestamp){

            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put(TAG_ONB_DC_ID,dcId);
                contentValues.put(TAG_ONB_TYPE_OF,typeofP);
                contentValues.put(TAG_ONB_DEM_ID,demId);
                contentValues.put(TAG_ONB_PSNGR_ID,psngrId);
                contentValues.put(TAG_ONB_SL_ID,slId);
                contentValues.put(TAG_ONB_NFC_ID,nfcId);
                contentValues.put(TAG_ONB_TIMESTAMP,timestamp);
                db.insert(TABLE_ONBOARD_PASSENGER, null, contentValues);
            } catch (Exception e){}

    }

    public void insertAddPassenger(String name,String mobile,String date,String timestamp,String slid,String elid){

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TAG_ADDP_NAME,name);
            contentValues.put(TAG_ADDP_MOBILE,mobile);
            contentValues.put(TAG_ADDP_DATE,date);
            contentValues.put(TAG_ADDP_SLID,slid);
            contentValues.put(TAG_ADDP_ELID,elid);
            contentValues.put(TAG_ADDP_TIMESTAMP,timestamp);
            db.insert(TABLE_ADD_PASSENGER, null, contentValues);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public ArrayList<HashMap<String,String>> getAddPsngrList(){
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        Cursor  cursor = db.rawQuery("select * from "+TABLE_ADD_PASSENGER,null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                HashMap<String,String> map = new HashMap<>();
                map.put(TAG_ADDP_NAME,cursor.getString(cursor.getColumnIndex(TAG_ADDP_NAME)));
                map.put(TAG_ADDP_MOBILE,cursor.getString(cursor.getColumnIndex(TAG_ADDP_MOBILE)));
                map.put(TAG_ADDP_DATE,cursor.getString(cursor.getColumnIndex(TAG_ADDP_DATE)));
                map.put(TAG_ADDP_TIMESTAMP,cursor.getString(cursor.getColumnIndex(TAG_ADDP_TIMESTAMP)));
                map.put(TAG_ADDP_SLID,cursor.getString(cursor.getColumnIndex(TAG_ADDP_SLID)));
                map.put(TAG_ADDP_ELID,cursor.getString(cursor.getColumnIndex(TAG_ADDP_ELID)));

                list.add(map);
                cursor.moveToNext();
            }
        }
        return list;
    }

    public void deleteAddedPassenger(String timestamp){
        db.delete(TABLE_ADD_PASSENGER,TAG_ADDP_TIMESTAMP + "=?",new String[]{timestamp});
    }

    public void updateOnBoard(String passengerid,String dailycharterid,String demandid,String timestamp){

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TAG_ONB_PSNGR_ID,passengerid);
            contentValues.put(TAG_ONB_DC_ID,dailycharterid);
            contentValues.put(TAG_ONB_DEM_ID,demandid);
            db.update(TABLE_ONBOARD_PASSENGER,contentValues,TAG_ONB_TIMESTAMP+"= ?",new String[] {timestamp});
        } catch (Exception e){}

    }

    public void updateDeBoard(String passengerid,String dailycharterid,String demandid,String timestamp){

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TAG_DEB_PSGI,passengerid);
            contentValues.put(TAG_DEB_DCI,dailycharterid);
            contentValues.put(TAG_DEB_DEMI,demandid);
            db.update(TABLE_DEBOARD_PASSENGER,contentValues,TAG_DEB_TIMESTAMP+"= ?",new String[] {timestamp});
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void updateDeBoardPtdi(String ptdi,String demid){

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TAG_DEB_PTDI,ptdi);
            db.update(TABLE_DEBOARD_PASSENGER,contentValues,TAG_DEB_PSGI+"= ?",new String[] {demid});
        } catch (Exception e){}

    }

    public void insertDeBoardPassenger(String ptdid,String dcId,String typeofP,String demId,String psngrId,String elId,String fare,String nfcId,String timestamp){

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TAG_DEB_PTDI,ptdid);
            contentValues.put(TAG_DEB_DCI,dcId);
            contentValues.put(TAG_DEB_TOP,typeofP);
            contentValues.put(TAG_DEB_DEMI,demId);
            contentValues.put(TAG_DEB_PSGI,psngrId);
            contentValues.put(TAG_DEB_ELID,elId);
            contentValues.put(TAG_DEB_FARE,fare);
            contentValues.put(TAG_DEB_NFCID,nfcId);
            contentValues.put(TAG_DEB_TIMESTAMP,timestamp);
            db.insert(TABLE_DEBOARD_PASSENGER, null, contentValues);
        } catch (Exception e){}

    }

    public void deleteDeBoardItem(String did){
        db.delete(TABLE_DEBOARD,TAG_PSNGER_PASSENGER_ID + "=?",new String[]{did});
    }

    public void deleteDeBoardItemByTimestamp(String timestamp){
        db.delete(TABLE_DEBOARD,TAG_PSNGER_TIMESTAMP + "=?",new String[]{timestamp});
    }

    public ArrayList<HashMap<String,String>> getDeBoardPsngrList(){
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        Cursor  cursor = db.rawQuery("select * from "+TABLE_DEBOARD_PASSENGER,null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                HashMap<String,String> map = new HashMap<>();
                map.put(TAG_DEB_DCI,cursor.getString(cursor.getColumnIndex(TAG_DEB_DCI)));
                map.put(TAG_DEB_DEMI,cursor.getString(cursor.getColumnIndex(TAG_DEB_DEMI)));
                map.put(TAG_DEB_PSGI,cursor.getString(cursor.getColumnIndex(TAG_DEB_PSGI)));
                map.put(TAG_DEB_TOP,cursor.getString(cursor.getColumnIndex(TAG_DEB_TOP)));
                map.put(TAG_DEB_ELID,cursor.getString(cursor.getColumnIndex(TAG_DEB_ELID)));
                map.put(TAG_DEB_PTDI,cursor.getString(cursor.getColumnIndex(TAG_DEB_PTDI)));
                map.put(TAG_DEB_FARE,cursor.getString(cursor.getColumnIndex(TAG_DEB_FARE)));
                map.put(TAG_DEB_NFCID,cursor.getString(cursor.getColumnIndex(TAG_DEB_NFCID)));
                map.put(TAG_DEB_TIMESTAMP,cursor.getString(cursor.getColumnIndex(TAG_DEB_TIMESTAMP)));
                list.add(map);
                cursor.moveToNext();
            }
        }
        return list;
    }

    public void deleteDeBoardPassenger(String did){
        db.delete(TABLE_DEBOARD_PASSENGER,TAG_DEB_PSGI + "=?",new String[]{did});
    }

    public void insertTopUp(String nfctagid,String amount,String timestamp){

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TAG_TOPUP_NFCID,nfctagid);
            contentValues.put(TAG_TOPUP_AMOUNT,amount);
            contentValues.put(TAG_TOPUP_TIMESTAMP,timestamp);
            db.insert(TABLE_SAVE_TOPUP, null, contentValues);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<HashMap<String,String>> getTopUpListList(){
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        Cursor  cursor = db.rawQuery("select * from "+TABLE_SAVE_TOPUP,null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                HashMap<String,String> map = new HashMap<>();
                map.put(TAG_TOPUP_NFCID,cursor.getString(cursor.getColumnIndex(TAG_TOPUP_NFCID)));
                map.put(TAG_TOPUP_AMOUNT,cursor.getString(cursor.getColumnIndex(TAG_TOPUP_AMOUNT)));
                map.put(TAG_TOPUP_TIMESTAMP,cursor.getString(cursor.getColumnIndex(TAG_TOPUP_TIMESTAMP)));
                list.add(map);
                cursor.moveToNext();
            }
        }
        return list;
    }

    public void deleteTopUpItemByTimestamp(String timestamp){
        db.delete(TABLE_SAVE_TOPUP,TAG_TOPUP_TIMESTAMP + "=?",new String[]{timestamp});
    }

    public void deleteTopUp(){
        db.delete(TABLE_SAVE_TOPUP,null,null);
    }

}
