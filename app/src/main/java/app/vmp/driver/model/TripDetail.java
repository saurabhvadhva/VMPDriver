package app.vmp.driver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TripDetail {

    @SerializedName("Id")
    @Expose
    private String ID = "";

    @SerializedName("DriverId")
    @Expose
    private String DriverId = "";

    @SerializedName("RouteId")
    @Expose
    private String RouteId = "";

    @SerializedName("StartDate")
    @Expose
    private String StartDate = "";

    @SerializedName("EndDate")
    @Expose
    private String EndDate = "";

    @SerializedName("TimeSlot")
    @Expose
    private String TimeSlot = "";

    @SerializedName("RouteName")
    @Expose
    private String RouteName = "";

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDriverId() {
        return DriverId;
    }

    public void setDriverId(String driverId) {
        DriverId = driverId;
    }

    public String getRouteId() {
        return RouteId;
    }

    public void setRouteId(String routeId) {
        RouteId = routeId;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getTimeSlot() {
        return TimeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        TimeSlot = timeSlot;
    }

    public String getRouteName() {
        return RouteName;
    }

    public void setRouteName(String routeName) {
        RouteName = routeName;
    }
}
