package app.vmp.driver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DistrictData {

    @SerializedName("Id")
    @Expose
    private String ID = "";

    @SerializedName("districtName")
    @Expose
    private String districtName = "";

    @SerializedName("StateID")
    @Expose
    private String StateID = "";

    @SerializedName("StateName")
    @Expose
    private String StateName = "";

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getStateID() {
        return StateID;
    }

    public void setStateID(String stateID) {
        StateID = stateID;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }
}
