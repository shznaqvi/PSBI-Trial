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
            Toast.makeText(this, "Temperature Updated for MR.No.: " +mrno, Toast.LENGTH_LONG).show();

            return true;
        } else {
            Toast.makeText(this, "MR.No Incorrect or not on hold.", Toast.LENGTH_LONG).show();
            bi.mrno.setError("MR.No Incorrect or not on hold.");
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

        String holdmap = "";



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


            holdmap += mark+"MR. Number: " + entry.getKey() +
                    ",\t |\t On-Hold Since: " + cal.get(Calendar.HOUR)+":"+cal.get(Calendar.MINUTE)
                    + ",\t |\t Lapsed: " + String.format("%02d hours, %02d mins",
                                                        TimeUnit.MILLISECONDS.toHours(timeLapsed),
                                                        TimeUnit.MILLISECONDS.toMinutes(timeLapsed)
            -TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeLapsed)))
                    + "\r\n";
        }
        bi.onholdlist.setText(holdmap);
        bi.mrno.setText(null);
        bi.tsf305.setText(null);
        Toast.makeText(this, onhold.toString(), Toast.LENGTH_SHORT).show();
    }

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(this, bi.GrpName);
    }
}
