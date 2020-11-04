package com.sakibuddinbhuiyan.zikir.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.sakibuddinbhuiyan.zikir.PublicVariables;
import com.sakibuddinbhuiyan.zikir.R;
import com.sakibuddinbhuiyan.zikir.database.DatabaseHandler;
import com.sakibuddinbhuiyan.zikir.database.Zikir;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FragmentSettings extends Fragment {
    private Spinner languages;
    private SwitchMaterial vibrate;
    private RadioGroup mode;
    private RadioButton light;
    private RadioButton dark;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        languages = (Spinner)rootView.findViewById(R.id.languages);
        vibrate = (SwitchMaterial) rootView.findViewById(R.id.vibrate);
        mode = (RadioGroup) rootView.findViewById(R.id.mode);
        light = (RadioButton) rootView.findViewById(R.id.lightMode);
        dark = (RadioButton) rootView.findViewById(R.id.darkMode);
        light.setChecked(true);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(),
                android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.languages));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languages.setAdapter(adapter);

        languages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PublicVariables.selectedLanguage = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        vibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                PublicVariables.vibrate = b;
            }
        });

        mode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(light.isChecked()){
                    PublicVariables.light = true;
                    PublicVariables.dark = false;
                }
                else{
                    PublicVariables.dark = true;
                    PublicVariables.light = false;
                }
            }
        });

        return rootView;
    }
}
