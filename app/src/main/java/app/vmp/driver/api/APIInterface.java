package app.vmp.driver.api;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import app.vmp.driver.model.AddPassengerResponse;
import app.vmp.driver.model.DirectionResponse;
import app.vmp.driver.model.DistrictData;
import app.vmp.driver.model.FetchAPKResponse;
import app.vmp.driver.model.FixedFareResponse;
import app.vmp.driver.model.GeneralResponse;
import app.vmp.driver.model.LoginResponse;
import app.vmp.driver.model.NFCCardDetails;
import app.vmp.driver.model.NextStopDetails;
import app.vmp.driver.model.Options;
import app.vmp.driver.model.PassengerData;
import app.vmp.driver.model.RouteData;
import app.vmp.driver.model.RouteScheduleSlotDetails;
import app.vmp.driver.model.StartLocation;
import app.vmp.driver.model.TalukaData;
import app.vmp.driver.model.TripDetail;
import app.vmp.driver.model.VehicleData;
import app.vmp.driver.model.VillageData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

import static app.vmp.driver.utils.Constant.API_ADD_EXPENSE;
import static app.vmp.driver.utils.Constant.API_ADD_PASSENGER;
import static app.vmp.driver.utils.Constant.API_DEBOARD_PASSENGER;
import static app.vmp.driver.utils.Constant.API_END_TRIP;
import static app.vmp.driver.utils.Constant.API_FETCH_APK;
import static app.vmp.driver.utils.Constant.API_GET_DIRECTIONS;
import static app.vmp.driver.utils.Constant.API_GET_DISTRICT;
import static app.vmp.driver.utils.Constant.API_GET_DRIVER_LOCATION;
import static app.vmp.driver.utils.Constant.API_GET_FARE;
import static app.vmp.driver.utils.Constant.API_GET_FIXED_RATE;
import static app.vmp.driver.utils.Constant.API_GET_NEXT_STOP;
import static app.vmp.driver.utils.Constant.API_GET_OPTIONS;
import static app.vmp.driver.utils.Constant.API_GET_OTP;
import static app.vmp.driver.utils.Constant.API_GET_ROUTE;
import static app.vmp.driver.utils.Constant.API_GET_SHGDETAILS;
import static app.vmp.driver.utils.Constant.API_GET_TALUKA;
import static app.vmp.driver.utils.Constant.API_GET_VEHICLE_LIST;
import static app.vmp.driver.utils.Constant.API_GET_VILLAGE;
import static app.vmp.driver.utils.Constant.API_ISSUE_CARD;
import static app.vmp.driver.utils.Constant.API_LOGOUT;
import static app.vmp.driver.utils.Constant.API_ONBOARD_PASSENGER;
import static app.vmp.driver.utils.Constant.API_PASSENGER_LIST;
import static app.vmp.driver.utils.Constant.API_SEARCH_NFC_CARD;
import static app.vmp.driver.utils.Constant.API_START_TRIP;
import static app.vmp.driver.utils.Constant.API_SUBMIT_LOCATION;
import static app.vmp.driver.utils.Constant.API_TOPUP_CARD;
import static app.vmp.driver.utils.Constant.API_VERIFY_OTP;
import static app.vmp.driver.utils.Constant.GET_TRIP_DETAILS;

public interface APIInterface {

    @POST(API_GET_OTP)
    Call<GeneralResponse> getOTP(@Header("Content-Type") String content_Type, @Header("Accept") String Accept, @Body JsonObject jsonObject);

    @POST(API_VERIFY_OTP)
    Call<LoginResponse> verifyOTP(@Header("Content-Type") String content_Type, @Header("Accept") String Accept, @Body JsonObject jsonObject);

    @POST(API_GET_OPTIONS)
    Call<ArrayList<Options>> getOptionsType(@Header("Authorization") String auth, @Header("DeviceId") String DeviceId,@Header("Content-Type") String content_Type, @Header("Accept") String Accept, @Body JsonObject jsonObject);

    @POST(API_SEARCH_NFC_CARD)
    Call<NFCCardDetails> searchNFCcard(@Header("Authorization") String auth, @Header("DeviceId") String DeviceId,@Header("Content-Type") String content_Type, @Header("Accept") String Accept, @Body JsonObject jsonObject);

    @POST(API_TOPUP_CARD)
    Call<GeneralResponse> topupNFCcard(@Header("Authorization") String auth, @Header("DeviceId") String DeviceId,@Header("Content-Type") String content_Type, @Header("Accept") String Accept, @Body JsonObject jsonObject);

    @POST(API_ISSUE_CARD)
    Call<GeneralResponse> issueNFCcard(@Header("Authorization") String auth, @Header("DeviceId") String DeviceId,@Header("Content-Type") String content_Type, @Header("Accept") String Accept, @Body JsonObject jsonObject);

    @POST(API_GET_ROUTE)
    Call<ArrayList<RouteData>> getRoute(@Header("Authorization") String auth, @Header("DeviceId") String DeviceId,@Header("Content-Type") String content_Type, @Header("Accept") String Accept, @Body JsonObject jsonObject);

    @POST(API_START_TRIP)
    Call<GeneralResponse> startTrip(@Header("Authorization") String auth, @Header("DeviceId") String DeviceId,@Header("Content-Type") String content_Type, @Header("Accept") String Accept, @Body JsonObject jsonObject);

    @POST(API_PASSENGER_LIST)
    Call<ArrayList<PassengerData>> getPassengersList(@Header("Authorization") String auth, @Header("DeviceId") String DeviceId,@Header("Content-Type") String content_Type, @Header("Accept") String Accept, @Body JsonObject jsonObject);

    @POST(API_ONBOARD_PASSENGER)
    Call<GeneralResponse> onBoardPassenger(@Header("Authorization") String auth, @Header("DeviceId") String DeviceId,@Header("Content-Type") String content_Type, @Header("Accept") String Accept, @Body JsonObject jsonObject);

    @POST(API_GET_FARE)
    Call<GeneralResponse> getFare(@Header("Authorization") String auth, @Header("DeviceId") String DeviceId,@Header("Content-Type") String content_Type, @Header("Accept") String Accept, @Body JsonObject jsonObject);

    @POST(API_DEBOARD_PASSENGER)
    Call<GeneralResponse> deBoardPassenger(@Header("Authorization") String auth, @Header("DeviceId") String DeviceId,@Header("Content-Type") String content_Type, @Header("Accept") String Accept, @Body JsonObject jsonObject);

    @POST(API_GET_DISTRICT)
    Call<ArrayList<DistrictData>> getDistrictList(@Header("Authorization") String auth, @Header("DeviceId") String DeviceId,@Header("Content-Type") String content_Type, @Header("Accept") String Accept, @Body JsonObject jsonObject);

//    @GET(API_GET_TALUKA)
//    Call<ArrayList<TalukaData>> getTalukaList(@Header("Authorization") String auth, @Header("DeviceId") String DeviceId,@Header("Content-Type") String content_Type, @Header("Accept") String Accept, @Query("DistrictID") String id);
//
//    @GET(API_GET_VILLAGE)
//    Call<ArrayList<VillageData>> getVillageList(@Header("Authorization") String auth, @Header("DeviceId") String DeviceId,@Header("Content-Type") String content_Type, @Header("Accept") String Accept, @Query("TalukaID") String id);

    @POST(API_END_TRIP)
    Call<GeneralResponse> endTrip(@Header("Authorization") String auth, @Header("DeviceId") String DeviceId,@Header("Content-Type") String content_Type, @Header("Accept") String Accept, @Body JsonObject jsonObject);

    @POST(GET_TRIP_DETAILS)
    Call<TripDetail> getTripDetails(@Header("Authorization") String auth, @Header("DeviceId") String DeviceId,@Header("Content-Type") String content_Type, @Header("Accept") String Accept, @Body JsonObject jsonObject);

    @POST(API_LOGOUT)
    Call<GeneralResponse> logout(@Header("Authorization") String auth, @Header("DeviceId") String DeviceId,@Header("Content-Type") String content_Type, @Header("Accept") String Accept, @Body JsonObject jsonObject);

    @POST()
    Call<ArrayList<StartLocation>> getLocationList(@Header("Authorization") String auth, @Header("DeviceId") String DeviceId,@Header("Content-Type") String content_Type, @Header("Accept") String Accept, @Url String url);

    @POST(API_ADD_PASSENGER)
    Call<AddPassengerResponse> addPassenger(@Header("Authorization") String auth, @Header("DeviceId") String DeviceId, @Header("Content-Type") String content_Type, @Header("Accept") String Accept, @Body JsonObject jsonObject);

    @POST(API_GET_DIRECTIONS)
    Call<DirectionResponse> getDirections(@Header("Authorization") String auth, @Header("DeviceId") String DeviceId, @Header("Content-Type") String content_Type, @Header("Accept") String Accept, @Body JsonObject jsonObject);

    @POST(API_GET_SHGDETAILS)
    Call<ArrayList<RouteScheduleSlotDetails>> getShgDetails(@Header("Authorization") String auth, @Header("DeviceId") String DeviceId, @Header("Content-Type") String content_Type, @Header("Accept") String Accept, @Body JsonObject jsonObject);

    @POST(API_SUBMIT_LOCATION)
    Call<GeneralResponse> submitLocation(@Header("Authorization") String auth, @Header("DeviceId") String DeviceId,@Header("Content-Type") String content_Type, @Header("Accept") String Accept, @Body JsonObject jsonObject);

    @POST(API_GET_NEXT_STOP)
    Call<NextStopDetails> getNextStopDetails(@Header("Authorization") String auth, @Header("DeviceId") String DeviceId, @Header("Content-Type") String content_Type, @Header("Accept") String Accept, @Body JsonObject jsonObject);

    @POST(API_GET_DRIVER_LOCATION)
    Call<NextStopDetails> getDriverLocationDetails(@Header("Authorization") String auth, @Header("DeviceId") String DeviceId, @Header("Content-Type") String content_Type, @Header("Accept") String Accept, @Body JsonObject jsonObject);

    @POST(API_GET_FIXED_RATE)
    Call<ArrayList<FixedFareResponse>> getGetFixedRate(@Header("Authorization") String auth, @Header("DeviceId") String DeviceId, @Header("Content-Type") String content_Type, @Header("Accept") String Accept, @Body JsonObject jsonObject);

    @POST(API_FETCH_APK)
    Call<FetchAPKResponse> fetchAPK(@Header("Content-Type") String content_Type, @Header("Accept") String Accept, @Body JsonObject jsonObject);

    @POST()
    Call<ResponseBody> getSecurityAmount(@Header("Authorization") String auth, @Header("DeviceId") String DeviceId, @Header("Content-Type") String content_Type, @Header("Accept") String Accept, @Url String url);

    @POST(API_GET_VEHICLE_LIST)
    Call<ArrayList<VehicleData>> getVehicleList(@Header("Authorization") String auth, @Header("DeviceId") String DeviceId, @Header("Content-Type") String content_Type, @Header("Accept") String Accept, @Body JsonObject jsonObject);

    @POST(API_ADD_EXPENSE)
    Call<GeneralResponse> addExpense(@Header("Authorization") String auth, @Header("DeviceId") String DeviceId, @Header("Content-Type") String content_Type, @Header("Accept") String Accept, @Body JsonObject jsonObject);


}
