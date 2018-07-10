package travelpool.app.travelpool.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import travelpool.app.travelpool.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TermsCondition extends Fragment {


    public TermsCondition() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_terms_condition, container, false);

        getActivity().setTitle("Terms & Conditions");
        return view;
    }

}
