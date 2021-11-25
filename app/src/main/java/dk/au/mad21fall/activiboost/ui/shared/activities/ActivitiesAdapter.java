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

public class ActivitiesAdapter extends  RecyclerView.Adapter<ActivitiesAdapter.ActivitiesViewHolder>{

    private ArrayList<Activity> activityList;
    private ActivitiesAdapter.IAdviceItemClickedListener activitiesListener;


    public ActivitiesAdapter(ActivitiesAdapter.IAdviceItemClickedListener activitiesListener){
        this.activitiesListener = activitiesListener;
    }

    public void updateActivitiesList(ArrayList<Activity> lists){
        activityList = lists;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ActivitiesAdapter.ActivitiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activitypatientitem, parent, false);
        ActivitiesAdapter.ActivitiesViewHolder avh = new ActivitiesAdapter.ActivitiesViewHolder(v, activitiesListener);
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull ActivitiesAdapter.ActivitiesViewHolder viewHolder, int position) {
        viewHolder.title.setText(activityList.get(position).getActivityName());

    }

    @Override
    public int getItemCount() {
        if(activityList == null){
            return 0;
        } else {
            return activityList.size();
        }
    }

    //Lavet ud fra PersonViewHolder i Lists and grids demoen
    public class ActivitiesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;

        ActivitiesAdapter.IAdviceItemClickedListener adviceListener;

        public ActivitiesViewHolder(@NonNull View itemView, ActivitiesAdapter.IAdviceItemClickedListener adviceItemClickedListener) {
            super(itemView);

            title = itemView.findViewById(R.id.title_activity);

            adviceListener = adviceItemClickedListener;
            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
            adviceListener.onAdviceClicked(getAdapterPosition());
        }
    }

    public static interface IAdviceItemClickedListener {
        void onAdviceClicked(int index);
    }
}
