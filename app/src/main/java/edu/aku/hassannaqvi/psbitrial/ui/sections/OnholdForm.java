package edu.aku.hassannaqvi.psbitrial.ui.sections;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Validator;

import java.util.Map;

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
            Intent i = new Intent(this, Section4Activity.class);
            startActivity(i);
        }
    }

    private boolean updateDB() {

        long updCount = db.updateTemp(bi.mrno.getText().toString(), bi.tsf305.getText().toString());
        if (updCount != -1) {
            updateOnHoldList();
            Toast.makeText(this, "Temperature Updated for MR.No.: " + bi.mrno.getText().toString(), Toast.LENGTH_LONG).show();

            return true;
        } else {
            Toast.makeText(this, "MR.No Incorrect or not on hold.", Toast.LENGTH_LONG).show();
            bi.mrno.setError("MR.No Incorrect or not on hold.");
            return false;
        }
    }


    private void updateOnHoldList() {

        edit.remove(bi.mrno.getText().toString());
        String holdmap = "";
        Map<String, ?> onhold = sp.getAll();
        for (Map.Entry<String, ?> entry : onhold.entrySet())
            holdmap += "ID = " + entry.getKey() +
                    ", | Time = " + entry.getValue();

        bi.onholdlist.setText(onhold.toString());
        bi.mrno.setText(null);
        bi.tsf305.setText(null);
        Toast.makeText(this, onhold.toString(), Toast.LENGTH_SHORT).show();
    }

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(this, bi.GrpName);
    }
}
