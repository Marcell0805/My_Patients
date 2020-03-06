package com.example.mypatients.Data;

import android.provider.BaseColumns;

public final class MyPatients {
    public static final  class Patient implements BaseColumns
    {
        public static final String TABLE_NAME = "Patient";
       public static final String Column_patientId = BaseColumns._ID;
       public static final String Column_roomNum="roomNum";
       public static final String Column_patientName="patientName";
       public static final String Column_patientSurname="patientSurname";
       public static final String Column_dateOfBirth="dateOfBirth";
       public static final String Column_age="age";
       public static final String Column_doctorName="doctorName";
       public static final String Column_doctorSurname="doctorSurname";
       public static final String Column_diet="diet";
       public static final String Column_cc="cc";
       public static final String Column_hasIvf="hasIvf";
       public static final String Column_hasVsq="hasVsq";
       public static final String Column_hasNeb="hasNeb";
       public static final String Column_hasMedication="hasMedication";
       public static final String Column_ngt="ngt";
       public static final String Column_captureDate="captureDate";
    }
    public static final  class PatientIntake implements BaseColumns
    {
        public static final String TABLE_NAME = "PatientIntake";
        public static final String Column_intakeId = BaseColumns._ID;
        public static final String Column_timePeriod="timePeriod";
        public static final String Column_urineInput="urineInput";
        public static final String Column_urineOutput="urineOutput";
        public static final String Column_stoolInput="stoolInput";
        public static final String Column_waterIn="waterIn";
        public static final String Column_milkIn="milkIn";
        public static final String Column_patientId="patientId";
    }
    public static final  class PatientTime implements BaseColumns
    {
        public static final String TABLE_NAME = "PatientTime";
        public static final String Column_timeTblId = BaseColumns._ID;
        public static final String Column_timePeriod="timePeriod";
        public static final String Column_pulseRate="pulseRate";
        public static final String Column_temperature="temperature";
        public static final String Column_bloodPressure="bloodPressure";
        public static final String Column_ivf="ivf";
        public static final String Column_oxygen="oxygen";
        public static final String Column_patientId="patientId";
    }
}
