package edu.aku.hassannaqvi.psbitrial.models;

import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

import edu.aku.hassannaqvi.psbitrial.contracts.ZStandardContract;

/**
 * @author hassan.naqvi.
 */

public class ZStandard {

    private static final String TAG = "ZStandard_CONTRACT";
    String sex;
    String age;
    String measure;
    String L;
    String M;
    String S;
    String cat;
//    String REGION_DSS;

    public ZStandard() {
        // Default Constructor
    }

    public ZStandard(String age, String height, String weight, double L, double M, double S) {


    }

    public double getHLAZ() {
        int y = Integer.parseInt(measure);
        double Lz = Double.parseDouble(L);
        double Mz = Double.parseDouble(M);
        double Sz = Double.parseDouble(S);


        return (Math.pow(y / Mz, Lz)) - (1 / (Sz * Lz));

    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }


    public String getAge() {
        return this.age;
    }

    public void setAge(String age) {
        this.age = age;
    }


    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }


    public String getL() {
        return L;
    }

    public void setL(String l) {
        L = l;
    }


    public String getM() {
        return M;
    }

    public void setM(String m) {
        M = m;
    }


    public String getS() {
        return S;
    }

    public void setS(String s) {
        S = s;
    }


    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }


    public edu.aku.hassannaqvi.psbitrial.models.ZStandard Sync(JSONObject jsonObject) throws JSONException {
        this.sex = jsonObject.getString(ZStandardContract.ZScoreTable.COLUMN_SEX);
        this.age = jsonObject.getString(ZStandardContract.ZScoreTable.COLUMN_AGE);
        this.measure = jsonObject.getString(ZStandardContract.ZScoreTable.COLUMN_MEASURE);
        this.L = jsonObject.getString(ZStandardContract.ZScoreTable.COLUMN_L);
        this.M = jsonObject.getString(ZStandardContract.ZScoreTable.COLUMN_M);
        this.S = jsonObject.getString(ZStandardContract.ZScoreTable.COLUMN_S);
        this.cat = jsonObject.getString(ZStandardContract.ZScoreTable.COLUMN_CAT);
//        this.REGION_DSS = jsonObject.getString(singleUser.REGION_DSS);
        return this;

    }

    public edu.aku.hassannaqvi.psbitrial.models.ZStandard Hydrate(Cursor cursor) {
        this.sex = cursor.getString(cursor.getColumnIndex(ZStandardContract.ZScoreTable.COLUMN_SEX));
        this.age = cursor.getString(cursor.getColumnIndex(ZStandardContract.ZScoreTable.COLUMN_AGE));
        this.measure = cursor.getString(cursor.getColumnIndex(ZStandardContract.ZScoreTable.COLUMN_MEASURE));
        this.L = cursor.getString(cursor.getColumnIndex(ZStandardContract.ZScoreTable.COLUMN_L));
        this.M = cursor.getString(cursor.getColumnIndex(ZStandardContract.ZScoreTable.COLUMN_M));
        this.S = cursor.getString(cursor.getColumnIndex(ZStandardContract.ZScoreTable.COLUMN_S));
        this.cat = cursor.getString(cursor.getColumnIndex(ZStandardContract.ZScoreTable.COLUMN_CAT));
        return this;

    }


    public JSONObject toJSONObject() throws JSONException {

        JSONObject json = new JSONObject();
        json.put(ZStandardContract.ZScoreTable.COLUMN_SEX, this.sex == null ? JSONObject.NULL : this.sex);
        json.put(ZStandardContract.ZScoreTable.COLUMN_AGE, this.age == null ? JSONObject.NULL : this.age);
        json.put(ZStandardContract.ZScoreTable.COLUMN_MEASURE, this.measure == null ? JSONObject.NULL : this.measure);
        json.put(ZStandardContract.ZScoreTable.COLUMN_L, this.L == null ? JSONObject.NULL : this.L);
        json.put(ZStandardContract.ZScoreTable.COLUMN_M, this.M == null ? JSONObject.NULL : this.M);
        json.put(ZStandardContract.ZScoreTable.COLUMN_S, this.S == null ? JSONObject.NULL : this.S);
        json.put(ZStandardContract.ZScoreTable.COLUMN_CAT, this.cat == null ? JSONObject.NULL : this.cat);
        return json;
    }

}