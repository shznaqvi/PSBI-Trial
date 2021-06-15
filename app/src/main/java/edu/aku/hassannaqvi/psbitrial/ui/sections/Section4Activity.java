package edu.aku.hassannaqvi.psbitrial.ui.sections;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Validator;

import edu.aku.hassannaqvi.psbitrial.R;
import edu.aku.hassannaqvi.psbitrial.contracts.TableContracts;
import edu.aku.hassannaqvi.psbitrial.core.MainApp;
import edu.aku.hassannaqvi.psbitrial.database.DatabaseHelper;
import edu.aku.hassannaqvi.psbitrial.databinding.ActivitySection4Binding;

import static edu.aku.hassannaqvi.psbitrial.core.MainApp.form;

public class Section4Activity extends AppCompatActivity {
    ActivitySection4Binding bi;
    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section4);
        bi.setCallback(this);
        setListeners();
        //MainApp.form = new Form();
        bi.setForm(MainApp.form);
        setSupportActionBar(bi.toolbar);
        setTitle(R.string.section4_mainheading);

        db = MainApp.appInfo.dbHelper;

    }

    private void setListeners() {
        //  bi.tsf40104.setOnCheckedChangeListener((radioGroup, i) -> Clear.clearAllFields(bi.fldGrpCVtsf402));


        bi.tsf40104.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                Toast.makeText(this, "checked", Toast.LENGTH_SHORT).show();
                bi.fldGrpCVtsf402.setVisibility(View.GONE);
                bi.tsf40201.setChecked(false);
                bi.tsf40202.setChecked(false);
                bi.tsf40203.setChecked(false);
                bi.tsf40204.setChecked(false);
                bi.tsf40205.setChecked(false);
                bi.tsf40206.setChecked(false);
                bi.tsf40207.setChecked(false);
                bi.tsf40208.setChecked(false);
                bi.tsf40209.setChecked(false);
                bi.tsf40298.setChecked(false);
            } else {
                Toast.makeText(this, "checked false", Toast.LENGTH_SHORT).show();
                bi.fldGrpCVtsf402.setVisibility(View.VISIBLE);

            }
        });

    }

    public void btnContinue(View view) {
        if (!formValidation()) return;
        saveDraft();
        if (updateDB()) {
            Toast.makeText(this, "Patient Added.", Toast.LENGTH_SHORT).show();
            finish();
            Intent i = new Intent(this, Section5Activity.class);
            startActivity(i);
        }
    }

    public void btnEnd(View view) {
        saveDraft();
        if (updateDB()) {
            Toast.makeText(this, "Patient information not recorded.", Toast.LENGTH_SHORT).show();
            finish();
            Intent i = new Intent(this, EndingActivity.class);
            i.putExtra("complete",false);
            startActivity(i);
        }

    }

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(this, bi.GrpName);    }

    private void saveDraft() {

        // MainApp.form is only initialised at first section
        //MainApp.form = new Form();
        form.setTsf40101(bi.tsf40101.isChecked() ? "1" : "-1");
        form.setTsf40102(bi.tsf40102.isChecked() ? "2" : "-1");
        form.setTsf40103(bi.tsf40103.isChecked() ? "3" : "-1");
        form.setTsf40104(bi.tsf40104.isChecked() ? "4" : "-1");
        form.setTsf40201(bi.tsf40201.isChecked() ? "1" : "-1");
        form.setTsf40202(bi.tsf40202.isChecked() ? "2" : "-1");
        form.setTsf40203(bi.tsf40203.isChecked() ? "3" : "-1");
        form.setTsf40204(bi.tsf40204.isChecked() ? "4" : "-1");
        form.setTsf40205(bi.tsf40205.isChecked() ? "5" : "-1");
        form.setTsf40206(bi.tsf40206.isChecked() ? "6" : "-1");
        form.setTsf40207(bi.tsf40207.isChecked() ? "7" : "-1");
        form.setTsf40208(bi.tsf40208.isChecked() ? "8" : "-1");
        form.setTsf40209(bi.tsf40209.isChecked() ? "9" : "-1");
        form.setTsf40298(bi.tsf40298.isChecked() ? "98" : "-1");
        form.setTsf40298x(bi.tsf40298x.getText().toString());

        // Don't forget to popuate Section variable with JSON String...
        // it will be used in UpdateDB()
        form.setS4(form.s4toString());


    }


    private boolean updateDB() {
        // THIS FUNCTION IS NOT SAME AS INSERTNEWRECORD() in FIRST SECTION

        long updCount = db.updatesFormColumn(TableContracts.FormsTable.COLUMN_S4, MainApp.form.getS4());

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