package edu.aku.hassannaqvi.psbitrial.ui.sections;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import edu.aku.hassannaqvi.psbitrial.MainActivity;
import edu.aku.hassannaqvi.psbitrial.R;
import edu.aku.hassannaqvi.psbitrial.core.MainApp;
import edu.aku.hassannaqvi.psbitrial.database.DatabaseHelper;
import edu.aku.hassannaqvi.psbitrial.databinding.OnholdFormBinding;
import edu.aku.hassannaqvi.psbitrial.models.Form;

public class OnholdForm extends AppCompatActivity {

    OnholdFormBinding bi;
    private DatabaseHelper db;
    SharedPreferences sp;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.onhold_form);
        bi.setCallback(this);
        MainApp.form = new Form();
        bi.setForm(MainApp.form);
        setSupportActionBar(bi.toolbar);
        setTitle("ONHOLD FORMS");
        db = MainApp.appInfo.dbHelper;
        sp = getSharedPreferences("onhold", MODE_PRIVATE);
        edit = sp.edit();

        updateOnHoldList();

    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        // saveDraft();
        if (updateDB()) {
            Toast.makeText(this, "Patient Added.", Toast.LENGTH_SHORT).show();
            finish();
            Intent i = new Intent(this, OnholdForm.class);
            startActivity(i);
        }
    }

    private boolean updateDB() {

        String mrno = bi.mrno.getText().toString();
        String temp = bi.tsf305.getText().toString();

        int updCount = db.updateTemp(mrno, temp);
        Log.d("TAG", "updateDB: "+updCount);
        if (updCount > 0) {
            updateOnHoldList();
            Toast.makeText(this, "Temperature Updated for Assessment.No.: " +mrno, Toast.LENGTH_LONG).show();

            return true;
        } else {
            Toast.makeText(this, "Assessment.No Incorrect or not on hold.", Toast.LENGTH_LONG).show();
            bi.mrno.setError("Assessment.No Incorrect or not on hold.");
            return false;
        }
    }


    private void updateOnHoldList() {

        edit.remove(bi.mrno.getText().toString());
        edit.apply();

        SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
        String todayDate = df.format(Calendar.getInstance().getTime());
       Calendar cal = Calendar.getInstance();
       Calendar cur = Calendar.getInstance();
        long timeLapsed = 0;

        String holdmap = "PATIENTS DUE (past 30 mins):\r\n";
        holdmap += "===========================\r\n\r\n";
        String pendingmap = "PATIENTS ON HOLD (30 mins waiting):\r\n";
        pendingmap += "==================================\r\n\r\n";



        Map<String, ?> onhold = sp.getAll();
        for (Map.Entry<String, ?> entry : onhold.entrySet()) {

            try {
                cal.setTime(df.parse(String.valueOf(entry.getValue())));
                timeLapsed = System.currentTimeMillis() - cal.getTimeInMillis();
            } catch (ParseException e) {
                e.printStackTrace();
            }


            String mark = "\t         ";
            if(TimeUnit.MILLISECONDS.toMinutes(timeLapsed) > 120){
                mark = "\t***** --> ";
            } else if(TimeUnit.MILLISECONDS.toMinutes(timeLapsed) > 90){
                mark = "\t***   --> ";
            } else if(TimeUnit.MILLISECONDS.toMinutes(timeLapsed) > 60){
                mark = "\t**    --> ";
            } else if(TimeUnit.MILLISECONDS.toMinutes(timeLapsed) > 30){
                mark = "\t*     --> ";
            }

            if(TimeUnit.MILLISECONDS.toMinutes(timeLapsed) > 30) {

                holdmap += mark + "Assessment No.: " + entry.getKey() +
                        ",\t |\t On-Hold Since: " + cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE)
                        + ",\t |\t Lapsed: " + String.format("%02d mins",
                        TimeUnit.MILLISECONDS.toMinutes(timeLapsed))
                        + "\r\n";

            } else {
              //  holdmap = "More patients will show after their 30 mins have passed.";
                pendingmap += mark + "Assessment No.: " + entry.getKey() +
                        ",\t |\t On-Hold Since: " + cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE)
                        + ",\t |\t Remining: " + String.format("%02d mins",
                        TimeUnit.MILLISECONDS.toMinutes(timeLapsed)-30)
                        + "\r\n";
            }
        }


        bi.onholdlist.setText(holdmap);
        bi.pendinglist.setText(pendingmap);
        bi.mrno.setText(null);
        bi.tsf305.setText(null);
        Toast.makeText(this, onhold.toString(), Toast.LENGTH_SHORT).show();
    }

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(this, bi.GrpName);
    }

    public void btnEnd(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        // Toast.makeText(getApplicationContext(), "Back Press Not Allowed", Toast.LENGTH_LONG).show();
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
}
