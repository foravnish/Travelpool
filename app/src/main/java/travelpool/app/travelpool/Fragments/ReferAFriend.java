package travelpool.app.travelpool.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import travelpool.app.travelpool.R;
import travelpool.app.travelpool.Utils.MyPrefrences;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReferAFriend extends Fragment {


    public ReferAFriend() {
        // Required empty public constructor
    }


    Button share;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_refer_afriend, container, false);
        share=view.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
//                String shareBody =comName.getText().toString()+ " "+phone.getText().toString();
                String shareBody ="Hi, \n" +
                        "Please visit my App  on Travelpool app. Click on the link below. "+ MyPrefrences.getMyRefrel(getActivity());

                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Details");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));


            }
        });
        return  view;
    }

}
