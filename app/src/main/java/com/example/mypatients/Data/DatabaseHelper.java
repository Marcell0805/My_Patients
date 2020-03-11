package com.example.mypatients.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Debug;

import com.example.mypatients.Data.MyPatients.Patient;
import com.example.mypatients.Data.MyPatients.PatientIntake;
import com.example.mypatients.Data.MyPatients.PatientTime;

import androidx.annotation.Nullable;

import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private DatabaseHelper helper;
    private SQLiteDatabase db;
    private static final String DATABASE_NAME="myPatientsDb.db";
    private static final int DATABASE_VERSION=1;
    private static final String TABLE_PATIENTS_CREATE=
            "CREATE TABLE " + Patient.TABLE_NAME + " (" +
                    Patient.Column_patientId + " INTEGER PRIMARY KEY, " +
                    Patient.Column_roomNum + " TEXT,"+
                    Patient.Column_age + " INTEGER," +
                    Patient.Column_captureDate + " TEXT,"+
                    Patient.Column_cc + " INTEGER,"+
                    Patient.Column_dateOfBirth + " TEXT,"+
                    Patient.Column_diet + " TEXT,"+
                    Patient.Column_doctorName + " TEXT,"+
                    Patient.Column_doctorSurname + " TEXT,"+
                    Patient.Column_hasIvf + " INTEGER,"+
                    Patient.Column_hasMedication + " INTEGER,"+
                    Patient.Column_hasNeb + " INTEGER,"+
                    Patient.Column_hasVsq + " INTEGER,"+
                    Patient.Column_ngt + " INTEGER,"+
                    Patient.Column_patientName + " TEXT,"+
                    Patient.Column_patientSurname + " TEXT " +
                    ")";

    private static final String TABLE_PatientIntake_CREATE =
            "CREATE TABLE " + PatientIntake.TABLE_NAME + " (" +
                    PatientIntake.Column_intakeId + " INTEGER PRIMARY KEY, " +
                    PatientIntake.Column_timePeriod + " TEXT,"+
                    PatientIntake.Column_urineInput + " INTEGER,"+
                    PatientIntake.Column_urineOutput + " INTEGER,"+
                    PatientIntake.Column_stoolInput + " INTEGER,"+
                    PatientIntake.Column_waterIn + " INTEGER,"+
                    PatientIntake.Column_milkIn + " INTEGER ,"+
                    PatientIntake.Column_patientId + " INTEGER ,"+
                    " FOREIGN KEY("+ PatientIntake.Column_patientId + ") REFERENCES " +
                    Patient.TABLE_NAME +
                    "(" + PatientTime._ID +") " + ")";

    private static final String TABLE_PatientTime_CREATE =
            "CREATE TABLE " + PatientTime.TABLE_NAME + " (" +
                    PatientTime.Column_timeTblId + " INTEGER PRIMARY KEY, " +
                    PatientTime.Column_timePeriod + " TEXT,"+
                    PatientTime.Column_pulseRate + " TEXT,"+
                    PatientTime.Column_temperature + " INTEGER,"+
                    PatientTime.Column_bloodPressure + " TEXT,"+
                    PatientTime.Column_ivf + " INTEGER,"+
                    PatientTime.Column_oxygen + " TEXT ,"+
                    PatientTime.Column_patientId + " INTEGER ,"+
                    " FOREIGN KEY("+ PatientTime.Column_patientId + ") REFERENCES " +
                    Patient.TABLE_NAME +
                    "(" + PatientTime._ID +") " + ")";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        try
        {
            db.execSQL(TABLE_PATIENTS_CREATE);
            db.execSQL(TABLE_PatientIntake_CREATE);
            db.execSQL(TABLE_PatientTime_CREATE);
        }
        catch (Exception e)
        {
            String a= e.toString();

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " +Patient.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +PatientIntake.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +PatientTime.TABLE_NAME);
        onCreate(db);

    }
    public void CreateSQLHelpers(Context context)
    {
        helper= new DatabaseHelper(context);
        db= helper.getWritableDatabase();
    }

    public long CreatePatient(String[] values)
    {
        ContentValues value= new ContentValues();
        value.put(Patient.Column_roomNum ,values[0]);
        value.put(Patient.Column_captureDate,values[1]);
        value.put(Patient.Column_patientName ,values[2]);
        value.put(Patient.Column_patientSurname,values[3]);
        value.put(Patient.Column_dateOfBirth ,values[4]);
        value.put(Patient.Column_age ,values[5]);

        value.put(Patient.Column_doctorName,values[6]);
        value.put(Patient.Column_doctorSurname,values[7]);
        value.put(Patient.Column_diet ,values[8]);
        value.put(Patient.Column_cc ,values[9]);
        value.put(Patient.Column_ngt ,values[10]);


        value.put(Patient.Column_hasIvf ,values[11]);
        value.put(Patient.Column_hasMedication,values[12]);
        value.put(Patient.Column_hasVsq,values[13]);
        value.put(Patient.Column_hasNeb ,values[14]);


        long patient_id=db.insert(Patient.TABLE_NAME,null,value);
        return patient_id;
    }
    public void CreatePatientTime(String[] values)
    {
        ContentValues value= new ContentValues();
        value.put(PatientTime.Column_timePeriod,values[0]);
        value.put(PatientTime.Column_pulseRate ,values[1]);
        value.put(PatientTime.Column_temperature,values[2]);
        value.put(PatientTime.Column_bloodPressure ,values[3]);
        value.put(PatientTime.Column_ivf ,values[4]);
        value.put(PatientTime.Column_oxygen,values[5]);
        value.put(PatientTime.Column_patientId,values[6]);

        long timeTbl_id=db.insert(PatientTime.TABLE_NAME,null,value);
    }
    public void CreatePatientIntake(String[] values)
    {
        ContentValues value= new ContentValues();
        value.put(PatientIntake.Column_timePeriod,values[0]);
        value.put(PatientIntake.Column_urineInput ,values[1]);
        value.put(PatientIntake.Column_urineOutput,values[2]);
        value.put(PatientIntake.Column_stoolInput,values[3]);
        value.put(PatientIntake.Column_waterIn,values[4]);
        value.put(PatientIntake.Column_milkIn ,values[5]);
        value.put(PatientIntake.Column_patientId,values[6]);

        long patient_id=db.insert(PatientIntake.TABLE_NAME,null,value);
    }
    public int GetPatientId()
    {
        String[] projection={Patient.Column_patientId};
        String select =Patient.Column_patientId+" = ?";
        String[] selectionArgs={""};
        Cursor c= db.query(Patient.TABLE_NAME,projection,select,selectionArgs,null,null,null);
        c.moveToLast();
        int Id=c.getInt(0);
        return Id;
    }
    public void GetByPatientDetails()
    {

    }
}
