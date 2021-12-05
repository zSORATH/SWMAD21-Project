package dk.au.mad21fall.activiboost.ui.caregiver.patients;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dk.au.mad21fall.activiboost.R;
import dk.au.mad21fall.activiboost.models.Patient;

// This is inspired by the Demo in week 3 called "Demo 2: RecyclerView in action".

public class PatientsAdapter extends RecyclerView.Adapter<PatientsAdapter.PatientViewHolder> {

    private ArrayList<Patient> patientList;
    private Context context;

    // Constructor
    public PatientsAdapter(Context context){
        this.context = context;
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
        holder.patient_name.setText(context.getText(R.string.name_) +" "+patientList.get(position).getName()+",");
        holder.patient_age.setText(Integer.toString(patientList.get(position).getAge())+" "+R.string.age_);
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


        //constructor
        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);

            patient_name = itemView.findViewById(R.id.patient_name);
            patient_age = itemView.findViewById(R.id.patient_age);
        }
    }

}
