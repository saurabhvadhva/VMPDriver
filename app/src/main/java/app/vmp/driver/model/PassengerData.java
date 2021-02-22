package app.vmp.driver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PassengerData {

    @SerializedName("Id")
    @Expose
    private String ID = "";

    @SerializedName("ByMainPassengerName")
    @Expose
    private String ByMainPassengerName = "";

    @SerializedName("PassengerTravelDetailId")
    @Expose
    private String PassengerTravelDetailId = "";

    @SerializedName("DailyCharterId")
    @Expose
    private String DailyCharterId = "";

    @SerializedName("PassengerId")
    @Expose
    private String PassengerId = "";

    @SerializedName("PassengerName")
    @Expose
    private String PassengerName = "";

    @SerializedName("MobileNumber")
    @Expose
    private String MobileNumber = "";

    @SerializedName("ProfileImage")
    @Expose
    private String ProfileImage = "";

    @SerializedName("StartLocationId")
    @Expose
    private String StartLocationId = "";

    @SerializedName("EndLocationId")
    @Expose
    private String EndLocationId = "";

    @SerializedName("RouteId")
    @Expose
    private String RouteId = "";

    @SerializedName("TypeOfPassenger")
    @Expose
    private String TypeOfPassenger = "";

    @SerializedName("StartRouteLocation")
    @Expose
    private StartLocation StartRouteLocation ;

    @SerializedName("EndRouteLocation")
    @Expose
    private StartLocation EndRouteLocation ;

    @SerializedName("isOffline")
    @Expose
    private String isOffline = "false" ;

    @SerializedName("timestamp")
    @Expose
    private String timestamp = "" ;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getIsOffline() {
        return isOffline;
    }

    public void setIsOffline(String isOffline) {
        this.isOffline = isOffline;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDailyCharterId() {
        return DailyCharterId;
    }

    public void setDailyCharterId(String dailyCharterId) {
        DailyCharterId = dailyCharterId;
    }

    public String getPassengerId() {
        return PassengerId;
    }

    public void setPassengerId(String passengerId) {
        PassengerId = passengerId;
    }


    public String getPassengerName() {
        return PassengerName;
    }

    public void setPassengerName(String passengerName) {
        PassengerName = passengerName;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getStartLocationId() {
        return StartLocationId;
    }

    public void setStartLocationId(String startLocationId) {
        StartLocationId = startLocationId;
    }

    public String getEndLocationId() {
        return EndLocationId;
    }

    public void setEndLocationId(String endLocationId) {
        EndLocationId = endLocationId;
    }

    public String getRouteId() {
        return RouteId;
    }

    public void setRouteId(String routeId) {
        RouteId = routeId;
    }

    public StartLocation getStartRouteLocation() {
        return StartRouteLocation;
    }

    public void setStartRouteLocation(StartLocation startRouteLocation) {
        StartRouteLocation = startRouteLocation;
    }

    public StartLocation getEndRouteLocation() {
        return EndRouteLocation;
    }

    public void setEndRouteLocation(StartLocation endRouteLocation) {
        EndRouteLocation = endRouteLocation;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public String getPassengerTravelDetailId() {
        return PassengerTravelDetailId;
    }

    public void setPassengerTravelDetailId(String passengerTravelDetailId) {
        PassengerTravelDetailId = passengerTravelDetailId;
    }

    public String getTypeOfPassenger() {
        return TypeOfPassenger;
    }

    public String getByMainPassengerName() {
        return ByMainPassengerName;
    }

    public void setByMainPassengerName(String byMainPassengerName) {
        ByMainPassengerName = byMainPassengerName;
    }

    public void setTypeOfPassenger(String typeOfPassenger) {
        TypeOfPassenger = typeOfPassenger;
    }
}
