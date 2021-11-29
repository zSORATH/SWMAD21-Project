package dk.au.mad21fall.activiboost.ui.shared.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dk.au.mad21fall.activiboost.R;
import dk.au.mad21fall.activiboost.models.Activity;

public class MyActivitiesAdapter extends  RecyclerView.Adapter<MyActivitiesAdapter.MyActivitiesViewHolder>{

    private ArrayList<Activity> activitiesList;
    private MyActivitiesAdapter.IMyActivitiesItemClickedListener activitiesListener;


    public MyActivitiesAdapter(MyActivitiesAdapter.IMyActivitiesItemClickedListener activitiesListener){
        this.activitiesListener = activitiesListener;
    }

    public void updateActivitiesList(ArrayList<Activity> lists){
        activitiesList = lists;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public MyActivitiesAdapter.MyActivitiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.myactivitypatientitem, parent, false);
        MyActivitiesAdapter.MyActivitiesViewHolder avh = new MyActivitiesAdapter.MyActivitiesViewHolder(v, activitiesListener);
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyActivitiesAdapter.MyActivitiesViewHolder viewHolder, int position) {
        viewHolder.activitytitle.setText(activitiesList.get(position).getActivityName());

    }

    @Override
    public int getItemCount() {
        if(activitiesList == null){
            return 0;
        } else {
            return activitiesList.size();
        }
    }

    //Lavet ud fra PersonViewHolder i Lists and grids demoen
    public class MyActivitiesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView activitytitle;

        MyActivitiesAdapter.IMyActivitiesItemClickedListener activitiesListener;

        public MyActivitiesViewHolder(@NonNull View itemView, MyActivitiesAdapter.IMyActivitiesItemClickedListener activitiesItemClickedListener) {
            super(itemView);

            activitytitle = itemView.findViewById(R.id.title_activity);

            activitiesListener = activitiesItemClickedListener;
            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
            activitiesListener.onActivityClicked(getAdapterPosition());
        }
    }

    public static interface IMyActivitiesItemClickedListener {
        void onActivityClicked(int index);
    }
}