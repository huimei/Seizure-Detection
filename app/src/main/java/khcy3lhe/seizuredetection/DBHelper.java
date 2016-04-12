package khcy3lhe.seizuredetection;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SeizureDetection";
    //Increase database version whenever new table made
    private static final int DATABASE_VERSION = 2;

    private static final String SQLITE_TABLE_Medication = "Medication";
    public static final String KEY_ROWID_MEDICATION = "_id";
    public static final String KEY_MEDICATION = "medication";
    public static final String KEY_TIME = "time";

    private static final String DATABASE_CREATE_Medication =
            "CREATE TABLE if not exists " + SQLITE_TABLE_Medication + " (" +
                    KEY_ROWID_MEDICATION + " integer PRIMARY KEY autoincrement," +
                    KEY_MEDICATION + " varchar," +
                    KEY_TIME + " integer)" ;

    private static final String SQLITE_TABLE_Seizure = "Seizure";
    public static final String KEY_ROWID_SEIZURE = "_id";
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
    private static final String TAG_Seizure = "DB_Seizure";

    private static final String DATABASE_CREATE_Seizure =
            "CREATE TABLE if not exists " + SQLITE_TABLE_Seizure + " (" +
                    KEY_ROWID_SEIZURE + " integer PRIMARY KEY autoincrement," +
                    KEY_SEIZURE + " varchar," +
                    KEY_DATE + " integer," +
                    KEY_STARTTIME + " integer," +
                    KEY_DURATION + " integer," +
                    KEY_PREICTAL + " varchar," +
                    KEY_POSTICTAL + " varchar," +
                    KEY_TRIGGER + " varchar," +
                    KEY_SLEEP+ " varchar," +
                    KEY_MEDICATED + " int," +
                    KEY_VIDEO + " varchar," +
                    KEY_COMMENTS + " varchar)";



    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_Medication);
        db.execSQL(DATABASE_CREATE_Seizure);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(), "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE_Medication);
        db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE_Seizure);
        onCreate(db);
    }

    public boolean deleteDatabase(Context context) {
        return context.deleteDatabase(DATABASE_NAME);
    }
}
