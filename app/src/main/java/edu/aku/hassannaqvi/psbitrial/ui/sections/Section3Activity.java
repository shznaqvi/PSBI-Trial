package edu.aku.hassannaqvi.psbitrial.ui.sections;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.validatorcrawler.aliazaz.Validator;

import java.util.Calendar;
import java.util.Date;

import edu.aku.hassannaqvi.psbitrial.R;
import edu.aku.hassannaqvi.psbitrial.contracts.TableContracts;
import edu.aku.hassannaqvi.psbitrial.core.MainApp;
import edu.aku.hassannaqvi.psbitrial.database.DatabaseHelper;
import edu.aku.hassannaqvi.psbitrial.databinding.ActivitySection3Binding;
import edu.aku.hassannaqvi.psbitrial.models.Form;

import static edu.aku.hassannaqvi.psbitrial.core.MainApp.form;

public class Section3Activity extends AppCompatActivity {
    ActivitySection3Binding bi;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bi = DataBindingUtil.setContentView(this, R.layout.activity_section3);
        bi.setCallback(this);
        MainApp.form = new Form();
        bi.setForm(MainApp.form);
        setSupportActionBar(bi.toolbar);
        setTitle(R.string.section3_mainheading);

        db = MainApp.appInfo.dbHelper;

      /*  bi.tsf303.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==bi.tsf30301.getId()){
                    form.setTsf301("0");
                }else {
                    form.setTsf301("66");

                }
            }
        });*/
    }


    public void btnContinue(View view) {
        if (!formValidation()) return;
        saveDraft();
        if (updateDB()) {
            Toast.makeText(this, "Patient Added.", Toast.LENGTH_SHORT).show();
            finish();
            Intent i = new Intent(this, Section4Activity.class);
            startActivity(i);
        }
    }

   /* public void btnEnd(View view) {
        AppUtilsKt.contextEndActivity(this);
    }*/

    private boolean formValidation() {
        return Validator.emptyCheckingContainer(this, bi.GrpName);
    }

    private void saveDraft() {

        // MainApp.form is only initialised at first section
        //MainApp.form = new Form();

        form.setTsf301(bi.tsf301.getText().toString());
        form.setTsf302(bi.tsf302.getText().toString());
        form.setTsf303(bi.tsf30301.isChecked() ? "1"
                : bi.tsf30302.isChecked() ? "2"
                : "-1");
        form.setTsf304(bi.tsf304.getText().toString());
        //  form.setTsf305(bi.tsf305.getText().toString());   <-- removed from section.
        form.setTsf306(bi.tsf30601.isChecked() ? "1"
                : bi.tsf30602.isChecked() ? "2"
                : bi.tsf30603.isChecked() ? "3"
                : "-1");
        form.setTsf307(bi.tsf30701.isChecked() ? "1"
                : bi.tsf30702.isChecked() ? "2"
                : bi.tsf30703.isChecked() ? "3"
                : "-1");
        form.setTsf308(bi.tsf30801.isChecked() ? "1"
                : bi.tsf30802.isChecked() ? "2"
                : "-1");
        form.setTsf309(bi.tsf30901.isChecked() ? "1"
                : bi.tsf30902.isChecked() ? "2"
                : "-1");
        form.setTsf310(bi.tsf31001.isChecked() ? "1"
                : bi.tsf31002.isChecked() ? "2"
                : "-1");
        form.setTsf311(bi.tsf31101.isChecked() ? "1"
                : bi.tsf31102.isChecked() ? "2"
                : "-1");
        form.setTsf312(bi.tsf31201.isChecked() ? "1"
                : bi.tsf31202.isChecked() ? "2"
                : "-1");
        form.setTsf313(bi.tsf31301.isChecked() ? "1"
                : bi.tsf31302.isChecked() ? "2"
                : "-1");
        form.setTsf314(bi.tsf31401.isChecked() ? "1"
                : bi.tsf31402.isChecked() ? "2"
                : "-1");
        form.setTsf315(bi.tsf31501.isChecked() ? "1"
                : bi.tsf31502.isChecked() ? "2"
                : "-1");
        form.setTsf316(bi.tsf31601.isChecked() ? "1"
                : bi.tsf31602.isChecked() ? "2"
                : "-1");
        form.setTsf317(bi.tsf31701.isChecked() ? "1"
                : bi.tsf31702.isChecked() ? "2"
                : "-1");
        form.setTsf318(bi.tsf31801.isChecked() ? "1"
                : bi.tsf31802.isChecked() ? "2"
                : "-1");
        form.setTsf319(bi.tsf31901.isChecked() ? "1"
                : bi.tsf31902.isChecked() ? "2"
                : "-1");
        form.setTsf320(bi.tsf32001.isChecked() ? "1"
                : bi.tsf32002.isChecked() ? "2"
                : "-1");
        form.setTsf321(bi.tsf32101.isChecked() ? "1"
                : bi.tsf32102.isChecked() ? "2"
                : "-1");
        form.setTsf322(bi.tsf32201.isChecked() ? "1"
                : bi.tsf32202.isChecked() ? "2"
                : "-1");
        form.setTsf323(bi.tsf32301.isChecked() ? "1"
                : bi.tsf32302.isChecked() ? "2"
                : "-1");


        // Don't forget to popuate Section variable with JSON String...
        // it will be used in UpdateDB()
        form.setS3(form.s3toString());


    }

    private boolean updateDB() {
        // THIS FUNCTION IS NOT SAME AS INSERTNEWRECORD() in FIRST SECTION

        long updCount = db.updatesFormColumn(TableContracts.FormsTable.COLUMN_S3, MainApp.form.getS3());

        // Chech if Form inserted into the database
        if (updCount != -1) {
            if (Float.parseFloat(bi.tsf304.getText().toString()) > 38 || Float.parseFloat(bi.tsf304.getText().toString()) < 35.5) {
                Date currentTime = Calendar.getInstance().getTime();
                SharedPreferences sp = getSharedPreferences("onhold", MODE_PRIVATE);
                SharedPreferences.Editor spEdit = sp.edit();
                spEdit.putString(form.getMrNo(), currentTime.toString());
                spEdit.apply();
                Toast.makeText(this, "This child has been added to On-Hold List!", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else {

            // Error message in case when new record in not inserted (check logcat for error messages)
            Toast.makeText(this, "SORRY! Failed to update DB", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void setupSkips() {

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bi.llscrollviewmh26.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {

                    bi.llscrollviewmh26.getChildAt(bi.llscrollviewmh26.getChildCount() - 1);
                    int diff = (bi.llscrollviewmh26.getRight() - (bi.llscrollviewmh26.getWidth() + bi.llscrollviewmh26.getScrollX()));
                    if (diff == 0) {
                        AllVaccinationsViewed = true;
                    }
                }
            });
        }*/

        // bi.tsf301.
        //bi.tsf301.setOnCheckedChangeListener((compoundButton, b) -> Clear.clearAllFields(bi.mh012, !b));


      /*  //TODO:
        bi.mh025.setOnCheckedChangeListener((radioGroup, i) -> {
            //Log.d("TAG", "setupSkips:1 "+bi.mh02202.isChecked()+"|"+bi.mh02501.isChecked());
            if (bi.mh02202.isChecked() && bi.mh02501.isChecked()) {
                // Log.d("TAG", "setupSkips:2 ");
                openWarningDialog(this, "Error", "Answer conflicts with Q. MH022", bi.mh025);
                //bi.mh025.clearCheck();
            }
        });*/
    }

}