package com.example.robert.traceit;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PauseFragment.OnPauseFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PauseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PauseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TYPE = "type";

    // TODO: Rename and change types of parameters
    private PauseFragmentType mType;

    private OnPauseFragmentInteractionListener mListener;

    public PauseFragment() {
        // Required empty public constructor
    }

    public static PauseFragment newInstance(PauseFragmentType type) {
        PauseFragment fragment = new PauseFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = (PauseFragmentType) getArguments().getSerializable(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pause, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPauseFragmentInteractionListener) {
            mListener = (OnPauseFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnPauseFragmentInteractionListener {
        void OnRestartPressed();
        void OnMenuPressed();
        void OnResumedPressed();
    }

    public enum PauseFragmentType {
        Pause, Geme_Over
    }
}
