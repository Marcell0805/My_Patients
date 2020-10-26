package com.example.mypatients.ui.home;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.print.PrintManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mypatients.Data.DatabaseHelper;
import com.example.mypatients.FragmentArg;
import com.example.mypatients.PatientSearch;
import com.example.mypatients.PrinterClass;
import com.example.mypatients.R;
import com.example.mypatients.databinding.FragmentHomeBinding;
import com.example.mypatients.ui.Ivf_Calculator;
import com.google.android.material.snackbar.Snackbar;

import java.io.Console;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
/*
    To-Do:
        The data for the tables aren't saving each column
        After data fetch populate patient details fields with the data


 */
public class HomeFragment extends Fragment
{
    public  boolean activeCalled =false;
    private FragmentHomeBinding binding;
    private Button printbtn,calcIvfBtn,genTimeBtn,saveBtn,addNewBtn,searchBtn;
    private Spinner startTimeSpin,endTimeSpin;
    private EditText dateOfBirthTxt,roomTxt,dateNowTxt,pName,pSname,dName,dSname,dietTxt,ccTxt/*,intakeTxt,outTxt,stoolTxt,urineTxt,*/,ngtTxt;
    private TextView ageTxt;
    private View curView;
    private TableLayout timeGrid,stoolGrid;
    private PrintManager printManager;
    private RadioButton ivfRadioBtn,vsqRadioBtn,nevRadioBtn,medRadioBtn,noIvfRadioBtn,noVsqRadioBtn,noNevRadioBtn,noMedRadioBtn;
    private RadioGroup ivfRadioGroup,vsqRadioGroup, nebRadioGroup,medRadioGroup;
    public String id;
    private boolean addNewPressed=false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        binding=FragmentHomeBinding.inflate(inflater);
        SetListeners(root);
        activeCalled=false;
        /*
        If Testing Uncomment below method
         */
        //setTestValues();
        DatabaseHelper helper= new DatabaseHelper(root.getContext());
        helper.CreateSQLHelpers(root.getContext());
        //SQLiteDatabase db= helper.getReadableDatabase();
        //String I=helper.GetPatientId(new String[]{"Marcell", "van Niekerk", "23", "Room 1"});
        curView=root;
        printManager = (PrintManager) getActivity()
                .getSystemService(root.getContext().PRINT_SERVICE);
        SetDefaultValues(root);
        TestData();
        return root;
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode)
    {
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==999)
        {
            try
            {
                if(data!=null)
                {
                    Bundle result= data.getExtras();
                    String a = result.getString("PatientId");
                    setPatientData(a);
                }
            }
            catch (Exception e)
            {
                String a= String.valueOf(e);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        activeCalled=true;
    }

    private void setTestValues()
    {
        dateOfBirthTxt.setText("1996/08/05");
        ageTxt.setText("23");
        roomTxt.setText("Room 1");
        dateNowTxt.setText("25 Feb 2020");
        pName.setText("Marcell");
        pSname.setText("van Niekerk");
        dName.setText("John");
        dSname.setText("Doe");
        dietTxt.setText("Apples and fruit");
        ccTxt.setText("12");
        ngtTxt.setText("30");
    }
    private void setRetrievedValues(List<Object>values)
    {
        roomTxt.setText(values.get(1).toString());
        ageTxt.setText(values.get(2).toString());
        ccTxt.setText(values.get(3).toString());
        dateOfBirthTxt.setText(values.get(4).toString());
        dateNowTxt.setText(values.get(5).toString());
        dietTxt.setText(values.get(6).toString());
        dName.setText(values.get(7).toString());
        dSname.setText(values.get(8).toString());
        ngtTxt.setText(values.get(13).toString());
        pName.setText(values.get(14).toString());
        pSname.setText(values.get(15).toString());
        //Radio Buttons
        if(values.get(9)!=null)
        {
            int a=Integer.parseInt(values.get(9).toString());
            int id=curView.findViewById(R.id.hasIvfRadio).getId();
            if(a==id)
            {
                ivfRadioBtn.setChecked(true);
            }
            else
                noIvfRadioBtn.setChecked(true);

        }
        if(values.get(10)!=null)
        {
            int a=Integer.parseInt(values.get(10).toString());
            int id=curView.findViewById(R.id.medicationRadio).getId();
            if(a==id)
            {
                medRadioBtn.setChecked(true);
            }
            else
                noMedRadioBtn.setChecked(true);
        }
        if(values.get(11)!=null)
        {
            int a=Integer.parseInt(values.get(11).toString());
            int id=curView.findViewById(R.id.vsqRadio).getId();
            if(a==id)
            {
                noVsqRadioBtn.setChecked(true);
            }
            else
                vsqRadioBtn.setChecked(true);

            //((RadioButton)vsqRadioGroup.getChildAt(Integer.parseInt(values.get(11).toString()))).setChecked(true);
        }
        if(values.get(12)!=null)
        {
            int a=Integer.parseInt(values.get(12).toString());
            int id=curView.findViewById(R.id.NoNebRadio).getId();
            if(a==id)
            {
                nevRadioBtn.setChecked(true);
            }
            else
                noNevRadioBtn.setChecked(true);
            //((RadioButton)nebRadioGroup.getChildAt(Integer.parseInt(values.get(12).toString()))).setChecked(true);
        }
        //End of radio buttons


    }

    private void SetListeners(View root)
    {
        startTimeSpin=root.findViewById(R.id.startTime);
        endTimeSpin=root.findViewById(R.id.endTime);
        //------------------------------------------
        //Buttons
        //------------------------------------------
        printbtn=root.findViewById(R.id.printBtn);
        saveBtn=root.findViewById(R.id.saveBtn);
        calcIvfBtn= root.findViewById(R.id.ivfCalcBtn);
        genTimeBtn = root.findViewById(R.id.tableBtn);
        addNewBtn= root.findViewById(R.id.AddNewPatientBtn);
        searchBtn=root.findViewById(R.id.GetPatientBtn);
        //--------------------------------------------
        ageTxt=root.findViewById(R.id.ageText);
        //--------------------------------------------------
        //Tables
        timeGrid = root.findViewById(R.id.timeTableLayout);
        stoolGrid = root.findViewById(R.id.stoolTable);
        //--------------------------------------------------
        roomTxt=root.findViewById(R.id.roomNoText);
        dateNowTxt=root.findViewById(R.id.nowDate);
        pName=root.findViewById(R.id.nameText);
        pSname=root.findViewById(R.id.surnameText);
        dateOfBirthTxt=root.findViewById(R.id.dateBirthText);
        dName=root.findViewById(R.id.doctorName);
        dSname=root.findViewById(R.id.doctorSurname);
        dietTxt=root.findViewById(R.id.dietText);
        ccTxt=root.findViewById(R.id.ccText);
        ngtTxt=root.findViewById(R.id.ngtTxt);
        //Radio Groups
        ivfRadioGroup=root.findViewById(R.id.ivfGroup);
        medRadioGroup=root.findViewById(R.id.medicationGroup);
        nebRadioGroup =root.findViewById(R.id.NebGroup);
        vsqRadioGroup=root.findViewById(R.id.sqOqGroup);
        //---------------------------------------------------------
        //--Radio Buttons
        //---------------------------------------------------------
        ivfRadioBtn=root.findViewById(R.id.hasIvfRadio);
        vsqRadioBtn=root.findViewById(R.id.vsqRadio);
        nevRadioBtn=root.findViewById(R.id.nedRadio);
        medRadioBtn=root.findViewById(R.id.medicationRadio);
        noIvfRadioBtn=root.findViewById(R.id.noIvfRadio);
        noVsqRadioBtn=root.findViewById(R.id.noVsqRadio);
        noNevRadioBtn=root.findViewById(R.id.NoNebRadio);
        noMedRadioBtn=root.findViewById(R.id.noMedicationRadio);


        dateOfBirthTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(dateOfBirthTxt.getText().toString()!=null && dateOfBirthTxt.getText().toString()!="" && dateOfBirthTxt.getText().toString().length()==10)
                {
                    CalculateAge();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        setTxtListener();

        printbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hasFields())
                {
                    if(hasTable())
                    {
                        PrintDetails();
                    }
                    else
                        Snackbar.make(curView,"No table created.",Snackbar.LENGTH_LONG).show();
                }
                else
                {
                    Snackbar.make(curView,"Not all values are present",Snackbar.LENGTH_LONG).show();
                }
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertData();
            }
        });
        calcIvfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showIvfCal(view);
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearch();
            }
        });
        genTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartGenerateTable(view);
            }
        });

        addNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetUI();
            }
        });
    }

    private void ResetUI()
    {
        addNewPressed=true;
        roomTxt.setText("");
        dateNowTxt.setText("");
        pName.setText("");
        pSname.setText("");
        dateOfBirthTxt.setText("");
        ageTxt.setText("");
        dName.setText("");
        dSname.setText("");
        dietTxt.setText("");
        ccTxt.setText("");
        ngtTxt.setText("");
        ivfRadioGroup.clearCheck();
        medRadioGroup.clearCheck();
        vsqRadioGroup.clearCheck();
        nebRadioGroup.clearCheck();
        SetDefaultValues(curView);
        CheckIfRows(0);
        CheckIfRows(1);

    }

    private boolean hasTable() {
        if(timeGrid.getChildCount()>0)
            return true;
        else
            return false;
    }
    /*private boolean hasRequiredSaveFields()
    {
        boolean hasRequired=true;
        boolean hasdateAge=false;
        List<String> values=setPrintDetails();
        String[] splitL= setSaveDetails();

        for(int i=0;i<splitL.length;i++)
        {
            if(splitL[i].toLowerCase().contains(getResources().getString(R.string.dobText).toLowerCase())||splitL[i].contains(getResources().getString(R.string.age).toLowerCase()))
            {
                if(!splitL[i].equals(""))
                    hasdateAge=true;
                else if(!hasdateAge)
                    hasdateAge=false;
            }
            else
            {
                if(splitL[0].equals(""))
                    hasRequired=false;
            }
        }
        if(hasRequired&&hasdateAge)
            return  hasRequired;
        return  false;
    }*/
    //Check to ensure all required fields have data
    private boolean hasFields()
    {
        boolean hasRequired=true;
        boolean hasdateAge=false;
        List<String> values=setPrintDetails();
        String[][] splitL= new String[values.size()][2];
        values.toArray(new String[values.size() * 2]);
        String[]a;
        for(int i=0;i<values.size();i++)
        {
            String s=values.get(i);
            a=s.split("-");
            splitL[i][0]=a[0];
            splitL[i][1]=a[1];
        }
        for(int i=0;i<splitL.length;i++)
        {
            if(splitL[i][1].toLowerCase().contains(getResources().getString(R.string.dobText).toLowerCase())||splitL[i][1].contains(getResources().getString(R.string.age).toLowerCase()))
            {
                if(!splitL[i][0].equals(""))
                    hasdateAge=true;
                else if(!hasdateAge)
                    hasdateAge=false;
            }
            else
            {
                if(splitL[0][0].equals(""))
                    hasRequired=false;
            }
        }
        if(hasRequired&&hasdateAge)
            return  hasRequired;
        return  false;
    }

    private void showIvfCal(View view)
    {
        Intent ivfCalc= new Intent(getActivity(), Ivf_Calculator.class);
        getActivity().startActivity(ivfCalc);
    }
    private void showSearch()
    {
        Intent patientSearch= new Intent(getActivity(), PatientSearch.class);
        startActivityForResult(patientSearch,999);
    }
    public void setPatientData(String id)
    {
        int aId=Integer.parseInt(id);
        DatabaseHelper helper= new DatabaseHelper(curView.getContext());
        helper.CreateSQLHelpers(curView.getContext());
        List<Object> patientDetails=helper.GetByPatientId(aId);
        setRetrievedValues(patientDetails);
        List<Object> patientIntake=helper.GetPatientIntake(aId);
        List<Object> patientTime=helper.GetPatientTime(aId);
        int endIndex=patientTime.size()-6;
        String[] timeRange=patientTime.get(0).toString().split(" - ");
        String[] EndTimeRange=patientTime.get(endIndex).toString().split(" - ");
        int sTime=Integer.parseInt(timeRange[0].replaceAll(":00",""));
        int eTime=Integer.parseInt(EndTimeRange[1].replaceAll(":00",""));

        int rowsToGen=CalculateHours(sTime,eTime);
        List<String> rowsTimeValue=MakeTimeList(rowsToGen,sTime,eTime);
        GenerateTable(curView,rowsTimeValue,0,6,patientTime);
        GenerateTable(curView,rowsTimeValue,1,6,patientIntake);
    }

    private void CalculateAge()
    {
        String sDateOfBirth=dateOfBirthTxt.getText().toString();
        Date parsed= new Date();
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat format =
                new SimpleDateFormat("yyyy/MM/dd");
        try {
            parsed = format.parse(sDateOfBirth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar[] calendar = new Calendar[2];
        calendar[0]=new GregorianCalendar();
        calendar[1]=new GregorianCalendar();
        calendar[0].setTime(parsed);
        int yearEntered = calendar[0].get(Calendar.YEAR);
        calendar[1].setTime(currentTime);
        int yearNow=calendar[1].get(Calendar.YEAR);
        int iAge= yearNow-yearEntered;
        Boolean isMonthAfter=calendar[1].get(Calendar.MONTH)>=calendar[0].get(Calendar.MONTH);
        Boolean isDayAft=calendar[1].get(Calendar.DAY_OF_MONTH)>=calendar[0].get(Calendar.DAY_OF_MONTH);
        if(isMonthAfter&&iAge!=0)
        {
            if(!isDayAft)
                iAge--;
        }
        else if(iAge!=0)
        {
            iAge--;
        }
        if(iAge!=0)
        {
            ageTxt.setText(String.valueOf(iAge));
        }
    }

    private void setTxtListener()
    {
        TextWatcher tw = new TextWatcher() {
            private String current = "";
            private String dateFormat = "YYYYMMDD";
            private Calendar cal = Calendar.getInstance();
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(addNewPressed)
                {
                    addNewPressed=false;
                }
                else
                {
                    if (!charSequence.toString().equals(current))
                    {
                        SetDateFormat(charSequence);
                    }
                }
            }

            private void SetDateFormat(CharSequence charSequence) {
                String clean = charSequence.toString().replaceAll("[^\\d.]|\\.", "");
                String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                int cl = clean.length();
                int sel = cl;
                for (int a = 2; a <= cl && a < 6; a += 2) {
                    sel++;
                }
                //Fix for pressing delete next to a forward slash
                if (clean.equals(cleanC)) sel--;

                if (clean.length() < 8){
                    clean = clean + dateFormat.substring(clean.length());
                }
                else{
                    int year = Integer.parseInt(clean.substring(0,4));
                    int mon  = Integer.parseInt(clean.substring(4,6));
                    int day  = Integer.parseInt(clean.substring(6,8));

                    mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                    cal.set(Calendar.MONTH, mon-1);
                    year = (year<1900)?1900:(year>2100)?2100:year;
                    cal.set(Calendar.YEAR, year);
                    // ^ first set year for the line below to work correctly
                    //with leap years - otherwise, date e.g. 29/02/2012
                    //would be automatically corrected to 28/02/2012

                    day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                    //clean = String.format("%02d%02d%02d",day, mon, year);
                    clean = String.format("%02d%02d%02d",year, mon, day);
                }

                clean = String.format("%s/%s/%s", clean.substring(0, 4),
                        clean.substring(4, 6),
                        clean.substring(6, 8));

                sel = sel < 0 ? 0 : sel;
                current = clean;
                dateOfBirthTxt.setText(current);
                dateOfBirthTxt.setSelection(sel < current.length() ? sel : current.length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        dateOfBirthTxt.addTextChangedListener(tw);
    }

    private void SetDefaultValues(View root)
    {
        String date_n = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        EditText DateNow= root.findViewById(R.id.nowDate);
        DateNow.setText(date_n);
        genTimeBtn.setText("CREATE TABLE");
    }

    private void PrintDetails()
    {
        PrinterClass printerClass= new PrinterClass();
        List<String> normalDetails=setPrintDetails();
        String[][] tableDetails=setPrintForTblDetails(0);
        String[][] tableDetails2=setPrintForTblDetails(1);
        printerClass.printDocument(curView,printManager,normalDetails,tableDetails,tableDetails2);
    }

    private  String[][] setPrintForTblDetails(int tblCount)
    {
        //Sets single array for row data
        //Count for the first array size
        int rowCount;
        if(tblCount==1)
        {
            rowCount =timeGrid.getChildCount();
        }
        else
        {
            rowCount =stoolGrid.getChildCount();
        }
        //Setting single array
        String[] tableValues= new String[rowCount];
        for (int i=0;i<rowCount;i++)
        {
            TableRow tableRow;
            String[] tableColValues;
            if(tblCount==0)
            {
                tableRow=(TableRow)timeGrid.getChildAt(i);
                tableColValues= new String[timeGrid.getChildCount()];
            }
            else
            {
                tableRow=(TableRow)stoolGrid.getChildAt(i);
                tableColValues= new String[stoolGrid.getChildCount()];
            }

            for(int c=0;c<tableRow.getChildCount();c++)
            {
                TextView a =(TextView)tableRow.getChildAt(c);
                String b=a.getText().toString();
                if(c==0)
                    tableColValues[i]=b;
                else
                    tableColValues[i]+="; "+b;

            }
            tableValues[i]=tableColValues[i];

        }
        //Get the column count
        List<String>list= Arrays.asList(tableValues);
        String s=list.get(0);
        String[]a =s.split(";");
        int columnCount=a.length;
        //------------------------------------

        String[][] tableDetails= new String[rowCount][columnCount];
        String[]values;

        for(int i=0;i<rowCount;i++)
        {
            String r=list.get(i);
            values=r.split(";");
            String[]vCount=values;
            tableDetails[i][0]=values[0];
            for(int c=0;c<columnCount;c++)
            {
                if(c<vCount.length)
                    tableDetails[i][c]=values[c];
                else
                    break;
            }
        }
        return tableDetails;
    }
    private  String[]setSaveForTblDetails(int tblCount)
    {
        //Sets single array for row data
        //Count for the first array size
        int rowCount;
        int columnCount;
        int Index=0;
        if(tblCount==1)
        {
            rowCount =timeGrid.getChildCount();
        }
        else
        {
            rowCount =stoolGrid.getChildCount();
        }
        TableRow tableRowC=(TableRow)timeGrid.getChildAt(0);
        int rowCountNoHeader=rowCount-1;
        int arraySize=tableRowC.getChildCount()*rowCountNoHeader;
        //Setting single array
        String[] tableValues= new String[arraySize+1];
        for (int i=1;i<rowCount;i++)
        {
            TableRow tableRow;
            if(tblCount==0)
            {
                tableRow=(TableRow)timeGrid.getChildAt(i);
            }
            else
            {
                tableRow=(TableRow)stoolGrid.getChildAt(i);
            }

            for(int c=0;c<tableRow.getChildCount();c++)
            {
                TextView a =(TextView)tableRow.getChildAt(c);
                String b=a.getText().toString();
                tableValues[Index]=b;
                Index++;
            }
            tableValues[tableValues.length-1]="PatientId";
        }
        return tableValues;
    }

    private List<String> setPrintDetails()
    {
        List<String> col=new ArrayList<>();
        col.add(roomTxt.getText().toString()+"-"+roomTxt.getHint()+":");
        col.add(dateNowTxt.getText().toString()+"-"+dateNowTxt.getHint()+":");
        col.add(pName.getText().toString()+"-"+pName.getHint()+":");
        col.add(pSname.getText().toString()+"-"+pSname.getHint()+":");
        col.add(dateOfBirthTxt.getText().toString()+"-"+dateOfBirthTxt.getHint()+":");
        col.add(ageTxt.getText().toString()+"-"+ageTxt.getHint()+":");
        col.add(dName.getText().toString()+"-"+dName.getHint()+":");
        col.add(dSname.getText().toString()+"-"+dSname.getHint()+":");
        col.add(dietTxt.getText().toString()+"-"+dietTxt.getHint()+":");
        col.add(ccTxt.getText().toString()+"-"+ccTxt.getHint());
        col.add(ngtTxt.getText().toString()+"ml-"+ngtTxt.getHint()+":");
        int ivfId=ivfRadioGroup.getCheckedRadioButtonId();
        int medId=medRadioGroup.getCheckedRadioButtonId();
        int vsqId=vsqRadioGroup.getCheckedRadioButtonId();
        int nebId=nebRadioGroup.getCheckedRadioButtonId();

        //region Radio Buttons
        if(ivfId==-1)
        {
            //col.add("-:");
        }
        else if(ivfId==ivfRadioBtn.getId())
        {
            col.add("Yes"+"-"+ivfRadioBtn.getText()+":");
        }
        else
        {
            col.add("Yes"+"-"+noIvfRadioBtn.getText()+":");
        }
        if(medId==-1)
        {
            //col.add("-:");
        }
        else if(medId==medRadioBtn.getId())
        {
            col.add("Yes"+"-"+medRadioBtn.getText()+":");
        }
        else
        {
            col.add("Yes"+"-"+noMedRadioBtn.getText()+":");
        }
        if(vsqId==-1)
        {
            //col.add("-:");
        }
        else if(vsqId==vsqRadioBtn.getId())
        {
            col.add("Yes"+"-"+vsqRadioBtn.getText()+":");
        }
        else
        {
            col.add("Yes"+"-"+"I & Oq:");
        }
        if(nebId==-1)
        {
            //col.add("-:");
        }
        else if(nebId==nevRadioBtn.getId())
        {
            col.add("Yes"+"-"+nevRadioBtn.getText()+":");
        }
        else
        {
            col.add("Yes"+"-"+noNevRadioBtn.getText()+":");
        }
        //endregion
        return col;
    }
    private String[] setSaveDetails()
    {
        Boolean isValid=true;
        List<String> col=new ArrayList<>();
        String roomNum=roomTxt.getText().toString();
        if(TextUtils.isEmpty(roomNum))
        {
            roomTxt.setError("Please Enter a Room Number.");
            isValid=false;
        }
        else
            col.add(roomNum);
        String captureDate=dateNowTxt.getText().toString();
        if(TextUtils.isEmpty(captureDate))
        {
            dateNowTxt.setError("Please Enter Today's Date.");
            isValid=false;
        }
        else
            col.add(captureDate);
        String patientName=pName.getText().toString();
        if(TextUtils.isEmpty(patientName))
        {
            pName.setError("Please Enter The Patient's Name.");
            isValid=false;
        }
        else
            col.add(patientName);
        String patientSurName=pSname.getText().toString();
        if(TextUtils.isEmpty(patientSurName))
        {
            pSname.setError("Please Enter The Patient's Surname.");
            isValid=false;
        }
        else
            col.add(patientSurName);

        String dobString=dateOfBirthTxt.getText().toString();
        String ageString =ageTxt.getText().toString();
        boolean isDate=true;
        try
        {
            SimpleDateFormat dateFormat= new SimpleDateFormat("YYYY/MM/DD");
            Date ofBirth=dateFormat.parse(dobString);

        }
        catch (ParseException e)
        {
            isDate=false;
        }

        if(!isDate && ageString.equals(""))
        {
            String Error="Please Enter Date Of Birth Or Age";
            dateOfBirthTxt.setError(Error);
            ageTxt.setError(Error);

            isValid=false;
        }
        else
        {
            col.add(dateOfBirthTxt.getText().toString());
            col.add(ageTxt.getText().toString());
        }
        String docName=dName.getText().toString();
        if(TextUtils.isEmpty(docName))
        {
            dName.setError("Please Enter Doctor Name.");
            isValid=false;
        }
        else
        {
            col.add(docName);
        }
        String docSurname=dSname.getText().toString();
        if(TextUtils.isEmpty(docSurname))
        {
            dSname.setError("Please Enter Doctor Surname.");
            isValid=false;
        }
        else
        {
            col.add(docSurname);
        }
        String diet=dietTxt.getText().toString();
        if(TextUtils.isEmpty(diet))
        {
            dietTxt.setError("Please Enter Diet.");
            isValid=false;
        }
        else
        {
            col.add(diet);
        }
        String ccString=ccTxt.getText().toString();
        if(TextUtils.isEmpty(ccString))
        {
            ccTxt.setError("Please Enter CC.");
            isValid=false;
        }
        else
        {
            col.add(ccString);
        }
        String ngtString=ngtTxt.getText().toString();
        if(TextUtils.isEmpty(ngtString))
        {
            ngtTxt.setError("Please Enter NGT.");
            isValid=false;
        }
        else
        {
            col.add(ngtString);
        }
        int ivfId=ivfRadioGroup.getCheckedRadioButtonId();
        int medId=medRadioGroup.getCheckedRadioButtonId();
        int vsqId=vsqRadioGroup.getCheckedRadioButtonId();
        int nebId=nebRadioGroup.getCheckedRadioButtonId();

        //region Radio Buttons
        if(ivfId==-1)
        {
            col.add("");
        }
        else if(ivfId==ivfRadioBtn.getId())
        {
            col.add(String.valueOf(ivfRadioBtn.getId()));
        }
        else
        {
            col.add(String.valueOf(noIvfRadioBtn.getId()));
        }
        if(medId==-1)
        {
            col.add("");
        }
        else if(medId==medRadioBtn.getId())
        {
            col.add(String.valueOf(medRadioBtn.getId()));
        }
        else
        {
            col.add(String.valueOf(noMedRadioBtn.getId()));
        }
        if(vsqId==-1)
        {
            col.add("");
        }
        else if(vsqId==vsqRadioBtn.getId())
        {
            col.add(String.valueOf(vsqRadioBtn.getId()));
        }
        else
        {
            col.add(String.valueOf(noVsqRadioBtn.getId()));
        }
        if(nebId==-1)
        {
            col.add("");
        }
        else if(nebId==nevRadioBtn.getId())
        {
            col.add(String.valueOf(nevRadioBtn.getId()));
        }
        else
        {
            col.add(String.valueOf(noNevRadioBtn.getId()));
        }
        //endregion
        if(isValid)
            return col.toArray(new String[0]);
        else
            return null;
    }

    private void StartGenerateTable(View view)
    {
        String startTime = startTimeSpin.getSelectedItem().toString().replaceAll(":00","");
        String endTime = endTimeSpin.getSelectedItem().toString().replaceAll(":00","");
        int sTime=Integer.parseInt(startTime);
        int eTime=Integer.parseInt(endTime);
        if(eTime==0)
        {
            eTime=24;
        }
        if(eTime<sTime)
        {
            int swapStart=sTime;
            eTime=sTime;
            sTime=swapStart;
        }
        int rowsToGen=CalculateHours(sTime,eTime);
        List<String> rowsTimeValue=MakeTimeList(rowsToGen,sTime,eTime);
        GenerateTable(view,rowsTimeValue,0,6,null);
        GenerateTable(view,rowsTimeValue,1,6,null);
    }

    private void GenerateTable(View view,List<String>rowsTimeValue,int tablecount,int columnCount,List<Object>tblValues)
    {
        CheckIfRows(tablecount);
        ShapeDrawable sd = new ShapeDrawable();
        ShapeDrawable sdCol = new ShapeDrawable();
        ShapeDrawable inputSd = new ShapeDrawable();
        ShapeDrawable input2Sd = new ShapeDrawable();
        setShapeDetails(sd,view,sdCol,inputSd,input2Sd);
        int SizeWithHeading=rowsTimeValue.size()+1;
        TableRow[] rws=new TableRow[SizeWithHeading];
        TextView[] textView=new TextView[SizeWithHeading];
        EditText[] editTxt=new EditText[5];
        int existDataSize=0;
        if(tblValues!=null) {
            existDataSize = tblValues.size();
        }
        int rowData=1;

        for (int i=0;i<SizeWithHeading;i++)
        {
            //Set Header values
            rws[i]=new TableRow(view.getContext());
            if(i==0)
            {
                TextView[] headTxt=new TextView[columnCount];
                for (int hT=0;hT<headTxt.length;hT++)
                {
                    headTxt[hT]= new TextView(view.getContext());
                    headTxt[hT].setTextSize(15);
                    headTxt[hT].setTypeface(null, Typeface.BOLD);
                    headTxt[hT].setGravity(Gravity.LEFT);
                }
                headTxt[0].setText(getResources().getString(R.string.timeTblHead));
                if(tablecount==0)
                {
                    headTxt[1].setText(getResources().getString(R.string.prTblHead));
                    headTxt[2].setText(getResources().getString(R.string.tempTblHead));
                    headTxt[3].setText(getResources().getString(R.string.bpTblHead));
                    headTxt[4].setText(getResources().getString(R.string.ivfTblHead));
                    headTxt[5].setText(getResources().getString(R.string.o2TblHead));
                }
                else
                {
                    headTxt[1].setText(getResources().getString(R.string.urineInTblHead));
                    headTxt[2].setText(getResources().getString(R.string.urineOutTblHead));
                    headTxt[3].setText(getResources().getString(R.string.stoolInTblHead));
                    headTxt[4].setText(getResources().getString(R.string.waterCcTblHead));
                    headTxt[5].setText(getResources().getString(R.string.milkCcTblHead));
                }

                for (int h=0;h<headTxt.length;h++)
                {
                    rws[i].setBackground(sd);
                    rws[i].setGravity(Gravity.CENTER);
                    rws[i].addView(headTxt[h]);

                }
                switch ( tablecount)
                {
                    case 0:
                    {
                        timeGrid.addView(rws[i]);
                        break;
                    }
                    case 1:
                    {

                        stoolGrid.addView(rws[i]);
                        break;
                    }
                }

            }
            //Set Row data
            else
            {
                textView[i]= new TextView(view.getContext());
                textView[i].setText(rowsTimeValue.get(i-1));
                textView[i].setGravity(Gravity.CENTER);
                textView[i].setWidth(215);
                textView[i].setHeight(150);
                textView[i].setBackground(sdCol);
                if(i==0)
                {
                    textView[i].setFreezesText(true);
                }
                rws[i].addView(textView[i]);
                for (int c=0;c<5;c++)
                {
                    editTxt[c]= new EditText(view.getContext());
                    editTxt[c].setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    editTxt[c].setHeight(150);
                    editTxt[c].setWidth(215);
                    if(existDataSize>0)
                    {
                        editTxt[c].setText(tblValues.get(rowData).toString());
                        rowData++;
                    }
                    if(c+1==5)
                        rowData++;
                    editTxt[c].setGravity(Gravity.CENTER);
                    if(c==1||c==3)
                        editTxt[c].setBackground(inputSd);
                    else
                        editTxt[c].setBackground(input2Sd);
                    rws[i].addView(editTxt[c]);
                }
                switch ( tablecount)
                {
                    case 0:
                    {
                        timeGrid.addView(rws[i]);
                        break;
                    }
                    case 1:
                    {

                        stoolGrid.addView(rws[i]);
                        break;
                    }
                }
            }
        }
        genTimeBtn.setText("RECREATE TABLE");
    }

    private void setShapeDetails(ShapeDrawable sd,View view,ShapeDrawable sdCol,ShapeDrawable inputSd,ShapeDrawable input2Sd)
    {
        // Specify the shape of ShapeDrawable
        sd.setShape(new RectShape());
        // Specify the border color of shape
        sd.getPaint().setColor(ContextCompat.getColor(view.getContext(), R.color.tableHeading));
        // Set the border width
        sd.getPaint().setStrokeWidth(10f);
        // Specify the style is a Stroke
        sd.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);
        //------------------------------------------------------------
        // Specify the shape of ShapeDrawable
        sdCol.setShape(new RectShape());
        // Specify the border color of shape
        sdCol.getPaint().setColor(ContextCompat.getColor(view.getContext(), R.color.tableColor));
        // Set the border width
        sdCol.getPaint().setStrokeWidth(10f);
        // Specify the style is a Stroke
        sdCol.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);
        //------------------------------------------------------------
        // Specify the shape of ShapeDrawable
        inputSd.setShape(new RectShape());
        // Specify the border color of shape
        inputSd.getPaint().setColor(ContextCompat.getColor(view.getContext(), R.color.inputTblColor));
        // Set the border width
        inputSd.getPaint().setStrokeWidth(10f);
        // Specify the style is a Stroke
        inputSd.getPaint().setStyle(Paint.Style.STROKE);
        //------------------------------------------------------------
        // Specify the shape of ShapeDrawable
        input2Sd.setShape(new RectShape());
        // Specify the border color of shape
        input2Sd.getPaint().setColor(ContextCompat.getColor(view.getContext(), R.color.input2TblColor));
        // Set the border width
        input2Sd.getPaint().setStrokeWidth(10f);
        // Specify the style is a Stroke
        input2Sd.getPaint().setStyle(Paint.Style.STROKE);
    }

    private void CheckIfRows(int tableCount)
    {
        if(tableCount==0)
        {
            int count=timeGrid.getChildCount();
            if(count>0)
            {
                timeGrid.removeAllViews();
            }
        }
        else
        {
            int count=stoolGrid.getChildCount();
            if(count>0)
            {
                stoolGrid.removeAllViews();
            }
        }
    }

    private List<String> MakeTimeList(int rowCount,int startTime,int endTime)
    {
        List<String>newList= new ArrayList<>();
        int toTime=startTime+1;
        for(int row=0;row<rowCount;row++)
        {
            newList.add(startTime+":00 - "+toTime+":00");
            startTime++;
            toTime++;
        }

        return newList;
    }

    private int CalculateHours(int sTime, int eTime)
    {
        int rows=(eTime-sTime);
        return rows;
    }
    //---------------------------------------------------------------------------------------------------------------------
    // DATABASE METHODS
    //---------------------------------------------------------------------------------------------------------------------

    private void InsertData()
    {
        if(timeGrid.getChildCount()==0)
            Toast.makeText(curView.getContext(),"Please Generate The Tables", Toast.LENGTH_LONG).show();
        else
        {

            String[]tableD = setSaveForTblDetails(0);
            String[]tableIn = setSaveForTblDetails(1);
            String[] patientDetails= setSaveDetails();
            if(patientDetails!=null)
            {
                try
                {
                    DatabaseHelper helper= new DatabaseHelper(curView.getContext());
                    helper.CreateSQLHelpers(curView.getContext());
                    long idP=helper.CreatePatient(patientDetails);
                    tableD[tableD.length-1]=String.valueOf(idP);
                    tableIn[tableD.length-1]=String.valueOf(idP);
                    helper.CreatePatientTime(tableD);
                    helper.CreatePatientIntake(tableIn);
                }
                catch (Exception ex)
                {
                    String error= ex.getMessage();
                    Toast.makeText(curView.getContext(),error,Toast.LENGTH_LONG).show();
                }
                AfterSave();
            }
            else
                Toast.makeText(curView.getContext(),"Missing Data", Toast.LENGTH_LONG).show();

        }
    }
    private void TestData()
    {
        /*DatabaseHelper helper= new DatabaseHelper(curView.getContext());
        helper.CreateSQLHelpers(curView.getContext());
        String[] searchIdList= new String[5];
        searchIdList[0]="Marcell";
        searchIdList[1]="van Niekerk";
        searchIdList[2]="";
        searchIdList[3]="Room 1";
        String IdForUpdate=helper.GetPatientId(searchIdList,false);
        List<Object> items=helper.GetByPatientId(1);
        String Name= String.valueOf(items.get(1));
        String LastName = String.valueOf(items.get(2));*/
    }
    private void UpdateData()
    {
        if(timeGrid.getChildCount()==0)
            Toast.makeText(curView.getContext(),"Please Generate The Tables", Toast.LENGTH_LONG).show();
        else
        {

            String[]tableD = setSaveForTblDetails(0);
            String[]tableIn = setSaveForTblDetails(1);
            String[] patientDetails= setSaveDetails();
            String[] searchIdList= new String[5];
            searchIdList[0]=patientDetails[2];
            searchIdList[1]=patientDetails[3];
            searchIdList[2]=patientDetails[5];
            searchIdList[3]=patientDetails[0];

            if(patientDetails!=null)
            {
                try
                {
                    DatabaseHelper helper= new DatabaseHelper(curView.getContext());
                    helper.CreateSQLHelpers(curView.getContext());
                    String IdForUpdate=helper.GetPatientId(searchIdList,false);
                    helper.UpdatePatient(patientDetails,IdForUpdate);
                    helper.UpdatePatientTime(tableD,IdForUpdate);
                    helper.UpdatePatientPatientIntake(tableIn,IdForUpdate);
                }
                catch (Exception ex)
                {
                    String error= ex.getMessage();
                    Toast.makeText(curView.getContext(),error,Toast.LENGTH_LONG).show();
                }
                AfterSave();
            }
            else
                Toast.makeText(curView.getContext(),"Missing Data", Toast.LENGTH_LONG).show();

        }



    }

    private void AfterSave() {
        Toast.makeText(curView.getContext(),"The patient details have been saved", Toast.LENGTH_LONG).show();
        saveBtn.setText("Update");
    }
}