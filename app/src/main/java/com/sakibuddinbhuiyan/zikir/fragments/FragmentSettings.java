package com.sakibuddinbhuiyan.zikir.fragments;

import android.graphics.Color;
import android.os.Build;
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
import android.widget.Toast;

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
    private DatabaseHandler databaseHandler;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        languages = (Spinner)rootView.findViewById(R.id.languages);
        vibrate = (SwitchMaterial) rootView.findViewById(R.id.vibrate);

        databaseHandler = new DatabaseHandler(getActivity(),DatabaseHandler.DATABASE_NAME,null,DatabaseHandler.DATABASE_VERSION);

        //setting settings state according to saved status
        if(PublicVariables.selectedLanguage.equals("English")){
            languages.setSelection(0);
        }
        else{
            languages.setSelection(1);
        }

        if(PublicVariables.vibrate == 1){
            vibrate.setChecked(true);
        }
        else{
            vibrate.setChecked(false);
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(),
                android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.languages));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languages.setAdapter(adapter);

        languages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PublicVariables.selectedLanguage = adapterView.getItemAtPosition(i).toString();
                //updateLanguageStatusInDatabase(PublicVariables.selectedLanguage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        vibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    PublicVariables.vibrate = 1;
                    databaseHandler.updateVibrateStatusInDatabase(1);
                }
                else{
                    PublicVariables.vibrate = 0;
                    databaseHandler.updateVibrateStatusInDatabase(0);
                }
            }
        });

        return rootView;
    }
}
