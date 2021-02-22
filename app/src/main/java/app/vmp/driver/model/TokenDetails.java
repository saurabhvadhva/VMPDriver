package app.vmp.driver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenDetails {

    @SerializedName("unique_name")
    @Expose
    private String unique_name = "";

    @SerializedName("role")
    @Expose
    private String role = "";

    @SerializedName("DEVICE_ID")
    @Expose
    private String DEVICE_ID = "";

    @SerializedName("ROLE_ID")
    @Expose
    private String ROLE_ID = "";

    @SerializedName("NGO_ID")
    @Expose
    private String NGO_ID = "";

    @SerializedName("EMP_ID")
    @Expose
    private String EMP_ID = "";

    @SerializedName("USER_ID")
    @Expose
    private String USER_ID = "";

    @SerializedName("NAME")
    @Expose
    private String NAME = "";

    @SerializedName("STATE_ID")
    @Expose
    private String STATE_ID = "";

    @SerializedName("ADDRESS")
    @Expose
    private String ADDRESS = "";

    @SerializedName("PROFILE_PHOTO")
    @Expose
    private String PROFILE_PHOTO = "";

    public String getUnique_name() {
        return unique_name;
    }

    public void setUnique_name(String unique_name) {
        this.unique_name = unique_name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDEVICE_ID() {
        return DEVICE_ID;
    }

    public void setDEVICE_ID(String DEVICE_ID) {
        this.DEVICE_ID = DEVICE_ID;
    }

    public String getROLE_ID() {
        return ROLE_ID;
    }

    public void setROLE_ID(String ROLE_ID) {
        this.ROLE_ID = ROLE_ID;
    }

    public String getNGO_ID() {
        return NGO_ID;
    }

    public void setNGO_ID(String NGO_ID) {
        this.NGO_ID = NGO_ID;
    }

    public String getEMP_ID() {
        return EMP_ID;
    }

    public void setEMP_ID(String EMP_ID) {
        this.EMP_ID = EMP_ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getSTATE_ID() {
        return STATE_ID;
    }

    public void setSTATE_ID(String STATE_ID) {
        this.STATE_ID = STATE_ID;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getPROFILE_PHOTO() {
        return PROFILE_PHOTO;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public void setPROFILE_PHOTO(String PROFILE_PHOTO) {
        this.PROFILE_PHOTO = PROFILE_PHOTO;
    }
}
