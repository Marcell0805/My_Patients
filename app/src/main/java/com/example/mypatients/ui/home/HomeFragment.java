package com.example.mypatients.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.print.PrintManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import com.example.mypatients.PrinterClass;
import com.example.mypatients.R;
import com.example.mypatients.databinding.FragmentHomeBinding;
import com.example.mypatients.ui.Ivf_Calculator;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private FragmentHomeBinding binding;
    private Button printbtn,calcIvfBtn,genTimeBtn;
    private Spinner startTimeSpin,endTimeSpin;
    private EditText dateOfBirthTxt,roomTxt,dateNowTxt,pName,pSname,dName,dSname,dietTxt,ccTxt,intakeTxt,outTxt,stoolTxt,urineTxt,ngtTxt;
    private TextView ageTxt;
    private View curView;
    private TableLayout timeGrid;
    private PrintManager printManager;
    private RadioButton ivfRadioBtn,vsqRadioBtn,nevRadioBtn,medRadioBtn,noIvfRadioBtn,noVsqRadioBtn,noNevRadioBtn,noMedRadioBtn;
    private RadioGroup ivfRadioGroup,vsqRadioGroup, nebRadioGroup,medRadioGroup;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        binding=FragmentHomeBinding.inflate(inflater);
        SetListeners(root);
        /*
        If Testing Uncomment below method
         */
        setTestValues();

        curView=root;
        printManager = (PrintManager) getActivity()
                .getSystemService(root.getContext().PRINT_SERVICE);
        SetDefaultValues(root);

        //Patient patient= new Patient();
        //binding.setPatients(patient);
        return root;
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
        intakeTxt.setText("20");
        outTxt.setText("30");
        stoolTxt.setText("40");
        urineTxt.setText("20");
        ngtTxt.setText("30");
    }

    private void SetListeners(View root)
    {
        startTimeSpin=root.findViewById(R.id.startTime);
        endTimeSpin=root.findViewById(R.id.endTime);
        printbtn=root.findViewById(R.id.printBtn);
        calcIvfBtn= root.findViewById(R.id.ivfCalcBtn);
        genTimeBtn = root.findViewById(R.id.tableBtn);
        dateOfBirthTxt= root.findViewById(R.id.dateBirthText);
        ageTxt=root.findViewById(R.id.ageText);
        timeGrid = root.findViewById(R.id.timeTableLayout);
        roomTxt=root.findViewById(R.id.roomNoText);
        dateNowTxt=root.findViewById(R.id.nowDate);
        pName=root.findViewById(R.id.nameText);
        pSname=root.findViewById(R.id.surnameText);
        dateOfBirthTxt=root.findViewById(R.id.dateBirthText);
        dName=root.findViewById(R.id.doctorName);
        dSname=root.findViewById(R.id.doctorSurname);
        dietTxt=root.findViewById(R.id.dietText);
        ccTxt=root.findViewById(R.id.ccText);
        intakeTxt=root.findViewById(R.id.intakeTxt);
        outTxt=root.findViewById(R.id.outTakeTxt);
        stoolTxt=root.findViewById(R.id.stoolTxt);
        urineTxt=root.findViewById(R.id.urineTxt);
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
        noIvfRadioBtn=root.findViewById(R.id.hasIvfRadio);
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
                //Bitmap a=createBitmapFromView(view);
                //PrintDetails();
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
        calcIvfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showIvfCal(view);
            }
        });
        genTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartGenerateTable(view);
            }
        });
    }

    private boolean hasTable() {
        if(timeGrid.getChildCount()>0)
            return true;
        else
            return false;
    }

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
                if (!charSequence.toString().equals(current)) {
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
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        //int day  = Integer.parseInt(clean.substring(0,2));
                        //int mon  = Integer.parseInt(clean.substring(2,4));
                        //int year = Integer.parseInt(clean.substring(4,8));
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


    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(),"Click event fired", Toast.LENGTH_SHORT).show();

    }
    private void PrintDetails()
    {
        PrinterClass printerClass= new PrinterClass();
        List<String> normalDetails=setPrintDetails();
        //String[] tableDetails=null;
        String[][] tableDetails=setPrintForTblDetails();
        printerClass.printDocument(curView,printManager,normalDetails,tableDetails);

    }

    private  String[][] setPrintForTblDetails()
    {
        //Sets single array for row data
        //Count for the first array size
        int rowCount =timeGrid.getChildCount();
        //Setting single array
        String[] tableValues= new String[rowCount];
        for (int i=0;i<rowCount;i++)
        {
            TableRow tableRow=(TableRow)timeGrid.getChildAt(i);
            String[] tableColValues= new String[timeGrid.getChildCount()];
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


        //return tableValues;

        return tableDetails;
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
        col.add(intakeTxt.getText().toString()+" ml-"+intakeTxt.getHint()+":");
        col.add(outTxt.getText().toString()+"ml-"+outTxt.getHint()+":");
        col.add(stoolTxt.getText().toString()+"ml-"+stoolTxt.getHint()+":");
        col.add(urineTxt.getText().toString()+"ml-"+urineTxt.getHint()+":");
        col.add(ngtTxt.getText().toString()+"ml-"+ngtTxt.getHint()+":");
        int ivfId=ivfRadioGroup.getCheckedRadioButtonId();
        int medId=medRadioGroup.getCheckedRadioButtonId();
        int vsqId=vsqRadioGroup.getCheckedRadioButtonId();
        int nebId=nebRadioGroup.getCheckedRadioButtonId();


        if(ivfId==-1)
        {
            //col.add("-:");
        }
        else if(ivfId==ivfRadioBtn.getId())
        {
            col.add("true"+"-"+ivfRadioBtn.getText()+":");
        }
        else
        {
            col.add("true"+"-"+noIvfRadioBtn.getText()+":");
        }
        if(medId==-1)
        {
            //col.add("-:");
        }
        else if(medId==medRadioBtn.getId())
        {
            col.add("true"+"-"+medRadioBtn.getText()+":");
        }
        else
        {
            col.add("true"+"-"+noMedRadioBtn.getText()+":");
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
        return col;
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
        GenerateTable(view,rowsTimeValue);
    }

    private void GenerateTable(View view,List<String>rowsTimeValue)
    {
        CheckIfRows();
        ShapeDrawable sd = new ShapeDrawable();
        ShapeDrawable sdCol = new ShapeDrawable();
        ShapeDrawable inputSd = new ShapeDrawable();
        ShapeDrawable input2Sd = new ShapeDrawable();
        setShapeDetails(sd,view,sdCol,inputSd,input2Sd);
        int SizeWithHeading=rowsTimeValue.size()+1;
        TableRow[] rws=new TableRow[SizeWithHeading];
        TextView[] textView=new TextView[SizeWithHeading];
        EditText[] editTxt=new EditText[5];

        for (int i=0;i<SizeWithHeading;i++)
        {
            rws[i]=new TableRow(view.getContext());
            if(i==0)
            {
                TextView[] headTxt=new TextView[6];
                for (int hT=0;hT<headTxt.length;hT++)
                {
                    headTxt[hT]= new TextView(view.getContext());
                    headTxt[hT].setTextSize(15);
                    headTxt[hT].setTypeface(null, Typeface.BOLD);
                    headTxt[hT].setGravity(Gravity.LEFT);
                }
                headTxt[0].setText(getResources().getString(R.string.timeTblHead));
                headTxt[1].setText(getResources().getString(R.string.prTblHead));
                headTxt[2].setText(getResources().getString(R.string.tempTblHead));
                headTxt[3].setText(getResources().getString(R.string.bpTblHead));
                headTxt[4].setText(getResources().getString(R.string.ivfTblHead));
                headTxt[5].setText(getResources().getString(R.string.o2TblHead));
                for (int h=0;h<headTxt.length;h++)
                {
                    rws[i].setBackground(sd);
                    rws[i].setGravity(Gravity.CENTER);
                    rws[i].addView(headTxt[h]);
                }
                //rws[i].setWeightSum(6);
                timeGrid.addView(rws[i]);
            }
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
                //sd.getPaint().setColor(ContextCompat.getColor(view.getContext(), R.color.tableColor));
                //extView[i].setBackgroundColor(getResources().getColor(R.color.tableColor));
                rws[i].addView(textView[i]);
                for (int c=0;c<5;c++)
                {
                    editTxt[c]= new EditText(view.getContext());
                    editTxt[c].setInputType(InputType.TYPE_CLASS_NUMBER);
                    editTxt[c].setHeight(150);
                    editTxt[c].setWidth(215);
                    editTxt[c].setGravity(Gravity.CENTER);
                    if(c==1||c==3)
                        editTxt[c].setBackground(inputSd);
                    else
                        editTxt[c].setBackground(input2Sd);
                    rws[i].addView(editTxt[c]);
                }
                timeGrid.addView(rws[i]);
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

    private void CheckIfRows()
    {
        int count=timeGrid.getChildCount();
        if(count>0)
        {
            timeGrid.removeAllViews();
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


    public @NonNull static Bitmap createBitmapFromView(@NonNull View view)
    {

        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        view.measure(View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED));
        view.layout(0,0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap a=Bitmap.createBitmap(view.getMeasuredHeight(),view.getMeasuredWidth(), Bitmap.Config.ARGB_8888);
        Bitmap bitmap= Bitmap.createBitmap(view.getWidth(),view.getHeight(),Bitmap.Config.ARGB_8888);

        return bitmap;
    }
}