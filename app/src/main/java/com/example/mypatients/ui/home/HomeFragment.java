package com.example.mypatients.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mypatients.R;
import com.example.mypatients.databinding.FragmentHomeBinding;
import com.example.mypatients.models.Patient;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private FragmentHomeBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
      binding=FragmentHomeBinding.inflate(inflater);
        Patient patient= new Patient();
        //binding.setPatients(patient);

        return binding.getRoot();
    }
    public void SetListeners()
    {
        //EditText nameText= SOCL
    }

    @Override
    public void onClick(View view) {
        //EditText a =view.findViewById(R.id.nameText).setOnClickListener();
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