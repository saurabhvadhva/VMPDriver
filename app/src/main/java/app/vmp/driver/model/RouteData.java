package app.vmp.driver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RouteData {

    @SerializedName("VehicleRegistration")
    @Expose
    private String VehicleRegistration = "";

    @SerializedName("VehicleId")
    @Expose
    private String VehicleId = "";

    @SerializedName("RouteId")
    @Expose
    private String RouteId = "";

    @SerializedName("TimeSlot")
    @Expose
    private String TimeSlot = "";

    @SerializedName("RouteName")
    @Expose
    private String RouteName = "";

    @SerializedName("RouteTimeSlotId")
    @Expose
    private String RouteTimeSlotId = "";

    @SerializedName("DriverId")
    @Expose
    private String DriverId = "";

    @SerializedName("PassengerCount")
    @Expose
    private String PassengerCount = "";

    @SerializedName("StartLocation")
    @Expose
    private StartLocation StartLocation ;

    @SerializedName("EndLocation")
    @Expose
    private StartLocation EndLocation ;

    public String getVehicleRegistration() {
        return VehicleRegistration;
    }

    public void setVehicleRegistration(String vehicleRegistration) {
        VehicleRegistration = vehicleRegistration;
    }

    public String getVehicleId() {
        return VehicleId;
    }

    public void setVehicleId(String vehicleId) {
        VehicleId = vehicleId;
    }

    public String getRouteId() {
        return RouteId;
    }

    public void setRouteId(String routeId) {
        RouteId = routeId;
    }

    public String getTimeSlot() {
        return TimeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        TimeSlot = timeSlot;
    }

    public String getRouteTimeSlotId() {
        return RouteTimeSlotId;
    }

    public void setRouteTimeSlotId(String routeTimeSlotId) {
        RouteTimeSlotId = routeTimeSlotId;
    }

    public String getDriverId() {
        return DriverId;
    }

    public void setDriverId(String driverId) {
        DriverId = driverId;
    }

    public String getPassengerCount() {
        return PassengerCount;
    }

    public void setPassengerCount(String passengerCount) {
        PassengerCount = passengerCount;
    }

    public app.vmp.driver.model.StartLocation getStartLocation() {
        return StartLocation;
    }

    public void setStartLocation(app.vmp.driver.model.StartLocation startLocation) {
        StartLocation = startLocation;
    }

    public app.vmp.driver.model.StartLocation getEndLocation() {
        return EndLocation;
    }

    public void setEndLocation(app.vmp.driver.model.StartLocation endLocation) {
        EndLocation = endLocation;
    }

    public String getRouteName() {
        return RouteName;
    }

    public void setRouteName(String routeName) {
        RouteName = routeName;
    }
}
