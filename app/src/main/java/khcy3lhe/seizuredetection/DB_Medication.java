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

public class DB_Medication {

    public String DBPath;
    private static final String DATABASE_NAME = "SeizureDetection";
    private static final String SQLITE_TABLE = "Medication";
    private final Context mCtx;

    private DBHelper mDbHelper;
    private SQLiteDatabase mDb;

    public static final String KEY_ROWID = "_id";
    public static final String KEY_MEDICATION = "medication";
    public static final String KEY_TIME = "time";


    public DB_Medication(Context ctx) {
        DBPath = "/data/data/" + ctx.getPackageName() + "/databases";
        this.mCtx = ctx;
    }

    public DB_Medication open() throws SQLException {
        mDbHelper = new DBHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public int numberOfRows(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, SQLITE_TABLE);
        return numRows;
    }

    public boolean insertMedication  (String medicationName, int medicationTime) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("medication", medicationName);
        contentValues.put("time", medicationTime);

        db.insert(SQLITE_TABLE, null, contentValues);
        return true;
    }

    public void deleteMedication (int id) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(SQLITE_TABLE,
                KEY_ROWID + "=?",
                new String[] {Integer.toString(id)});
    }


    public Cursor fetchAllMedication() {

        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                        KEY_MEDICATION, KEY_TIME}, null, null, null, null, null);

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
