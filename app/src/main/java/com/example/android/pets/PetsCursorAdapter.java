package com.example.android.pets;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.pets.data.PetContract;

/**
 * Created by HQ on 8/3/2017.
 */

public class PetsCursorAdapter extends CursorAdapter {
    public PetsCursorAdapter(Context context, Cursor cursor){
        super(context,cursor,0);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.singlepet,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView petnameView = (TextView) view.findViewById(R.id.petnameview);
        TextView petbreedView = (TextView) view.findViewById(R.id.petbreedview);

        String name = cursor.getString(cursor.getColumnIndexOrThrow(
                PetContract.PetEntry.COLUMN_NAME_NAME));
        String breed = cursor.getString(cursor.getColumnIndexOrThrow(
                PetContract.PetEntry.COLUMN_NAME_BREED));

        petnameView.setText(name);
        petbreedView.setText(breed);
    }
}
