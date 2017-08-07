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

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.pets.data.PetContract;

import static com.example.android.pets.data.PetContract.PetEntry.COLUMN_NAME_BREED;
import static com.example.android.pets.data.PetContract.PetEntry.COLUMN_NAME_GENDER;
import static com.example.android.pets.data.PetContract.PetEntry.COLUMN_NAME_ID;
import static com.example.android.pets.data.PetContract.PetEntry.COLUMN_NAME_NAME;
import static com.example.android.pets.data.PetContract.PetEntry.COLUMN_NAME_WEIGHT;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {
    public final static String Intent_Name = "IntentName";
    public final static String Intent_Breed = "IntentBreed";
    public final static String Intent_Weight = "IntentWeight";
    public final static String Intent_Gender = "IntentGender";
    public final static String Intent_URI = "pet to be edited URI";
    private PetsCursorAdapter mAdapter;
    //private SimpleCursorAdapter mAdapter;
    private static final int PET_LOADER = 0;

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

        /*Uri uri = PetContract.PetEntry.CONTENT_URI;
        String[] columns = {COLUMN_NAME_ID, COLUMN_NAME_NAME, COLUMN_NAME_BREED, COLUMN_NAME_GENDER, COLUMN_NAME_WEIGHT};
        Cursor petscursor = getContentResolver().query( uri, columns,null,null,null);
        // Find the ListView which will be populated with the pet data*/

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        ListView petslistview = (ListView) findViewById(R.id.petsScrollable);
        //sets empty view for the empty cursor
        View emptyView = findViewById(R.id.empty_view);
        petslistview.setEmptyView(emptyView);

        mAdapter = new PetsCursorAdapter(this, null);
        petslistview.setAdapter(mAdapter);

        petslistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = ContentUris.withAppendedId(PetContract.PetEntry.CONTENT_URI, position + 1);
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                intent.putExtra(Intent_URI, uri.toString());
                startActivity(intent);

            }
        });

        getLoaderManager().initLoader(PET_LOADER, null, new android.app.LoaderManager.LoaderCallbacks<Cursor>() {

            @Override
            public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
                String[] columns = {
                        COLUMN_NAME_ID,
                        COLUMN_NAME_NAME,
                        COLUMN_NAME_BREED,
                        COLUMN_NAME_GENDER,
                        COLUMN_NAME_WEIGHT};

                return new CursorLoader(
                        getBaseContext(),
                        PetContract.PetEntry.CONTENT_URI,
                        columns,
                        null,
                        null,
                        null);
            }

            @Override
            public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
                mAdapter.swapCursor(data);
            }

            @Override
            public void onLoaderReset(android.content.Loader<Cursor> loader) {
                mAdapter.swapCursor(null);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
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
                onDeletePet();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onDeletePet() {
        int rowsdeleted = getContentResolver().delete(
                PetContract.PetEntry.CONTENT_URI,
                null,
                null);
    }


    private void insertPet() {
        Uri uri = PetContract.PetEntry.CONTENT_URI;

        ContentValues values = new ContentValues();
        //values.put(BaseColumns._ID, 1);
        values.put(COLUMN_NAME_NAME, "Garfield");
        values.put(COLUMN_NAME_BREED, "Tabby");
        values.put(COLUMN_NAME_GENDER, PetContract.PetEntry.GENDER_MALE);
        values.put(COLUMN_NAME_WEIGHT, 20);
        getContentResolver().insert(uri, values);
    }

}
