package khcy3lhe.seizuredetection;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SeizureDetection";

    //Increase database version whenever new table made
    private static final int DATABASE_VERSION = 7;

    //Database for Personal details
    private static final String SQLITE_TABLE_PERSONAL = "Personal";
    public static final String KEY_ROWID_PERSONAL = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_SEIZUREP = "seizure";
    public static final String KEY_CONTACT = "contact";

    private static final String DATABASE_CREATE_PERSONAL =
            "CREATE TABLE if not exists " + SQLITE_TABLE_PERSONAL + " (" +
                    KEY_ROWID_PERSONAL + " integer PRIMARY KEY autoincrement," +
                    KEY_NAME + " varchar," +
                    KEY_GENDER + " varchar," +
                    KEY_SEIZUREP + " varchar," +
                    KEY_CONTACT + " varchar)" ;

    //Database for Medication Reminder
    private static final String SQLITE_TABLE_Medication = "Medication";
    public static final String KEY_ROWID_MEDICATION = "_id";
    public static final String KEY_MEDICATION = "medication";
    public static final String KEY_TIME = "time";

    private static final String DATABASE_CREATE_Medication =
            "CREATE TABLE if not exists " + SQLITE_TABLE_Medication + " (" +
                    KEY_ROWID_MEDICATION + " integer PRIMARY KEY autoincrement," +
                    KEY_MEDICATION + " varchar," +
                    KEY_TIME + " text)" ;

    //Database for seizure record
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
                    KEY_DATE + " text," +
                    KEY_STARTTIME + " text," +
                    KEY_DURATION + " integer," +
                    KEY_PREICTAL + " varchar," +
                    KEY_POSTICTAL + " varchar," +
                    KEY_TRIGGER + " varchar," +
                    KEY_SLEEP+ " varchar," +
                    KEY_MEDICATED + " int," +
                    KEY_VIDEO + " varchar," +
                    KEY_COMMENTS + " varchar)";

    //Database for appoinment
    private static final String SQLITE_TABLE_Appointment = "Appointment";
    public static final String KEY_ROWID_APP = "_id";
    public static final String KEY_DRNAME = "drname";
    public static final String KEY_DATE_APP = "date";
    public static final String KEY_TIME_APP = "time";
    public static final String KEY_COMMENT_APP = "comment";

    private static final String DATABASE_CREATE_Appointment =
            "CREATE TABLE if not exists " + SQLITE_TABLE_Appointment + " (" +
                    KEY_ROWID_APP + " integer PRIMARY KEY autoincrement," +
                    KEY_DRNAME + " varchar," +
                    KEY_DATE_APP + " text," +
                    KEY_TIME_APP + " text," +
                    KEY_COMMENT_APP + " text)";

    //Database for Seizure record from sensor
    private static final String SQLITE_TABLE_SeizureRecord = "SeizureRecord";
    public static final String KEY_ROWID_SEIZURERECORD = "_id";
    public static final String KEY_DATE_RECORD = "date";
    public static final String KEY_STARTTIME_RECORD = "startTime";
    public static final String KEY_DURATION_RECORD = "duration";
    public static final String KEY_STARTHEART = "startHeart";
    public static final String KEY_ENDHEART = "endHeart";
    public static final String KEY_DURATIONHEART = "durationHeart";
    public static final String KEY_THRESHOLDMEDIAN = "thresholdMedian";
    public static final String KEY_THRESHOLDMEAN = "thresholdMean";
    public static final String KEY_STARTAcc = "startAcc";
    public static final String KEY_ENDAcc = "endAcc";
    public static final String KEY_DURATIONAcc = "durationAcc";
    public static final String KEY_FALSEALARM = "falseAlarm";

    private static final String TAG_SeizureRecord = "DB_SeizureRecord";

    private static final String DATABASE_CREATE_SeizureRecord =
            "CREATE TABLE if not exists " + SQLITE_TABLE_SeizureRecord + " (" +
                    KEY_ROWID_SEIZURERECORD + " integer PRIMARY KEY autoincrement," +
                    KEY_DATE_RECORD + " text," +
                    KEY_STARTTIME_RECORD + " text," +
                    KEY_DURATION_RECORD + " integer," +
                    KEY_STARTHEART + " varchar," +
                    KEY_ENDHEART + " varchar," +
                    KEY_DURATIONHEART + " varchar," +
                    KEY_THRESHOLDMEDIAN + " integer," +
                    KEY_THRESHOLDMEAN + " integer," +
                    KEY_STARTAcc + " varchar," +
                    KEY_ENDAcc + " varchar," +
                    KEY_DURATIONAcc + " varchar," +
                    KEY_FALSEALARM + " integer)";


    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_Medication);
        db.execSQL(DATABASE_CREATE_Seizure);
        db.execSQL(DATABASE_CREATE_Appointment);
        db.execSQL(DATABASE_CREATE_SeizureRecord);
        db.execSQL(DATABASE_CREATE_PERSONAL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(), "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE_Medication);
        db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE_Seizure);
        db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE_Appointment);
        db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE_SeizureRecord);
        db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE_PERSONAL);
        onCreate(db);
    }

    public boolean deleteDatabase(Context context) {
        return context.deleteDatabase(DATABASE_NAME);
    }
}
