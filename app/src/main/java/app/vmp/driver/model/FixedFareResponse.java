package app.vmp.driver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FixedFareResponse {

    @SerializedName("StartLocationId")
    @Expose
    private String StartLocationId = "";

    @SerializedName("StartLocationLocationName")
    @Expose
    private String StartLocationLocationName = "";

    @SerializedName("EndLocationId")
    @Expose
    private String EndLocationId = "";

    @SerializedName("EndLocationLocationName")
    @Expose
    private String EndLocationLocationName = "";

    @SerializedName("Rate")
    @Expose
    private String Rate = "";

    public String getStartLocationId() {
        return StartLocationId;
    }

    public void setStartLocationId(String startLocationId) {
        StartLocationId = startLocationId;
    }

    public String getStartLocationLocationName() {
        return StartLocationLocationName;
    }

    public void setStartLocationLocationName(String startLocationLocationName) {
        StartLocationLocationName = startLocationLocationName;
    }

    public String getEndLocationId() {
        return EndLocationId;
    }

    public void setEndLocationId(String endLocationId) {
        EndLocationId = endLocationId;
    }

    public String getEndLocationLocationName() {
        return EndLocationLocationName;
    }

    public void setEndLocationLocationName(String endLocationLocationName) {
        EndLocationLocationName = endLocationLocationName;
    }

    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }
}
