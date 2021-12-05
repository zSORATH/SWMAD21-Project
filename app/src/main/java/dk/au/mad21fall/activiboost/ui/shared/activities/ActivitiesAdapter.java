package dk.au.mad21fall.activiboost.ui.shared.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dk.au.mad21fall.activiboost.R;
import dk.au.mad21fall.activiboost.models.Activity;

//With inspiration from PersonAdapter in Lists and Grid demo
public class ActivitiesAdapter extends  RecyclerView.Adapter<ActivitiesAdapter.ActivitiesViewHolder>{

    private ArrayList<Activity> activitiesList;
    private String listenerType;
    private ActivitiesAdapter.IActivitiesItemClickedListener activitiesListener;


    public ActivitiesAdapter(ActivitiesAdapter.IActivitiesItemClickedListener activitiesListener){
        this.activitiesListener = activitiesListener;
    }

    public void updateActivitiesList(ArrayList<Activity> lists, String type){
        activitiesList = lists;
        listenerType = type;
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
        viewHolder.activitytitle.setText(activitiesList.get(position).getActivityName());
        viewHolder.activitytime.setText(activitiesList.get(position).getTime().toString());
        if (listenerType.equals("c")) {
            if (activitiesList.get(position).getUserInActivity() == true) {
                viewHolder.signUpBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.alreadySignedUp();
                        viewHolder.signUpBtn.setText(R.string.activitySignupBtn);
                    }
                });
            }
            if (activitiesList.get(position).getUserInActivity() == false) {
                viewHolder.signUpBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.signUp();
                        viewHolder.signUpBtn.setText(R.string.attendingBtn);
                    }
                });
            }
        }
        if (listenerType.equals("p")){
            viewHolder.signUpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.signUp();
                }
            });
        }
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
    public class ActivitiesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView activitytitle, activitytime;
        Button signUpBtn;

        ActivitiesAdapter.IActivitiesItemClickedListener activitiesListener;

        public ActivitiesViewHolder(@NonNull View itemView, ActivitiesAdapter.IActivitiesItemClickedListener activitiesItemClickedListener) {
            super(itemView);

            activitytitle = itemView.findViewById(R.id.title_activity);
            activitytime = itemView.findViewById(R.id.time_activity);
            signUpBtn = itemView.findViewById(R.id.signUpBtn);

            activitiesListener = activitiesItemClickedListener;
            itemView.setOnClickListener(this);

        }
        private void alreadySignedUp(){
                    activitiesListener.unsubscribeActivity(activitiesList.get(getAdapterPosition()));
        }
        private void signUp(){
            activitiesListener.addToActivity(activitiesList.get(getAdapterPosition()));
        }

        @Override
        public void onClick(View view) {
            activitiesListener.onActivityClicked(getAdapterPosition());
        }
    }

    public static interface IActivitiesItemClickedListener {
        void onActivityClicked(int index);
        void addToActivity(Activity activity);
        void unsubscribeActivity(Activity activity);
    }
}
