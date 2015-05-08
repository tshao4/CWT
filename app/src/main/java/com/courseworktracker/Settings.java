package com.courseworktracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileOutputStream;


public class Settings extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getApplicationContext();
        final SharedPreferences sharedPreferences = context.getSharedPreferences("setting", 0);
        SharedPreferences.Editor setting_editor =sharedPreferences.edit();
        setContentView(R.layout.activity_settings);
        String day =sharedPreferences.getString("day", null);
        String hour =sharedPreferences.getString("hour", null);
        if (day == null){
            day = "3";
            hour = "8";
            setting_editor.putString("day", day);
            setting_editor.putString("hour", hour);
        }

        setContentView(R.layout.activity_settings);
        EditText reminder = (EditText) findViewById(R.id.edittext_reminder);
        EditText time = (EditText) findViewById(R.id.edittext_hour);
        reminder.setText(day);
        time.setText(hour);

        Button done = (Button) findViewById(R.id.button_done_setting);
        done.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                SharedPreferences.Editor setting = sharedPreferences.edit();
                String day = "" + ((EditText) findViewById(R.id.edittext_reminder)).getText();
                String hour = "" + (((EditText) findViewById(R.id.edittext_hour)).getText());
                setting.putString("day", day);
                setting.putString("hour", hour);
                setting.apply();

                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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
