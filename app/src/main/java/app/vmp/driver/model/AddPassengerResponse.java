package app.vmp.driver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddPassengerResponse {

    @SerializedName("ResponseCode")
    @Expose
    private String code = "";

    @SerializedName("ResponseMsg")
    @Expose
    private String msg = "";

    @SerializedName("DailyCharterId")
    @Expose
    private String DailyCharterId = "";

    @SerializedName("DemandId")
    @Expose
    private String DemandId = "";

    @SerializedName("PassengerId")
    @Expose
    private String PassengerId = "";

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

    public String getDailyCharterId() {
        return DailyCharterId;
    }

    public void setDailyCharterId(String dailyCharterId) {
        DailyCharterId = dailyCharterId;
    }

    public String getDemandId() {
        return DemandId;
    }

    public void setDemandId(String demandId) {
        DemandId = demandId;
    }

    public String getPassengerId() {
        return PassengerId;
    }

    public void setPassengerId(String passengerId) {
        PassengerId = passengerId;
    }
}
