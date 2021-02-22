package app.vmp.driver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.vmp.driver.R;
import app.vmp.driver.activity.TripActiity;
import app.vmp.driver.model.PassengerData;
import app.vmp.driver.utils.CircleTransform;
import butterknife.BindView;
import butterknife.ButterKnife;

import static app.vmp.driver.utils.Constant.BASE_URL;

public class TripDropOffAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<PassengerData> listTrip;
    private Context context;
    private FragmentManager fragmentManager;

    public TripDropOffAdapter(Context context, ArrayList<PassengerData> listTrip, FragmentManager fragmentManager) {
        this.context = context;
        this.listTrip = listTrip;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.row_dropoff_new, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).bindData(listTrip.get(position), position);
    }

    @Override
    public int getItemCount() {
        return listTrip.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvPickup)
        AppCompatTextView tvPickup;

        @BindView(R.id.imgUser)
        ImageView imgUser;
        @BindView(R.id.tvName)
        AppCompatTextView tvName;
        @BindView(R.id.tvEndPoint)
        AppCompatTextView tvEndPoint;
        @BindView(R.id.tvDropoffNumber)
        AppCompatTextView tvDropoffNumber;
        @BindView(R.id.tvByName)
        AppCompatTextView tvByName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindData(final PassengerData passengerData, final int position) {

            tvName.setText(passengerData.getPassengerName());

//            Picasso.get().load(BASE_URL+ passengerData.getProfileImage()).transform(new CircleTransform()).error(R.drawable.ic_leader_demo).into(imgUser);
            if (passengerData.getByMainPassengerName()!=null && passengerData.getByMainPassengerName().trim().length()>0){
                tvByName.setVisibility(View.VISIBLE);
                tvByName.setText("(By: "+passengerData.getByMainPassengerName()+")");
            } else {
                tvByName.setVisibility(View.INVISIBLE);
            }

            if (passengerData.getTypeOfPassenger() == null || passengerData.getTypeOfPassenger().trim().length()==0 || passengerData.getTypeOfPassenger().equals("null")) {
                Picasso.get().load(R.drawable.icon_common).transform(new CircleTransform()).into(imgUser);
            } else if (passengerData.getTypeOfPassenger().equals("M")){
                Picasso.get().load(BASE_URL+ passengerData.getProfileImage()).placeholder(R.drawable.icon_male).error(R.drawable.icon_male).transform(new CircleTransform()).into(imgUser);
            } else {
                Picasso.get().load(BASE_URL+ passengerData.getProfileImage()).placeholder(R.drawable.icon_female).error(R.drawable.icon_female).transform(new CircleTransform()).into(imgUser);
            }

            if (passengerData.getStartRouteLocation()!=null){
                tvEndPoint.setText(passengerData.getStartRouteLocation().getLocationName());
            }

            tvPickup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((TripActiity)context).openDropOffDialog(passengerData);
                }
            });
            tvDropoffNumber.setText(context.getResources().getString(R.string.drop_off)+" "+(position+1));

        }
    }
}