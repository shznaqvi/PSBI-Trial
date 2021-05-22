package edu.aku.hassannaqvi.psbitrial.ui.sections;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Validator;

import edu.aku.hassannaqvi.psbitrial.R;
import edu.aku.hassannaqvi.psbitrial.contracts.TableContracts;
import edu.aku.hassannaqvi.psbitrial.core.MainApp;
import edu.aku.hassannaqvi.psbitrial.database.DatabaseHelper;
import edu.aku.hassannaqvi.psbitrial.databinding.ActivitySection1Binding;
import edu.aku.hassannaqvi.psbitrial.models.Form;


import static edu.aku.hassannaqvi.psbitrial.core.MainApp.form;


public class Section1Activity extends AppCompatActivity {

    ActivitySection1Binding bi;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section1);
        bi.setCallback(this);
        MainApp.form = new Form();
      bi.setCallback(this);
      bi.setForm(MainApp.form = new Form());
        setSupportActionBar(bi.toolbar);
        setTitle(R.string.section1_mainheading);

        db = MainApp.appInfo.dbHelper;


        // Enables Always-on
    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        saveDraft();
        if ((insertNewRecord())) {
            Toast.makeText(this, "Patient Added.", Toast.LENGTH_SHORT).show();
            finish();
            Intent i = new Intent(this, Section2Activity.class);
            startActivity(i);
        }
    }

 /*   public void btnEnd(View view) {
        AppUtilsKt.contextEndActivity(this);
    }*/

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(this, bi.GrpName);
    }

    private void saveDraft() {

        // MainApp.form is only initialised at first section

        form.setTsf101(bi.tsf101.getText().toString());
        form.setTsf102(bi.tsf102.getText().toString());
        form.setTsf103(bi.tsf103.getText().toString());
        form.setTsf104(bi.tsf104.getText().toString());
        form.setTsf105(bi.tsf10501.isChecked() ? "1"
                : bi.tsf10502.isChecked() ? "2"
                : bi.tsf10503.isChecked() ? "3"
                : "-1");
        form.setTsf106(bi.tsf106.getText().toString());
        form.setTsf107(bi.tsf107.getText().toString());
        form.setTsf108(bi.tsf108.getText().toString());
        form.setTsf109(bi.tsf10901.isChecked() ? "1"
                : bi.tsf10902.isChecked() ? "2"
                : "-1");
        form.setTsf110(bi.tsf110.getText().toString());
        form.setTsf111(bi.tsf11101.isChecked() ? "1"
                : bi.tsf11102.isChecked() ? "2"
                : bi.tsf11103.isChecked() ? "3"
                : "-1");
        form.setTsf112(bi.tsf112.getText().toString());

        // Don't forget to popuate Section variable with JSON String...it will be used in UpdateDB()
        form.setS1(form.s1toString());
    }

    private boolean insertNewRecord() {
    // THIS FUNCTION IS NOT SAME AS UPDATEDB() IN OTHER SECTIONS

        long rowid = db.addForm(MainApp.form);

        // Chech if Form inserted into the database
        if (rowid > 0) {

            // UPDATE ID and UID field in form object.
            form.setId(String.valueOf(rowid));
            form.setUid(form.getDeviceId() + form.getId());

            // UPDATE newly created UID in Database.
            long count = db.updatesFormColumn(TableContracts.FormsTable.COLUMN_UID, form.getUid());
            if (count > 0) return true;
            else {
                // Error message in case when UID fails to update the row (check logcat for error messages)
                Toast.makeText(this, "SORRY! Failed to update UID", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {

            // Error message in case when new record in not inserted (check logcat for error messages)
            Toast.makeText(this, "SORRY! Failed to update DB", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}