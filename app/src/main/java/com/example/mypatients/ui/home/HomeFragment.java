package com.example.mypatients.ui.home;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.text.Editable;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private FragmentHomeBinding binding;
    private Button printbtn,calcIvfBtn,genTimeBtn;
    private Spinner startTimeSpin,endTimeSpin;
    private EditText dateOfBirthTxt;
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
        timeGrid = root.findViewById(R.id.timeTableLayout);
        setDefaultNaming();
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

    private void setDefaultNaming()
    {
        genTimeBtn.setText("CREATE TABLE");
    }

    private void setTxtListener()
    {
        TextWatcher tw = new TextWatcher() {
            private String current = "";
            private String dateFormat = "DDMMYYYY";
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
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

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
        setShapeDetails(sd,view);
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
                textView[i].setWidth(200);
                textView[i].setBackgroundColor(getResources().getColor(R.color.tableColor));

                rws[i].addView(textView[i]);
                for (int c=0;c<4;c++)
                {
                    editTxt[c]= new EditText(view.getContext());
                    editTxt[c].setHint("");
                    editTxt[c].setWidth(200);
                    rws[i].addView(editTxt[c]);
                }
                rws[i].setWeightSum(5);
                timeGrid.addView(rws[i]);
            }
        }
        genTimeBtn.setText("RECREATE TABLE");
    }

    private void setShapeDetails(ShapeDrawable sd,View view)
    {
        // Specify the shape of ShapeDrawable
        sd.setShape(new RectShape());
        // Specify the border color of shape
        sd.getPaint().setColor(ContextCompat.getColor(view.getContext(), R.color.tableHeading));
        // Set the border width
        sd.getPaint().setStrokeWidth(5f);
        // Specify the style is a Stroke
        sd.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);
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