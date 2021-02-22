package app.vmp.driver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("ResponseCode")
    @Expose
    private String code = "";

    @SerializedName("ResponseMessage")
    @Expose
    private String msg = "";

    @SerializedName("UserID")
    @Expose
    private String UserID = "";

    @SerializedName("JwtToken")
    @Expose
    private String JwtToken = "";


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getJwtToken() {
        return JwtToken;
    }

    public void setJwtToken(String jwtToken) {
        JwtToken = jwtToken;
    }
}
