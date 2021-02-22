package app.vmp.driver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VillageData {

    @SerializedName("Id")
    @Expose
    private String Id = "";

    @SerializedName("VillageName")
    @Expose
    private String VillageName = "";

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getVillageName() {
        return VillageName;
    }

    public void setVillageName(String villageName) {
        VillageName = villageName;
    }
}
