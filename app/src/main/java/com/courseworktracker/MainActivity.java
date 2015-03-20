package com.courseworktracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private DBManager dbm = new DBManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        String[] strs = mNavigationDrawerFragment.getTerms();
        mTitle = strs[number - 1];
    }

    public void onCourseSectionAttached(String s) {
        mTitle = s;
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(mTitle);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.add_term) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle(getString(R.string.add_term));
            alert.setMessage(getString(R.string.add_term_msg));

            // Set an EditText view to get user input
            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                String tname = input.getText().toString();
                dbm.open();
                if (dbm.existTerm(tname))
                    Toast.makeText(getApplicationContext(),
                            R.string.add_term_err_exist, Toast.LENGTH_SHORT).show();
                else
                    dbm.addTerm(tname);
                dbm.close();
                mNavigationDrawerFragment.refreshList();
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                }
            });

            alert.show();
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // press back button go back to previous fragment
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0)
            fm.popBackStack();
        else
            super.onBackPressed();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static boolean doUpdate = false;

        private ListView courseList;
        private String[] courseNames;
        String[] terms;
        private DBManager dbm;
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // empty container at start
            container.removeAllViews();
            courseList = (ListView)inflater.inflate(R.layout.listview_layout, null);
            Bundle args = getArguments();
            final int position = args.getInt(ARG_SECTION_NUMBER) - 1;
            if (position > 0) {
                dbm.open();
                terms = dbm.getTerms();

                courseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        // TODO course list click
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, CourseDetailFragment.newInstance(i,
                                        terms[position], (String)adapterView.getItemAtPosition(i)))
                                .addToBackStack("null")
                                .commit();
                    }
                });
                courseList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        return holdItem(i, (String)adapterView.getItemAtPosition(i));
                    }
                });

                courseNames = dbm.getCourseNames(terms[position]);
                dbm.close();

                courseList.setAdapter(new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.simple_list_item_activated_1,
                        android.R.id.text1,
                        courseNames));
                container.addView(courseList);
            }
            return inflater.inflate(R.layout.fragment_main, container, false);
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        dbm = new DBManager(getActivity());
        }

        @Override
        public void onResume() {
            super.onResume();  // Always call the superclass method first

            if (doUpdate) {
                refreshList();
            }
        }

        public boolean holdItem(int position, final String cname) {

            Bundle args = getArguments();
            final int i = args.getInt(ARG_SECTION_NUMBER) - 1;
            AlertDialog.Builder alert = new AlertDialog.Builder(this.getActivity());

            alert.setTitle(getString(R.string.delete_course));
            alert.setMessage(getString(R.string.delete_course_msg));

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dbm.open();
                    dbm.deleteCourse(terms[i], cname);
                    dbm.close();
                    refreshList();
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });

            alert.show();
            return true;
        }

        public void refreshList(){
            Bundle args = getArguments();
            int position = args.getInt(ARG_SECTION_NUMBER) - 1;
            dbm.open();
            courseNames = dbm.getCourseNames(terms[position]);
            dbm.close();
            courseList.setAdapter(new ArrayAdapter<String>(
                    getActivity(),
                    android.R.layout.simple_list_item_activated_1,
                    android.R.id.text1,
                    courseNames
            ));
        }
    }
}
