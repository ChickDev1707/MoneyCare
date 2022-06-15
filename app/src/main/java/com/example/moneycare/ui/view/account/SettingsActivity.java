package com.example.moneycare.ui.view.account;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.example.moneycare.MoneyFormatDialogFragment;
import com.example.moneycare.R;
import com.example.moneycare.databinding.ActivitySettingsBinding;
import com.example.moneycare.utils.DateUtil;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;
import java.util.Date;

public class SettingsActivity extends AppCompatActivity {
    ActivitySettingsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.settings, new SettingsFragment())
            .commit();

        initToolbar();
    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.basic_app_bar);
        toolbar.setTitle(R.string.title_settings);
        this.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                onBackPressed();
            }
        });
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        final int ALARM_REQ_CODE = 100;
        AlarmManager alarmManager;
        SharedPreferences sharedPreferences;
        Preference notificationTimePref;
        ListPreference dateFormatPref;
        Preference moneyFormatPref;
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
            initPref();
            createNotificationChanel();
        }
        private void initPref(){
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            initNotificationTimePref();
            initDateFormatPref();
            initMoneyFormatPref();
        }
        // notification
        private void createNotificationChanel(){
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("TransactionReminder", "TransactionReminderChanel", NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("Chanel for reminding add transaction");

                NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        // notification time
        }
        private void initNotificationTimePref(){
            String value = sharedPreferences.getString("notification_time", "");
            notificationTimePref = findPreference("notification_time");
            notificationTimePref.setSummary(value);
            initNotificationTimePrefClickEvent();
        }
        private void initNotificationTimePrefClickEvent(){
            notificationTimePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    MaterialTimePicker picker = createTimePicker();
                    picker.show(getParentFragmentManager(), "notification_time");
                    return true;
                }
            });
        }
        private MaterialTimePicker createTimePicker(){
            Date now = new Date();
            MaterialTimePicker picker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(now.getHours())
                    .setMinute(now.getMinutes())
                    .setTitleText("Chọn thời gian thông báo")
                    .build();
            initTimePickerClickEvent(picker);
            return picker;
        }
        private void initTimePickerClickEvent(MaterialTimePicker picker){
            picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int hour = picker.getHour();
                    int minute = picker.getMinute();
                    saveNotificationTimePref(hour, minute);
                    setNotificationTimeAlarmFromPref();
                    Toast toast =  Toast.makeText(getContext(), "Đặt giờ nhắc thành công", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }
        private void saveNotificationTimePref(int hour, int minute){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String timeString = DateUtil.getFullTimeString(hour, minute);
            editor.putString("notification_time", timeString);
            editor.apply();
            notificationTimePref.setSummary(timeString);
        }
        private void setNotificationTimeAlarmFromPref(){
            PendingIntent pendingIntent = createPendingIntent();
            Calendar calendar = createCalendar();
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        private PendingIntent createPendingIntent(){
            Context context = getContext();
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ALARM_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            return pendingIntent;
        }
        private Calendar createCalendar(){
            String timeString = sharedPreferences.getString("notification_time", "00:00");
            String[] timeParts = timeString.split(":");
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar;
        }

        // date format
        private void initDateFormatPref() {
            dateFormatPref = findPreference("date_formats");
            dateFormatPref.setSummary(dateFormatPref.getValue());
            initDateFormatPrefClickEvent();
        }
        private void initDateFormatPrefClickEvent() {
            dateFormatPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    dateFormatPref.setSummary(newValue.toString());
                    return true;
                }
            });
        }

        // money format
        private void initMoneyFormatPref(){
            moneyFormatPref = findPreference("money_format");
            moneyFormatPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    MoneyFormatDialogFragment fragment = new MoneyFormatDialogFragment(getActivity());
                    fragment.showDialog();
                    return true;
                }
            });

        }
    }
}