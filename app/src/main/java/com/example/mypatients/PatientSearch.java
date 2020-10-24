package com.example.mypatients;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.mypatients.Data.DatabaseHelper;
import com.example.mypatients.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class PatientSearch extends AppCompatActivity
{

    private EditText pName,pSname,pAge,dateOfBirthTxt,roomNumber;
    private Button getSearchBtn,proceedBtn;
    private ListView patientList;
    private ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_search);
        adapter= new ArrayAdapter(this,R.layout.activity_listview,R.id.viewPatients);
        SetListeners();
    }
    private void SetListeners() {
        getSearchBtn=findViewById(R.id.searchPatientBtn);
        patientList=findViewById(R.id.patientListView);
        pAge=findViewById(R.id.searchAge);
        pName=findViewById(R.id.searchName);
        roomNumber=findViewById(R.id.searchRoom);
        pSname=findViewById(R.id.searchSurname);
        getSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchForPatient();
            }
        });
        patientList.setAdapter(adapter);
        patientList.setOnItemClickListener(listClick);
    }
    private AdapterView.OnItemClickListener listClick= new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String item=(String) patientList.getItemAtPosition(position);
            item=item.replace('\n',':');
            item=item.replace(" ","");
            getSelectedPatient(item);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //OpenHomeFragment("");
    }

    private void OpenHomeFragment(String id)
    {

        Bundle args = new Bundle();
        args.putString("PatientId",id);
        /*fr.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.homeLayout,fr).commit();*/

        /*FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        //fragmentTransaction.replace(R.layout.activity_patient_search, fr);
        fragmentTransaction.commit();*/
    }

    private void getSelectedPatient(String patient)
    {
        String[] items= patient.split(":");
        Bundle bundle = new Bundle();
        bundle.putString("PatientId",items[9]);
        Intent res= new Intent();
        res.putExtra("PatientId",items[9]);
        setResult(999,res);
        finish();
    }
    private void searchForPatient()
    {
        DatabaseHelper helper= new DatabaseHelper(this);
        helper.CreateSQLHelpers(this);
        String[] searchIdList= new String[5];
            searchIdList[0]=pName.getText().toString();
            searchIdList[1]=pSname.getText().toString();
            searchIdList[2]=pAge.getText().toString();
            searchIdList[3]=roomNumber.getText().toString();
        List<Object> items=helper.GetByPatientDetails(searchIdList);
        int size=(items.size()/5);
        String[][] splitL= new String[size][5];
        int k=0;
        for(int i=0;i<size;i++)
        {
            for(int c=0;c<5;c++)
            {
                splitL[i][c]= String.valueOf(items.get(k));
                k++;
            }
        }
        adapter= new ArrayAdapter(this,R.layout.activity_listview,R.id.viewPatients);
        for (int list=0;list<splitL.length;list++)
        {
            adapter.add("Name : "+splitL[list][1]+"\nSurname : "+splitL[list][2]+" \nAge : "+splitL[list][3]+"\nRoom No : "+splitL[list][4]
            +"\nPatient No : "+splitL[list][0]);
        }

        patientList.setAdapter(adapter);

    }
}