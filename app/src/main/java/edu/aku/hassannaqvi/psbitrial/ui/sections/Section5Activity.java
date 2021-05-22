package edu.aku.hassannaqvi.psbitrial.ui.sections;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.validatorcrawler.aliazaz.Validator;

import edu.aku.hassannaqvi.psbitrial.R;
import edu.aku.hassannaqvi.psbitrial.contracts.TableContracts;
import edu.aku.hassannaqvi.psbitrial.core.MainApp;
import edu.aku.hassannaqvi.psbitrial.database.DatabaseHelper;
import edu.aku.hassannaqvi.psbitrial.databinding.ActivitySection5Binding;
import edu.aku.hassannaqvi.psbitrial.models.Form;

import static edu.aku.hassannaqvi.psbitrial.core.MainApp.form;

public class Section5Activity extends AppCompatActivity {
  ActivitySection5Binding bi;
    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section5);
        bi.setCallback(this);
        setSupportActionBar(bi.toolbar);
        MainApp.form = new Form();
        bi.setForm(MainApp.form);
        setTitle(R.string.section5_mainheading);

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

/*
    public void btnEnd(View view) {
        AppUtilsKt.contextEndActivity(this);
    }
*/

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(this, bi.GrpName);    }

    private void saveDraft() {

        // MainApp.form is only initialised at first section
        //MainApp.form = new Form();
        form.setTsf501(bi.tsf501.getText().toString());
        form.setTsf502( bi.tsf50201.isChecked() ? "1"
                : bi.tsf50202.isChecked() ? "2"
                :  "-1");
        form.setTsf503( bi.tsf50301.isChecked() ? "1"
                : bi.tsf50302.isChecked() ? "2"
                : bi.tsf50303.isChecked() ? "3"
                : bi.tsf50304.isChecked() ? "4"
                : bi.tsf50305.isChecked() ? "5"
                : bi.tsf50306.isChecked() ? "6"
                :  "-1");

        // Don't forget to popuate Section variable with JSON String...
        // it will be used in UpdateDB()
        form.setS5(form.s5toString());


    }


    private boolean updateDB() {
        // THIS FUNCTION IS NOT SAME AS INSERTNEWRECORD() in FIRST SECTION

        long updCount = db.updatesFormColumn(TableContracts.FormsTable.COLUMN_S5, MainApp.form.getS5());

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