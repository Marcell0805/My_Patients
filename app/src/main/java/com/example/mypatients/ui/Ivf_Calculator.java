package com.example.mypatients.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.mypatients.R;

import java.util.ArrayList;
import java.util.List;

public class Ivf_Calculator extends AppCompatActivity
{
    EditText timeInMin,timeInHour,calcValueTxt,volumeTxt,dripRateTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ivf__calculator);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setListeners();
    }

    private void setListeners() {
        volumeTxt=findViewById(R.id.mlTxt);
        dripRateTxt=findViewById(R.id.dripRateTxt);
        timeInHour=findViewById(R.id.timeHourTxt);
        timeInMin=findViewById(R.id.timeMinTxt);
        calcValueTxt=findViewById(R.id.calcValueTxt);

        volumeTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                CalculateIvf();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //CalculateIvf();
            }
        });
        dripRateTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //CalculateIvf();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                CalculateIvf();

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //CalculateIvf();
            }
        });
        timeInHour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //CalculateIvf();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                CalculateIvf();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //CalculateIvf();
            }
        });
        timeInMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                CalculateIvf();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //CalculateIvf();
            }
        });
    }
    private boolean CheckTimesFields(String hours, String minutes)
    {
        if(!hours.equals("") ||!minutes.equals("") )
            return true;
        return false;
    }
    private void CalculateIvf()
    {
        String[] values=setValues();
        boolean hasAllValues=checkValues(values);
        if(hasAllValues)
        {
            int minutes= ConvertToMin(values);
            int i=Integer.parseInt(values[0]);
            int j=Integer.parseInt(values[3]);
            int dripRate = (i*j ) / minutes;
            String ivf=String.valueOf(dripRate);
            calcValueTxt.setText(ivf);
        }
        else
        {
            calcValueTxt.setText(null);
            calcValueTxt.setHint("Enter values.");
        }


    }

    private int ConvertToMin(String[] values)
    {
        int minutes=0;
        if(!values[2].equals(""))
            minutes=Integer.parseInt(values[2]);
        int hours=0;
        if(!values[1].equals(""))
            hours=Integer.parseInt(values[1])*60;
        int total=minutes+hours;
        return  total;

    }

    private boolean checkValues(String[] values)
    {
        Boolean hasRequired=true;
        Boolean hasTime=false;
        Boolean timeCheck=false;
        for(int v=0;v<values.length;v++)
        {
            if(v==1||v==2)
            {
                if(!hasTime)
                {
                    hasTime=CheckTimesFields(values[1],values[2]);
                    timeCheck=true;
                }
            }
            else
                timeCheck=false;
            if(values[v].equals("")&&!timeCheck)
            {
                return false;
            }

        }
        if(hasTime)
            return hasRequired;
        else
            return false;

    }

    private String[] setValues()
    {
        //ArrayList[] values=new ArrayList[4];
        String[] values=new String[4];
        values[0]=(volumeTxt.getText().toString());
        values[1]=(timeInHour.getText().toString());
        values[2]=(timeInMin.getText().toString());
        values[3]=(dripRateTxt.getText().toString());
        return values;
    }

}
