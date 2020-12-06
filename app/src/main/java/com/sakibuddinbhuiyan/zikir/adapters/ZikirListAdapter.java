package com.sakibuddinbhuiyan.zikir.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.sakibuddinbhuiyan.zikir.R;
import com.sakibuddinbhuiyan.zikir.activities.Read;
import com.sakibuddinbhuiyan.zikir.database.DatabaseHandler;
import com.sakibuddinbhuiyan.zikir.utils.PublicVariables;

import java.util.LinkedList;

import static com.sakibuddinbhuiyan.zikir.utils.PublicVariables.*;

public class ZikirListAdapter extends RecyclerView.Adapter<ZikirListAdapter.ZikrViewHolder> {

    private LayoutInflater mInflater;
    private LinkedList<String> mZikrList;
    private LinkedList<Integer> mTodayList;
    private LinkedList<Integer> mTotalList;
    private LinkedList<Integer> mFavouriteList;
    private DatabaseHandler databaseHandler;


    public ZikirListAdapter(Context context, LinkedList<String> zikrList, LinkedList<Integer> todayList, LinkedList<Integer> totalList, LinkedList<Integer> favouriteList) {
        mInflater = LayoutInflater.from(context);
        this.mZikrList = zikrList;
        this.mTodayList = todayList;
        this.mTotalList = totalList;
        this.mFavouriteList = favouriteList;

    }

    @NonNull
    @Override
    public ZikirListAdapter.ZikrViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.row_item_layout,
                parent, false);
        return new ZikrViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull final ZikirListAdapter.ZikrViewHolder holder, final int position) {
        final String mContent = PublicVariables.zikrList.get(position);
        final String mContentBangla = PublicVariables.zikrBanglaList.get(position);
        final Integer mTotal = PublicVariables.totalList.get(position);
        final Integer mToday = PublicVariables.todayList.get(position);
        final Integer mFavourite = PublicVariables.favouriteList.get(position);
        final int idOfZikir = databaseHandler.getIdOfZikir(mContent);

        if(selectedLanguage.equals("English")){
            holder.contentTextView.setText(mContent);
        }
        else{
            holder.contentTextView.setText(mContentBangla);
        }

        if(selectedLanguage.equals("English")){
            holder.todayTextView.setText("Today: " + mToday);
            holder.totalTextView.setText("Total: " + mTotal);
            holder.read.setText("Read");
        }
        else{
            String mTodayBangla = getBanglaNumber(mToday);
            String mTotalBangla = getBanglaNumber(mTotal);
            holder.todayTextView.setText("আজ: " + mTodayBangla);
            holder.totalTextView.setText("মোট: " + mTotalBangla);
            holder.read.setText("পড়ুন");
        }

        if (mFavourite == 1) {
            holder.favourite.setImageResource(R.drawable.content_favourite_filled_icon);
            holder.favourited = 1;
        } else {
            holder.favourite.setImageResource(R.drawable.content_favourite_outline_icon);
            holder.favourited = 0;
        }

        holder.read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Read.class);
                intent.putExtra("mContent", mContent);
                intent.putExtra("mContentBangla", mContentBangla);
                intent.putExtra("mTotal", mTotal);
                intent.putExtra("mToday", mToday);
                intent.putExtra("position", position);
                intent.putExtra("favourited", holder.favourited);
                v.getContext().startActivity(intent);
            }
        });

        holder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.favourited == 1) {
                    holder.favourite.setImageResource(R.drawable.content_favourite_outline_icon);
                    holder.favourited = 0;
                    databaseHandler.updateFavourite(idOfZikir, holder.favourited);
                    //update zikir to database as not favourited
                } else {
                    holder.favourite.setImageResource(R.drawable.content_favourite_filled_icon);
                    holder.favourited = 1;
                    databaseHandler.updateFavourite(idOfZikir, holder.favourited);
                    //update zikir to database as favourited
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return zikrList.size();
    }

    class ZikrViewHolder extends RecyclerView.ViewHolder {

        public final TextView contentTextView;
        public final TextView todayTextView;
        public final TextView totalTextView;
        public final ImageButton favourite;
        public final Button read;
        public int favourited = 0;
        final ZikirListAdapter mAdapter;


        public ZikrViewHolder(@NonNull View itemView, ZikirListAdapter mAdapter) {
            super(itemView);

            contentTextView = itemView.findViewById(R.id.content);
            todayTextView = itemView.findViewById(R.id.todayRead);
            totalTextView = itemView.findViewById(R.id.totalRead);
            favourite = itemView.findViewById(R.id.boookmarkButton);
            read = itemView.findViewById(R.id.read);

            databaseHandler = new DatabaseHandler(itemView.getContext().getApplicationContext(), DatabaseHandler.DATABASE_NAME, null, DatabaseHandler.DATABASE_VERSION);
            this.mAdapter = mAdapter;
        }
    }

    String getBanglaNumber(int englishNumber){
        StringBuilder banglaNumber = new StringBuilder();
        for(Character character:String.valueOf(englishNumber).toCharArray()){
            banglaNumber.append(numberMap.get(character));
        }
        return banglaNumber.toString();
    }
}
