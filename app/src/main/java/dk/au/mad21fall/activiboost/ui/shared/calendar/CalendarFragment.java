package dk.au.mad21fall.activiboost.ui.shared.calendar;

import static dk.au.mad21fall.activiboost.Constants.PATIENT;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import dk.au.mad21fall.activiboost.R;
import dk.au.mad21fall.activiboost.models.Activity;
import sun.bob.mcalendarview.MCalendarView;

import dk.au.mad21fall.activiboost.databinding.FragmentCalendarBinding;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.vo.DateData;

// Using https://github.com/SpongeBobSun/mCalendarView

public class CalendarFragment extends Fragment {

    private static final String TAG = "CALENDAR FRAGMENT";

    private String uid;
    private String userType;
    private ArrayList<Activity> activities = new ArrayList<>();
    private MutableLiveData<ArrayList<Activity>> lActivities = new MutableLiveData<>();;

    private CalendarViewModel cvm;
    private FragmentCalendarBinding binding;
    private MCalendarView calendarView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        cvm = new ViewModelProvider(this).get(CalendarViewModel.class);

        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        calendarView = binding.calendar;

        uid = (String) getActivity().getIntent().getSerializableExtra("user");
        userType = cvm.getUserType(uid);

        lActivities = cvm.getActivities(uid);

        setCalendarView();

        return root;
    }

    private void setCalendarView() {
        lActivities = cvm.getActivities(uid);
        activities = lActivities.getValue();

        Calendar cal = Calendar.getInstance();
        for (Activity a : activities) {
            Log.d(TAG, "Activity: " + a.getActivityName());
            cal.setTime(a.getTime());
            Log.d(TAG, "Date: " + cal.get(Calendar.YEAR) + (cal.get(Calendar.MONTH)+1) + cal.get(Calendar.DATE));
            calendarView.markDate(
                    new DateData(cal.get(Calendar.YEAR), (cal.get(Calendar.MONTH)+1), cal.get(Calendar.DATE))
                    .setMarkStyle(new MarkStyle(MarkStyle.DOT, Color.RED))
            );
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

