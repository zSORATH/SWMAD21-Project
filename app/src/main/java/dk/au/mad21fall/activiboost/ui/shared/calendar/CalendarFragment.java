package dk.au.mad21fall.activiboost.ui.shared.calendar;

import static java.time.temporal.ChronoUnit.DAYS;

import static dk.au.mad21fall.activiboost.Constants.PATIENT;

import android.app.AlertDialog;
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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dk.au.mad21fall.activiboost.R;
import dk.au.mad21fall.activiboost.models.Activity;
import dk.au.mad21fall.activiboost.models.Diary;
import sun.bob.mcalendarview.MCalendarView;

import dk.au.mad21fall.activiboost.databinding.FragmentCalendarBinding;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.vo.DateData;

// Using https://github.com/SpongeBobSun/mCalendarView

public class CalendarFragment extends Fragment {

    private static final String TAG = "CALENDAR FRAGMENT";

    private String uid;
    private String userType;
    private ArrayList<Activity> activities = new ArrayList<>();
    private MutableLiveData<ArrayList<Activity>> lActivities = new MutableLiveData<>();
    private ArrayList<Activity> curActivities = new ArrayList<>();

    private CalendarViewModel cvm;
    private FragmentCalendarBinding binding;
    private MCalendarView calendarView;
    private TextView txtUpcoming;

    // TODO: Localize text
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        cvm = new ViewModelProvider(this).get(CalendarViewModel.class);

        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        calendarView = binding.calendar;
        txtUpcoming = binding.txtUpcoming;

        uid = (String) getActivity().getIntent().getSerializableExtra("user");
        userType = cvm.getUserType(uid);

        lActivities = cvm.getActivities(uid);

        setCalendarView();

        calendarView.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData dateData) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, dateData.getYear());
                cal.set(Calendar.MONTH, dateData.getMonth()-1);
                cal.set(Calendar.DAY_OF_MONTH, dateData.getDay());
                Date date = cal.getTime();

                Log.d(TAG, "Date clicked: " + date + " with activity: " + cvm.dateHasActivity(date));

                boolean dateHasActivity = cvm.dateHasActivity(date);

                if (dateHasActivity) {
                    curActivities = cvm.getActivitiesOnDate(date);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setIcon(R.drawable.ic_activities)
                            .setTitle(date.toString())
                            .setMessage(R.string.activity + curActivities.get(0).getActivityName()
                                    + "\nDescription: " + curActivities.get(0).getDescription());

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });

        return root;
    }

    private void setCalendarView() {
        lActivities = cvm.getActivities(uid);
        activities = lActivities.getValue();

        Calendar cal = Calendar.getInstance();

        Date today = cal.getTime();
        cal.set(Calendar.YEAR, 2121); // arbitrarily late date
        Date closest = cal.getTime();
        Activity closestActivity = new Activity();

        for (Activity a : activities) {
            Log.d(TAG, "Activity: " + a.getActivityName());
            cal.setTime(a.getTime());
            Log.d(TAG, "Date: " + cal.get(Calendar.YEAR) + (cal.get(Calendar.MONTH)+1) + cal.get(Calendar.DAY_OF_MONTH));
            calendarView.markDate(
                    new DateData(cal.get(Calendar.YEAR), (cal.get(Calendar.MONTH)+1), cal.get(Calendar.DAY_OF_MONTH))
                    .setMarkStyle(new MarkStyle(MarkStyle.DOT, Color.RED))
            );

            if ((a.getTime().compareTo(closest) < 0) && (a.getTime().compareTo(today) >= 0)) {
                closest = a.getTime();
                closestActivity = a;
            }
        }

        long dateDiff = cvm.getDateDiff(today, closest);
        Log.d(TAG, "Day difference of today and closest activity: " + dateDiff);

        String text = "";
        String timeOfDay;
        String minutes;

        cal.setTime(closest);

        if (userType == PATIENT) {
            if (dateDiff == 0) {
                minutes = "" + cal.get(Calendar.MINUTE);
                if (minutes.length() < 2) {
                    minutes += "0";
                }
                timeOfDay = "" + cal.get(Calendar.HOUR_OF_DAY) + "." + minutes;
                text += R.string.acitvity_today + "\n"
                        + closestActivity.getActivityName() + "\n"
                        + getText(R.string.time) + " " + timeOfDay + "\n"
                        + getText(R.string.at_location) + ": " + closestActivity.getPlace();
            } else if (dateDiff > 0 && dateDiff < 1000) {
                minutes = "" + cal.get(Calendar.MINUTE);
                if (minutes.length() < 2) {
                    minutes += "0";
                }
                timeOfDay = "" + cal.get(Calendar.HOUR_OF_DAY) + "." + minutes;
                text += getText(R.string.acitvity_upcoming) + " " + dateDiff + " " + getText(R.string.days) + ":\n\n"
                        + closestActivity.getActivityName() + "\n"
                        + getText(R.string.time) + " " + timeOfDay + "\n"
                        + getText(R.string.at_location) + ": " + closestActivity.getPlace();
            } else {
                text += "You have no upcoming activities you are attending.";
            }
        } else {

        }

        txtUpcoming.setText(text);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

