package khcy3lhe.seizuredetection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DB_Personal {

    public String DBPath;
    private static final String DATABASE_NAME = "SeizureDetection";
    private static final String SQLITE_TABLE = "Personal";
    private final Context mCtx;

    private DBHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String SQLITE_TABLE_PERSONAL = "Personal";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_SEIZUREP = "seizure";
    public static final String KEY_CONTACT = "contact";


    public DB_Personal(Context ctx) {
        DBPath = "/data/data/" + ctx.getPackageName() + "/databases";
        this.mCtx = ctx;
    }

    public DB_Personal open() throws SQLException {
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

    public boolean insertPersonal  (String name, String gender, String seizure, String contact) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_GENDER, gender);
        contentValues.put(KEY_SEIZUREP, seizure);
        contentValues.put(KEY_CONTACT, contact);

        db.insert(SQLITE_TABLE, null, contentValues);
        return true;
    }


    public Cursor fetchAll() {

        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                KEY_NAME, KEY_GENDER, KEY_SEIZUREP, KEY_CONTACT}, null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean updatePersonal(int id, String name, String gender, String seizure, String contact) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_GENDER, gender);
        contentValues.put(KEY_SEIZUREP, seizure);
        contentValues.put(KEY_CONTACT, contact);

        db.update(SQLITE_TABLE, contentValues, "_id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public void deleteAll (){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(SQLITE_TABLE,null,null);
    }

}
