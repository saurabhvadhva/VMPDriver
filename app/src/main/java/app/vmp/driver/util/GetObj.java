package app.vmp.driver.util;

import android.util.Log;

import com.pax.dal.IDAL;

import app.vmp.driver.activity.MainActivity;


public class GetObj {

    private static IDAL dal;

    // 获取IDal dal对象
    public static IDAL getDal() {
        dal = MainActivity.idal;
        if (dal == null) {
            Log.e("NeptuneLiteDemo", "dal is null");
        }
        return dal;
    }

}
