package com.courseworktracker;

import android.app.Activity;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_POSITION = "position";
    private static final String ARG_CNAME = "cname";
    private static final String ARG_TNAME = "tname";

    // TODO: Rename and change types of parameters
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
    // TODO: Rename and change types and number of parameters
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
        // TODO: inflate layout

        View v = inflater.inflate(R.layout.fragment_course_detail, container, false);
        TextView text_credits = (TextView)v.findViewById(R.id.textview_course_detail_credit_container);
        TextView text_grade = (TextView)v.findViewById(R.id.textview_course_detail_grade_container);
        TextView text_breadth = (TextView)v.findViewById(R.id.textview_course_detail_breadth_container);
        TextView text_gen_ed = (TextView)v.findViewById(R.id.textview_course_detail_gen_ed_container);

        dbm.open();
        Course[] course = dbm.getCourse(getArguments().getString(ARG_TNAME), getArguments().getString(ARG_CNAME));
        dbm.close();

        if (text_credits == null)
            Log.i("xxx", "sssssssss");

        if (course.length > 0) {
            text_credits.setText(Integer.toString(course[0].getCredit()));
            text_grade.setText(course[0].getGrade());
            Resources res = getResources();
            String[] br = res.getStringArray(R.array.breadth);
            text_breadth.setText(br[course[0].getBreadth()]);
            String[] ge = res.getStringArray(R.array.gen_ed);
            text_gen_ed.setText(ge[course[0].getGen_ed()]);
        }

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

}
