package app.vmp.driver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeneralResponse {

    @SerializedName("ResponseCode")
    @Expose
    private String code = "";

    @SerializedName("ResponseMsg")
    @Expose
    private String msg = "";

    @SerializedName("FareAmount")
    @Expose
    private Double FareAmount;

    @SerializedName("PassengerTravelDetailId")
    @Expose
    private String PassengerTravelDetailId;

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

    public Double getFareAmount() {
        return FareAmount;
    }

    public void setFareAmount(Double fareAmount) {
        FareAmount = fareAmount;
    }

    public String getPassengerTravelDetailId() {
        return PassengerTravelDetailId;
    }

    public void setPassengerTravelDetailId(String passengerTravelDetailId) {
        PassengerTravelDetailId = passengerTravelDetailId;
    }
}
