package com.example.android.pets.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by HQ on 7/24/2017.
 */

public final class PetContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    public static final String CONTENT_AUTHORITY = "com.example.android.pets";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PETS = "pets";

    private PetContract() {}

    /* Inner class that defines the table contents */
    public static class PetEntry implements BaseColumns {



        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PETS);

        public static final String TABLE_NAME = "pets";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_BREED = "breed";
        public static final String COLUMN_NAME_GENDER = "gender";
        public static final String COLUMN_NAME_WEIGHT = "weight";
        public static final String COLUMN_NAME_ID = BaseColumns._ID;

        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;







    }
}
