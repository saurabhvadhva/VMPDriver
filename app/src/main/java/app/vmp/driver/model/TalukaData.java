package app.vmp.driver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TalukaData {

    @SerializedName("TalukaID")
    @Expose
    private String TalukaID = "";

    @SerializedName("TalukaName")
    @Expose
    private String TalukaName = "";

    public String getTalukaID() {
        return TalukaID;
    }

    public void setTalukaID(String talukaID) {
        TalukaID = talukaID;
    }

    public String getTalukaName() {
        return TalukaName;
    }

    public void setTalukaName(String talukaName) {
        TalukaName = talukaName;
    }
}
