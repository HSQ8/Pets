/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.pets.data.PetContract;
import com.example.android.pets.data.PetDBHelper;

import static com.example.android.pets.data.PetContract.PetEntry.COLUMN_NAME_BREED;
import static com.example.android.pets.data.PetContract.PetEntry.COLUMN_NAME_GENDER;
import static com.example.android.pets.data.PetContract.PetEntry.COLUMN_NAME_ID;
import static com.example.android.pets.data.PetContract.PetEntry.COLUMN_NAME_NAME;
import static com.example.android.pets.data.PetContract.PetEntry.COLUMN_NAME_WEIGHT;
import static com.example.android.pets.data.PetContract.PetEntry.TABLE_NAME;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        displayDatabaseInfo();
        //PetDBHelper mDBhelper = new PetDBHelper( this);

    }

    private void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        PetDBHelper mDbHelper = new PetDBHelper(this);

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
        String[] columns = {COLUMN_NAME_ID, COLUMN_NAME_NAME, COLUMN_NAME_BREED, COLUMN_NAME_GENDER, COLUMN_NAME_WEIGHT};
        String[] selection;
        String[] SelectionArgs;
        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);
        String names = "";
        while (cursor.moveToNext()) {

            names += cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ID)) +
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NAME)) +
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_BREED)) +
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME_GENDER)) +
                    cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_WEIGHT)) +
                    "\n";
        }
        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            TextView displayView = (TextView) findViewById(R.id.text_view_pet);
            displayView.setText(names
            );
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPet();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                //onDeletePet();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onDeletePet() {
        String deleteCMD = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        PetDBHelper dbHelper = new PetDBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(deleteCMD);
        Toast.makeText(this, TABLE_NAME + " has been deleted", Toast.LENGTH_SHORT).show();
        displayDatabaseInfo();
    }

    private void insertPet() {
        PetDBHelper dbHelper = new PetDBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(BaseColumns._ID, 1);
        values.put(PetContract.PetEntry.COLUMN_NAME_NAME, "Garfield");
        values.put(PetContract.PetEntry.COLUMN_NAME_BREED, "Tabby");
        values.put(PetContract.PetEntry.COLUMN_NAME_GENDER, PetContract.PetEntry.GENDER_MALE);
        values.put(PetContract.PetEntry.COLUMN_NAME_WEIGHT, 7);

        db.insert(TABLE_NAME, null, values);
        displayDatabaseInfo();

    }

}
