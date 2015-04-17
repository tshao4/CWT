package com.courseworktracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

    private DBManager dbm = new DBManager(this);
    private Activity activity = this;
    private Course course = new Course();
    private boolean[] selected = {false, false, false, false, false};
    private ArrayList<String> sec_title = new ArrayList<String>(),
            sec_record = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        final EditText course_name_et = (EditText)findViewById(R.id.edittext_course_name);
        Spinner sp_credit = (Spinner)findViewById(R.id.spinner_credit);
        Spinner sp_grade = (Spinner)findViewById(R.id.spinner_grade);
        Spinner sp_breadth = (Spinner)findViewById(R.id.spinner_breadth);
        Spinner sp_gen_ed = (Spinner)findViewById(R.id.spinner_gen_ed);

        Button bt_cancel = (Button)findViewById(R.id.button_cancel_add_course);
        Button bt_next = (Button)findViewById(R.id.button_ok_add_course);
        Button bt_add = (Button)findViewById(R.id.button_add_section_add_course);

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

        ListView lv_sec = (ListView) findViewById(R.id.listView_section);

        // add three buttons

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: add sections to db
                String cname = course_name_et.getText().toString();
                String temp = cname.substring(0);
                boolean valid = true;
                if(temp.trim().length() < 1){
                    selected[0] = false;
                }
                else{
                    course.setCname(cname);
                    selected[0] = true;
                }

                for(boolean b : selected){
                    if(!b) {
                        valid = false;
                    }
                }

                if (valid) {
                    String tname = getIntent().getStringExtra("tname");
                    dbm.open();
                    if (dbm.existCourse(tname, cname)) {
                        Toast.makeText(getApplicationContext(),
                                R.string.add_course_err_exist, Toast.LENGTH_SHORT).show();
                        dbm.close();
                    }
                    else {
                        dbm.addCourse(tname, course.getCname(), course.getCredit(),
                                course.getGrade(), course.getBreadth(), course.getGen_ed());
                        dbm.close();
                        MainActivity.PlaceholderFragment.doUpdate = true;
                        finish();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),
                            R.string.add_course_err_incomplete, Toast.LENGTH_SHORT).show();
                }
            }
        });

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO custom dialog box
                // add to sections listView
                AlertDialog.Builder alert = new AlertDialog.Builder(activity);

                alert.setTitle(getString(R.string.add_section));
                alert.setMessage(getString(R.string.add_section_msg));

                // Set an EditText view to get user input
                final EditText input = new EditText(activity);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // TODO
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
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
