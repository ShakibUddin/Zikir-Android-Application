package com.sakibuddinbhuiyan.zikir.fragments;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sakibuddinbhuiyan.zikir.BuildConfig;
import com.sakibuddinbhuiyan.zikir.activities.MainActivity;
import com.sakibuddinbhuiyan.zikir.models.ZikirDowload;
import com.sakibuddinbhuiyan.zikir.utils.PublicVariables;
import com.sakibuddinbhuiyan.zikir.R;
import com.sakibuddinbhuiyan.zikir.database.DatabaseHandler;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

import javax.xml.datatype.Duration;

public class FragmentSettings extends Fragment {
    private Spinner languages;
    private SwitchMaterial vibrate;
    private DatabaseHandler databaseHandler;
    private TextView languageTextView;
    private TextView vibrateTextView;
    private TextView share;
    private TextView rateUs;
    private TextView feedBack;
    private TextView privacyPolicy;
    private Button download;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private int totalDownloaded = 0;

    private static final String TAG = "FragmentSettings";
    private int count=0;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);


        languages = (Spinner)rootView.findViewById(R.id.languages);
        vibrate = (SwitchMaterial) rootView.findViewById(R.id.vibrate);
        languageTextView = (TextView) rootView.findViewById(R.id.languageTextView);
        vibrateTextView = (TextView) rootView.findViewById(R.id.vibrateTextView);
        share = (TextView) rootView.findViewById(R.id.share);
        rateUs = (TextView) rootView.findViewById(R.id.rate);
        feedBack = (TextView) rootView.findViewById(R.id.feedback);
        privacyPolicy = (TextView) rootView.findViewById(R.id.privacyPolicy);
        download = (Button) rootView.findViewById(R.id.download);


        if(PublicVariables.selectedLanguage.equals("English")){

            languageTextView.setText("Language");
            vibrateTextView.setText("Vibrate");
            share.setText("Share");
            rateUs.setText("Rate Us");
            feedBack.setText("Give Feedback");
            privacyPolicy.setText("Privacy Policy");
        }
        else{
            languageTextView.setText("ভাষা");
            vibrateTextView.setText("ভাইব্রেট");
            share.setText("শেয়ার করুন");
            rateUs.setText("রেটিং দিন");
            feedBack.setText("মতামত জানান");
            privacyPolicy.setText("প্রাইভিসি পলিসি");
        }

        databaseHandler = new DatabaseHandler(getActivity(),DatabaseHandler.DATABASE_NAME,null,DatabaseHandler.DATABASE_VERSION);

        languages.setSelected(false);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(),
                android.R.layout.simple_spinner_item,PublicVariables.zikirLanguages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languages.setAdapter(adapter);

        //setting settings state according to saved status
        if(PublicVariables.selectedLanguage.equals("English")){
            languages.setSelection(0);
        }
        else{
            languages.setSelection(1);
        }

        vibrate.setChecked(PublicVariables.vibrate == 1);

        languages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(++count>1){
                    String selected = adapterView.getItemAtPosition(i).toString();
                    PublicVariables.selectedLanguage = selected;
                    databaseHandler.updateLanguageStatusInDatabase(PublicVariables.selectedLanguage);
                    refreshFragment();
                    updateNavigationBarTitle();
                }
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

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareApp();
            }
        });
        rateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateApp();
            }
        });
        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPrivacyPolicy();
            }
        });
        feedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGmail();
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadContents(db);
            }
        });

        return rootView;
    }
    private void downloadContents(FirebaseFirestore db) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Downloading Content");
        progressDialog.show();
        totalDownloaded = 0;
        db.collection("zikirs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                ZikirDowload zikirDownload = document.toObject(ZikirDowload.class);
                                if(!databaseHandler.contentAlreadyExists(zikirDownload)){
                                    ++totalDownloaded;
                                    databaseHandler.addDownloadedZikirinDatabase(zikirDownload);
                                }
                            }
                            progressDialog.dismiss();
                            Toast.makeText(getContext(),totalDownloaded+" New Content(s) Downloaded!",Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(getContext(),"No Internet",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    }
                });
    }

    void refreshFragment(){
        getFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new FragmentSettings()).commit();
    }

    void updateNavigationBarTitle(){
        if(PublicVariables.selectedLanguage.equals("English")){
            MainActivity.bottomNavigationView.getMenu().getItem(0).setTitle("Home");
            MainActivity.bottomNavigationView.getMenu().getItem(1).setTitle("Favourites");
            MainActivity.bottomNavigationView.getMenu().getItem(2).setTitle("Settings");
        }
        else{
            MainActivity.bottomNavigationView.getMenu().getItem(0).setTitle("হোম");
            MainActivity.bottomNavigationView.getMenu().getItem(1).setTitle("ফেভারিটস");
            MainActivity.bottomNavigationView.getMenu().getItem(2).setTitle("সেটিংস");
        }
    }
    void openGmail(){
        String[] sendTo = new String[]{"shakibuddinbhuiyan@gmail.com"};
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, sendTo);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client"));
    }

    void shareApp(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey check out this app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    void rateApp(){
        try{
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id="+getContext().getPackageName())));
        }
        catch (ActivityNotFoundException e){
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id="+getContext().getPackageName())));
        }
    }

    void openPrivacyPolicy(){
        String url = "https://sites.google.com/view/shakibuddinbhuiyan/zikir";

        // Parse the URI and create the intent.
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

        // Find an activity to hand the intent and start that activity.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d("ImplicitIntents", "Can't handle this intent!");
        }
    }
}
