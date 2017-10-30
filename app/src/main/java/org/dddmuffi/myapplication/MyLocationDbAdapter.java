package org.dddmuffi.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Einfache Hilfsklasse ReminderDBAdapter
 * Definiert die grundlegenden CRUD-Operationen (Create, Read, Update, Delete)
 * f�r das Beispiel und erm�glicht die Auflistung  aller Erinnerungstermine
 * sowie das Auslesen und die �nderung eines bestimmten Termins.
 */

public class MyLocationDbAdapter {
    //
    // Datenbankspezifische Konstanten
    //
    private static final String DATABASE_NAME = "mytelescope";
    private static final String DATABASE_TABLE = "loacation";
    private static final int DATABASE_VERSION = 1;

    public static final String KEY_TITLE = "locationtitle";
    public static final String KEY_BODY = "locationbody";
    public static final String KEY_LONG = "longitude";
    public static final String KEY_LATI = "latitude";
    public static final String KEY_ALTI = "altitude";
    public static final String KEY_ROWID = "_id";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * SQL-Anweisung zur Erstellung der Datenbank
     */

    private static final String DATABASE_CREATE =
            "create table " + DATABASE_TABLE + " ("
                    + KEY_ROWID + " integer primary key autoincrement, "
                    + KEY_TITLE + " text not null, "
                    + KEY_BODY + " text not null, "
                    + KEY_LONG + " text not null, "
                    + KEY_LATI + " text not null, "
                    + KEY_ALTI + " text not null);";

    private final Context mCtx;

    /**
     * Konstruktor - �bernimmt den Kontext, in dem die Datenbank
     * ge�ffnet/erstellt wird
     *
     * @param ctx - Kontext, in dem gearbeitet wird
     */
    public MyLocationDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Datenbank �ffnen. Wenn sie nicht ge�ffnet werden kann, wird versucht,
     * eine neue Instanz der Datenbank zu erzeugen. Wenn diese nicht
     * erzeugt werden kann, wird ein Ausnahmefehler ausgel�st, um auf
     * den Fehler hinzuweisen.
     *
     * @return this - (Selbstreferenz um Verkettung in Initialisierungsaufruf
     *                 zu erm�glichen)
     * @throws SQLException - Wenn Datenbank nicht ge�ffnet und nicht erzeugt
     *                        werden kann
     */
    public MyLocationDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public long createLocation(String title, String body, String actlong, String actlati, String actalti) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_BODY, body);
        initialValues.put(KEY_LONG, actlong);
        initialValues.put(KEY_LATI, actlati);
        initialValues.put(KEY_ALTI, actalti);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteLocation(long rowId) {
        return
                mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Cursor fetchAllLocations() {
        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,
                KEY_BODY, KEY_LONG, KEY_LATI, KEY_ALTI}, null, null, null, null, null, null);
    }

    public Cursor fetchLocation(long rowId) throws SQLException {
        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_TITLE, KEY_BODY, KEY_LONG, KEY_LATI, KEY_ALTI}, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean updateLocation(long rowId, String title, String body, String actlong, String actlati, String actalti) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_BODY, body);
        args.put(KEY_LONG, actlong);
        args.put(KEY_LATI, actlati);
        args.put(KEY_ALTI, actalti);

        return
                mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            // Nicht benutzt, Datenbank kann aber mit ALTER aktualisiert werden
            // Skripts
        }
    }

}
