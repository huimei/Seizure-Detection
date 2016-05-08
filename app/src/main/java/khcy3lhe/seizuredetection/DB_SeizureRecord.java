package khcy3lhe.seizuredetection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DB_SeizureRecord {

    public String DBPath;
    private static final String DATABASE_NAME = "SeizureDetection";
    private static final String SQLITE_TABLE = "SeizureRecord";
    private final Context mCtx;

    private DBHelper mDbHelper;
    private SQLiteDatabase mDb;

    public static final String KEY_ROWID = "_id";
    public static final String KEY_DATE = "date";
    public static final String KEY_STARTTIME = "startTime";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_STARTHEART = "startHeart";
    public static final String KEY_ENDHEART = "endHeart";
    public static final String KEY_DURATIONHEART = "durationHeart";
    public static final String KEY_STARTAcc = "startAcc";
    public static final String KEY_ENDAcc = "endAcc";
    public static final String KEY_DURATIONAcc = "durationAcc";
    public static final String KEY_FALSEAMLARM = "falseAlarm";


    public DB_SeizureRecord(Context ctx) {
        DBPath = "/data/data/" + ctx.getPackageName() + "/databases";
        this.mCtx = ctx;
    }

    public DB_SeizureRecord open() throws SQLException {
        mDbHelper = new DBHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public boolean isTableExists()
    {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String tableName = SQLITE_TABLE;

        if (tableName == null || db == null || !db.isOpen())
        {
            return false;
        }
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", tableName});
        if (!cursor.moveToFirst())
        {
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    public int numberOfRows(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, SQLITE_TABLE);
        return numRows;
    }

    public boolean insertSeizureRecord  (String date, String startTime, String duration,
                                         int startHeart, int endHeart, String durationHeart, int startAcc,
                                         int endAcc, String durationAcc, int falseAlarm) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_DATE, date);
        contentValues.put(KEY_STARTTIME, startTime);
        contentValues.put(KEY_DURATION, duration);
        contentValues.put(KEY_STARTHEART, startHeart);
        contentValues.put(KEY_ENDHEART, endHeart);
        contentValues.put(KEY_DURATIONHEART, durationHeart);
        contentValues.put(KEY_STARTAcc, startAcc);
        contentValues.put(KEY_ENDAcc, endAcc);
        contentValues.put(KEY_DURATIONAcc, durationAcc);
        contentValues.put(KEY_FALSEAMLARM, falseAlarm);

        db.insert(SQLITE_TABLE, null, contentValues);
        return true;
    }

    public boolean updateSeizureRecord (int id, String date, String startTime, String duration,
                                 int startHeart, int endHeart, String durationHeart, int startAcc,
                                 int endAcc, String durationAcc, int falseAlarm) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_DATE, date);
        contentValues.put(KEY_STARTTIME, startTime);
        contentValues.put(KEY_DURATION, duration);
        contentValues.put(KEY_STARTHEART, startHeart);
        contentValues.put(KEY_ENDHEART, endHeart);
        contentValues.put(KEY_DURATIONHEART, durationHeart);
        contentValues.put(KEY_STARTAcc, startAcc);
        contentValues.put(KEY_ENDAcc, endAcc);
        contentValues.put(KEY_DURATIONAcc, durationAcc);
        contentValues.put(KEY_FALSEAMLARM, falseAlarm);

        db.update(SQLITE_TABLE, contentValues, "_id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public void deleteSeizureRecord (int id) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(SQLITE_TABLE,
                KEY_ROWID + "=?",
                new String[] {Integer.toString(id)});
    }


    public Cursor fetchAllSeizureRecord () {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor mCursor = db.query(SQLITE_TABLE, new String[]{KEY_ROWID, KEY_DATE, KEY_STARTTIME,
                KEY_DURATION, KEY_STARTHEART, KEY_ENDHEART, KEY_DURATIONHEART, KEY_STARTAcc, KEY_ENDAcc,
                KEY_DURATIONAcc, KEY_FALSEAMLARM}, null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchSeizureRecord (int id){

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor mCursor = db.rawQuery("select * from SeizureRecord where _id="+id+"", null);

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
