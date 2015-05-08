package com.courseworktracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.zip.CheckedOutputStream;


public class AddCourse extends ActionBarActivity {

    private static final String TNAME = "tname";
    private static final String CNAME = "cname";
    private static final String MODE = "mode";
    private static final String INFO = "info";
    private static final int MODE_ADD = 0;
    private static final int MODE_EDIT = 1;

    private DBManager dbm = new DBManager(this);
    private Activity activity = this;
    private Course course = new Course();
    private boolean[] selected = {false, false, false, false, false};
    private int mode;
    private ArrayList<String> sec_title = new ArrayList<String>(),
            sec_record = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        mode = getIntent().getIntExtra(MODE, MODE_ADD);
        int[] info = getIntent().getIntArrayExtra(INFO);

        final EditText course_name_et = (EditText)findViewById(R.id.edittext_course_name);
        Spinner sp_credit = (Spinner)findViewById(R.id.spinner_credit);
        Spinner sp_grade = (Spinner)findViewById(R.id.spinner_grade);
        Spinner sp_breadth = (Spinner)findViewById(R.id.spinner_breadth);
        Spinner sp_gen_ed = (Spinner)findViewById(R.id.spinner_gen_ed);

        Button bt_cancel = (Button)findViewById(R.id.button_cancel_add_course);
        Button bt_next = (Button)findViewById(R.id.button_ok_add_course);

        ArrayAdapter<CharSequence> sp_credit_adt = ArrayAdapter.createFromResource(this,
                R.array.credits, R.layout.spinner_target);
        sp_credit_adt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_credit.setAdapter(sp_credit_adt);

        ArrayAdapter<CharSequence> sp_grade_adt = ArrayAdapter.createFromResource(this,
                R.array.grade, R.layout.spinner_target);
        sp_grade_adt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_grade.setAdapter(sp_grade_adt);

        ArrayAdapter<CharSequence> sp_breadth_adt = ArrayAdapter.createFromResource(this,
                R.array.breadth, R.layout.spinner_target);
        sp_breadth_adt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_breadth.setAdapter(sp_breadth_adt);

        ArrayAdapter<CharSequence> sp_gen_ed_adt = ArrayAdapter.createFromResource(this,
                R.array.gen_ed, R.layout.spinner_target);
        sp_gen_ed_adt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_gen_ed.setAdapter(sp_gen_ed_adt);

        if (mode == MODE_EDIT) {
            for (int i = 0; i < selected.length; i++) {
                selected[i] = true;
            }
            course_name_et.setText(getIntent().getStringExtra(CNAME), TextView.BufferType.EDITABLE);
            sp_credit.setSelection(info[0] + 1, false);
            course.setCredit(info[0]);

            String[] gr = getResources().getStringArray(R.array.grade);
            sp_grade.setSelection(info[1], false);
            course.setGrade(gr[info[1]]);

            sp_breadth.setSelection(info[2] + 1, false);
            course.setBreadth(info[2]);
            sp_gen_ed.setSelection(info[3] + 1, false);
            course.setGen_ed(info[3]);
        }

        sp_credit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) selected[1] = false;
                else {
                    course.setCredit(i - 1);
                    selected[1] = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_grade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) selected[2] = false;
                else {
                    course.setGrade((String)adapterView.getItemAtPosition(i));
                    selected[2] = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_breadth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) selected[3] = false;
                else {
                    course.setBreadth(i - 1);
                    selected[3] = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_gen_ed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) selected[4] = false;
                else {
                    course.setGen_ed(i - 1);
                    selected[4] = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // add buttons

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String cname = course_name_et.getText().toString();
                String temp = cname.substring(0);
                boolean valid = true;
                if (temp.trim().length() < 1) {
                    selected[0] = false;
                } else {
                    course.setCname(cname);
                    selected[0] = true;
                }

                for (boolean b : selected) {
                    if (!b) {
                        valid = false;
                    }
                }

                if (valid) {
                    String tname = getIntent().getStringExtra(TNAME);
                    dbm.open();

                    if (mode == MODE_ADD) {
                        if (dbm.existCourse(tname, cname)) {
                            Toast.makeText(getApplicationContext(),
                                    R.string.add_course_err_exist, Toast.LENGTH_SHORT).show();
                            dbm.close();
                        } else {
                            dbm.addCourse(tname, course.getCname(), course.getCredit(),
                                    course.getGrade(), course.getBreadth(), course.getGen_ed());
                            dbm.close();
                            MainActivity.PlaceholderFragment.doUpdate = true;
                            finish();
                        }
                    }
                    else {
                        CourseDetailFragment.doUpdate = true;
                        dbm.updateCourse(tname, getIntent().getStringExtra(CNAME),course);
                        dbm.close();
                        finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            R.string.add_course_err_incomplete, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
