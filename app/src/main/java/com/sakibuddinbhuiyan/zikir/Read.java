package com.sakibuddinbhuiyan.zikir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sakibuddinbhuiyan.zikir.database.DatabaseHandler;

public class Read extends AppCompatActivity {

    private TextView readPageContent;
    private TextView readPageTotal;
    private ImageButton fingerPrintButton;
    private ImageButton favourite;
    private int position;
    private int idOfZikir;
    private int favourited = 0;
    private int clickedToday = 0;
    private int clickedNow = 0;
    private DatabaseHandler databaseHandler;

    @Override
    public void onBackPressed() {
        databaseHandler.incrementToday(clickedToday,idOfZikir);
        databaseHandler.incrementTotal(clickedNow,idOfZikir);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        favourite = (ImageButton) findViewById(R.id.boookmarkButton);
        databaseHandler = new DatabaseHandler(getApplicationContext(),DatabaseHandler.DATABASE_NAME,null,DatabaseHandler.DATABASE_VERSION);

        readPageContent = (TextView) findViewById(R.id.readPageContent);
        readPageTotal = (TextView) findViewById(R.id.readpageTotalRead);
        fingerPrintButton = (ImageButton) findViewById(R.id.fingerPrintButton);

        Intent recyclerAdapterIntent = getIntent();

        String zikir = recyclerAdapterIntent.getStringExtra("mContent");
        idOfZikir = databaseHandler.getIdOfZikir(zikir);
        readPageContent.setText(zikir);
        clickedToday = recyclerAdapterIntent.getIntExtra("mToday",0);
        readPageTotal.setText(String.valueOf(clickedNow));
        position = recyclerAdapterIntent.getIntExtra("position",0);
        favourited = recyclerAdapterIntent.getIntExtra("favourited",0);

        if(favourited == 1){
            favourite.setImageResource(R.drawable.content_favourite_filled_icon);
        }
        else{
            favourite.setImageResource(R.drawable.content_favourite_outline_icon);
        }

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(favourited == 1){
                    favourite.setImageResource(R.drawable.content_favourite_outline_icon);
                    favourited = 0;
                    databaseHandler.updateFavourite(idOfZikir,favourited);
                    //update zikir to database as not favourited
                }
                else{
                    favourite.setImageResource(R.drawable.content_favourite_filled_icon);
                    favourited = 1;
                    databaseHandler.updateFavourite(idOfZikir,favourited);
                    //update zikir to database as favourited
                }
            }
        });

        fingerPrintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++clickedToday;
                ++clickedNow;
                readPageTotal.setText(String.valueOf(clickedNow));
            }
        });

    }
}