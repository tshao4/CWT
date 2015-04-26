package com.courseworktracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CourseDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CourseDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseDetailFragment extends Fragment {

    public static boolean doUpdate = false;
    public static boolean rList = false;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_POSITION = "position";
    private static final String ARG_CNAME = "cname";
    private static final String ARG_TNAME = "tname";

    private int position;
    private String tname;
    private String cname;

    private DBManager dbm;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param position Parameter 1.
     * @return A new instance of fragment CourseDetailFragment.
     */
    public static CourseDetailFragment newInstance(int position, String tname, String cname) {
        CourseDetailFragment fragment = new CourseDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putString(ARG_TNAME, tname);
        args.putString(ARG_CNAME, cname);
        fragment.setArguments(args);
        return fragment;
    }

    public CourseDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_POSITION);
            tname = getArguments().getString(ARG_TNAME);
            cname = getArguments().getString(ARG_CNAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        container.removeAllViews();

        View v = inflater.inflate(R.layout.fragment_course_detail, container, false);
        Button button_edit_course = (Button)v.findViewById(R.id.button_course_detail_edit);
        Button button_add_event = (Button)v.findViewById(R.id.button_course_detail_add_todo);

        final int[] info = getCourseInfo();

        v = refreshView(v, info);

        button_edit_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddCourse.class);
                intent.putExtra("tname", getArguments().getString(ARG_TNAME));
                intent.putExtra("cname", getArguments().getString(ARG_CNAME));
                intent.putExtra("mode", 1);
                intent.putExtra("info", getCourseInfo());
                startActivity(intent);
            }
        });

        button_add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());

                Context context = view.getContext();
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText titleBox = new EditText(context);
                titleBox.setHint("Title");
                layout.addView(titleBox);

                final EditText dueBox = new EditText(context);
                dueBox.setHint("Due (mmddyyyy)");
                layout.addView(dueBox);

                alert.setView(layout);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String title = titleBox.getText().toString();
                        String dueDate = dueBox.getText().toString();
                        boolean valid = true;

                        int yy = 0, mm = 0, dd = 0;

                        if (dueDate.length() != 8) {
                            valid = false;
                        }
                        else {
                            try {
                                yy = Integer.parseInt(dueDate.substring(4));
                                mm = Integer.parseInt(dueDate.substring(0,2));
                                dd = Integer.parseInt(dueDate.substring(2,4));

                            } catch (Exception e) {
                                valid = false;
                            }
                        }

                        if (valid) {
                            int date = yy*10000 + mm*100 + dd;
                            CourseWork cw = new CourseWork(getArguments().getString(ARG_CNAME), title, date);
                            dbm.open();
                            dbm.addAssignment(getArguments().getString(ARG_CNAME), cw);
                            dbm.close();
                            // refresh the list after addition
                            refreshList();
                        }
                        else {
                            Toast.makeText(view.getContext(),
                                    R.string.add_assignment_format, Toast.LENGTH_SHORT).show();
                        }
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

        List<List<String>> lists = getTodoList();

        ListView todoList = (ListView)v.findViewById(R.id.listView_todoList);
        CustomAdapter cadapter = new CustomAdapter(v.getContext(), lists.get(0), lists.get(1));
        todoList.setAdapter(cadapter);

        todoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                // TODO: onhold delete
                return holdItem(getArguments().getString(ARG_CNAME), (String)adapterView.getItemAtPosition(i));
            }
        });

        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        if (this.doUpdate) {
            int[] info = getCourseInfo();
            refreshView(getView(), info);
            this.doUpdate = false;
        }

        if (this.rList) {
            refreshList();
            this.rList = false;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // update title
        ((MainActivity) activity).onCourseSectionAttached(
                getArguments().getString(ARG_CNAME));
        dbm = new DBManager(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: (if needed) implement onFragmentInteraction() in MainActivity
        public void onFragmentInteraction(Uri uri);
    }

    private int[] getCourseInfo() {
        int[] info = new int[4];
        dbm.open();
        Course[] course = dbm.getCourse(getArguments().getString(ARG_TNAME), getArguments().getString(ARG_CNAME));
        dbm.close();
        if (course.length > 0) {
            info[0] = course[0].getCredit();

            Resources res = getResources();
            String[] gr = res.getStringArray(R.array.grade);
            for (int i = 0; i < gr.length; i++) {
                if (course[0].getGrade().equals(gr[i])) {
                    info[1] = i;
                    break;
                }
            }

            info[2] = course[0].getBreadth();
            info[3] = course[0].getGen_ed();
        }
        return info;
    }

    private List<List<String>> getTodoList(){
        dbm.open();
        List<List<String>> lists = dbm.getAssignemnts(getArguments().getString(ARG_CNAME));
        dbm.close();
        return lists;
    }

    public boolean holdItem(final String cname, final String aname) {

        Bundle args = getArguments();
        AlertDialog.Builder alert = new AlertDialog.Builder(this.getActivity());

        alert.setTitle(getString(R.string.delete_assignment));
        alert.setMessage(getString(R.string.delete_assignment_msg));

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dbm.open();
                dbm.deleteAssignment(cname, aname);
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

    private View refreshView(View v, int[] info){
        TextView text_credits = (TextView)v.findViewById(R.id.textview_course_detail_credit_container);
        TextView text_grade = (TextView)v.findViewById(R.id.textview_course_detail_grade_container);
        TextView text_breadth = (TextView)v.findViewById(R.id.textview_course_detail_breadth_container);
        TextView text_gen_ed = (TextView)v.findViewById(R.id.textview_course_detail_gen_ed_container);

        text_credits.setText(Integer.toString(info[0]));
        Resources res = getResources();
        String[] gr = res.getStringArray(R.array.grade);
        text_grade.setText(gr[info[1]]);
        String[] br = res.getStringArray(R.array.breadth);
        text_breadth.setText(br[info[2] + 1]);
        String[] ge = res.getStringArray(R.array.gen_ed);
        text_gen_ed.setText(ge[info[3] + 1]);

        return v;
    }

    private void refreshList() {
        ListView todoList = (ListView)getView().findViewById(R.id.listView_todoList);
        List<List<String>> lists = getTodoList();
        CustomAdapter cadapter = new CustomAdapter(getView().getContext(), lists.get(0), lists.get(1));
        todoList.setAdapter(cadapter);
    }
}
