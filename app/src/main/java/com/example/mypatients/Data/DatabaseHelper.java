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
import java.util.Date;
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
        int rowsToSave=values.length/6;
        int timeIndex=0;
        int pulseIndex=1;
        int tempIndex=2;
        int bloodIndex=3;
        int ivfIndex=4;
        int oxIndex=5;
        int patIndex=values.length-1;
        for(int i=0;i<rowsToSave;i++)
        {
            value.put(PatientTime.Column_timePeriod,values[timeIndex]);
            value.put(PatientTime.Column_pulseRate ,values[pulseIndex]);
            value.put(PatientTime.Column_temperature,values[tempIndex]);
            value.put(PatientTime.Column_bloodPressure ,values[bloodIndex]);
            value.put(PatientTime.Column_ivf ,values[ivfIndex]);
            value.put(PatientTime.Column_oxygen,values[oxIndex]);
            value.put(PatientTime.Column_patientId,values[patIndex]);
            if(i+1==rowsToSave)
                break;
            db.insert(PatientTime.TABLE_NAME,null,value);
            timeIndex=timeIndex+6;
            pulseIndex=pulseIndex+6;
            tempIndex=tempIndex+6;
            bloodIndex=bloodIndex+6;
            ivfIndex=ivfIndex+6;
            oxIndex=oxIndex+6;
        }

        long timeTbl_id=db.insert(PatientTime.TABLE_NAME,null,value);
    }
    public void CreatePatientIntake(String[] values)
    {
        ContentValues value= new ContentValues();
        int rowsToSave=values.length/6;
        int timeIndex=0;
        int urineIndex=1;
        int urineOutIndex=2;
        int stoolIndex=3;
        int waterIndex=4;
        int milkIndex=5;
        int patIndex=values.length-1;
        for(int i=0;i<rowsToSave;i++)
        {
            value.put(PatientIntake.Column_timePeriod, values[timeIndex]);
            value.put(PatientIntake.Column_urineInput, values[urineIndex]);
            value.put(PatientIntake.Column_urineOutput, values[urineOutIndex]);
            value.put(PatientIntake.Column_stoolInput, values[stoolIndex]);
            value.put(PatientIntake.Column_waterIn, values[waterIndex]);
            value.put(PatientIntake.Column_milkIn, values[milkIndex]);
            value.put(PatientTime.Column_patientId,values[patIndex]);
            if(i+1==rowsToSave)
                break;
            db.insert(PatientIntake.TABLE_NAME,null,value);
            timeIndex=timeIndex+6;
            urineIndex=urineIndex+6;
            urineOutIndex=urineOutIndex+6;
            stoolIndex=stoolIndex+6;
            waterIndex=waterIndex+6;
            milkIndex=milkIndex+6;
        }
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

    //Have to check the regeneration for the tables so if the table size is bigger it has to do an insert not an update
    //Since it is busy overiding the last values over and over again


    public void UpdatePatientTime(String[] values,String idToUpdate)
    {
        String[] args ={idToUpdate};
        ContentValues value= new ContentValues();
        int rowsToSave=values.length/6;
        int timeIndex=0;
        int pulseIndex=1;
        int tempIndex=2;
        int bloodIndex=3;
        int ivfIndex=4;
        int oxIndex=5;
        int patIndex=values.length-1;
        for(int i=0;i<rowsToSave;i++)
        {
            value.put(PatientTime.Column_timePeriod,values[timeIndex]);
            value.put(PatientTime.Column_pulseRate ,values[pulseIndex]);
            value.put(PatientTime.Column_temperature,values[tempIndex]);
            value.put(PatientTime.Column_bloodPressure ,values[bloodIndex]);
            value.put(PatientTime.Column_ivf ,values[ivfIndex]);
            value.put(PatientTime.Column_oxygen,values[oxIndex]);
            value.put(PatientTime.Column_patientId,values[patIndex]);
            if(i+1==rowsToSave)
                break;
            db.update(PatientTime.TABLE_NAME,value,PatientTime.Column_patientId +" =?",args);
            timeIndex=timeIndex+6;
            pulseIndex=pulseIndex+6;
            tempIndex=tempIndex+6;
            bloodIndex=bloodIndex+6;
            ivfIndex=ivfIndex+6;
            oxIndex=oxIndex+6;
        }

        long timeTbl_id=db.update(PatientTime.TABLE_NAME,value,PatientTime.Column_patientId +" =?",args);
    }
    public void UpdatePatientPatientIntake(String[] values,String idToUpdate)
    {
        String[] args ={idToUpdate};
        ContentValues value= new ContentValues();
        int rowsToSave=values.length/6;
        int timeIndex=0;
        int urineIndex=1;
        int urineOutIndex=2;
        int stoolIndex=3;
        int waterIndex=4;
        int milkIndex=5;
        int patIndex=values.length-1;
        for(int i=0;i<rowsToSave;i++)
        {
            value.put(PatientIntake.Column_timePeriod, values[0]);
            value.put(PatientIntake.Column_urineInput, values[1]);
            value.put(PatientIntake.Column_urineOutput, values[2]);
            value.put(PatientIntake.Column_stoolInput, values[3]);
            value.put(PatientIntake.Column_waterIn, values[4]);
            value.put(PatientIntake.Column_milkIn, values[5]);
            value.put(PatientIntake.Column_patientId, values[6]);
            value.put(PatientTime.Column_patientId,values[patIndex]);
            if(i+1==rowsToSave)
                break;
            db.update(PatientIntake.TABLE_NAME,value,PatientIntake.Column_patientId +" =?",args);
            timeIndex=timeIndex+6;
            urineIndex=urineIndex+6;
            urineOutIndex=urineOutIndex+6;
            stoolIndex=stoolIndex+6;
            waterIndex=waterIndex+6;
            milkIndex=milkIndex+6;
        }

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
                Patient.Column_roomNum,
                Patient.Column_cc,
                Patient.Column_diet,
                Patient.Column_doctorName,
                Patient.Column_doctorSurname,
                Patient.Column_hasIvf,
                Patient.Column_hasMedication,
                Patient.Column_hasNeb,
                Patient.Column_hasVsq,
                Patient.Column_ngt,
                Patient.Column_dateOfBirth,
                Patient.Column_captureDate
        };

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
            final int dobIndex = cursor.getColumnIndex(Patient.Column_dateOfBirth);
            final int roomIndex = cursor.getColumnIndex(Patient.Column_roomNum);
            final int ccIndex = cursor.getColumnIndex(Patient.Column_cc);
            final int dietIndex = cursor.getColumnIndex(Patient.Column_diet);
            final int docNameIndex = cursor.getColumnIndex(Patient.Column_doctorName);
            final int docSurnameIndex = cursor.getColumnIndex(Patient.Column_doctorSurname);
            final int ivfIndex = cursor.getColumnIndex(Patient.Column_hasIvf);
            final int medicationIndex = cursor.getColumnIndex(Patient.Column_hasMedication);
            final int nebIndex = cursor.getColumnIndex(Patient.Column_hasNeb);
            final int vsgIndex = cursor.getColumnIndex(Patient.Column_hasVsq);
            final int ngtIndex = cursor.getColumnIndex(Patient.Column_ngt);
            final int dateCaptureIndex = cursor.getColumnIndex(Patient.Column_captureDate);

            if (!cursor.moveToFirst()) {
                return new ArrayList<>();
            }
            do {
                int id = cursor.getInt(idIndex);
                String  pName = cursor.getString(nameIndex);
                String  pSname = cursor.getString(surnameIndex);
                int pAge = cursor.getInt(ageIndex);
                String cc = cursor.getString(ccIndex);
                String dob = cursor.getString(dobIndex);
                String diet = cursor.getString(dietIndex);
                String roomNumber = cursor.getString(roomIndex);
                String docName = cursor.getString(docNameIndex);
                String docSurname = cursor.getString(docSurnameIndex);
                int ivf = cursor.getInt(ivfIndex);
                int medication = cursor.getInt(medicationIndex);
                int neb = cursor.getInt(nebIndex);
                int vsg = cursor.getInt(vsgIndex);
                int ngt = cursor.getInt(ngtIndex);
                String captureDate = cursor.getString(dateCaptureIndex);
                patientList.add(id);
                patientList.add(roomNumber);
                patientList.add(pAge);
                patientList.add(cc);
                patientList.add(dob);
                patientList.add(captureDate);
                patientList.add(diet);
                patientList.add(docName);
                patientList.add(docSurname);
                patientList.add(ivf);
                patientList.add(medication);
                patientList.add(neb);
                patientList.add(vsg);
                patientList.add(ngt);
                patientList.add(pName);
                patientList.add(pSname);



            }while(cursor.moveToNext());

        }
        catch (Exception e)
        {
            String a= String.valueOf(e);
        }
        return  patientList;
    }
    public List<Object> GetPatientIntake(int PatientId)
    {
        String[] columns={PatientIntake.Column_patientId,
                PatientIntake.Column_milkIn,
                PatientIntake.Column_stoolInput,
                PatientIntake.Column_timePeriod,
                PatientIntake.Column_urineInput,
                PatientIntake.Column_urineOutput,
                PatientIntake.Column_waterIn};

        String where =PatientIntake.Column_patientId + "=?";
        String[] selectionArgs=new String[]{String.valueOf(PatientId)};
        final List<Object> patientList= new ArrayList<>();
        try
        {
            Cursor cursor= db.query(PatientIntake.TABLE_NAME,columns,where,selectionArgs,null,null,null);
            final int milkIndex = cursor.getColumnIndex(PatientIntake.Column_milkIn);
            final int stoolIndex = cursor.getColumnIndex(PatientIntake.Column_stoolInput);
            final int timeIndex = cursor.getColumnIndex(PatientIntake.Column_timePeriod);
            final int urineInIndex = cursor.getColumnIndex(PatientIntake.Column_urineInput);
            final int urineOutIndex = cursor.getColumnIndex(PatientIntake.Column_urineOutput);
            final int waterIndex = cursor.getColumnIndex(PatientIntake.Column_waterIn);

            if (!cursor.moveToFirst()) {
                return new ArrayList<>();
            }
            do {
                String milk = cursor.getString(milkIndex);
                String  stool = cursor.getString(stoolIndex);
                String  time = cursor.getString(timeIndex);
                String  urineIn = cursor.getString(urineInIndex);
                String  urineOut = cursor.getString(urineOutIndex);
                String  water = cursor.getString(waterIndex);

                 patientList.add(time);
                patientList.add(urineIn);
                patientList.add(urineOut);
                patientList.add(stool);
                patientList.add(water);
                patientList.add(milk);


            }while(cursor.moveToNext());

        }
        catch (Exception e)
        {
            String a= String.valueOf(e);
        }
        return  patientList;
    }

    public List<Object> GetPatientTime(int PatientId)
    {
        String[] columns={PatientTime.Column_patientId,
                PatientTime.Column_bloodPressure,
                PatientTime.Column_ivf,
                PatientTime.Column_oxygen,
                PatientTime.Column_temperature,
                PatientTime.Column_timePeriod,
                PatientTime.Column_pulseRate};

        String where =PatientTime.Column_patientId + "=?";
        String[] selectionArgs=new String[]{String.valueOf(PatientId)};
        final List<Object> patientList= new ArrayList<>();
        try
        {
            Cursor cursor= db.query(PatientTime.TABLE_NAME,columns,where,selectionArgs,null,null,null);
            final int bloodPressureIndex = cursor.getColumnIndex(PatientTime.Column_bloodPressure);
            final int ivfIndex = cursor.getColumnIndex(PatientTime.Column_ivf);
            final int oxIndex = cursor.getColumnIndex(PatientTime.Column_oxygen);
            final int tempIndex = cursor.getColumnIndex(PatientTime.Column_temperature);
            final int timeIndex = cursor.getColumnIndex(PatientTime.Column_timePeriod);
            final int pulseIndex = cursor.getColumnIndex(PatientTime.Column_pulseRate);

            if (!cursor.moveToFirst()) {
                return new ArrayList<>();
            }
            do {
                String blood = cursor.getString(bloodPressureIndex);
                String  ivf = cursor.getString(ivfIndex);
                String  ox = cursor.getString(oxIndex);
                int temp = cursor.getInt(tempIndex);
                String time = cursor.getString(timeIndex);
                String pulse = cursor.getString(pulseIndex);
                patientList.add(time);
                patientList.add(pulse);
                patientList.add(temp);
                patientList.add(blood);
                patientList.add(ivf);
                patientList.add(ox);
            }while(cursor.moveToNext());

        }
        catch (Exception e)
        {
            String a= String.valueOf(e);
        }
        return  patientList;
    }
    public List<Object> GetByPatientDetails(String[] searchValues)
    {
        String[] columns={Patient.Column_patientId,
                Patient.Column_patientName,
                Patient.Column_patientSurname,
                Patient.Column_age,
                Patient.Column_roomNum};

        String where =Patient.Column_patientName + " LIKE ?"+ " and " +
                Patient.Column_patientSurname+ " LIKE ?"+ " and " +
                Patient.Column_age+ " LIKE ?" + " and " +
                Patient.Column_roomNum+ " LIKE ?";

        String[] selectionArgs=new String[]{"%"+searchValues[0]+"%","%"+searchValues[1]+"%","%"+searchValues[2]+"%","%"+searchValues[3]+"%"};
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
            /*String lista[]=new String[cursor.getCount()];
            int index=0;*/
            do {
                int id = cursor.getInt(idIndex);
                String  pName = cursor.getString(nameIndex);
                String  pSname = cursor.getString(surnameIndex);
                int pAge = cursor.getInt(ageIndex);
                int roomNumber = cursor.getInt(roomIndex);
                //lista[index]= id+","+pName+","+pSname+","+pAge+","+roomNumber;
                patientList.add(id);
                patientList.add(pName);
                patientList.add(pSname);
                patientList.add(pAge);
                patientList.add(roomNumber);
                //index++;
            }while(cursor.moveToNext());

        }
        catch (Exception e)
        {
            String a= String.valueOf(e);
        }
        return  patientList;
    }

    public boolean PatientExists(String[] searchValues)
    {
        String[] columns={Patient.Column_patientId,
                Patient.Column_patientName,
                Patient.Column_patientSurname,
                Patient.Column_age,
                Patient.Column_roomNum};

        String where =Patient.Column_patientName + " LIKE ?"+ " and " +
                Patient.Column_patientSurname+ " LIKE ?"+ " and " +
                Patient.Column_age+ " LIKE ?" + " and " +
                Patient.Column_roomNum+ " LIKE ?";

        String[] selectionArgs=new String[]{"%"+searchValues[0]+"%","%"+searchValues[1]+"%","%"+searchValues[2]+"%","%"+searchValues[3]+"%"};
        final List<Object> patientList= new ArrayList<>();

        try
        {
            Cursor cursor= db.query(Patient.TABLE_NAME,columns,where,selectionArgs,null,null,null);
            return cursor.moveToFirst();

        }
        catch (Exception e)
        {
            String a= String.valueOf(e);
        }
        return  false;
    }
    /*public void GetAllPatients()
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
    }*/
}
