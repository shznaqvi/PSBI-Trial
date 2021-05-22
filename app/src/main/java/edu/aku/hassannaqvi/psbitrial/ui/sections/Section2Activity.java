package edu.aku.hassannaqvi.psbitrial.ui.sections;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.validatorcrawler.aliazaz.Validator;

import edu.aku.hassannaqvi.psbitrial.R;
import edu.aku.hassannaqvi.psbitrial.contracts.TableContracts.FormsTable;
import edu.aku.hassannaqvi.psbitrial.core.MainApp;
import edu.aku.hassannaqvi.psbitrial.database.DatabaseHelper;
import edu.aku.hassannaqvi.psbitrial.databinding.ActivitySection2Binding;
import edu.aku.hassannaqvi.psbitrial.models.Form;

import static edu.aku.hassannaqvi.psbitrial.core.MainApp.form;

public class Section2Activity extends AppCompatActivity {
    ActivitySection2Binding bi;
    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section2);
        bi.setCallback(this);
        MainApp.form = new Form();
        bi.setForm(MainApp.form);
        setSupportActionBar(bi.toolbar);
        setTitle(R.string.section2_mainheading);

        db = MainApp.appInfo.dbHelper;

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

  /*  public void btnEnd(View view) {
        AppUtilsKt.contextEndActivity(this);
    }*/

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

}