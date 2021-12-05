package dk.au.mad21fall.activiboost.ui.shared.home;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dk.au.mad21fall.activiboost.R;
import dk.au.mad21fall.activiboost.models.Activity;

//With inspiration from PersonAdapter in Lists and Grid demo
public class HomeActivityAdapter extends  RecyclerView.Adapter<HomeActivityAdapter.HomeActivityAdapterViewHolder> {

    private ArrayList<Activity> activitiesList;


    public HomeActivityAdapter() {

    }

    public void updateActivitiesList(ArrayList<Activity> lists) {
        activitiesList = lists;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public HomeActivityAdapter.HomeActivityAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.myactivitypatientitem, parent, false);
        HomeActivityAdapter.HomeActivityAdapterViewHolder avh = new HomeActivityAdapter.HomeActivityAdapterViewHolder(v);
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeActivityAdapterViewHolder holder, int position) {
        holder.activityTitle.setText(activitiesList.get(position).getActivityName());
        if(activitiesList.get(position).getTime() != null) {
            holder.activityTime.setText(activitiesList.get(position).getTime().toString());
        }
    }


    @Override
    public int getItemCount() {
        if (activitiesList == null) {
            return 0;
        } else {
            return activitiesList.size();
        }
    }

    //Lavet ud fra PersonViewHolder i Lists and grids demoen
    public class HomeActivityAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView activityTitle, activityTime;

        public HomeActivityAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            activityTitle = itemView.findViewById(R.id.title_myactivity);
            activityTime = itemView.findViewById(R.id.time_myactivity);




        }

    }
}

