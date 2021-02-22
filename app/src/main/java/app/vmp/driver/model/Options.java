package app.vmp.driver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Options {

    @SerializedName("Id")
    @Expose
    private String id = "";

    @SerializedName("OptionTypeValue")
    @Expose
    private String value = "";

    @SerializedName("OptionTypeDisplayText")
    @Expose
    private String text = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
