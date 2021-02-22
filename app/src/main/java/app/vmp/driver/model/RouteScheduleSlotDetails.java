package app.vmp.driver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RouteScheduleSlotDetails {

    @SerializedName("Id")
    @Expose
    private String ID = "";

    @SerializedName("SHGContactNumber")
    @Expose
    private String SHGContactNumber = "";

    @SerializedName("SHGId")
    @Expose
    private String SHGId = "";


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getSHGContactNumber() {
        return SHGContactNumber;
    }

    public void setSHGContactNumber(String SHGContactNumber) {
        this.SHGContactNumber = SHGContactNumber;
    }

    public String getSHGId() {
        return SHGId;
    }

    public void setSHGId(String SHGId) {
        this.SHGId = SHGId;
    }
}
