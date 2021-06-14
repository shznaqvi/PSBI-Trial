package edu.aku.hassannaqvi.psbitrial.ui.sections;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.validatorcrawler.aliazaz.Validator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import edu.aku.hassannaqvi.psbitrial.R;
import edu.aku.hassannaqvi.psbitrial.contracts.TableContracts.FormsTable;
import edu.aku.hassannaqvi.psbitrial.core.MainApp;
import edu.aku.hassannaqvi.psbitrial.core.ZScore;
import edu.aku.hassannaqvi.psbitrial.database.DatabaseHelper;
import edu.aku.hassannaqvi.psbitrial.databinding.ActivitySection2Binding;
import edu.aku.hassannaqvi.psbitrial.models.Form;

import static edu.aku.hassannaqvi.psbitrial.core.MainApp.form;
import static edu.aku.hassannaqvi.psbitrial.utils.DateUtilsKt.dobDiffInDays;
import static edu.aku.hassannaqvi.psbitrial.utils.DateUtilsKt.getCalDate;
import static edu.aku.hassannaqvi.psbitrial.utils.DateUtilsKt.getDateDiff;
import static edu.aku.hassannaqvi.psbitrial.utils.DateUtilsKt.getMonthsBack;

public class Section2Activity extends AppCompatActivity {
    private static final String TAG = "Section2Activity";
    ActivitySection2Binding bi;
    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section2);
        bi.setCallback(this);
        //MainApp.form = new Form();
        bi.setForm(MainApp.form);
        setSupportActionBar(bi.toolbar);
        setTitle(R.string.section2_mainheading);

        db = MainApp.appInfo.dbHelper;
        Calendar cal = Calendar.getInstance();
        cal.getTime();
        cal.add(Calendar.MONTH, -2);

       //Log.d(TAG, "onCreate: "+getMonthsBack("MM/dd/yyyy", 2));
        bi.tsf201.setMinDate(getMonthsBack("dd/MM/yyyy", -2));

    }



    public void btnContinue(View view) {
        if (!formValidation()) return;
        saveDraft();
        if (updateDB()) {
            Toast.makeText(this, "Patient Added.", Toast.LENGTH_SHORT).show();
            finish();
            Intent i = new Intent(this, Section3Activity.class);
                    startActivity(i);
        }
    }

    public void btnEnd(View view) {
        saveDraft();

        Intent i = new Intent(this, EndingActivity.class);
        i.putExtra("complete",false);
        startActivity(i);

    }

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(this, bi.GrpName);    }

    private void saveDraft() {

        // MainApp.form is only initialised at first section
        //MainApp.form = new Form();
        form.setTsf201(bi.tsf201.getText().toString());

        form.setTsf202(bi.tsf202.getText().toString());

        form.setTsf203(bi.tsf203.getText().toString());

        form.setTsf204(bi.tsf204.getText().toString());

        form.setTsf205( bi.tsf20501.isChecked() ? "1"
                : bi.tsf20502.isChecked() ? "2"
                :  "-1");

        form.setTsf206c1(bi.tsf206c1.getText().toString());
        form.setTsf206d1(bi.tsf206d1.getText().toString());
        form.setTsf206c2(bi.tsf206c2.getText().toString());
        form.setTsf206d2(bi.tsf206d2.getText().toString());
        form.setTsf206c3(bi.tsf206c3.getText().toString());
        form.setTsf206d3(bi.tsf206d3.getText().toString());
        form.setTsf206c4(bi.tsf206c4.getText().toString());
        form.setTsf206d4(bi.tsf206d4.getText().toString());

        // Don't forget to popuate Section variable with JSON String...
        // it will be used in UpdateDB()
        form.setS2(form.s2toString());


    }


    private boolean updateDB() {
        // THIS FUNCTION IS NOT SAME AS INSERTNEWRECORD() in FIRST SECTION

        long updCount = db.updatesFormColumn(FormsTable.COLUMN_S2, MainApp.form.getS2());

        // Chech if Form inserted into the database
        if (updCount != -1) {

        return true;
        } else {

            // Error message in case when new record in not inserted (check logcat for error messages)
            Toast.makeText(this, "SORRY! Failed to update DB", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public void CheckZScore(View view) {
        if (Validator.emptyRadioButton(this, bi.tsf205, bi.tsf20501)
                && Validator.emptyTextBox(this, bi.tsf202)
                && Validator.emptyTextBox(this, bi.tsf203)

        ) {
           /* int ageinmonths = Integer.parseInt(bi.cs1502.getText().toString()) + (Integer.parseInt(bi.cs1501.getText().toString())*12);
            Log.d("TAG", "CheckZScore: "+ ageinmonths);
            int ageindays = (int) Math.floor(ageinmonths * DAYS_IN_A_MONTH);
            Log.d("TAG", "CheckZScore: "+ ageindays);*/

            int gender = bi.tsf20501.isChecked() ? 1 : bi.tsf20502.isChecked() ? 2 : 0;

            //DOB(format): dd-MM-yyyy
            int ageindays = Integer.parseInt(bi.tsf202.getText().toString());

            ZScore zs = new ZScore((int) ageindays, gender);
            //double HLAZ = zs.getZScore_HLAZ(bi.cs21.getText().toString());
            double WAZ = zs.getZScore_WAZ(String.valueOf(Integer.parseInt(bi.tsf203.getText().toString())/1000));
            //double WHZ = zs.getZScore_WHZ(bi.cs22.getText().toString(), bi.cs21.getText().toString());
            Log.d("TAG", "CheckZScore: "+WAZ);
            bi.tsf204.setText(String.valueOf(WAZ));
        } else {
            Toast.makeText(this, getString(R.string.zScoreEmpty), Toast.LENGTH_SHORT).show();
        }
    }

    public void ZScoreOnTextChanged(CharSequence s, int start, int before, int count) {
        bi.tsf204.setText(null);

    }

    public void ZScoreOnCheckChanged(RadioGroup group, int checkedId) {
        bi.tsf204.setText(null);
    }

    public void setAgeInDays(CharSequence s, int start, int before, int count) {

        if(!bi.tsf201.getText().toString().equals(""))
        {
            // For testing only
           // form = new Form();
            if(form.getTsf106().equals("")){
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                String todayDate = df.format(Calendar.getInstance().getTime());
                form.setTsf106(todayDate);}

            getCalDate(form.getTsf106());
            int ageindayss = dobDiffInDays(
                    getCalDate(form.getTsf106()),
                    getCalDate( bi.tsf201.getText().toString()));
            bi.tsf202.setText(String.valueOf(ageindayss));
        }

    }
}