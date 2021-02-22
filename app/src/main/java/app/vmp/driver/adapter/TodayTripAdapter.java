package app.vmp.driver.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import app.vmp.driver.R;
import app.vmp.driver.activity.TodayTripActivity;
import app.vmp.driver.activity.TripActiity;
import app.vmp.driver.model.RouteData;
import app.vmp.driver.model.StartLocation;
import app.vmp.driver.utils.CircleTransform;
import app.vmp.driver.utils.Util;

import static app.vmp.driver.utils.Constant.BASE_URL;
import static app.vmp.driver.utils.Constant.USER_PHOTO;

public class TodayTripAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<RouteData> listTrip;
    LayoutInflater mLayoutInflater;
    String date = "";

    public TodayTripAdapter(Context context,ArrayList<RouteData> list){
        mContext = context;
        listTrip = list;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Date currentTime = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd MMM");
        date = df.format(currentTime);
    }

    @Override
    public int getCount() {
        return listTrip.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View layout = mLayoutInflater.inflate(R.layout.row_trip_new, null);
        container.addView(layout);

        AppCompatTextView tvStartTrip = layout.findViewById(R.id.tvStartTrip);
        AppCompatTextView tvCallSHG = layout.findViewById(R.id.tvCallSHG);
        AppCompatTextView tvTripNo = layout.findViewById(R.id.tvTripNo);
        AppCompatTextView tvRegiNumber = layout.findViewById(R.id.tvRegiNumber);
        AppCompatTextView tvDate = layout.findViewById(R.id.tvDate);
        AppCompatTextView tvTime = layout.findViewById(R.id.tvTime);
        AppCompatTextView tvStartPoint = layout.findViewById(R.id.tvStartPoint);
        AppCompatTextView tvEndPoint = layout.findViewById(R.id.tvEndPoint);
        AppCompatTextView tvTripName = layout.findViewById(R.id.tvTripName);
        AppCompatTextView tvPassengerCount = layout.findViewById(R.id.tvPassengerCount);

        ImageView imgStartLoc = layout.findViewById(R.id.imgStartLoc);
        ImageView imgEndLoc = layout.findViewById(R.id.imgEndLoc);

        tvTripNo.setText("Trip "+(position+1));
        tvDate.setText(date);

        final RouteData routeData = listTrip.get(position);

        if (routeData.getStartLocation()!=null){
           StartLocation startLocation = routeData.getStartLocation();
            tvStartPoint.setText(startLocation.getLocationName());
            Picasso.get().load(startLocation.getLocationURL()).into(imgStartLoc);
        }

        if (routeData.getEndLocation()!=null){
            StartLocation startLocation = routeData.getEndLocation();
            tvEndPoint.setText(startLocation.getLocationName());
            Picasso.get().load(startLocation.getLocationURL()).into(imgEndLoc);
        }

        tvPassengerCount.setText(routeData.getPassengerCount());
        tvTime.setText(routeData.getTimeSlot());
        tvRegiNumber.setText(routeData.getVehicleRegistration());
        tvTripName.setText(routeData.getRouteName());


        tvStartTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TodayTripActivity)mContext).startTrip(routeData);
            }
        });

        tvCallSHG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TodayTripActivity)mContext).callShg(routeData);
            }
        });

        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

}
