package travelpool.app.travelpool.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.adefruandta.spinningwheel.SpinningWheelView;

import java.util.Arrays;

import travelpool.app.travelpool.R;

public class HomeFragment extends Fragment {



    public HomeFragment() {
        // Required empty public constructor
    }
    SpinningWheelView wheelView;

    String[] value={"Abc","Xyx","ijk","lmn","poi","uud","jis","yys","usu","puf","etf"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        wheelView = (SpinningWheelView) view.findViewById(R.id.wheel);
        Button play = (Button) view.findViewById(R.id.play);

        // Can be array string or list of object
        wheelView.setItems(Arrays.asList(value));


        // Set listener for rotation event
        wheelView.setOnRotationListener(new SpinningWheelView.OnRotationListener<String>() {
            // Call once when start rotation
            @Override
            public void onRotation() {
                Log.d("XXXX", "On Rotation");

            }

            // Call once when stop rotation
            @Override
            public void onStopRotation(String item) {
                Log.d("XXXX", "On Rotation");
                Log.d("sdfsdfgsdfgds",item);
//                wheelView.rotate(50, 5000, 50);

            }


        });

        // If true: user can rotate by touch
        // If false: user can not rotate by touch
        wheelView.setEnabled(false);


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wheelView.rotate(50, 5000, 50);
            }
        });
        return view;

    }



}
