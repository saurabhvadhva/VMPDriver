package app.vmp.driver.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DirectionResponse {

    @SerializedName("RouteName")
    @Expose
    private String RouteName = "";

    @SerializedName("subRoutes")
    @Expose
    private ArrayList<SubRouteData> subRoutes;

    public String getRouteName() {
        return RouteName;
    }

    public void setRouteName(String routeName) {
        RouteName = routeName;
    }

    public ArrayList<SubRouteData> getSubRoutes() {
        return subRoutes;
    }

    public void setSubRoutes(ArrayList<SubRouteData> subRoutes) {
        this.subRoutes = subRoutes;
    }
}
