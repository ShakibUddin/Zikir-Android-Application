package com.sakibuddinbhuiyan.zikir.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sakibuddinbhuiyan.zikir.utils.PublicVariables;
import com.sakibuddinbhuiyan.zikir.R;
import com.sakibuddinbhuiyan.zikir.database.DatabaseHandler;

import static com.sakibuddinbhuiyan.zikir.utils.PublicVariables.numberMap;

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
    private Vibrator vibrator;

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

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        favourite = (ImageButton) findViewById(R.id.boookmarkButton);
        databaseHandler = new DatabaseHandler(getApplicationContext(),DatabaseHandler.DATABASE_NAME,null,DatabaseHandler.DATABASE_VERSION);

        readPageContent = (TextView) findViewById(R.id.readPageContent);
        readPageTotal = (TextView) findViewById(R.id.readpageTotalRead);
        fingerPrintButton = (ImageButton) findViewById(R.id.fingerPrintButton);

        Intent recyclerAdapterIntent = getIntent();

        String zikir = recyclerAdapterIntent.getStringExtra("mContent");
        String zikirBangla = recyclerAdapterIntent.getStringExtra("mContentBangla");
        if(PublicVariables.selectedLanguage.equals("English")){
            readPageContent.setText(zikir);
        }
        else{
            readPageContent.setText(zikirBangla);
        }
        idOfZikir = databaseHandler.getIdOfZikir(zikir);
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
                if(PublicVariables.vibrate == 1){
                    vibrator.vibrate(100);
                }
                ++clickedToday;
                ++clickedNow;
                if(PublicVariables.selectedLanguage.equals("English")){
                    readPageTotal.setText(String.valueOf(clickedNow));
                }
                else{
                    readPageTotal.setText(getBanglaNumber(clickedNow));
                }
            }
        });

    }
    String getBanglaNumber(int englishNumber){
        StringBuilder banglaNumber = new StringBuilder();
        for(Character character:String.valueOf(englishNumber).toCharArray()){
            banglaNumber.append(numberMap.get(character));
        }
        return banglaNumber.toString();
    }
}