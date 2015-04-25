package com.courseworktracker;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


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
        // TODO: Update argument type and name
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
}
