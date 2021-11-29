package dk.au.mad21fall.activiboost.ui.caregiver.patients;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dk.au.mad21fall.activiboost.R;
import dk.au.mad21fall.activiboost.models.Patient;

// This is inspired by the Demo in week 3 called "Demo 2: RecyclerView in action".

public class PatientsAdapter extends RecyclerView.Adapter<PatientsAdapter.PatientViewHolder> {

    private ArrayList<Patient> patientList;

    // Constructor
    public PatientsAdapter(){
    }

    public void updatePatientList(ArrayList<Patient> lists){
        patientList = lists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.patientitem, parent, false);
        PatientViewHolder vh = new PatientViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        // Using glide inspired from the "Demo: Rick and Morty Gallery with Volley and Glide" from L6 in this course
        holder.patient_name.setText(patientList.get(position).getName());
        holder.patient_age.setText(patientList.get(position).getAge());
        holder.patient_id.setText("id: "+patientList.get(position).getId());
    }

    @Override
    public int getItemCount() {
        // we have to check if the list is null to not crash the app
        if(patientList != null){
            return patientList.size();}
        else{
            return 0;
        }
    }

    public class PatientViewHolder extends RecyclerView.ViewHolder{

        TextView patient_name;
        TextView patient_age;
        TextView patient_id;

        //constructor
        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);

            patient_name = itemView.findViewById(R.id.patient_name);
            patient_age = itemView.findViewById(R.id.patient_age);
            patient_id = itemView.findViewById(R.id.patient_id);
        }
    }

}
