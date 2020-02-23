package com.example.mypatients.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Patient implements Parcelable
{
    private int patientId;
    private String patientName;
    private String patientSurname;
    private Date dateOfBirth;
    private int Age;
    private int RoomNumber;
    private String DoctorName;
    private String DoctorSurname;
    private String Diet;
    private String CC;
    private Boolean HasIVF;
    private Boolean HasVSQ;
    private Boolean HasNeb;
    private Boolean HasMedication;
    private int Intake;
    private int OutTake;
    private int Urine;
    private int Stool;

    public Patient()
    {

    }


    public static final Parcelable.Creator CREATOR= new Parcelable.Creator() {

        @Override
        public Object createFromParcel(Parcel parcel) {
            return null;
        }

        @Override
        public Object[] newArray(int i) {
            return new Object[0];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientSurname() {
        return patientSurname;
    }

    public void setPatientSurname(String patientSurname) {
        this.patientSurname = patientSurname;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public int getRoomNumber() {
        return RoomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        RoomNumber = roomNumber;
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String doctorName) {
        DoctorName = doctorName;
    }

    public String getDoctorSurname() {
        return DoctorSurname;
    }

    public void setDoctorSurname(String doctorSurname) {
        DoctorSurname = doctorSurname;
    }

    public String getDiet() {
        return Diet;
    }

    public void setDiet(String diet) {
        Diet = diet;
    }

    public String getCC() {
        return CC;
    }

    public void setCC(String CC) {
        this.CC = CC;
    }

    public Boolean getHasIVF() {
        return HasIVF;
    }

    public void setHasIVF(Boolean hasIVF) {
        HasIVF = hasIVF;
    }

    public Boolean getHasVSQ() {
        return HasVSQ;
    }

    public void setHasVSQ(Boolean hasVSQ) {
        HasVSQ = hasVSQ;
    }

    public Boolean getHasNeb() {
        return HasNeb;
    }

    public void setHasNeb(Boolean hasNeb) {
        HasNeb = hasNeb;
    }

    public Boolean getHasMedication() {
        return HasMedication;
    }

    public void setHasMedication(Boolean hasMedication) {
        HasMedication = hasMedication;
    }

    public int getIntake() {
        return Intake;
    }

    public void setIntake(int intake) {
        Intake = intake;
    }

    public int getOutTake() {
        return OutTake;
    }

    public void setOutTake(int outTake) {
        OutTake = outTake;
    }

    public int getUrine() {
        return Urine;
    }

    public void setUrine(int urine) {
        Urine = urine;
    }

    public int getStool() {
        return Stool;
    }

    public void setStool(int stool) {
        Stool = stool;
    }
}
