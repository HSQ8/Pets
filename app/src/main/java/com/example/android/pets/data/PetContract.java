package com.example.android.pets.data;

import android.provider.BaseColumns;

/**
 * Created by HQ on 7/24/2017.
 */

public final class PetContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private PetContract() {}

    /* Inner class that defines the table contents */
    public static class PetEntry implements BaseColumns {
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
