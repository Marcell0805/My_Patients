package com.example.mypatients.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mypatients.R;
import com.example.mypatients.databinding.FragmentHomeBinding;
import com.example.mypatients.models.Patient;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private FragmentHomeBinding binding;
    private Button printbtn,calcIvfBtn,genTimeBtn;
    private Spinner startTimeSpin,endTimeSpin;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        binding=FragmentHomeBinding.inflate(inflater);
        SetListeners(root);
        //Patient patient= new Patient();
        //binding.setPatients(patient);
        return root;
    }
    private void SetListeners(View root)
    {
        printbtn=root.findViewById(R.id.printBtn);
        calcIvfBtn= root.findViewById(R.id.ivfCalcBtn);
        genTimeBtn = root.findViewById(R.id.tableBtn);

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
                GenerateTable(view);
            }
        });

        /*printbtn= (inflater.inflate(R.layout.fragment_home, container, false).findViewById(R.id.printBtn));
        printbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        /*calcIvfBtn= (inflater.inflate(R.layout.fragment_home, container, false).findViewById(R.id.ivfCalcBtn));
        calcIvfBtn.setOnClickListener(this);
        genTimeBtn= (inflater.inflate(R.layout.fragment_home, container, false).findViewById(R.id.tableBtn));
        genTimeBtn.setOnClickListener(this);*/
        /*startTimeSpin= (inflater.inflate(R.layout.fragment_home, container, false).findViewById(R.id.startTime));
        startTimeSpin.setOnClickListener(this);
        endTimeSpin= (inflater.inflate(R.layout.fragment_home, container, false).findViewById(R.id.endTime));
        startTimeSpin.setOnClickListener(this);*/
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
    private void GenerateTable(View view)
    {
        Snackbar.make(view, "Table", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
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