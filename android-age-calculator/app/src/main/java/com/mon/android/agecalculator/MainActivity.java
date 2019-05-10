package com.mon.android.agecalculator;

import java.util.Calendar;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import android.app.DatePickerDialog.OnDateSetListener;

public class MainActivity extends AppCompatActivity {

    private TextView startDateDisplay;
    private TextView endDateDisplay;
    private TextView txtResult;
    private TextView txtMonthDays;
    private TextView txtWeekDays;
    private TextView txtTotalDays,txtHour,txtMin,txtSec;
    private Button startPickDate;
    private Button endPickDate;
    private Button btnCalculate;
    private Calendar startDate;
    private Calendar endDate;

    static final int DATE_DIALOG_ID = 0;

    private TextView activeDateDisplay;
    private Calendar activeDate;

    /**
     *
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isNetworkOnline(this);
        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

//        AdView adView = (AdView)this.findViewById(R.id.adView2);
//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .addTestDevice("                                  ")
//                .build();
//        adView.loadAd(adRequest);


        /*  capture our View elements for the start date function   */
        startDateDisplay = (TextView) findViewById(R.id.startDateDisplay);
        startPickDate = (Button) findViewById(R.id.btnStartDate);

        /* get the current date */
        startDate = Calendar.getInstance();

        /* add a click listener to the button   */
        startPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDateDialog(startDateDisplay, startDate);
            }
        });

        /* capture our View elements for the end date function */
        endDateDisplay = (TextView) findViewById(R.id.endDateDisplay);
        endPickDate = (Button) findViewById(R.id.btnEndDate);

        /* get the current date */
        endDate = Calendar.getInstance();

        /* add a click listener to the button   */
        endPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDateDialog(endDateDisplay, endDate);
            }
        });

        txtResult = (TextView) findViewById(R.id.txtResult);
        txtMonthDays = (TextView) findViewById(R.id.txtMonthDay);
        txtWeekDays = (TextView) findViewById(R.id.txtWeekDays);
        txtTotalDays = (TextView) findViewById(R.id.txtTotalDays);
        txtHour = (TextView) findViewById(R.id.txtTotalHour);
        txtMin = (TextView) findViewById(R.id.txtTotalMin);
        txtSec = (TextView) findViewById(R.id.txtTotalSec);
        btnCalculate = (Button) findViewById(R.id.btnCalculateAge);


        /* add a click listener to the button   */
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Calendar startDate1=Calendar.getInstance();

                startDate1.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH)+1, startDate.get(Calendar.DAY_OF_MONTH));
                Calendar endDate1=Calendar.getInstance();
                endDate1.set(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH)+1, endDate.get(Calendar.DAY_OF_MONTH));

                DateCalculator dateCaculator=DateCalculator.calculateAge(startDate1,endDate1);
                String age = "Age: " + dateCaculator.getYear() + " Years " + dateCaculator.getMonth() + " Months " + dateCaculator.getDay()+ " Days";
                int num_weeks = (int) dateCaculator.getTotalDay()/7;
                int num_months = dateCaculator.getYear()*12 + dateCaculator.getMonth();
                System.out.println(dateCaculator.getYear());
                txtResult.setText(age);
                txtResult.setVisibility(View.GONE);
                txtTotalDays.setText(""+dateCaculator.getTotalDay()+" Days");
                txtWeekDays.setText(""+num_weeks+" Weeks " + dateCaculator.getTotalDay()%7 + " Days");
                txtMonthDays.setText(""+num_months+" Months "+dateCaculator.getDay()+" Days ");

                int hour = dateCaculator.getTotalDay() * 24;
                int min = hour * 60;
                int sec = min * 60;

                txtHour.setText(""+hour+" Hours");
                txtMin.setText(""+min+" Minuits");
                txtSec.setText(""+sec+" Seconds");
                //showDateDialog(endDateDisplay, endDate);
                EditText editTextY = findViewById(R.id.editTextYear);
                EditText editTextM = findViewById(R.id.editTextMoth);
                EditText editTextD = findViewById(R.id.editTextDay);
                String y = String.valueOf(dateCaculator.getYear());
                editTextY.setText(String.valueOf(dateCaculator.getYear()));
                editTextM.setText(String.valueOf(dateCaculator.getMonth()));
                editTextD.setText(String.valueOf(dateCaculator.getDay()));


                txtWeekDays.setVisibility(View.VISIBLE);
                txtTotalDays.setVisibility(View.VISIBLE);
                txtHour.setVisibility(View.VISIBLE);
                txtMin.setVisibility(View.VISIBLE);
                txtSec.setVisibility(View.VISIBLE);

            }
        });

        /* display the current date (this method is below)  */
        updateDisplay(startDateDisplay, startDate);
        updateDisplay(endDateDisplay, endDate);


    }

    /**
     *  update the date at ui text view
     * @param dateDisplay text view where the date will be shown
     * @param date selected date
     */
    private void updateDisplay(TextView dateDisplay, Calendar date) {
        dateDisplay.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append("Today Date: ")
                        .append(date.get(Calendar.MONTH) + 1).append("-")
                        .append(date.get(Calendar.DAY_OF_MONTH)).append("-")
                        .append(date.get(Calendar.YEAR)).append(" "));


    }
    private void updateDisplayEnd(TextView dateDisplay, Calendar date) {
        dateDisplay.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append("Birthday Date: ")
                        .append(date.get(Calendar.MONTH) + 1).append("-")
                        .append(date.get(Calendar.DAY_OF_MONTH)).append("-")
                        .append(date.get(Calendar.YEAR)).append(" "));



    }

    /**
     * display the date dialog
     * @param dateDisplay
     * @param date
     */
    public void showDateDialog(TextView dateDisplay, Calendar date) {
        activeDateDisplay = dateDisplay;
        activeDate = date;
        showDialog(DATE_DIALOG_ID);
    }

    private OnDateSetListener dateSetListener = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            activeDate.set(Calendar.YEAR, year);
            activeDate.set(Calendar.MONTH, monthOfYear);
            activeDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDisplayEnd(activeDateDisplay, activeDate);
//            unregisterDateDisplay();
        }
    };

    private void unregisterDateDisplay() {
        activeDateDisplay = null;
        activeDate = null;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, dateSetListener, activeDate.get(Calendar.YEAR), activeDate.get(Calendar.MONTH), activeDate.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        switch (id) {
            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(activeDate.get(Calendar.YEAR), activeDate.get(Calendar.MONTH), activeDate.get(Calendar.DAY_OF_MONTH));
                break;
        }
    }
    public boolean isNetworkOnline(Context con)
    {

        boolean status = false;
        try
        {
            ConnectivityManager cm = (ConnectivityManager) con
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);

            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);

                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                    status = true;
                } else {
                    status = false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return status;
    }

}
