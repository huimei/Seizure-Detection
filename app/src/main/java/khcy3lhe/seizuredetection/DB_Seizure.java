package khcy3lhe.seizuredetection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DB_Seizure {

    public String DBPath;
    private static final String DATABASE_NAME = "SeizureDetection";
    private static final String SQLITE_TABLE = "Seizure";
    private static final int DATABASE_VERSION = 1;
    private final Context mCtx;

    public static final String KEY_ROWID = "_id";
    public static final String KEY_SEIZURE = "seizureType";
    public static final String KEY_DATE = "date";
    public static final String KEY_STARTTIME = "startTime";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_PREICTAL = "preictal";
    public static final String KEY_POSTICTAL = "postictal";
    public static final String KEY_TRIGGER = "trigger";
    public static final String KEY_SLEEP = "sleep";
    public static final String KEY_MEDICATED = "medicated";
    public static final String KEY_VIDEO = "video";
    public static final String KEY_COMMENTS = "comments";


    private static final String TAG = "DB_Seizure";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_SEIZURE + "varchar," +
                    KEY_DATE + "integer," +
                    KEY_STARTTIME + "integer," +
                    KEY_DURATION + "integer," +
                    KEY_PREICTAL + "varchar," +
                    KEY_POSTICTAL + "varchar," +
                    KEY_TRIGGER + "varchar," +
                    KEY_SLEEP+ "varchar," +
                    KEY_MEDICATED + "varchar," +
                    KEY_VIDEO + "blob," +
                    KEY_COMMENTS + "text)";



    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
            onCreate(db);
        }
    }

    public DB_Seizure(Context ctx) {
        DBPath = "/data/data/" + ctx.getPackageName() + "/databases";
        this.mCtx = ctx;
    }

    public DB_Seizure open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public boolean insertSeizure  (String seizureType, int date, int startTime, int duration,
                                   String preictal, String postictal, String trigger, String sleep,
                                   String medicated, String video, String comment) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("seizureType", seizureType);
        contentValues.put("date", date);
        contentValues.put("startTime", startTime);
        contentValues.put("duration", duration);
        contentValues.put("preictal", preictal);
        contentValues.put("postictal", postictal);
        contentValues.put("trigger", trigger);
        contentValues.put("sleep", sleep);
        contentValues.put("medicated", medicated);
        contentValues.put("video", video);
        contentValues.put("comments", comment);

        db.insert(SQLITE_TABLE, null, contentValues);
        return true;
    }

    public boolean updateSeizure (int id, String seizureType, int date, int startTime, int duration,
                                  String preictal, String postictal, String trigger, String sleep,
                                  String medicated, String video, String comment) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("seizureType", seizureType);
        contentValues.put("date", date);
        contentValues.put("startTime", startTime);
        contentValues.put("duration", duration);
        contentValues.put("preictal", preictal);
        contentValues.put("postictal", postictal);
        contentValues.put("trigger", trigger);
        contentValues.put("sleep", sleep);
        contentValues.put("medicated", medicated);
        contentValues.put("video", video);
        contentValues.put("comments", comment);

        db.update(SQLITE_TABLE, contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public void deleteMedication (String name, String time)
    {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(SQLITE_TABLE,
                KEY_DATE + "=? AND " + KEY_STARTTIME + "=? ",
                new String[] {name, time});
    }


    public Cursor fetchAllMedication() {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor mCursor = db.query(SQLITE_TABLE, new String[]{KEY_ROWID, KEY_SEIZURE, KEY_DATE, KEY_STARTTIME,
                                    KEY_DURATION, KEY_PREICTAL, KEY_POSTICTAL, KEY_TRIGGER, KEY_SLEEP, KEY_MEDICATED,
                                    KEY_VIDEO, KEY_COMMENTS}, null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void deleteAll (){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(SQLITE_TABLE,null,null);
    }


}
