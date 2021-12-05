package dk.au.mad21fall.activiboost.ui.shared.calendar;

import static java.time.temporal.ChronoUnit.DAYS;

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
    private List<Diary> diaries;
    private LiveData<List<Diary>> lDiaries;
    private Activity curActivity;

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
            public void onDateClick(View view, DateData date) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, date.getYear());
                cal.set(Calendar.MONTH, date.getMonth());
                cal.set(Calendar.DAY_OF_MONTH, date.getDay());
                Date _date = cal.getTime();

                if (hasActivity(_date)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setIcon(R.drawable.ic_activities)
                            .setTitle(_date.toString())
                            .setMessage("Activity: " + curActivity.getActivityName()
                                    + "\nDescription: " + curActivity.getDescription());

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });

        return root;
    }

    private boolean hasActivity(Date date) {
        String _date, _aDate;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String year = "" + cal.get(Calendar.YEAR);
        String month = "" + cal.get(Calendar.MONTH);
        String day = "" + cal.get(Calendar.DAY_OF_MONTH);
        if (month.equals("0")) {
            month = "12";
            year = "" + (cal.get(Calendar.YEAR) - 1);
        }
        _date = year + month + day;

        for (Activity a : activities) {
            cal.setTime(a.getTime());
            _aDate = "" + cal.get(Calendar.YEAR) + (cal.get(Calendar.MONTH)+1) + cal.get(Calendar.DAY_OF_MONTH);
            if (_date.equals(_aDate)) {
                Log.d(TAG, "Activity found: " + a.getActivityName());
                curActivity = a;
                return true;
            }
        }
        return false;
    }

    /*
    public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
     */

    private void setCalendarView() {
        lActivities = cvm.getActivities(uid);
        activities = lActivities.getValue();

        Calendar cal = Calendar.getInstance();

        Date today = cal.getTime();
        cal.set(Calendar.YEAR, 2050); // arbitrarily late date
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

        cal.setTime(closest);
        LocalDate closestDate = LocalDate.of(cal.get(Calendar.YEAR), (cal.get(Calendar.MONTH) + 1), cal.get(Calendar.DAY_OF_MONTH));
        LocalDate todayDate = LocalDate.now();

        long dateDiff = DAYS.between(todayDate, closestDate);
        Log.d(TAG, "Day difference of today and closest activity: " + dateDiff);

        String text;
        String timeOfDay;
        String minutes;

        if (dateDiff == 0) {
            minutes = "" + cal.get(Calendar.MINUTE);
            if (minutes.length() < 2) {
                minutes += "0";
            }
            timeOfDay = "" + cal.get(Calendar.HOUR_OF_DAY) + "." + minutes;
            text = "You have an activity happening today!\n\n\"" + closestActivity.getActivityName() + "\" at " + timeOfDay + " o'clock.";
        } else if (dateDiff > 0 && dateDiff < 365) {
            minutes = "" + cal.get(Calendar.MINUTE);
            if (minutes.length() < 2) {
                minutes += "0";
            }
            timeOfDay = "" + cal.get(Calendar.HOUR_OF_DAY) + "." + minutes;
            text = "You are attending an activity in " + dateDiff + " days:\n\n\"" + closestActivity.getActivityName() + "\" at " + timeOfDay + " o'clock.";
        } else {
            text = "You have no upcoming activities you are attending.";
        }

        txtUpcoming.setText(text);

        // TODO: Figure out if we can add diaries to the calendar with Pernille
        /*
        lDiaries = cvm.getDiaries();
        diaries = lDiaries.getValue();

        for (Diary d : diaries) {
            try {
                cal.setTime(stringToDate(d.getDate()));
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            calendarView.markDate(
                    new DateData(cal.get(Calendar.YEAR), (cal.get(Calendar.MONTH)+1), cal.get(Calendar.DATE))
                            .setMarkStyle(new MarkStyle(MarkStyle.DOT, Color.BLUE))
            );
        }
         */
    }

    public static Date stringToDate(String string) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
        Date date = formatter.parse(string);
        return date;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

