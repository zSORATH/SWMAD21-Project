package dk.au.mad21fall.activiboost.ui.caregiver.patients;

import android.app.Application;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import dk.au.mad21fall.activiboost.models.Patient;
import dk.au.mad21fall.activiboost.repository.Repository;

public class PatientsViewModel extends ViewModel {

    private Repository repository;

    private MutableLiveData<ArrayList<Patient>> patients;

    public PatientsViewModel() {
    }

    public void CreateRepository(Application app){
        repository = Repository.getInstance(app);  //get Repository singleton
        patients = repository.getPatients();
    }

    public MutableLiveData<ArrayList<Patient>> GetPatientLiveData(){
        return patients;
    }
}