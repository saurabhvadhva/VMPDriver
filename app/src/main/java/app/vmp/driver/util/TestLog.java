package app.vmp.driver.util;

import android.util.Log;

public class TestLog {

    private String childName = "";

    public TestLog() {
        childName = getClass().getSimpleName() + ".";
    }

    public void logTrue(String method) {
        Log.i("IPPITest", childName + method);
    }

    public void logErr(String method, String errString) {
        Log.e("IPPITest", childName + method + "   出错信息：" + errString);
    }
}
