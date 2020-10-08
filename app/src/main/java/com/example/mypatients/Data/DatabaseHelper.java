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

import java.util.ArrayList;
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
        db.execSQL(TABLE_PATIENTS_CREATE);
        db.execSQL(TABLE_PatientIntake_CREATE);
        db.execSQL(TABLE_PatientTime_CREATE);
        /*try
        {
            db.execSQL(TABLE_PATIENTS_CREATE);
            db.execSQL(TABLE_PatientIntake_CREATE);
            db.execSQL(TABLE_PatientTime_CREATE);
        }
        catch (Exception e)
        {
            String a= e.toString();

        }*/
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
    //Update Section
    public void UpdatePatient(String[] values,String idToUpdate)
    {
        String[] args ={idToUpdate};
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


        long timeTbl_id=db.update(Patient.TABLE_NAME,value,Patient.Column_patientId +" =?",args);
    }
    public void UpdatePatientTime(String[] values,String idToUpdate)
    {
        String[] args ={idToUpdate};
        ContentValues value= new ContentValues();
        value.put(PatientTime.Column_timePeriod,values[0]);
        value.put(PatientTime.Column_pulseRate ,values[1]);
        value.put(PatientTime.Column_temperature,values[2]);
        value.put(PatientTime.Column_bloodPressure ,values[3]);
        value.put(PatientTime.Column_ivf ,values[4]);
        value.put(PatientTime.Column_oxygen,values[5]);

        long timeTbl_id=db.update(PatientTime.TABLE_NAME,value,PatientTime.Column_patientId +" =?",args);
    }
    public void UpdatePatientPatientIntake(String[] values,String idToUpdate)
    {
        String[] args ={idToUpdate};
        ContentValues value= new ContentValues();
        value.put(PatientIntake.Column_timePeriod,values[0]);
        value.put(PatientIntake.Column_urineInput ,values[1]);
        value.put(PatientIntake.Column_urineOutput,values[2]);
        value.put(PatientIntake.Column_stoolInput,values[3]);
        value.put(PatientIntake.Column_waterIn,values[4]);
        value.put(PatientIntake.Column_milkIn ,values[5]);

        long timeTbl_id=db.update(PatientIntake.TABLE_NAME,value,PatientIntake.Column_patientId +" =?",args);
    }
    //End Update Section
    public String GetPatientId(String[] searchValues,boolean justId)
    {
        String[] columns={Patient.Column_patientId,
                Patient.Column_patientName,
                Patient.Column_patientSurname,
                Patient.Column_age,
                Patient.Column_roomNum};

        String where =Patient.Column_patientName + "=?"+ " and " +
                Patient.Column_patientSurname+ "=?"+ " and " +
                Patient.Column_age+ " LIKE ?" + " and " +
                Patient.Column_roomNum+ " LIKE ?";

        String[] selectionArgs=new String[]{searchValues[0],searchValues[1],"%"+searchValues[2]+"%","%"+searchValues[3]+"%"};
        try
        {
            Cursor cursor= db.query(Patient.TABLE_NAME,columns,where,selectionArgs,null,null,null);
            int count= cursor.getCount();
            String ids="";
            while(cursor.moveToNext())
            {
                ids= cursor.getString(0);
            }

            return ids;
        }
        catch (Exception e)
        {
            String a= String.valueOf(e);
        }
        return "";
    }
    public List<Object> GetByPatientId(int PatientId)
    {

        String[] columns={Patient.Column_patientId,
                Patient.Column_patientName,
                Patient.Column_patientSurname,
                Patient.Column_age,
                Patient.Column_roomNum};

        String where =Patient.Column_patientId + "=?";
        String[] selectionArgs=new String[]{String.valueOf(PatientId)};
        final List<Object> patientList= new ArrayList<>();
        try
        {
            Cursor cursor= db.query(Patient.TABLE_NAME,columns,where,selectionArgs,null,null,null);
            final int idIndex = cursor.getColumnIndex(Patient.Column_patientId);
            final int nameIndex = cursor.getColumnIndex(Patient.Column_patientName);
            final int surnameIndex = cursor.getColumnIndex(Patient.Column_patientSurname);
            final int ageIndex = cursor.getColumnIndex(Patient.Column_age);
            final int roomIndex = cursor.getColumnIndex(Patient.Column_roomNum);

            if (!cursor.moveToFirst()) {
                return new ArrayList<>();
            }

            /*while(cursor.moveToNext())
            {
                final int id = cursor.getInt(idIndex);
                final String  pName = cursor.getString(nameIndex);
                final String  pSname = cursor.getString(surnameIndex);
                final int pAge = cursor.getInt(ageIndex);
                final int roomNumber = cursor.getInt(roomIndex);
            }*/
            do {
                int id = cursor.getInt(idIndex);
                String  pName = cursor.getString(nameIndex);
                String  pSname = cursor.getString(surnameIndex);
                int pAge = cursor.getInt(ageIndex);
                int roomNumber = cursor.getInt(roomIndex);
                patientList.add(id);
                patientList.add(pName);
                patientList.add(pSname);
                patientList.add(pAge);
                patientList.add(roomNumber);

            }while(cursor.moveToNext());

        }
        catch (Exception e)
        {
            String a= String.valueOf(e);
        }
        return  patientList;
    }
    public void GetAllPatients()
    {
        String[] columns={Patient.Column_patientId,
                Patient.Column_patientName,
                Patient.Column_patientSurname,
                Patient.Column_age,
                Patient.Column_roomNum};
        try
        {
            Cursor cursor= db.query(Patient.TABLE_NAME,columns,null,null,null,null,null);
            int count= cursor.getCount();
            String ids="";
            while(cursor.moveToNext())
            {
                ids= cursor.getString(0);
            }

            //return ids;
        }
        catch (Exception e)
        {
            String a= String.valueOf(e);
        }
        //return "";
    }
}
