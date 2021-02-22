package app.vmp.driver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NextStopDetails {

    @SerializedName("Id")
    @Expose
    private String ID = "";

    @SerializedName("LocationName")
    @Expose
    private String LocationName = "";

    @SerializedName("LocationURL")
    @Expose
    private String LocationURL = "";

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public String getLocationURL() {
        return LocationURL;
    }

    public void setLocationURL(String locationURL) {
        LocationURL = locationURL;
    }
}
