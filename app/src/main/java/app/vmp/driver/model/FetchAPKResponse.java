package app.vmp.driver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FetchAPKResponse {

    @SerializedName("Status")
    @Expose
    private String Status = "";

    @SerializedName("Message")
    @Expose
    private String Message = "";

    @SerializedName("Version")
    @Expose
    private String Version = "";

    @SerializedName("Location")
    @Expose
    private String Location = "";

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }
}
