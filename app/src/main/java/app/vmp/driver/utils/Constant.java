package app.vmp.driver.utils;

public class Constant {

    public static int splash_time = 1500;
    public static int LOCATION_UPDATE_TIME = 10000;
    public static int NEXT_STOP_UPDATE_TIME = 10000;
    public static final String BASE_URL = "http://18.191.172.215:1990/";
    public static final String BASE_URL2 = "http://18.191.172.215:1994/";

    public static final String CONTENT_TYPE = "application/json";
    public static final String PREF_NAME = "VmpDriver";

    public static final String SEL_LANG = "SelLanguage";

    public static final String USER_ID = "UserID";
    public static final String VEHICLE_ID = "VehicleID";
    public static final String EMPLOYEE_ID = "EmployeeID";
    public static final String CURONG_ROUTE_ID = "CurOngRouteId";
    public static final String CURONG_ROUTE_NAME = "CurOngRouteName";
    public static final String CURONG_TIME_SLOT = "CurOngTimeSlot";
    public static final String NGO_ID = "NGOId";
    public static final String STATE_ID = "StateID";
    public static final String USER_NAME = "UserName";
    public static final String FULL_NAME = "FullName";
    public static final String USER_PHOTO = "ProfilePhoto";
    public static final String PROFESSION_TYPE = "ProfessionType";
    public static final String TOKEN = "JwtToken";
    public static final String DEVICE_ID = "DeviceId";
    public static final String CUR_ROUTE_ID = "CurRouteId";
    public static final String ROUTE_ID_DIRECTION = "CurRouteIdDirection";
    public static final String ROUTE_ID_SHG = "CurRouteIdShg";
    public static final String ROUTE_ID_FARE = "CurRouteIdFare";
    public static final String LOCATION_LIST = "LocationList";
    public static final String DIRECTION_LIST = "DirectionList";
    public static final String SHG_NUMBER = "ShgNumber";
    public static final String FIXED_FARE_LIST = "FixedFareList";

    public static final String API_GET_OTP = "api/Users/GenerateOtp";
    public static final String API_VERIFY_OTP = "api/Users/VerifyOTP";
    public static final String API_GET_OPTIONS = "api/OptionTypes/GetOptionType?";
    public static final String API_SEARCH_NFC_CARD = "api/NFC/SearchNFCCard";
    public static final String API_TOPUP_CARD = "api/NFC/AddTransactionToNFCCard";
    public static final String API_ISSUE_CARD = "api/NFC/EnrollNFCCard";
    public static final String API_GET_ROUTE = "api/Routes/GetRoutesByDriver";
    public static final String API_START_TRIP = "api/DailyCharter/InsertStartRoute";
    public static final String API_PASSENGER_LIST = "api/Passenger/GetPassengersByRouteIdAndTimeSlot";
    public static final String API_ONBOARD_PASSENGER = "api/Passenger/OnBoardPassenger";
    public static final String API_DEBOARD_PASSENGER = "api/Passenger/DeBoardPassenger";
    public static final String API_GET_FARE = "api/Passenger/GetPassengerFare";
    public static final String API_GET_DISTRICT = "api/Geography/GetAllDistrictByStateID";
    public static final String API_GET_TALUKA = "api/Geography/GetTalukaListByDistrictID?";
    public static final String API_GET_VILLAGE = "api/Geography/GetVillageListByTalukaID?";
    public static final String API_END_TRIP = "api/DailyCharter/EndRoute";
    public static final String GET_TRIP_DETAILS = "api/DailyCharter/GetDriverOnGoingTripDetails";
    public static final String API_LOGOUT = "api/Users/LogOut";
    public static final String API_GET_LOCATIONS = "api/Location/GetLocationsByRouteId?";
    public static final String API_ADD_PASSENGER = "api/Demand/AddPassengerWithOutEnroll";
    public static final String API_GET_DIRECTIONS = "api/Routes/GetRouteByID";
    public static final String API_GET_SHGDETAILS = "api/Routes/GetRouteScheduleSlotDetails?";
    public static final String API_GET_NEXT_STOP = "api/Driver/GetNextSpotByDriverId?";
    public static final String API_GET_DRIVER_LOCATION = "api/Driver/GetLocationDetailsBasedOnGps";
    public static final String API_GET_FIXED_RATE = "api/Fare/GetFixedFareByRouteId";
    public static final String API_SUBMIT_LOCATION = "api/Driver/UpdateDriverGPSLocation";
    public static final String API_FETCH_APK = "api/Apk/GetApkVersion";
    public static final String API_GET_SECURITY_AMOUNT = "api/NFC/GetNgoSecurityAmount?";
    public static final String API_GET_VEHICLE_LIST = "api/Vehicle/GetVehicleByNgoId";
    public static final String API_ADD_EXPENSE = "api/Expenses/InsertOperatingExpenses";

}
