package app.vmp.driver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubRouteData {

    @SerializedName("LocationId")
    @Expose
    private String LocationId = "";

    @SerializedName("LocationName")
    @Expose
    private String LocationName = "";

    @SerializedName("Longitude")
    @Expose
    private String Longitude = "";

    @SerializedName("Lattitude")
    @Expose
    private String Lattitude = "";

    public String getLocationId() {
        return LocationId;
    }

    public void setLocationId(String locationId) {
        LocationId = locationId;
    }

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLattitude() {
        return Lattitude;
    }

    public void setLattitude(String lattitude) {
        Lattitude = lattitude;
    }
}
