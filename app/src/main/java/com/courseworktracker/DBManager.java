package com.courseworktracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.SQLException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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

    // TODO: add import/export functionality

    // Term
    private static final String ATTR_TID = "tid";
    private static final String ATTR_TNAME = "tname";

    //Course
    private static final String ATTR_CID = "cid";
    private static final String ATTR_CNAME = "cname";
    private static final String ATTR_CREDIT = "credits";
    private static final String ATTR_CGRADE = "cgrade";

    // Breadth
    private static final String ATTR_BID = "bid";
    private static final String ATTR_BNAME = "bname";

    // Gen-Ed
    private static final String ATTR_GID = "gid";
    private static final String ATTR_GNAME = "gname";

    // Assignments
    private static final String ATTR_AID = "aid";
    private static final String ATTR_ANAME = "aname";
    private static final String ATTR_DUE = "duedate";


    private static final String DB_NAME = "CWT_DB";
    private static final int DB_VERSION = 1;

    private static final String TABLE_TERM = "terms";
    private static final String TABLE_ASSIGN = "assignments";
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
                db.beginTransaction();
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

                db.execSQL("create table if not exists " + TABLE_ASSIGN +
                        "(aid integer primary key autoincrement, " +
                        "aname varchar not null, duedate integer not null," +
                        "cname varchar not null)");

                Cursor cur = db.rawQuery("select 1 from " + TABLE_BREADTH, null);

                if (cur.getCount() < 1) {

                    dbInsert(db, TABLE_BREADTH, ATTR_BNAME, "Biological Science");
                    dbInsert(db, TABLE_BREADTH, ATTR_BNAME, "Humanities");
                    dbInsert(db, TABLE_BREADTH, ATTR_BNAME, "Interdivisional");
                    dbInsert(db, TABLE_BREADTH, ATTR_BNAME, "Literature");
                    dbInsert(db, TABLE_BREADTH, ATTR_BNAME, "Natural Science");
                    dbInsert(db, TABLE_BREADTH, ATTR_BNAME, "Physical Science");
                    dbInsert(db, TABLE_BREADTH, ATTR_BNAME, "Social Science");
                }

                cur = db.rawQuery("select 1 from " + TABLE_GEN_ED, null);

                if (cur.getCount() < 1) {

                    dbInsert(db, TABLE_GEN_ED, ATTR_GNAME, "None");
                    dbInsert(db, TABLE_GEN_ED, ATTR_GNAME, "Comm-A");
                    dbInsert(db, TABLE_GEN_ED, ATTR_GNAME, "Comm-B");
                    dbInsert(db, TABLE_GEN_ED, ATTR_GNAME, "Quan-A");
                    dbInsert(db, TABLE_GEN_ED, ATTR_GNAME, "Quan-B");
                }
                cur.close();
                db.setTransactionSuccessful();
            }
            catch(SQLException ex) {
                ex.printStackTrace();
            }
            finally {
                db.endTransaction();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i2) {

        }

        private void dbInsert(SQLiteDatabase db, String tbl_name, String col, String value) {
            ContentValues values = new ContentValues();
            values.put(col, value);
            db.insert(tbl_name, null, values);
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

        String  createTable = "create table if not exists " + tname +
                "(cid integer primary key autoincrement, " +
                "cname varchar not null, credits integer not null," +
                "cgrade varchar not null, bid integer not null, gid integer not null)";
        try {
            db.execSQL(createTable);
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }

        return db.insert(TABLE_TERM, null, values);
    }

    public boolean deleteTerm(String tname) {
        String[] whereArgs = new String[] {tname};
        boolean tmp = false;
        try {
            db.beginTransaction();
            Course[] courses = getCourses(tname);
            ArrayList<String> cnames = new ArrayList<String>();
            for (int i = 0; i < courses.length; i++) {
                cnames.add(courses[i].getCname());
            }
            db.execSQL("drop table if exists " + tname);
            tmp = db.delete(TABLE_TERM, ATTR_TNAME + "=?", whereArgs) > 0;

            for (int i = 0; i < cnames.size(); i++) {
                tmp = tmp && db.delete(TABLE_ASSIGN, ATTR_CNAME + "=?", new String[]{cnames.get(i)}) > 0;
            }

            db.setTransactionSuccessful();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            db.endTransaction();
        }
        return tmp;
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
        cur.close();
        return strs;
    }

    public boolean existTerm(String tname) {
        String[] terms = getTerms();
        for (String t : terms) {
            if (t == null) break;
            if (t.equals(tname)) {
                return true;
            }
        }
        return false;
    }

    public long addCourse(String tname, String cname, int credits, String cgrade, int bid, int gid) {

        ContentValues values = new ContentValues();
        values.put(ATTR_CNAME, cname);
        values.put(ATTR_CREDIT, credits);
        values.put(ATTR_CGRADE, cgrade);
        values.put(ATTR_BID, bid);
        values.put(ATTR_GID, gid);

        return db.insert(tname, null, values);
    }

    public int updateCourse(String tname, String cname, Course course){

        ContentValues values = new ContentValues();
        values.put(ATTR_CNAME, course.getCname());
        values.put(ATTR_CREDIT, course.getCredit());
        values.put(ATTR_CGRADE, course.getGrade());
        values.put(ATTR_BID, course.getBreadth());
        values.put(ATTR_GID, course.getGen_ed());
        return db.update(tname, values, ATTR_CNAME + " = ? ", new String[]{cname});
    }

    public Course[] getCourses(String tname){
        Cursor cur = db.query(tname, new String[]{ATTR_CID, ATTR_CNAME, ATTR_CREDIT,
                        ATTR_CGRADE, ATTR_BID, ATTR_GID},
                null, null, null, null, ATTR_CID);
        int count = cur.getCount();

        Course[] courses = new Course[count];

        cur.moveToFirst();

        for(int i = 0; i < count; ++i) {
            String cname = cur.getString(cur.getColumnIndex(ATTR_CNAME));
            int credit = cur.getInt(cur.getColumnIndex(ATTR_CREDIT));
            String grade = cur.getString(cur.getColumnIndex(ATTR_CGRADE));
            int bid = cur.getInt(cur.getColumnIndex(ATTR_BID));
            int gid = cur.getInt(cur.getColumnIndex(ATTR_GID));
            courses[i] = new Course(cname, credit, grade, bid, gid);
            cur.moveToNext();
        }
        cur.close();
        return courses;
    }

    public Course[] getCourse(String tname, String cname){
//        Cursor cur = db.query(tname, new String[]{ATTR_CID, ATTR_CNAME, ATTR_CREDIT,
//                        ATTR_CGRADE, ATTR_BID, ATTR_GID},
//                "cname = \'" + cname + "\'", null, null, null, ATTR_CID);
        String qry = "select * from " + tname + " where cname = ?";
        Cursor cur = db.rawQuery(qry, new String[]{cname});

        int count = cur.getCount();

        Course[] courses = new Course[count];

        cur.moveToFirst();

        for(int i = 0; i < count; ++i) {
            int credit = cur.getInt(cur.getColumnIndex(ATTR_CREDIT));
            String grade = cur.getString(cur.getColumnIndex(ATTR_CGRADE));
            int bid = cur.getInt(cur.getColumnIndex(ATTR_BID));
            int gid = cur.getInt(cur.getColumnIndex(ATTR_GID));
            courses[i] = new Course(cname, credit, grade, bid, gid);
            cur.moveToNext();
        }
        cur.close();
        return courses;
    }

    public String[] getCourseNames(String tname) {
        Cursor cur = db.query(tname, new String[]{ATTR_CID, ATTR_CNAME},
                null, null, null, null, ATTR_CID);
        int count = cur.getCount();

        String[] courses = new String[count];

        cur.moveToFirst();

        for(int i = 0; i < count; ++i) {
            String cname = cur.getString(cur.getColumnIndex(ATTR_CNAME));
            courses[i] = cname;
            cur.moveToNext();
        }
        cur.close();
        return courses;
    }



    public boolean deleteCourse(String tname, String cname) {
        String[] whereArgs = new String[] {cname};

        boolean tmp = false;
        try {
            db.beginTransaction();
            tmp = db.delete(TABLE_ASSIGN, ATTR_CNAME + "=?", whereArgs) > 0 && db.delete(tname, ATTR_CNAME + "=?", whereArgs) > 0;
            db.setTransactionSuccessful();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            db.endTransaction();
        }
        return tmp;
    }

    public boolean existCourse(String tname, String cname){
        Course[] courses = getCourses(tname);

        for (Course c : courses) {
            if (c == null) break;
            if (c.getCname().equals(cname)) {
                return true;
            }
        }
        return false;
    }

    public long addAssignment(String cname, CourseWork cw){
        ContentValues values = new ContentValues();
        values.put(ATTR_ANAME, cw.getAname());
        values.put(ATTR_DUE, cw.getDuedate());
        values.put(ATTR_CNAME, cw.getCname());

        return db.insert(TABLE_ASSIGN, null, values);
    }

    public List<List<String>> getAssignemnts(String cname){
        String[] whereArgs = new String[] {cname};
        Cursor cur = db.query(TABLE_ASSIGN, new String[]{ATTR_ANAME, ATTR_DUE},
                ATTR_CNAME + "=?", whereArgs, null, null, ATTR_DUE);

        int count = cur.getCount();

        List<List<String>> lists = new ArrayList<List<String>>(2);
        lists.add(new ArrayList<String>());
        lists.add(new ArrayList<String>());

        cur.moveToFirst();

        for(int i = 0; i < count; ++i) {
            lists.get(0).add(cur.getString(cur.getColumnIndex(ATTR_ANAME)));
            lists.get(1).add(Integer.toString(cur.getInt(cur.getColumnIndex(ATTR_DUE))));
            cur.moveToNext();
        }
        cur.close();
        return lists;
    }

    public double[] Credits(){
        String[] terms = getTerms();
        double[] credits = {0, 0};
        for(int i = 1; i < terms.length; i++){
            Log.i("" + terms.length, terms[1]);
            Course[] course = getCourses(terms[i]);
            for(int j = 0; j < course.length; j++){
                credits[0] = credits[0] + course[j].getCredit();
                String grade = course[j].getGrade();
                if(grade.equals("A")){
                    credits[1] = credits[1] + 4;
                } else if(grade.equals("AB")){
                    credits[1] += 3.5;
                }else if(grade.equals("B")){
                    credits[1] += 3;
                }else if(grade.equals("BC")){
                    credits[1] += 2.5;
                }else if(grade.equals("C")){
                    credits[1] += 2;
                }else if(grade.equals("D")){
                    credits[1] += 1;
                }
            }
        }
        return credits;
    }



    public List<CourseWork> getAllAssingments(){
        Cursor cur = db.query(TABLE_ASSIGN, new String[]{ATTR_ANAME, ATTR_DUE, ATTR_CNAME},
                ATTR_CNAME + "=?", null, null, null, ATTR_DUE);

        int count = cur.getCount();

        List<CourseWork> list = new ArrayList<CourseWork>();

        cur.moveToFirst();

        for(int i = 0; i < count; ++i) {
            CourseWork cw = new CourseWork(cur.getString(cur.getColumnIndex(ATTR_CNAME)),
                    cur.getString(cur.getColumnIndex(ATTR_ANAME)), cur.getInt(cur.getColumnIndex(ATTR_DUE)));
            list.add(cw);
            cur.moveToNext();
        }
        cur.close();
        return list;
    }

    public boolean deleteAssignment(String cname, String aname) {
        String[] whereArgs = new String[] {aname, cname};
        return db.delete(TABLE_ASSIGN, ATTR_ANAME + "=? and " + ATTR_CNAME + "=?", whereArgs) > 0;
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