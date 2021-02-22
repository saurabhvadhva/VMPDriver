package app.vmp.driver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StartLocation {

    @SerializedName("Id")
    @Expose
    private String ID = "";

    @SerializedName("LocationName")
    @Expose
    private String LocationName = "";

    @SerializedName("LocationURL")
    @Expose
    private String LocationURL = "";

    @SerializedName("NGOId")
    @Expose
    private String NGOId = "";

    @SerializedName("VillageId")
    @Expose
    private String VillageId = "";

    @SerializedName("Lattitude")
    @Expose
    private String Lattitude = "";

    @SerializedName("Longitude")
    @Expose
    private String Longitude = "";

    @SerializedName("VillageName")
    @Expose
    private String VillageName = "";

    @SerializedName("NGOName")
    @Expose
    private String NGOName = "";

    @SerializedName("DistrictId")
    @Expose
    private String DistrictId = "";

    @SerializedName("DistrictName")
    @Expose
    private String DistrictName = "";

    @SerializedName("SubRouteId")
    @Expose
    private String SubRouteId = "";

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

    public String getNGOId() {
        return NGOId;
    }

    public void setNGOId(String NGOId) {
        this.NGOId = NGOId;
    }

    public String getVillageId() {
        return VillageId;
    }

    public void setVillageId(String villageId) {
        VillageId = villageId;
    }

    public String getLattitude() {
        return Lattitude;
    }

    public void setLattitude(String lattitude) {
        Lattitude = lattitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getVillageName() {
        return VillageName;
    }

    public void setVillageName(String villageName) {
        VillageName = villageName;
    }

    public String getNGOName() {
        return NGOName;
    }

    public void setNGOName(String NGOName) {
        this.NGOName = NGOName;
    }

    public String getDistrictId() {
        return DistrictId;
    }

    public void setDistrictId(String districtId) {
        DistrictId = districtId;
    }

    public String getDistrictName() {
        return DistrictName;
    }

    public String getSubRouteId() {
        return SubRouteId;
    }

    public void setSubRouteId(String subRouteId) {
        SubRouteId = subRouteId;
    }

    public void setDistrictName(String districtName) {
        DistrictName = districtName;
    }
}
