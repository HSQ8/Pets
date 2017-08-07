package com.example.android.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.example.android.pets.data.PetContract.PetEntry.TABLE_NAME;

/**
 * Created by HQ on 7/31/2017.
 */

public class PetProvider extends ContentProvider {
    public static final String LOG_TAG = PetProvider.class.getSimpleName();
    private static final int PETS = 100;

    /**
     * URI matcher code for the content URI for a single pet in the pets table
     */
    private static final int PET_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_PETS, PETS);
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_PETS + "/#", PET_ID);
    }

    PetDBHelper mDBHelper;

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return PetContract.PetEntry.CONTENT_LIST_TYPE;
            case PET_ID:
                return PetContract.PetEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("uri not recognized");
        }

    }

    @Override
    public boolean onCreate() {
        mDBHelper = new PetDBHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mDBHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                cursor = database.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PET_ID:
                selection = PetContract.PetEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("cannot query unknown URI");
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case PETS:
                //update multiple lines

                return updatePet(uri, values, selection, selectionArgs);
            case PET_ID:
                //update a single line
                selection = PetContract.PetEntry._ID + " =? ";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.size() == 0)
            return 0;
        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        if (values.containsKey(PetContract.PetEntry.COLUMN_NAME_NAME)) {
            String name = values.getAsString(PetContract.PetEntry.COLUMN_NAME_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Pet requires a name");
            }
        }
        if (values.containsKey(PetContract.PetEntry.COLUMN_NAME_GENDER)) {
            int gender = values.getAsInteger(PetContract.PetEntry.COLUMN_NAME_GENDER);
            if (!(gender == PetContract.PetEntry.GENDER_FEMALE ||
                    gender == PetContract.PetEntry.GENDER_MALE ||
                    gender == PetContract.PetEntry.GENDER_UNKNOWN)) {
                throw new IllegalArgumentException("Pet requires a gender of one of three types");
            }
        }
        if (values.containsKey(PetContract.PetEntry.COLUMN_NAME_WEIGHT)) {
            int weight = values.getAsInteger(PetContract.PetEntry.COLUMN_NAME_WEIGHT);
            if (weight < 0) {
                throw new IllegalArgumentException("Pet weight must be greater than 0");
            }
        }
        if (values.containsKey(PetContract.PetEntry.COLUMN_NAME_WEIGHT)) {
            String breed = values.getAsString(PetContract.PetEntry.COLUMN_NAME_BREED);
            if (breed == null) {
                throw new IllegalArgumentException("Pet requires a breed");
            }
        }
        int id = database.update(PetContract.PetEntry.TABLE_NAME, values, selection, selectionArgs);
        if (id == 0) {
            Log.v("update", "Update failed");
            throw new IllegalArgumentException();
        }
        if (id != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return id;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return insertPet(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

   /* @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return deletePet(uri, selection, selectionArgs);
            case PET_ID:
                selection = PetContract.PetEntry._ID + " =? ";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return deletePet(uri, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    private int deletePet(Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        int rowsDeleted = database.delete(PetContract.PetEntry.TABLE_NAME, selection, selectionArgs);
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }
*/
   @Override
   public int delete(Uri uri, String selection, String[] selectionArgs) {
       // Get writeable database
       SQLiteDatabase database = mDBHelper.getWritableDatabase();

       
       int rowsDeleted;

       final int match = sUriMatcher.match(uri);
       switch (match) {
           case PETS:
               // Delete all rows that match the selection and selection args
               rowsDeleted = database.delete(PetContract.PetEntry.TABLE_NAME, selection, selectionArgs);
               break;
           case PET_ID:
               // Delete a single row given by the ID in the URI
               selection = PetContract.PetEntry._ID + "=?";
               selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
               rowsDeleted = database.delete(PetContract.PetEntry.TABLE_NAME, selection, selectionArgs);
               break;
           default:
               throw new IllegalArgumentException("Deletion is not supported for " + uri);
       }

       // If 1 or more rows were deleted, then notify all listeners that the data at the
       // given URI has changed
       if (rowsDeleted != 0) {
           getContext().getContentResolver().notifyChange(uri, null);
       }

       // Return the number of rows deleted
       return rowsDeleted;
   }
    private Uri insertPet(Uri uri, ContentValues values) {
        String name = values.getAsString(PetContract.PetEntry.COLUMN_NAME_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Pet requires a name");
        }

        int gender = values.getAsInteger(PetContract.PetEntry.COLUMN_NAME_GENDER);
        if (!(gender == PetContract.PetEntry.GENDER_FEMALE ||
                gender == PetContract.PetEntry.GENDER_MALE ||
                gender == PetContract.PetEntry.GENDER_UNKNOWN)) {
            throw new IllegalArgumentException("Pet requires a gender of one of three types");
        }

        int weight = values.getAsInteger(PetContract.PetEntry.COLUMN_NAME_WEIGHT);
        if (weight < 0) {
            throw new IllegalArgumentException("Pet weight must be greater than 0");
        }

        String breed = values.getAsString(PetContract.PetEntry.COLUMN_NAME_BREED);
        if (breed == null) {
            throw new IllegalArgumentException("Pet requires a breed");
        }

        SQLiteDatabase database = mDBHelper.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(PetContract.PetEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }
}
