package rw.rt.com.mykomplek;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 * Created by embowth on 07/01/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "user_login";
    private static final String ID = "id";
    private static final String COL1 = "id_user";
    private static final String COL2 = "email";
    private static final String COL3 = "role";
    private static final String COL4 = "id_warga";
    private static final String COL5 = "nama";
    private static final String COL6 = "id_group";
    private static final String COL7 = "jenis_warga";
    private static final String COL8 = "password";
    private static final String COL9 = "tanggal_lahir";
    private static final String COL10 = "jenis_kelamin";

    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " ("+ ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL1 +" INTEGER, " + COL2 +" TEXT, "+ COL3 +" INTEGER, "+ COL4 +" INTEGER, "+ COL5 +" TEXT, "+
                COL6 +" INTEGER, "+ COL7 +" INTEGER, "+ COL8 +" TEXT, "+ COL9 +" TEXT, "+ COL10 +" TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String[] item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, item[0]);
        contentValues.put(COL1, item[1]);
        contentValues.put(COL2, item[2]);
        contentValues.put(COL3, item[3]);
        contentValues.put(COL4, item[4]);
        contentValues.put(COL5, item[5]);
        contentValues.put(COL6, item[6]);
        contentValues.put(COL7, item[7]);
        contentValues.put(COL8, item[8]);
        contentValues.put(COL9, item[9]);
        contentValues.put(COL10, item[10]);

        Log.d(TAG, "addData: Adding " + item + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void deleteData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME ;
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting  from database.");
        db.execSQL(query);
    }

    public void updatePassword(String password){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE "+ TABLE_NAME + " SET password='"+ password +"'";
        Log.d(TAG,"update query :" + query);
        db.execSQL(query);
    }

    public void updateProfil(String nama, String tanggal_lahir, String jenis_kelamin){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE "+ TABLE_NAME + " SET nama='"+ nama +"', tanggal_lahir='"+tanggal_lahir+"', jenis_kelamin='"+jenis_kelamin+"'";
        Log.d(TAG,"update query :" + query);
        db.execSQL(query);
    }

}
