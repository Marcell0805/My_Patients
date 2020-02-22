package com.example.mypatients.ui.home;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.mypatients.R;
import com.example.mypatients.databinding.FragmentHomeBinding;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private FragmentHomeBinding binding;
    private Button printbtn,calcIvfBtn,genTimeBtn;
    private Spinner startTimeSpin,endTimeSpin;
    private EditText dateOfBirthTxt, ageTxt;
    private View curView;
    private TableLayout timeGrid;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        binding=FragmentHomeBinding.inflate(inflater);
        SetListeners(root);
        curView=root;
        SetDefaultValues(root);
        //Patient patient= new Patient();
        //binding.setPatients(patient);
        return root;
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
        dateOfBirthTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(dateOfBirthTxt.getText().toString()!=null && dateOfBirthTxt.getText().toString()!="" && dateOfBirthTxt.getText().toString().length()==10)
                {
                    CalculateAge();
                }
            }
        });
        setTxtListener();

        printbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrintDetails(view);
            }
        });
        calcIvfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Nav to IVF calc", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        genTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartGenerateTable(view);
            }
        });
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
    private void PrintDetails(View view)
    {
        Snackbar.make(view, "Print Btn Action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
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
        EditText[] editTxt=new EditText[4];

        for (int i=0;i<SizeWithHeading;i++)
        {
            rws[i]=new TableRow(view.getContext());
            if(i==0)
            {
                TextView[] headTxt=new TextView[5];
                for (int hT=0;hT<headTxt.length;hT++)
                {
                    headTxt[hT]= new TextView(view.getContext());
                    headTxt[hT].setTextSize(15);
                    headTxt[hT].setTypeface(null, Typeface.BOLD);
                    headTxt[hT].setGravity(Gravity.LEFT);
                    headTxt[hT].setWidth(200);
                }
                headTxt[0].setText(getResources().getString(R.string.timeTblHead));
                headTxt[1].setText(getResources().getString(R.string.prTblHead));
                headTxt[2].setText(getResources().getString(R.string.tempTblHead));
                headTxt[3].setText(getResources().getString(R.string.bpTblHead));
                headTxt[4].setText(getResources().getString(R.string.ivfTblHead));
                for (int h=0;h<headTxt.length;h++)
                {
                    rws[i].setBackground(sd);
                    rws[i].setGravity(Gravity.CENTER);
                    rws[i].addView(headTxt[h]);
                }
                rws[i].setWeightSum(5);
                timeGrid.addView(rws[i]);
            }
            else
            {
                textView[i]= new TextView(view.getContext());
                textView[i].setText(rowsTimeValue.get(i-1));
                textView[i].setGravity(Gravity.CENTER);
                textView[i].setWidth(215);
                textView[i].setHeight(130);
                textView[i].setBackground(sdCol);
                //sd.getPaint().setColor(ContextCompat.getColor(view.getContext(), R.color.tableColor));
                //extView[i].setBackgroundColor(getResources().getColor(R.color.tableColor));
                rws[i].addView(textView[i]);
                for (int c=0;c<4;c++)
                {
                    editTxt[c]= new EditText(view.getContext());
                    editTxt[c].setInputType(InputType.TYPE_CLASS_NUMBER);
                    editTxt[c].setHeight(130);
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


    //private HomeViewModel homeViewModel;

    /*public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        FragmentHomeBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
       final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }*/
/*
@Override
public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    FragmentTest binding = DataBindingUtil.inflate(inflater, R.layout.fragment_test, container, false);
    Item item = new Item();
    item.setName("Thomas");
    binding.setItem(item);
    return binding.getRoot();
}
 */
}