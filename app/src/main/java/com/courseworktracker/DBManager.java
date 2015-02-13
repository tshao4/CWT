package com.courseworktracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.SQLException;

/**
 * Created by TerryS on 2/6/15.
 */

/*
    Database:
        Overview
        Term (tid*, sname)
            Schedule
            Course (cid*, lname)
                Lecture
                Discussion
                Lab
                Coursework
                    categories(catid, cname)
                    HW
                    Exam
                    quiz
                    paper
                    project

                    Grade (id*, sid.)

 */

public class DBManager {

    // Term
    private static final String ATTR_TID = "tid";
    private static final String ATTR_TNAME = "tname";

    //Course
    private static final String ATTR_CID = "cid";
    private static final String ATTR_CNAME = "cname";
    private static final String ATTR_CREDIT = "credit";
    private static final String ATTR_CGRADE = "cgrade";

    // Breadth
    private static final String ATTR_BID = "bid";
    private static final String ATTR_BNAME = "bname";

    // Gen-Ed
    private static final String ATTR_GID = "gid";
    private static final String ATTR_GNAME = "gname";


    private static final String DB_NAME = "CWT_DB";
    private static final int DB_VERSION = 1;

    private static final String TABLE_TERM = "terms";
    private static final String TABLE_COURSE = "courses";
    private static final String TABLE_BREADTH = "breadth";
    private static final String TABLE_GEN_ED = "gen_ed";

    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context ctx) {

        helper = new DBHelper(ctx);
    }

    private static class DBHelper extends SQLiteOpenHelper {

        DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL("create table if not exists " + TABLE_TERM +
                        "(tid integer primary key autoincrement, " +
                        "tname varchar not null, tgpa real, " +
                        "UNIQUE(tname) ON CONFLICT IGNORE)");

                db.execSQL("create table if not exists " + TABLE_BREADTH +
                        "(bid integer primary key autoincrement, " +
                        "bname varchar not null)");

                db.execSQL("create table if not exists " + TABLE_GEN_ED +
                        "(gid integer primary key autoincrement, " +
                        "gname varchar not null)");

                Cursor cur = db.rawQuery("select 1 from " + TABLE_BREADTH, null);
                ContentValues values = new ContentValues();

                if (cur.getCount() < 1) {
                    values.put(ATTR_BNAME, "Biological Science");
                    db.insert(TABLE_BREADTH, null, values);
                    values.put(ATTR_BNAME, "Humanities");
                    db.insert(TABLE_BREADTH, null, values);
                    values.put(ATTR_BNAME, "Interdivisional");
                    db.insert(TABLE_BREADTH, null, values);
                    values.put(ATTR_BNAME, "Literature");
                    db.insert(TABLE_BREADTH, null, values);
                    values.put(ATTR_BNAME, "Natural Science");
                    db.insert(TABLE_BREADTH, null, values);
                    values.put(ATTR_BNAME, "Physical Science");
                    db.insert(TABLE_BREADTH, null, values);
                    values.put(ATTR_BNAME, "Social Science");
                    db.insert(TABLE_BREADTH, null, values);
                }

                cur = db.rawQuery("select 1 from " + TABLE_GEN_ED, null);

                if (cur.getCount() < 1) {
                    values.put(ATTR_GNAME, "Comm-A");
                    db.insert(TABLE_GEN_ED, null, values);
                    values.put(ATTR_GNAME, "Comm-B");
                    db.insert(TABLE_GEN_ED, null, values);
                    values.put(ATTR_GNAME, "Quan-A");
                    db.insert(TABLE_GEN_ED, null, values);
                    values.put(ATTR_GNAME, "Quan-B");
                    db.insert(TABLE_GEN_ED, null, values);
                }
                            }
            catch(SQLException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i2) {

        }
    }

    public DBManager open() throws SQLException{
        db = helper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public long addTerm(String tname) {

        ContentValues values = new ContentValues();
        values.put(ATTR_TNAME, tname);
        return db.insert(TABLE_TERM, null, values);
    }

    public boolean deleteTerm(String tname) {
        String[] whereArgs = new String[] {tname};
        return db.delete(TABLE_TERM, ATTR_TNAME + "=?", whereArgs) > 0;
    }

    public String[] getTerms() {

        Cursor cur = db.query(TABLE_TERM, new String[]{ATTR_TID, ATTR_TNAME},
                null, null, null, null, ATTR_TID);
        int count = cur.getCount();

        String[] strs = new String[count + 1];

        cur.moveToFirst();

        for(int i = 1; i <= count; ++i) {
            strs[i] = cur.getString(cur.getColumnIndex(ATTR_TNAME));
            cur.moveToNext();
        }

        return strs;
    }

    public long addCourse(String tname, String cname, int credits, String cgrade, int bid, int gid) {

        try {
            db.execSQL("create table if not exists " + tname +
                    "(cid integer primary key autoincrement, " +
                    "cname varchar not null" + "credits integer not null," +
                    "cgrade varchar" + "bid integer, gid integer)");
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }

        ContentValues values = new ContentValues();
        values.put(ATTR_CNAME, cname);
        values.put(ATTR_CREDIT, credits);
        values.put(ATTR_CGRADE, cgrade);
        values.put(ATTR_BID, bid);
        values.put(ATTR_GID, gid);
        return db.insert(TABLE_COURSE, null, values);
    }

    /*
    public long addToList(int lid, int sid) {

        ContentValues values = new ContentValues();
        values.put(ATTR_SID, sid);
        return db.insert(getListName(lid), null, values);
    }

    public boolean deleteSong(int sid) {
        return db.delete(TABLE_SONGS, ATTR_SID + "=" + sid, null) > 0;
    }

    public boolean deleteFromList(int lid, int sid) {
        return db.delete(getListName(lid), ATTR_SID + "=" + sid, null) > 0;
    }

    public boolean deleteList(int lid) {
        try {
            db.execSQL("drop table if exists " + getListName(lid));
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return db.delete(TABLE_PLAYLISTS, ATTR_LID + "=" + lid, null) > 0;
    }

    public Cursor getAllSongs() {
        return db.query(TABLE_SONGS, new String[] {ATTR_SID, ATTR_SNAME, ATTR_PATH},
                null, null, null, ATTR_SNAME, null);
    }

    public Cursor getAllPaths() {
        return db.query(TABLE_SONGS, new String[] {ATTR_PATH}, null, null, ATTR_PATH, ATTR_PATH, null);
    }

    public Cursor getFromPath(String path) {
        return db.query(TABLE_SONGS, new String[] {ATTR_SID, ATTR_SNAME}, null, null, null, ATTR_SNAME, null);
    }

    public Cursor getLists() {
        return db.query(TABLE_PLAYLISTS, new String [] {ATTR_LID, ATTR_LNAME},
                null, null, null, ATTR_LNAME, null);
    }

    public Cursor getFromList(int lid) {
        return db.rawQuery("select " + ATTR_ID + "," + ATTR_SID + "," + ATTR_SNAME + "," + ATTR_PATH +
                "from " + TABLE_SONGS + " s," + getListName(lid) + " p " +
                "where p." + ATTR_SID + "= s." + ATTR_SID, null);
    }

    private String getListName(int lid) {

        Cursor mCur = db.rawQuery("select " + ATTR_LNAME +
                " from " + TABLE_PLAYLISTS +
                " where " + ATTR_LID + "=" + lid, null);

        if (mCur.moveToFirst()) {
            int nameIndex = mCur.getColumnIndex(ATTR_LNAME);
            return mCur.getString(nameIndex);
        }

        return null;
    }
    */
}
