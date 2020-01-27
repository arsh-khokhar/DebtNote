package com.robotraccoons.debtnote.presentation;

import android.content.Intent;
import android.content.Context;
import android.database.DataSetObserver;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import android.support.v7.app.AppCompatActivity;

import com.robotraccoons.debtnote.R;

public class TransactionListAdapter extends AppCompatActivity implements ListAdapter {
    ArrayList<String[]> arrayList;
    Context context;

    public TransactionListAdapter(Context context, ArrayList<String[]> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
    @Override
    public boolean isEnabled(int position) {
        return true;
    }
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public int getCount() {
        return arrayList != null ? arrayList.size() : 0;
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("TESTING: ", Arrays.toString(arrayList.get(position)));
        final String[] subjectData = arrayList.get(position);
        System.out.println("called view");
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.list_row, null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TransactionInfoActivity.class);
                    intent.putExtra("transaction", subjectData[2]);
                    intent.putExtra("user", subjectData[3]);
                    context.startActivity(intent); //go to TransactionInfo page
                    finish();   //close the activity.
                }
            });
            TextView title = convertView.findViewById(R.id.name);
            title.setText(subjectData[0]);
            TextView title2 = convertView.findViewById(R.id.name2);
            title2.setText(subjectData[1]);
            TextView title3 = convertView.findViewById(R.id.name3);
            title3.setText(subjectData[5]);
            TextView title4 = convertView.findViewById(R.id.oweText);
            title4.setText(subjectData[4]);
            title3.setTextColor(Integer.parseInt(subjectData[6]));
            title4.setTextColor(Integer.parseInt(subjectData[6]));

        }
        return convertView;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getViewTypeCount() {
        return arrayList != null ? arrayList.size() == 0 ? 1 : arrayList.size() : 1;
    }
    @Override
    public boolean isEmpty() {
        return arrayList.size() == 0;
    }
}
