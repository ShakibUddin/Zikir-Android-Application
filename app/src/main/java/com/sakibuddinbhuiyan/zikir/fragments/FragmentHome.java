package com.sakibuddinbhuiyan.zikir.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sakibuddinbhuiyan.zikir.activities.MainActivity;
import com.sakibuddinbhuiyan.zikir.utils.PublicVariables;
import com.sakibuddinbhuiyan.zikir.R;
import com.sakibuddinbhuiyan.zikir.adapters.ZikirListAdapter;
import com.sakibuddinbhuiyan.zikir.database.DatabaseHandler;
import com.sakibuddinbhuiyan.zikir.models.Zikir;

import java.text.SimpleDateFormat;
import java.util.Date;


public class FragmentHome extends Fragment {
    private RecyclerView mRecyclerView;
    private ZikirListAdapter mAdapter;
    private int i = 1;
    private DatabaseHandler databaseHandler;
    private String savedDate = "";
    private String today = "";
    private String TAG = "MainActivity";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        databaseHandler = new DatabaseHandler(getContext(), DatabaseHandler.DATABASE_NAME, null, DatabaseHandler.DATABASE_VERSION);

        //get saved date
        savedDate = databaseHandler.getDate();
        //get todays date
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        today = formatter.format(date);
        Log.d(TAG, "today date: " + today);
        if (!savedDate.equals(formatter.format(date))) {
            //setting READ_TODAY column to 0
            databaseHandler.refreshTodaysColumn();
            //updating date in Date table
            databaseHandler.updateDate(String.valueOf(today));
        }
        // Get a handle to the RecyclerView.
        mRecyclerView = rootView.findViewById(R.id.recycleView);
        //clearing data
        PublicVariables.zikrList.clear();
        PublicVariables.zikrBanglaList.clear();
        PublicVariables.todayList.clear();
        PublicVariables.totalList.clear();
        PublicVariables.favouriteList.clear();

        PublicVariables.zikirObjList = databaseHandler.getAllZikirData();

        for (Zikir zikirObj : PublicVariables.zikirObjList) {
            PublicVariables.zikrList.add(zikirObj.zikir);
            PublicVariables.zikrBanglaList.add(zikirObj.zikirBangla);
            PublicVariables.todayList.add(zikirObj.readToday);
            PublicVariables.totalList.add(zikirObj.readTotal);
            PublicVariables.favouriteList.add(zikirObj.favourite);
        }
        // Create an adapter and supply the data to be displayed.
        mAdapter = new ZikirListAdapter(getContext(), PublicVariables.zikrList, PublicVariables.todayList, PublicVariables.totalList, PublicVariables.favouriteList);
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return rootView;
    }

}
